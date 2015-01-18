package linda.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {


	private static ServLinda monoserv;

	/** Initializes the Linda implementation.
	 *  @param serverURI the URI of the server, e.g. "//localhost:4000/LindaServ".
	 */
	public LindaClient(String serverURI) {
		try {
			monoserv = (ServLinda)Naming.lookup(serverURI);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void debug(String prefix) {
		
	}


	@Override
	public void write(Tuple t) {
		try {
			monoserv.write(t);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public Tuple take(Tuple template) {
		try {
			return monoserv.take(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public Tuple read(Tuple template) {
		try {
			return monoserv.read(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public Tuple tryTake(Tuple template) {
		try {
			return monoserv.tryTake(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public Tuple tryRead(Tuple template) {
		try {
			return monoserv.tryRead(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public Collection<Tuple> takeAll(Tuple template) {
		try {
			return monoserv.takeAll(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public Collection<Tuple> readAll(Tuple template) {
		try {
			return monoserv.readAll(template);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public void eventRegister(final eventMode mode, final eventTiming timing,
			final Tuple template, final Callback callback) {
		new Thread() {
			public void run() {
				try {
					ExecCallback ecb = new ExecCallbackImpl(callback);
					monoserv.waitEvent(mode, timing, template, ecb);		
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}