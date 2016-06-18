import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TickerServerImpl implements TickerServer, Runnable {

  List<Client> list = new ArrayList<Client>();


  public TickerServerImpl() {
    super();
    List<Client> list = new ArrayList<Client>();
  }


  public void connect(Client d) throws RemoteException {
    System.out.println("Adding client " + d);
    list.add(d);
  }

  public void run() {
    for (int i = 0; i < 1000; i++) {
      try {
        Thread.sleep(1000);
	System.out.println("i = " + i + " size of list = " + list.size());
      } catch (InterruptedException e) {
        break;
      }

      Iterator<Client> it = list.iterator();
      while (it.hasNext()) {
        try {
	  it.next().alert("i = " + i);
	} catch (RemoteException e) {
	  System.out.println("Client is disconnected");
	  it.remove();
	}
      }
    }
  }


  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    try {
      TickerServer impl = new TickerServerImpl();
      TickerServer stub
        = (TickerServer)UnicastRemoteObject.exportObject(impl, 0);

      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(TickerServer.LOOKUP_NAME, stub);
      System.out.println("TickerServerImpl bound");

      new Thread((TickerServerImpl)impl).start();

    } catch (Exception e) {
      System.err.println("TickerServerImpl exception:");
      e.printStackTrace();
    }
  }
}
