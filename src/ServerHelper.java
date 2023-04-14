import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.*;

/**
 * This class provides helper functions for 5 replica servers.
 * ServerA, ServerB, ServerC, ServerD, ServerE.
 */
public class ServerHelper {
	private static Logger LOGGER = LogManager.getLogger(ServerHelper.class.getName());

	/**
	 * This function returns the map that stores key-value.
	 * @return the map of the key-value stores.
	 */
	public static Map<String, String> getMap() {
		Map<String, String> map = new HashMap<>();

		try {
			Properties properties = new Properties();
			// Read in ports
			InputStream in = ServerHelper.class.getResourceAsStream("servers.properties");
			properties.load(in);			
			Enumeration<?> e = properties.propertyNames();

			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = properties.getProperty(name);
				map.put(name, value);
			}
		} catch (FileNotFoundException e) {
			LOGGER.debug("Properties not found" + e);
		} catch (IOException ioException) {
			LOGGER.debug("IOException while reading the file" + ioException);
		}

		return map;
	}

	/**
	 * Returns the pre-populated server ports
	 * @param value server name
	 * @return server port
	 */
	public static int getPortNumber(String value) {
		switch(value) {
			case Constants.SERVER1:
				return Constants.SERVER1_PORT_NUM;
			case Constants.SERVER2:
				return Constants.SERVER2_PORT_NUM;
			case Constants.SERVER3:
				return Constants.SERVER3_PORT_NUM;
			case Constants.SERVER4:
				return Constants.SERVER4_PORT_NUM;
			case Constants.SERVER5:
				return Constants.SERVER5_PORT_NUM;
			default:
				return 0;
		}
	} 
}
