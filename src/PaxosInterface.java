import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PaxosInterface extends Remote {
	/**
	 *
	 * @param key
	 * @return
	 * @throws RemoteException
	 */
	String get(int key) throws RemoteException;

	/**
	 *
	 * @param key
	 * @return
	 * @throws RemoteException
	 */
	String put(int key) throws RemoteException;

	/**
	 *
	 * @param key
	 * @return
	 * @throws RemoteException
	 */
	String delete(int key) throws RemoteException;

	/**
	 *
	 * @param proposalId
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 * @throws SocketTimeoutException
	 */
	boolean prepare(int proposalId, int key, int action) throws RemoteException, SocketTimeoutException;

	/**
	 *
	 * @param proposalId
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 * @throws SocketTimeoutException
	 */
	boolean accept(int proposalId, int key, int action) throws RemoteException, SocketTimeoutException;

	/**
	 *
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 * @throws SocketTimeoutException
	 */
	String commit(int key, int action) throws RemoteException, SocketTimeoutException;
}
