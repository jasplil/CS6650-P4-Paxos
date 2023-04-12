import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 * This class
 */
public class KeyValueStore {
	private static Logger LOGGER = LogManager.getLogger(KeyValueStore.class.getName());
	private HashMap<Integer, Integer> keyValueMap;

	/**
	 * Generate key value pairs
	 */
	public KeyValueStore() {
		keyValueMap = new HashMap<>();

		for (int i = 1; i <= Constants.MAP_SIZE ; i++){
			keyValueMap.put(i, getValue(i));
		}
	}

	/**
	 * Get the value according to key input.
	 * @param key the key of key-value store.
	 * @return the value according to the key.
	 */
	private static int getValue(int key) {
		int val = key * key;
		LOGGER.info("The value is: " + val);

		return val;
	}

	/**
	 * Returns the key value.
	 * @param key key value.
	 * @return key value.
	 */
	public String getKey(int key) {
		String res = "The key is: " + key;
		LOGGER.info(res);

		return res;
	}

	/**
	 * Put request to add key value pair to the map
	 * @param key key
	 * @return
	 */
	public String put(int key) {
		String res  = "Added key : " + key;
		keyValueMap.put(key, getValue(key));

		LOGGER.info(res);
		return res;
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public String deleteKey(int key) {
		String res  = "Deleted key: " + key;
		Set<Integer> set = keyValueMap.keySet();
	    Iterator<Integer> itr = set.iterator();

	    while (itr.hasNext()) {
	        Integer obj = itr.next();
	        if (obj.equals(key)) {
				itr.remove();
				LOGGER.info(res);
				break;
			}
	    }

	    return res;
	}

	/**
	 *
	 * @param key
	 * @param action
	 * @return
	 */
	public boolean checkAction(int key, int action) {
		switch(action) {
		case 1:if(keyValueMap.containsKey(key))
				return true;				
		case 2:if(!keyValueMap.containsKey(key))
				return true;				
		case 3:if(keyValueMap.containsKey(key))
				return true;				
		}
		return false;
	}
}
