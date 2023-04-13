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
	 * This function provides different phases of paxos:
	 * Phase I - sends Prepare(n) message to acceptors.
	 * Phase II - if consensus reached, send Accept() message
	 * @param key key value sent from client
	 * @param action
	 * @return
	 */
	public synchronized String propose(int key, int action) {
		proposalId++;

		String res = "";
		Map<String, String> serverMap = ServerHelper.getMap();
		int count = 0;

		try {
			for (Map.Entry<String, String> entry : serverMap.entrySet()) {
				try {
					registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
					PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

					// Send PREPARE message
					if (stub.sendPrepare(proposalId)) count++;
				} catch (SocketTimeoutException socketTimeoutException) {
					// Continue the process despite one server times out
					continue;
				} catch (RemoteException remoteException){
					// Continue the process despite one server is not reachable
					continue;
				}
			}

			// Proceed only if the majority of servers agreed
			if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
				sendPropose(count, key, action, res, serverMap);
			} else {
				res = "Consensus could not be reached as only " + count + " servers replied to the prepare request";
				LOGGER.info(res);
				return res;
			}

			// Proceed only if the majority of servers agreed
			if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
				LOGGER.info(count + " Servers accepted");

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
				res = "Consensus could not be reached as only " + count + " servers replied to the accept request";
				LOGGER.info(res);
				return res;
			}
		} catch (NotBoundException exception) {
			LOGGER.info("Remote Exception " + exception);
		}

		return res;
	}

	/**
	 * The proposer tells all the acceptors (that are live) what value to accept.
	 * @param count number of agreed servers.
	 * @param key key value from client.
	 * @param action // TODO: check actions
	 * @param res final response message of whether the consensus was reached or not.
	 * @param serverMap the pre-populated key-value store.
	 * @return final response message of whether the consensus was reached or not.
	 */
	public synchronized String sendPropose(int count, int key, int action, String res, Map<String, String>serverMap) {
		LOGGER.info(count + " Servers agreed");
		count = 0;

		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
				PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

				if (stub.accept(proposalId, key, action)) count ++;
			} catch (SocketTimeoutException se) {
				// Continue the process even if one server times out
				continue;
			} catch (RemoteException | NotBoundException re) {
				// Continue the process even if one server was not reachable
				continue;
			}
		}

		return res;
	}

	@Override
	public void run() {}
}
