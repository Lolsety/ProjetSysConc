package linda.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.Tuple;
import linda.shm.CentralizedLinda;

public class MonoServLinda extends UnicastRemoteObject implements ServLinda {
	
	private static final long serialVersionUID = 1L;
	private Linda linda;
	
	
	public MonoServLinda() throws RemoteException {
		linda = new CentralizedLinda();
	}

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			Naming.bind("//localhost:"+args[0]+"/LindaServ", new MonoServLinda());
		} catch (MalformedURLException | RemoteException
				| AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(Tuple t) {
		this.linda.write(t);
	}

	@Override
	public Tuple take(Tuple template) {
		return this.linda.take(template);
	}

	@Override
	public Tuple read(Tuple template) {
		return this.linda.read(template);
	}

	@Override
	public Tuple tryTake(Tuple template) {
		return this.linda.tryTake(template);
	}

	@Override
	public Tuple tryRead(Tuple template) {
		return this.linda.tryRead(template);
	}

	@Override
	public Collection<Tuple> takeAll(Tuple template) {
		return this.linda.takeAll(template);
	}

	@Override
	public Collection<Tuple> readAll(Tuple template) {
		return this.linda.readAll(template);
	}

	public void waitEvent(eventMode mode, eventTiming timing, Tuple template, final ExecCallback ecb) throws RemoteException {
		System.out.println("Enregistrement d'un Event");
		final class newCallback implements Callback {
			public void call(Tuple t) {
				try {
					ecb.call(t);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		this.linda.eventRegister(mode, timing, template, new newCallback());
	}
}
