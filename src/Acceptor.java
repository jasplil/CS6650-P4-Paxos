import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 */
public class Acceptor extends KeyValueStore implements Runnable {
	private static Logger LOGGER = LogManager.getLogger(Acceptor.class.getName());

	private static int proposalId;
	private static int promisedId = (int) 1e9;
	private boolean active;
	private int serverNumber;

	public void setProposalId(int proposalId) {
		Acceptor.proposalId = proposalId;
	}

	public void start() {
		active = true;
	}

	public void setServerNumber(int serverNumber) {
		this.serverNumber = serverNumber;
	}

	/**
	 * On receiving prepare message, check for proposal id.
	 * If promised to ignore the request, than continue to ignore such request.
	 * If not, now promises to ignore any request with a proposal number lower than n
	 * and replies with promise
	 * @param proposalId
	 * @param key
	 * @param action
	 * @return
	 */
	public boolean promise(int proposalId, int key, int action) {
		return check(proposalId, key, action);
	}

	public boolean accept(int proposalId, int key, int action) {
		return check(proposalId, key, action);
	}

	/**
	 * On receiving Prepare message:
	 * Check for whether it previously promised to ignore requests with this proposal number.
	 * If so: ignores the message
	 * If not: it now promises to ignore any request with a proposal number lower than this proposal number,
	 * and replies with Promise message.
	 * @param proposalId
	 * @param key
	 * @param action
	 * @return
	 */
	private boolean check(int proposalId, int key, int action) {
		// TODO: replies with promise(promiseId)
		try {
			// Randomly set a server to "fail"
			if (((int)((Math.random()*Constants.NUMBER_OF_SERVERS)+1)) == serverNumber) {
				LOGGER.info("Server" + serverNumber + " going to sleep");
				Thread.sleep(10000);
			}
		} catch (InterruptedException ie){
			// TODO: add exception handle
		}

		if (promisedId == (int) 1e9) {
			promisedId = proposalId;
		} else {
			if (proposalId > promisedId) {
				promisedId = proposalId;
			}
		}

		LOGGER.info("CHECK THIS PROMISED ID: " + promisedId);

		if (Acceptor.proposalId > proposalId) return false;

		if (super.checkAction(key, action)) {
			setProposalId(proposalId);
			return true;
		}

		return false;
	}

	@Override
	public void run() {}
}
