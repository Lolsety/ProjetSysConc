package linda.shm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

	// Protection des variables partag��es
	private Lock moniteur = new ReentrantLock();

	// Une condition de blocage commune �� tous
	private Condition acces = moniteur.newCondition();

	/** Ne pas prendre un ensemble car sinon erreur */
	private Collection<Tuple> tuples = new ArrayList<Tuple>();
	
	/** Collection des callback read */
	private Collection<EventCall> eventCallsRead = new ArrayList<>();
	/** Collection des callback take */
	private Collection<EventCall> eventCallsTake = new ArrayList<>();
	
	

	public CentralizedLinda() {
	}

	@Override
	public void write(Tuple t) {
		moniteur.lock();
		tuples.add(t);
		verifierCallBacksTake(t);
		verifierCallBacksRead(t);
		acces.signalAll();
		moniteur.unlock();
	}

	@Override
	public Tuple take(Tuple template) {
		Boolean trouve = false;
		moniteur.lock();
		Tuple res = new Tuple();
		while (!trouve) {
			for (Tuple t : tuples) {
				if (t.matches(template)) {
					res = t;
					tuples.remove(t);
					trouve = true;
					break;
				}
			}
			if (!trouve) {
				try {
					acces.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		moniteur.unlock();
		return res;
	}

	@Override
	public Tuple read(Tuple template) {
		Boolean trouve = false;
		moniteur.lock();
		Tuple res = new Tuple();
		while (!trouve) {
			for (Tuple t : tuples) {
				if (t.matches(template)) {
					res = t;
					trouve = true;
					break;
				}
			}
			if (!trouve) {
				try {
					acces.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		moniteur.unlock();
		return res;
	}

	@Override
	public Tuple tryTake(Tuple template) {
		moniteur.lock();
		Tuple res = null;
		for (Tuple t : tuples) {
			if (t.matches(template)) {
				res = t;
				tuples.remove(t);
				break;
			}
		}
		moniteur.unlock();
		return res;
	}

	@Override
	public Tuple tryRead(Tuple template) {
		moniteur.lock();
		Tuple res = null;
		for (Tuple t : tuples) {
			if (t.matches(template)) {
				res = t;
				break;
			}
		}
		moniteur.unlock();
		return res;
	}

	@Override
	public Collection<Tuple> takeAll(Tuple template) {
		moniteur.lock();
		Collection<Tuple> res = new ArrayList<Tuple>();
		for (Tuple t : tuples) {
			if (t.matches(template)) {
				res.add(t);
			}
		}
		for(Tuple t : res){
			tuples.remove(t);
		}
		moniteur.unlock();
		return res;
	}

	@Override
	public Collection<Tuple> readAll(Tuple template) {
		moniteur.lock();
		Collection<Tuple> res = new ArrayList<Tuple>();
		for (Tuple t : tuples) {
			if (t.matches(template)) {
				res.add(t);
			}
		}
		moniteur.unlock();
		return res;
	}

	@Override
	public void eventRegister(eventMode mode, eventTiming timing,
			Tuple template, Callback callback) {
		moniteur.lock();
		if (timing == eventTiming.IMMEDIATE) {
			if (mode == eventMode.READ) {
				Tuple x = tryRead(template);
				if (x == null) {
					EventCall event = new EventCall(callback, mode);
					eventCallsRead.add(event);
				} else {
					callback.call(x);
				}
			}
			else if (mode == eventMode.TAKE){
				Tuple x = tryTake(template);
				if (x == null) {
					EventCall event = new EventCall(callback, mode);
					eventCallsTake.add(event);
				} else {
					callback.call(x);
				}
			}
		}
		else if(timing == eventTiming.FUTURE) {
			if (mode == eventMode.READ){
				EventCall event = new EventCall(callback, mode);
				eventCallsRead.add(event);
			}
			else if (mode == eventMode.TAKE) {
				EventCall event = new EventCall(callback, mode);
				eventCallsTake.add(event);
			}
		}
		moniteur.unlock();
	}
	
	public void verifierCallBacksRead (Tuple t) {
		boolean x;
		Collection<EventCall> readEvent = new ArrayList<EventCall>();
		for (EventCall event : eventCallsRead) {
			x = event.verifierOccurence(t);
			if (x == true) {
				readEvent.add(event);
				//break;
			}
		}
		eventCallsRead.removeAll(readEvent);
	}

	public void verifierCallBacksTake (Tuple t) {
		boolean x;
		EventCall e = null;
		for (EventCall event : eventCallsTake) {
			x = event.verifierOccurence(t);
			if (x == true) {
				e = event;
				break;
			}
		}
		if (e != null) {eventCallsTake.remove(e);}
	}
	@Override
	public void debug(String prefix) {
		System.out.println("------------");
		for (Tuple t : tuples) {
			System.out.println(t);
		}
		System.out.println("------------");
	}
	
	private class EventCall {
		
		private Callback c;
		private eventMode e;
		
		public EventCall(Callback c, eventMode e) {
			this.c = c;
			this.e = e;
		}
		
		public boolean verifierOccurence (Tuple tuple){
			boolean res = false;
			if (e == eventMode.READ){
				Tuple x = tryRead(tuple);
				if (x != null) {
					c.call(x);
					res = true;
				}
			}
			else if (e == eventMode.TAKE) {
				Tuple x = tryTake(tuple);
				if (x != null) {
					c.call(x); 
					res = true;
				}
			}
		return res;
		}
	}
}