import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
  public void alert(String msg) throws RemoteException;
}
