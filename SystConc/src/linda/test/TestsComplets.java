package linda.test;

import java.util.Collection;
import java.util.LinkedList;

import linda.AsynchronousCallback;
import linda.Callback;
import linda.Linda;
import linda.Tuple;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;


public class TestsComplets {
	
   	private static class MyCallback7 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 7 :" + t);
		}
	}
   	private static class MyCallback8 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 8 :" + t);
		}
	}
   	
   	private static class MyCallback9 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 9 :" + t);
		}
	}
   	
   	private static class MyCallback10 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 10 :" + t);
		}
	}
   	
   	private static class MyCallback11 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 11 :" + t);
		}
	}
   	
   	private static class MyCallback12 implements Callback {
		@Override
		public void call(Tuple t) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println("Got in test 12 :" + t);
		}
	}
           
	
	 public static void main(String[] a) {
         
	       	//final Linda linda = new linda.shm.CentralizedLinda();
	        final Linda linda = new linda.server.LindaClient("//localhost:4000/LindaServ");
	       	
	     
	        new Thread() {
	            public void run() {
	                try {
	                    Thread.sleep(2000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                System.out.println("Test 1: take on existing Tuple : \n");
	                Tuple motif1 = new Tuple(Integer.class, String.class);
	                System.out.println("Tuplespace Before take :");
	                linda.debug("");
	                Tuple res1 = linda.take(motif1);
	                System.out.println("Take [Int,String] result :");
	                System.out.println("Found:" + res1);
	                System.out.println("Tuplespace after Take :");
	                linda.debug("");
	                
	                
	                System.out.println("\n \n Test 2: read on existing Tuple : \n");
	                Tuple motif2 = new Tuple(Integer.class, Integer.class);
	                System.out.println("Tuplespace Before take :");
	                linda.debug("");
	                Tuple res2 = linda.read(motif2);
	                System.out.println("read [Int,Int] result :");
	                System.out.println("Found:" + res2);
	                System.out.println("Tuplespace after Read :");
	                linda.debug("");
	                
	                System.out.println("\n \n Test 3: take on non-existing Tuple : \n");
	                System.out.println("Tuplespace Before take :");
	                linda.debug("");
	                Tuple res3 = linda.take(motif1);
	                System.out.println("take[Int,String] result :");
	                System.out.println("Found:" + res3);
	                System.out.println("Tuplespace after Take :");
	                linda.debug("");
	                
	                System.out.println("\n \n Test 4: Read on non-existing Tuple : \n");
	                System.out.println("Tuplespace Before Read :");
	                linda.debug("");
	                Tuple res4 = linda.read(motif1);
	                System.out.println("read [Int,String] result :");
	                System.out.println("Found:" + res4);
	                System.out.println("Tuplespace after Read :");
	                linda.debug("");
	                
	                System.out.println("\n \n Test 5: TakeAll : \n");
	                Tuple test52 = new Tuple(3, 4,"bonjour");
	                Tuple test51 = new Tuple(2, 8,"aurevoir");
	                Tuple test53 = new Tuple("aaa", 3,"rat�");
	                
	                linda.write(test51);
	                linda.write(test52);
	                linda.write(test53);
	                System.out.println("writting tuples " + test51 + test52 + test53);
	                System.out.println("Tuplespace Before TakeAll :");
	                linda.debug("");
	                Tuple motif3 = new Tuple(Integer.class, Integer.class, String.class);
	                Collection<Tuple> res5 = linda.takeAll(motif3);
	                System.out.println("TakeAll [Int,Int,String] result :");
	                System.out.println("Found: ");
	                for (Tuple t : res5){
	                	System.out.println(t);
	                }
	                System.out.println("Tuplespace after TakeAll:");
	                linda.debug("");
	                
	                System.out.println("\n \n Test 6: ReadAll : \n");
	                Tuple test6 = new Tuple("bbb", 3,"poisson");         
	                linda.write(test6);
	                System.out.println("writting tuple " + test6);
	                System.out.println("Tuplespace Before ReadAll :");
	                linda.debug("");
	                Tuple motif4 = new Tuple(String.class, Integer.class, String.class);
	                Collection<Tuple> res6 = linda.readAll(motif4);
	                System.out.println("ReadAll [String,Int,String] result :");
	                System.out.println("Found: ");
	                for (Tuple t : res6){
	                	System.out.println(t);
	                }
	                System.out.println("Tuplespace after ReadAll :");
	                linda.debug("");
	                
	                System.out.println("---------- test7 eventRegister - Immediate Read Possible ----------");
					linda.eventRegister(eventMode.READ, eventTiming.IMMEDIATE, new Tuple(4,5), new MyCallback7());
					System.out.println("Tuplespace after Immediate Read of [4,5] :");
					linda.debug("");
					
					System.out.println("---------- test8 eventRegister - Immediate Take Possible ----------");
					linda.eventRegister(eventMode.TAKE, eventTiming.IMMEDIATE, new Tuple(4,5), new MyCallback8());
					System.out.println("Tuplespace after Immediate Read of [4,5] :");
					linda.debug("");
					
					System.out.println("---------- test9 eventRegister - Immediate Read not Possible ----------");
					linda.eventRegister(eventMode.READ, eventTiming.IMMEDIATE, new Tuple(4,6), new MyCallback9());
					System.out.println("Tuplespace after Immediate Read of [4,6] :");
					linda.debug("");
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("adding [4,6]");
					linda.write(new Tuple(4,6));
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					linda.debug("");
					
					System.out.println("---------- test10 eventRegister - Immediate Take  not Possible ----------");
					linda.eventRegister(eventMode.TAKE, eventTiming.IMMEDIATE, new Tuple(String.class,String.class,String.class), new MyCallback10());
					System.out.println("Tuplespace after Immediate Take of [String,String,String] :");
					linda.debug("");
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("adding a matching tuple");
					linda.write(new Tuple("a","b","c"));
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					linda.debug("");
					
					System.out.println("---------- test11 eventRegister - Future Read ----------");
					System.out.println("Requesting future read of [Int,Int] :");
					linda.eventRegister(eventMode.READ, eventTiming.FUTURE, new Tuple(Integer.class,Integer.class), new MyCallback11());
					linda.debug("");
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("adding [6,7]");
					linda.write(new Tuple(6,7));
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					linda.debug("");
					
					System.out.println("---------- test12 eventRegister - Future Take ----------");
					linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, new Tuple(String.class,Integer.class), new MyCallback12());
					System.out.println("Requesting future take of [String,Int] :");
					linda.debug("");
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("adding [ \"bonjour\", 7 ]");
					linda.write(new Tuple("bonjour",7));
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					linda.debug("");
	                
	            }
	        }.start();
	                
	        new Thread() {
	            public void run() {
	                try {
	                    Thread.sleep(2);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }

	                Tuple t11 = new Tuple(4, 5);
	                System.out.println("(2) write: " + t11);
	                linda.write(t11);

	                Tuple t2 = new Tuple("hello", 15);
	                System.out.println("(2) write: " + t2);
	                linda.write(t2);

	                Tuple t3 = new Tuple(4, "foo");
	                System.out.println("(2) write: " + t3);
	                linda.write(t3);

	            }
	        }.start();
	        
	        new Thread() {
	            public void run() {
	                try {
	                    Thread.sleep(5000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }

	                Tuple test3 = new Tuple(1, "newtupletotake");
	                System.out.println("AddingTuple " + test3);
	                linda.write(test3);
	                
	                try {
	                    Thread.sleep(5000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	                Tuple test4 = new Tuple(1, "newtupletoread");
	                System.out.println("AddingTuple " + test4);
	                linda.write(test4);

	            }
	        }.start();


}
}
