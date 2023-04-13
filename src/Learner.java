/**
 * This class is the Learner. It learns the agreed upon value.
 * It is responsible to maintain the state of the server and handles the commits to it.
 */
public class Learner extends KeyValueStore implements Runnable{
	public void start() {}

	/**
	 * Commits the message from client.
	 * @param key key value from client.
	 * @param action request type from client.
	 * @return Committed value of the requests result.
	 */
	public String commit(int key, int action) {
		String response = "";

		switch(action) {
			case 1:
				response = super.getKey(key);
				break;
			case 2:
				response = super.put(key);
				break;
			case 3:
				response = super.delete(key);
				break;
		}

		return response;
	}

	@Override
	public void run() {}
}
