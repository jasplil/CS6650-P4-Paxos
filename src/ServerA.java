import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a replica server.
 */
public class ServerA extends PaxosServer{
	private static Logger LOGGER = LogManager.getLogger(ServerA.class.getName());

	public ServerA(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

	public static void main(String[] args) {
		try {
			ServerA server = new ServerA(1);
		    KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
		    Registry registry = LocateRegistry.createRegistry(Constants.SERVER1_PORT_NUM);
		    registry.bind(Constants.SERVER1, stub);

			LOGGER.info("Server A is ready");
		} catch (Exception e) {
			LOGGER.debug("Server A error: " + e);
		}
	}
}
