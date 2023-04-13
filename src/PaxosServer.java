import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

public class PaxosServer implements PaxosInterface {
	private Proposer proposer;
	private Learner learner;
	private Acceptor acceptor;

	/**
	 * Initiates paxos server
	 * @param serverNumber
	 * @throws RemoteException
	 */
	public PaxosServer(int serverNumber) throws RemoteException {
		proposer = new Proposer();
		acceptor = new Acceptor();
		learner = new Learner();

		proposer.start();
		learner.start();
		acceptor.start();
		acceptor.setServerNumber(serverNumber);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@Override
	public String get(int key) {
		return proposer.propose(key, 1);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@Override
	public String put(int key){
		return proposer.propose(key, 2);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@Override
	public String delete(int key){
		return proposer.propose(key, 3);
	}

	/**
	 * Send Prepare(n) message to (at least) a majority of acceptors.
	 * @param proposalId unique id for each proposal
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 */
	@Override
	public boolean prepare(int proposalId, int key, int action) throws RemoteException {
		return acceptor.promise(proposalId, key, action);
	}

	/**
	 *
	 * @param proposalId
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 */
	@Override
	public boolean accept(int proposalId, int key, int action) throws RemoteException {
		return acceptor.accept(proposalId, key, action);
	}

	/**
	 *
	 * @param key
	 * @param action
	 * @return
	 * @throws RemoteException
	 */
	@Override
	public String commit(int key, int action) 
			throws RemoteException {
		return learner.commit(key, action);			
	}
}
