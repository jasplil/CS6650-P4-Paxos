import java.rmi.RemoteException;

/**
 * This class achieves consensus among a set of servers that communicate.
 * One or more clients proposes a value to Paxos,
 * and we have consensus when a majority of systems running. Paxos agrees on one of the proposed values.
 */
public class PaxosServer implements PaxosInterface {
	private Proposer proposer;
	private Learner learner;
	private Acceptor acceptor;

	/**
	 * Initiates paxos server.
	 * @param serverNumber current server number.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	public PaxosServer(int serverNumber) throws RemoteException {
		proposer = new Proposer();
		acceptor = new Acceptor();
		learner = new Learner();

		proposer.start();
		learner.start();
		acceptor.setServerNumber(serverNumber);
	}

	/**
	 * Get key-value pair from store.
	 * @param key key value from client.
	 * @return get result.
	 */
	@Override
	public String get(int key) {
		return proposer.propose(key, 1);
	}

	/**
	 * Put key-value pair from store.
	 * @param key key value from client.
	 * @return put result.
	 */
	@Override
	public String put(int key) {
		return proposer.propose(key, 2);
	}

	/**
	 * Delete key-value pair from store
	 * @param key key value from client.
	 * @return deleted result.
	 */
	@Override
	public String delete(int key) {
		return proposer.propose(key, 3);
	}

	/**
	 * Send Prepare(n) message to (at least) a majority of acceptors.
	 * @param proposalId unique id for each proposal.
	 * @return Whether there are PROMISE responses from a majority of acceptors.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	@Override
	public boolean sendPrepare(int proposalId) throws RemoteException {
		return acceptor.promise(proposalId);
	}

	/**
	 * The proposer tells all the acceptors (that are live) what value to accept.
	 * @param proposalId proposal id from proposer.
	 * @param key key value from client.
	 * @param action request type from client.
	 * @return Whether the consensus was reached.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	@Override
	public boolean accept(int proposalId, int key, int action) throws RemoteException {
		return acceptor.accepted(proposalId, key, action);
	}

	/**
	 * Send the value to learners to commit the message.
	 * @param key key value from client.
	 * @param action request type from client.
	 * @return Whether the value was committed.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	@Override
	public String commit(int key, int action) throws RemoteException {
		return learner.commit(key, action);			
	}
}
