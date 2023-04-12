import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This class provides the pre-populated server port, server
 * name, server number and map size
 */
public interface Constants {

	/** Delete: The formatter for Date and timestamp **/
//	DateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss:SSS");

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
	int SERVER1_PORT_NUM = 12345;

	/**
	 * Port number of serverB
	 */
	int SERVER2_PORT_NUM = 12346;

	/**
	 * Port number of serverC
	 */
	int SERVER3_PORT_NUM = 12347;

	/**
	 * Port number of serverD
	 */
	int SERVER4_PORT_NUM = 12348;

	/**
	 * Port number of serverE
	 */
	int SERVER5_PORT_NUM = 12349;

	/**
	 * Size of the map in keystore
	 */
	int MAP_SIZE = 1500;

	/**
	 * Number of servers replicas
	 */
	int NUMBER_OF_SERVERS = 5;
}
