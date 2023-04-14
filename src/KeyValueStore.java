import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 * This class provide the remote methods for client's PUT, GET and DELETE requests.
 */
public class KeyValueStore {
	private static Logger LOGGER = LogManager.getLogger(KeyValueStore.class.getName());
	private HashMap<Integer, Integer> map;

	/**
	 * Set prepopulate client key value pairs
	 */
	public KeyValueStore() {
		map = new HashMap<>();

		for (int i = 1; i <= Constants.MAP_SIZE ; i++){
			map.put(i, calValue(i));
		}
	}

	/**
	 * Get the value according to key input.
	 * @param key the key of key-value store.
	 * @return the value according to the key.
	 */
	private static int calValue(int key) {
		return key * key;
	}

	/**
	 * Returns the key value.
	 * @param key key value.
	 * @return key value.
	 */
	public String getKey(int key) {
		String res = "GET key: " + key + " value: " + map.get(key);
		LOGGER.info(res);

		return res;
	}

	/**
	 * Put request to add key value pair to the map
	 * @param key key
	 * @return put result.
	 */
	public String put(int key) {
		String res  = "PUT key: " + key + " value: " + calValue(key);
		map.put(key, calValue(key));
		LOGGER.info(res);

		return res;
	}

	/**
	 * Delete request to delete the key value pair
	 * @param key key value from client.
	 * @return delete result.
	 */
	public String delete(int key) {
		String res  = "Deleted key: " + key + " value: " + map.get(key);
		map.remove(key);
		LOGGER.info(res);

	    return res;
	}

	/**
	 * Check for the key value pair in the store.
	 * @param key key sent from client.
	 * @param action request type from client.
	 * @return whether the key value pair is in the store.
	 */
	public boolean checkAction(int key, int action) {
		switch (action) {
		case 1:
			if (map.containsKey(key))
				return true;
		case 2:
			if (!map.containsKey(key))
				return true;				
		case 3:
			if(map.containsKey(key))
				return true;				
		}

		return false;
	}
}
