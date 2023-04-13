import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a replica server.
 */
public class ServerB extends PaxosServer {
	private static Logger LOGGER = LogManager.getLogger(ServerB.class.getName());

	public ServerB(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

	public static void main(String[] args) {
		try {
		    ServerB server = new ServerB(2);
		    PaxosInterface stub = (PaxosInterface) UnicastRemoteObject.exportObject(server, 0);
		    Registry registry = LocateRegistry.createRegistry(Constants.SERVER2_PORT_NUM);
		    registry.bind(Constants.SERVER2, stub);

			LOGGER.info("Server B is ready");
		} catch (Exception e) {
			LOGGER.debug("Server B error: " + e);
		}
	}
}