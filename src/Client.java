import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Client class using RMI for RPC communication.
 */
public class Client {
	private static Logger LOGGER = LogManager.getLogger(Client.class.getName());
	private static final int[] putNums = {1000, 1008, 1600, 1800, 2000};
	private static final int[] getNums = {100, 1000, 1666, 1800, 2000};
	private static final int[] deleteNums = {1000, 1008, 1600, 1888, 2000};

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", ServerHelper.getPortNumber("Server1"));
			PaxosInterface stub = (PaxosInterface) registry.lookup("Server1");

			// 5 PUT, DELETE, GET requests
			for (int i = 0; i < 5; i++) {
				LOGGER.info("Client sent PUT request" + stub.put(putNums[i]));
				LOGGER.info("Client sent GET request" + stub.get(getNums[i]));
				LOGGER.info("Client sent DELETE request" + stub.delete(deleteNums[i]));
			}
		} catch(RemoteException re) {
			System.out.println("Unable to find the RMI Server");
		} catch(NotBoundException ne) {
			System.out.println("RMI Server not bound");
		}
	}
}
