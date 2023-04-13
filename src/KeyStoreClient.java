import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class KeyStoreClient {
	private static Logger LOGGER = LogManager.getLogger(KeyStoreClient.class.getName());

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", ServerHelper.getPortNumber("Server1"));
			PaxosInterface stub = (PaxosInterface) registry.lookup("Server1");

			// TODO: 5 PUT, DELETE, GET
			LOGGER.info(stub.put(1007));
			LOGGER.info(stub.get(100));
			LOGGER.info(stub.put(2000));
			LOGGER.info(stub.delete(1410));
		} catch(RemoteException re) {
			System.out.println("Unable to find the RMI Server");
		} catch(NotBoundException ne) {
			System.out.println("RMI Server not bound");
		}
	}
}
