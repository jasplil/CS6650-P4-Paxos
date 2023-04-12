import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a replica server.
 */
public class ServerE extends PaxosServer{
	private static Logger LOGGER = LogManager.getLogger(ServerE.class.getName());

	public ServerE(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

	public static void main(String[] args) {
		try {
		    ServerE server = new ServerE(5);
		    KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
		    Registry registry = LocateRegistry.createRegistry(Constants.SERVER5_PORT_NUM);
		    registry.bind(Constants.SERVER5, stub);

			LOGGER.info("Server E is ready");
		} catch (Exception e) {
		    LOGGER.debug("Server E error: " + e);
		}
	}
}
