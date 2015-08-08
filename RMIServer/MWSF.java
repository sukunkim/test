import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MWSF extends Remote {
  public int refreshGateway(int gatewayId) throws RemoteException;
}
