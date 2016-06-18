import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ClientProgram implements Client {
  public ClientProgram() {
    super();
  }


  private void do_the_work(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    try {
      Registry registry = LocateRegistry.getRegistry(args[0]);

      Client stub = (Client)UnicastRemoteObject.exportObject(this, 0);
      registry.rebind("Client", stub);
      System.out.println("ClientProgram bound");

      System.out.println("Finding TickerServer");
      TickerServer impl
        = (TickerServer)registry.lookup(TickerServer.LOOKUP_NAME);
      System.out.println("Connecting to TickerServer");
      impl.connect(this);

    } catch (Exception e) {
      System.err.println("ClientProgram exception:");
      e.printStackTrace();
    }
  }

  public void alert(String msg) throws RemoteException {
    System.out.println(msg);
  }


  public static void main(String[] args) {
    new ClientProgram().do_the_work(args);
  }
}
