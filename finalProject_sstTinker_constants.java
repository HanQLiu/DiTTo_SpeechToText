package Assignments.finalProject_WastsonImplementation_SpeechToText;

public class finalProject_sstTinker_constants {
	// Credentials
	public static final String SPEECH_TO_TEXT_PASSWORD = "insert API key";
	public static final String SPEECH_TO_TEXT_URL = "insert host";
	public static final String STT_TEST_FILE = "insert stt test file absolute path";

	//STT parameters
	public static final String SST_Model = "en-US_BroadbandModel";

	// PostGreSQL properties
	public static final String POSTGRESQL_HOST = "insert host";
	public static final String POSTGRESQL_USERNAME = "admin"; // credentials from cloud.ibm.com
	public static final String POSTGRESQL_PASSWORD = "insert db password"; //credentials from cloud.ibm.com
	public static final String POSTGRESQL_DATABASE_NAME = "insert db name"; // Database
	public static final String POSTGRESQL_TABLE_NAME = "dittoDictonary"; // Database
	public static final String POSTGRESQL_PORT = "insert port"; // Database
	public static final String POSTGRESQL_SSLMODE = "verify-full"; // Database

	// PostGreSQL Properties Arguments
	public static final String POSTGRESQL_HOST_ARG = "host"; // Connection url @ cloud.ibm.com
	public static final String POSTGRESQL_USERNAME_ARG = "user"; // credentials from cloud.ibm.com
	public static final String POSTGRESQL_PASSWORD_ARG = "password"; //credentials from cloud.ibm.com
	public static final String POSTGRESQL_DATABASE_NAME_ARG = "dbname";
	public static final String POSTGRESQL_PORT_ARG = "port"; // Database
	public static final String POSTGRESQL_SSLMODE_ARG = "sslmode"; // Database

	// DB methods
	public static final int PARAM_INDEX = 1;
	public static final int RECORD_ID_COLUMN_NAMES_LIST_INDEX = 0;
	public static final int RECORD_LINE_COLUMN_NAMES_LIST_INDEX = 1;
	public static final String CREATE_TABLE = "CREATE TABLE " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + '\n' +
			"(ID SERIAL," +
			" WORD TEXT NOT NULL UNIQUE, " +
			" DEFINITION BOOLEAN NOT NULL UNIQUE)";
	public static final String INSERT_RECORD = "INSERT INTO " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + '\n'  + "(WORD, DEFINITION) VALUES " +  "(%s, %s)";
	public static final String GET_All_RECORDS = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " ORDER by Id";
	public static final String GET_SINGLE_RECORD_BY_ID = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " WHERE ID=%s";
	public static final String GET_SINGLE_RECORD_BY_WORD = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " WHERE WORD=%s";
	public static final String GET_TABLE_COLUMN_NAMES = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME;
	public static final String UPDATE_RECORD = "UPDATE " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " SET WORD=%s, DEFINITION=%s WHERE ID=%s";
	public static final String SELECT_TABLE = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME;
	public static final String REMOVE_RECORD = "SELECT * FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " WHERE ID=%s";
	public static final String GET_DEFINITION = "SELECT DEFINITION FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME + " WHERE WORD=%S";
	public static final String GET_MAX_ID = " SELECT MAX(ID) FROM " + finalProject_sstTinker_constants.POSTGRESQL_TABLE_NAME;

	// GUI parameters
	public static final int MAX_LINE_LENGTH = 20;
}
