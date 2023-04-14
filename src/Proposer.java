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
	 * Initiates proposal id.
	 */
	public void start() { proposalId = 0; }

	/**
	 * This function provides different phases of paxos:
	 * Phase I - sends Prepare(n) message to acceptors.
	 * Phase II - if consensus reached, send Accept() message.
	 * Then send the accepted message to all learners to commit.
	 * @param key key value sent from client
	 * @param action request type from client.
	 * @return Consensus result of the different phases.
	 */
	public synchronized String propose(int key, int action) {
		proposalId++;

		String res = "";
		Map<String, String> serverMap = ServerHelper.getMap();
		int count = 0;

		// Send PREPARE message
		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
				PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

				// Send PREPARE message
				if (stub.sendPrepare(proposalId)) count++;
			} catch (SocketTimeoutException | RemoteException | NotBoundException exception) {
				// Continue the process despite one server times out
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
			sendCommit(count, key, action, res, serverMap);
		} else {
			res = "Consensus could not be reached as only " + count + " servers replied to the accept request";
			LOGGER.info(res);
			return res;
		}

		return res;
	}

	/**
	 * The proposer tells all the acceptors (that are live) what value to accept.
	 * @param count number of agreed servers.
	 * @param key key value from client.
	 * @param action request type sent from client.
	 * @param res final response message of whether the consensus was reached or not.
	 * @param serverMap the pre-populated key-value store.
	 * @return final response message of whether the consensus was reached or not.
	 */
	public synchronized String sendPropose(int count, int key, int action, String res, Map<String, String>serverMap) {
		LOGGER.info(count + " Servers sent promised");
		count = 0;

		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
				PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

				if (stub.accept(proposalId, key, action)) count++;
			} catch (SocketTimeoutException | RemoteException | NotBoundException exception) {
				// Continue the process despite one server times out
				continue;
			}
		}

		return res;
	}

	/**
	 * Send the ACCEPTED message to learners to commit.
	 * @param count number of agreed servers.
	 * @param key key value from client.
	 * @param action request type sent from client.
	 * @param res final response message of whether the consensus was reached or not.
	 * @param serverMap the pre-populated key-value store.
	 * @return value of committed message.
	 */
	public synchronized String sendCommit(int count, int key, int action, String res, Map<String, String>serverMap) {
		LOGGER.info(count + " Servers sent accepted");

		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
				PaxosInterface stub = (PaxosInterface) registry.lookup(entry.getKey());

				// Ask all servers to commit
				res = stub.commit(key, action);
			} catch (SocketTimeoutException se) {
				// Continue the process despite one server was time out
				continue;
			} catch (RemoteException re) {
				//Continue the process despite one server was not reachable
				continue;
			} catch (NotBoundException exception) {
				throw new IllegalArgumentException();
			}
		}

		return res;
	}

	@Override
	public void run() {}
}
