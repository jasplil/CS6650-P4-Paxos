import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class is for the Acceptor. It accepts certain proposed values from
 * proposers and let proposers know if something else was accepted.
 * A response from an acceptor represents a vote for a particular proposal.
 */
public class Acceptor extends KeyValueStore implements Runnable {
	private static Logger LOGGER = LogManager.getLogger(Acceptor.class.getName());
	private static int promisedId = (int) 1e9;
	private static int serverNumber;

	/**
	 * Set the number of server.
	 * @param serverNumber number of server.
	 */
	public void setServerNumber(int serverNumber) {
		this.serverNumber = serverNumber;
	}

	/**
	 * On receiving PREPARE message, check for proposal id.
	 * Whether the ID is bigger than any round it has previously received.
	 * If yes, store the ID number, respond with a PROMISE message.
	 * If not, do not respond.
	 * @param proposalId proposal id sent by proposer
	 * @return Whether to respond to the PREPARE message.
	 */
	public boolean promise(int proposalId) {
		return checkPromise(proposalId);
	}

	/**
	 * Whether the is ID the largest it has seen so far.
	 * If yes, reply with an ACCEPTED message and send ACCEPTED(ID, VALUE) to all learners.
	 * if no, do not respond.
	 * @param proposalId proposal id from proposer.
	 * @param key key value sent by client
	 * @param action request type.
	 * @return Whether the server send ACCEPTED message or not
	 */
	public boolean accepted(int proposalId, int key, int action) {
		return checkAccepted(proposalId, key, action);
	}

	/**
	 * On receiving PREPARE message:
	 * Check for whether it previously promised to ignore requests with this proposal number.
	 * If so: ignores the message
	 * If not: it now promises to ignore any request with a proposal number lower than this proposal number,
	 * and replies with PROMISE message.
	 * @param proposalId proposal id sent by proposer.
	 * @return Whether to reply with PROMISE message or not.
	 */
	private boolean checkPromise(int proposalId) {
		failServer();

		if (promisedId == (int) 1e9) {
			promisedId = proposalId;
			return true;
		} else {
			if (proposalId > promisedId) {
				promisedId = proposalId;
				return true;
			}
		}

		return false;
	}

	/**
	 * Decides whether to accept the proposal.
	 * @param proposalId proposal id sent by proposer.
	 * @param key key value sent by client.
	 * @param action type of request sent by client.
	 * @return whether to accept the proposal.
	 */
	private boolean checkAccepted(int proposalId, int key, int action) {
		failServer();

		if (promisedId > proposalId) {
			return false;
		} else if (super.checkAction(key, action)) {
			return true;
		}

		return false;
	}

	/**
	 * Randomly set server to sleep for 1,000 milliseconds.
	 */
	public void failServer() {
		try {
			if (((int)((Math.random() * Constants.NUMBER_OF_SERVERS) + 1)) == serverNumber) {
				LOGGER.info("Server " + serverNumber + " going to sleep");
				Thread.sleep(1000);
			}
		} catch (InterruptedException ie) {
			LOGGER.debug(ie);
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void run() {}
}
