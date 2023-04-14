/**
 * This class provides the pre-populated server port, server
 * name, server number and map size
 */
public interface Constants {
	int[] ports = {1234, 2345, 3451, 1112, 1113};

	/**
	 * The RMI Name for ServerA
	 */
	String SERVER1 = "Server1";

	/**
	 * The RMI Name for ServerB
	 */
	String SERVER2 = "Server2";

	/**
	 * The RMI Name for ServerC
	 */
	String SERVER3 = "Server3";

	/**
	 * The RMI Name for ServerD
	 */
	String SERVER4 = "Server4";

	/**
	 * The RMI Name for ServerE
	 */
	String SERVER5 = "Server5";

	/**
	 * Port number of serverA
	 */
	int SERVER1_PORT_NUM = ports[0];

	/**
	 * Port number of serverB
	 */
	int SERVER2_PORT_NUM = ports[1];

	/**
	 * Port number of serverC
	 */
	int SERVER3_PORT_NUM = ports[2];

	/**
	 * Port number of serverD
	 */
	int SERVER4_PORT_NUM = ports[3];

	/**
	 * Port number of serverE
	 */
	int SERVER5_PORT_NUM = ports[4];

	/**
	 * Size of the map in keystore
	 */
	int MAP_SIZE = 1500;

	/**
	 * Number of servers replicas
	 */
	int NUMBER_OF_SERVERS = 5;
}
