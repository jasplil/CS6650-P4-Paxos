import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is a replica server.
 */
public class ServerC extends PaxosServer {
	private static Logger LOGGER = LogManager.getLogger(ServerC.class.getName());

	public ServerC(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

	public static void main(String[] args) {
		try {
		    ServerC server = new ServerC(3);
		    PaxosInterface stub = (PaxosInterface) UnicastRemoteObject.exportObject(server, 0);
		    Registry registry = LocateRegistry.createRegistry(Constants.SERVER3_PORT_NUM);
		    registry.bind(Constants.SERVER3, stub);
	
		    LOGGER.info("Server C is ready");
		} catch (Exception e) {
		    LOGGER.debug("Server C error: " + e);
		}
	}
}
