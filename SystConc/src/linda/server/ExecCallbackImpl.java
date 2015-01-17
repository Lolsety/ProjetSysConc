package linda.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import linda.Callback;
import linda.Tuple;

public class ExecCallbackImpl extends UnicastRemoteObject implements ExecCallback {
	
	private static final long serialVersionUID = 1L;

	private Callback cb;
	

	public ExecCallbackImpl(Callback cb) throws RemoteException {
		this.cb=cb;
	}

	 public Callback getCb() throws RemoteException {
		 return cb;
	 }
	@Override
	public void call(Tuple t) throws RemoteException{
		cb.call(t);
	}

	


}
