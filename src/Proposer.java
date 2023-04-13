import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

/**
 * This is the class that represents the proposer.
 * It will initiate the consensus process by proposing
 * a value to be agreed upon by the participants.
 */
public class Proposer extends KeyValueStore implements Runnable {
	private static Logger LOGGER = LogManager.getLogger(KeyValueStore.class.getName());
	private static int proposalId;
	Registry registry;

	public Proposer() { super(); }

	/**
	 * Initiates proposal id
	 */
	public void start() { proposalId = 0; }

	/**
	 * This function has three phases:
	 * Phase I - sends Prepare(n) message to acceptors.
	 * Phase II - if consensus reached, send Accept() message
	 * Phase III -
	 * @param key
	 * @param action
	 * @return
	 */
	public synchronized String propose (int key, int action) {
		proposalId++;

		String res = "";
		Map<String, String> serverMap = ServerHelper.getMap();
		int count = 0;

		try {
			for (Map.Entry<String, String> entry : serverMap.entrySet()) {
				try {
					registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
					PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

					// Send prepare(n) message
					if (stub.prepare(proposalId, key, action)) count++;
				} catch (SocketTimeoutException socketTimeoutException) {
					// Continue the process despite one server times out
					continue;
				} catch (RemoteException remoteException){
					// Continue the process despite one server is not reachable
					continue;
				}
			}

			accept(count, key, action, res, serverMap);

			// Ensure the majority of servers agreed
			if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
				LOGGER.info(count + "Servers accepted");

				for (Map.Entry<String, String> entry : serverMap.entrySet()) {
					try {
						registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
						PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

						// Ask all servers to commit as quorum number has accepted
						res = stub.commit(key, action);
					} catch (SocketTimeoutException se) {
						//Continue the process even if one server times out
						continue;
					} catch (RemoteException re) {
						//Continue the process even if one server was not reachable
						continue;
					}
				}
			} else {
				res = "Consensus could not be reached as only " + count + "servers replied to the accept request";
				LOGGER.info(res);
				return res;
			}
		} catch (NotBoundException exception) {
			LOGGER.info("Remote Exception" + exception);
		}

		return res;
	}

	/**
	 * Ensure the majority of servers agreed.
	 * Send accept message to (at least) a majority of acceptors
	 * @param count
	 * @param key
	 * @param action
	 * @param res final response message of whether the consensus was reached or not
	 * @param serverMap the pre-populated key-value store
	 * @return
	 */
	public synchronized String accept(int count, int key, int action, String res, Map<String, String>serverMap) {
		if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
			LOGGER.info(count + "Servers agreed");
			count = 0;

			for (Map.Entry<String, String> entry : serverMap.entrySet()) {
				try {
					registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
					PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

					// Check with all servers if they can accept the proposal
					if (stub.accept(proposalId, key, action)) {
						count ++;
					}
				} catch (SocketTimeoutException se) {
					//Continue the process even if one server times out
					continue;
				} catch (RemoteException | NotBoundException re) {
					//Continue the process even if one server was not reachable
					continue;
				}
			}
		} else {
			res = "Consensus could not be reached as only " + count + "servers replied to the prepare request";
			System.out.println(res);
			return res;
		}

		return res;
	}

	@Override
	public void run() {}
}
