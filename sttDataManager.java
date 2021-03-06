package Assignments.finalProject_WastsonImplementation_SpeechToText;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class sttDataManager {
	public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(sttDataManager.class);

	public static class UnavailableDataObjectException extends Exception {

		// declare instance method for Exception
		public UnavailableDataObjectException(String message) {
			super(message);

		}

	}

	public static class DataManager {
		public static Connection postgresqlConnection = null;
		public static Properties props = new Properties();
		public static List<String> columnNamesList = new ArrayList<>();

		/*
		#######################     Handlers      #######################
		*/

		// Initialize class, connect to database, create table if it isn't already present.
		public static void initializeDataManager() throws SQLException, sttDataManager.UnavailableDataObjectException {
			Boolean isSuccess;
			props.setProperty(finalProject_sstTinker_constants.POSTGRESQL_HOST_ARG, finalProject_sstTinker_constants.POSTGRESQL_HOST);
			props.setProperty(finalProject_sstTinker_constants.POSTGRESQL_DATABASE_NAME_ARG, finalProject_sstTinker_constants.POSTGRESQL_DATABASE_NAME);
			props.setProperty(finalProject_sstTinker_constants.POSTGRESQL_USERNAME_ARG, finalProject_sstTinker_constants.POSTGRESQL_USERNAME);
			props.setProperty(finalProject_sstTinker_constants.POSTGRESQL_PASSWORD_ARG, finalProject_sstTinker_constants.POSTGRESQL_PASSWORD);
			props.setProperty(finalProject_sstTinker_constants.POSTGRESQL_PORT_ARG, finalProject_sstTinker_constants.POSTGRESQL_PORT);

			try {
				isSuccess = setDatabaseConnection();
				if (isSuccess) {
					isSuccess = executeQuery(finalProject_sstTinker_constants.SELECT_TABLE);
					if (!isSuccess) {
						isSuccess = executeQuery(finalProject_sstTinker_constants.CREATE_TABLE);
						if (isSuccess) {
							setColumnNamesList();
						} else throw new sttDataManager.UnavailableDataObjectException("Table unavailable.");
					}
				} else throw new sttDataManager.UnavailableDataObjectException("Database unavailable.");
			} catch(SQLException | sttDataManager.UnavailableDataObjectException ex) {
				LOG.info(ex.getClass().getName() + ex.getMessage());
				printSQLException((SQLException) ex);
			}

		}

		// Exception Handler
		public static void printSQLException(SQLException ex) {
			for (Throwable e : ex) {
				if (e instanceof Exception) {
					e.printStackTrace(System.err);
					System.err.println("SQLState: " + ((SQLException) e).getSQLState());
					System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
					System.err.println("Message: " + e.getMessage());
					Throwable t = ex.getCause();
					while (t != null) {
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
				}
			}
		}

		// Close data base connection
		public static void closeDatabaseConnection() {
			try {
				postgresqlConnection.close();
			} catch(SQLException ex) {
				LOG.info(ex.getClass().getName() + ex.getMessage());
				printSQLException(ex);
			}
		}

		// Map builder for getTableData method
		public static Map<String, String> mapBuilder(ResultSet resultSetAtCurrentRecord, List<String> columnNamesList) {
			Map<String, String> recordMap = new HashMap<>();
			for (String name : columnNamesList) {
				try {
					recordMap.put(name, resultSetAtCurrentRecord.getString(name));
				} catch(SQLException se) {
					LOG.info(se.getClass().getName() + se.getMessage());
					printSQLException(se);
				}
			}
			return recordMap;
		}


		/*
		#######################     Setters      #######################
		*/

		public static Boolean setDatabaseConnection() throws SQLException {
			Boolean isSuccess = false;

			try {
				postgresqlConnection = DriverManager.getConnection(finalProject_sstTinker_constants.POSTGRESQL_HOST, props);
				isSuccess = true;
			} catch(SQLException ex) {
				LOG.info(ex.getClass().getName() + ex.getMessage());
				printSQLException(ex);
			}
			return isSuccess;
		}


		// Supports setDatabaseInfrastructure. Creates a list of column names for use in getter methods.
		public static void setColumnNamesList() {
			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(finalProject_sstTinker_constants.GET_TABLE_COLUMN_NAMES);
				ResultSet tableDataResultSet = prepStat.executeQuery();
				ResultSetMetaData tableResultSetMetaData = tableDataResultSet.getMetaData();
				int numColumns = tableResultSetMetaData.getColumnCount();
				for (int i=0; i < numColumns; i++) {
					columnNamesList.add(tableResultSetMetaData.getColumnName(i));
				}
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}
		}

		// Supports setDatabaseInfrastructure. Executes database queries to check table contents or to create initial table object.
		public static Boolean executeQuery(String command) throws SQLException {
			Boolean isSuccessful = false;

			try {
				//PreparedStatement prepStat = postgresqlConnection.prepareStatement(command);
				//ResultSet managerResultSet = prepStat.executeQuery();

				Statement statement = postgresqlConnection.createStatement();
				isSuccessful = statement.execute(command);
				LOG.info("result of query is " + isSuccessful.toString());
				statement.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}
			return isSuccessful;
		}

		public static List<Map<String, String>> setRecord(String word, String definition) {
			List<Map<String, String>> resultRecords = new ArrayList<>();
			String formattedQuery = String.format(finalProject_sstTinker_constants.INSERT_RECORD, word, definition);

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				ResultSet tableDataResultSet = prepStat.executeQuery();

				while (tableDataResultSet.next()) {
					resultRecords.add(mapBuilder(tableDataResultSet, columnNamesList));
				}
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

			return resultRecords;
		}

		public static void setUpdateRecord(List<String> newEntry) {
			int nextEntryId = getLastId();
			//@TODO Reminder:record parameters must be added in this order WORD=%s, DEFINITION=%s WHERE Id=%s
			String formattedQuery = String.format(finalProject_sstTinker_constants.UPDATE_RECORD,
					newEntry.get(0), // updated word
					nextEntryId);  //updated definition
			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				prepStat.executeQuery();
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

		}

		public static void setRemoveRecord(Integer recordId) {

			String formattedQuery = String.format(finalProject_sstTinker_constants.REMOVE_RECORD, recordId.toString());

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				prepStat.executeQuery();
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

		}

		/*
		#######################     Getters      #######################
		*/

		public static int getLastId(){
			Boolean isSuccess = false;
			int id=0;

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(finalProject_sstTinker_constants.GET_MAX_ID);
				prepStat.executeQuery();
				ResultSet rs = prepStat.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getInt(1);
					isSuccess = true;
				}
				prepStat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isSuccess) return id++;
			else return 0;
		}

		public static String getDefinition(String word) {
			String formattedQuery = finalProject_sstTinker_constants.GET_DEFINITION + word;
			String definition = null;
			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				ResultSet tableDataResultSet = prepStat.executeQuery();
				while (tableDataResultSet.next()) {
					definition = tableDataResultSet.toString();
				}
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}
			return definition;
		}


		public static List<Map<String, String>> getRecordById(Integer recordId) {
			List<Map<String, String>> resultRecords = new ArrayList<>();
			String formattedQuery = String.format(finalProject_sstTinker_constants.GET_SINGLE_RECORD_BY_ID, recordId.toString());

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				ResultSet tableDataResultSet = prepStat.executeQuery();

				while (tableDataResultSet.next()) {
					resultRecords.add(mapBuilder(tableDataResultSet, columnNamesList));
				}
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

			return resultRecords;
		}

		public static List<Map<String, String>> getRecordbyWord(String recordWord) {
			List<Map<String, String>> resultRecords = new ArrayList<>();
			String formattedQuery = String.format(finalProject_sstTinker_constants.GET_SINGLE_RECORD_BY_WORD, recordWord);

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(formattedQuery);
				ResultSet tableDataResultSet = prepStat.executeQuery();

				while (tableDataResultSet.next()) {
					resultRecords.add(mapBuilder(tableDataResultSet, columnNamesList));
				}
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

			return resultRecords;
		}

		// Get all data from Table and return as a List of Maps for which the Key is the column name and the value is the data
		public static List<Map<String, String>> getTable() {
			List<Map<String, String>> resultRecords = new ArrayList<>();

			try {
				PreparedStatement prepStat = postgresqlConnection.prepareStatement(finalProject_sstTinker_constants.GET_All_RECORDS);
				ResultSet tableDataResultSet = prepStat.executeQuery();

				while (tableDataResultSet.next()) {
					resultRecords.add(mapBuilder(tableDataResultSet, columnNamesList));
				}
				prepStat.close();
			} catch(SQLException se) {
				LOG.info(se.getClass().getName() + se.getMessage());
				printSQLException(se);
			}

			return resultRecords;
		}
	}
}

