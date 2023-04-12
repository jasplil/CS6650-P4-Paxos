import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a replica server.
 */
public class ServerD extends PaxosServer {
	private static Logger LOGGER = LogManager.getLogger(ServerD.class.getName());

	public ServerD(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

	public static void main(String[] args) {
		try {
		    ServerD server = new ServerD(4);
		    KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
		    Registry registry = LocateRegistry.createRegistry(Constants.SERVER4_PORT_NUM);
		    registry.bind(Constants.SERVER4, stub);
	
		    LOGGER.info("Server D is ready");
		} catch (Exception e) {
		    LOGGER.debug("Server D error: " + e);
		}
	}
}
