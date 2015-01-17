package linda.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import linda.Callback;
import linda.Tuple;

public interface ExecCallback extends Remote {
	
	public Callback getCb() throws RemoteException;

	void call(Tuple t) throws RemoteException;

}
