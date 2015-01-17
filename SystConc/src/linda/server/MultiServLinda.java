package linda.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import linda.Linda;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.shm.CentralizedLinda;
import linda.Tuple;

public class MultiServLinda extends UnicastRemoteObject implements ServLinda {

	private static final long serialVersionUID = 1L;
	private Linda linda;
	private int port;
	private static HashMap<Integer, LindaClient> clients;
	
	
	protected MultiServLinda(int port) throws RemoteException {
		linda = new CentralizedLinda();
		this.port = port;
		MultiServLinda.clients.put(port,new LindaClient("//localhost:"+port+"/LindaServ"));
	}
	
	public void main(String[] args) {
		try {
			LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			Naming.bind("//localhost:"+args[0]+"/LindaServ", new MultiServLinda(Integer.parseInt(args[0])));
			this.port = Integer.parseInt(args[0]);
			MultiServLinda.clients.put(Integer.parseInt(args[0]),new LindaClient("//localhost:"+args[0]+"/LindaServ"));
		} catch (MalformedURLException | RemoteException
				| AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(Tuple t) throws RemoteException {
		this.linda.write(t);
	}

	@Override
	public Tuple take(Tuple template) throws RemoteException {
		//TODO
		return null;
	}

	@Override
	public Tuple read(Tuple template) throws RemoteException {
		// TODO
		return null;
	}

	@Override
	public Tuple tryTake(Tuple template) throws RemoteException {
		Tuple res;
		res = this.linda.tryTake(template);
		if (res==null) {
			for (LindaClient cl : MultiServLinda.clients.values()) {
				res = cl.tryTake(template);
				if (res!=null) break;
			}
		}
		return res;
	}

	@Override
	public Tuple tryRead(Tuple template) throws RemoteException {
		Tuple res;
		res = this.linda.tryRead(template);
		if (res==null) {
			for (LindaClient cl : MultiServLinda.clients.values()) {
				res = cl.tryRead(template);
				if (res!=null) break;
			}
		}
		return res;
	}

	@Override
	public Collection<Tuple> takeAll(Tuple template) throws RemoteException {
		Collection<Tuple> res = new LinkedList<Tuple>();
		for (LindaClient cl : MultiServLinda.clients.values()) {
			res.addAll(cl.takeAll(template));
		}
		return res;
	}

	@Override
	public Collection<Tuple> readAll(Tuple template) throws RemoteException {
		Collection<Tuple> res = new LinkedList<Tuple>();
		for (LindaClient cl : MultiServLinda.clients.values()) {
			res.addAll(cl.readAll(template));
		}
		return res;
	}

	@Override
	public ExecCallback waitEvent(eventMode mode, eventTiming timing,
			Tuple template, ExecCallback ecb) throws RemoteException {
		// TODO
		return null;
	}

}
