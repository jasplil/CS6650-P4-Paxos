import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of paxos server.
 */
public interface PaxosInterface extends Remote {
	/**
	 * Get key-value pair from store.
	 * @param key key value from client.
	 * @return get result.
	 */
	String get(int key) throws RemoteException;

	/**
	 * Put key-value pair from store.
	 * @param key key value from client.
	 * @return put result.
	 */
	String put(int key) throws RemoteException;

	/**
	 * Delete key-value pair from store
	 * @param key key value from client.
	 * @return deleted result.
	 */
	String delete(int key) throws RemoteException;

	/**
	 * Send Prepare(n) message to (at least) a majority of acceptors.
	 * @param proposalId unique id for each proposal.
	 * @return Whether there are PROMISE responses from a majority of acceptors.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	boolean sendPrepare(int proposalId) throws RemoteException, SocketTimeoutException;

	/**
	 * The proposer tells all the acceptors (that are live) what value to accept.
	 * @param proposalId proposal id from proposer.
	 * @param key key value from client.
	 * @param action request type from client.
	 * @return Whether the consensus was reached.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	boolean accept(int proposalId, int key, int action) throws RemoteException, SocketTimeoutException;

	/**
	 * Send the value to learners to commit the message.
	 * @param key key value from client.
	 * @param action request type from client.
	 * @return Whether the value was committed.
	 * @throws RemoteException exception during the execution of a remote method call.
	 */
	String commit(int key, int action) throws RemoteException, SocketTimeoutException;
}
