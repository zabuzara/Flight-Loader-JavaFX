package com.ome.flight.tools;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.sql.CallableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Facade providing JDBC based operations.
 */
public class RelationalDatabases {

	/**
	 * Prevents external instantiation.
	 */
	private RelationalDatabases () {}
	

	/**
	 * Returns the column labels defined within the given JDBC metadata.
	 * @param jdbcTableMetaData the JDBC table metadata
	 * @return the column labels
	 * @throws NullPointerException if the given argument is {@code null}
	 * @throws SQLException if there is a JDBC related problem
	 */
	@SuppressWarnings("exports")
	static public List<String> columnLabels (final ResultSetMetaData jdbcTableMetaData) throws NullPointerException, SQLException {
		final List<String> columnLabels = new ArrayList<>();
		for (int index = 1, columnCount = jdbcTableMetaData.getColumnCount(); index <= columnCount; ++index)
			columnLabels.add(jdbcTableMetaData.getColumnLabel(index));

		return columnLabels;
	}


	/**
	 * Returns the remaining rows of the given result set as a list of row maps.
	 * @param jdbcResultSet the JDBC result set
	 * @param sortedColumns whether or not the columns shall be sorted 
	 * @return the list of row maps
	 * @throws NullPointerException if the given argument is {@code null}
	 * @throws SQLException if there is an SQL related problem
	 */
	@SuppressWarnings("exports")
	static public List<Map<String,Object>> toRowMaps (final ResultSet jdbcTable, final boolean sortedColumns) throws NullPointerException, SQLException {
		final List<String> columnLabels = columnLabels(jdbcTable.getMetaData());
		final List<Map<String,Object>> rows = new ArrayList<>();
		while (jdbcTable.next()) {
			final Map<String,Object> row = sortedColumns ? new TreeMap<>() : new HashMap<>();
			rows.add(row);

			for (final String columnLabel : columnLabels) {
				row.put(columnLabel, jdbcTable.getObject(columnLabel));
			}
		}
		return rows;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	static public <S,T> ObservableList<Map<S,T>> toGenerticRowMaps (final ResultSet jdbcTable, final boolean sortedColumns) throws NullPointerException, SQLException {
		final List<String> columnLabels = columnLabels(jdbcTable.getMetaData());
		final ObservableList<Map<S,T>> rows = FXCollections.observableArrayList();
		while (jdbcTable.next()) {
			final Map<S,T> row = sortedColumns ? new TreeMap<>() : new HashMap<>();
			rows.add(row);

			for (final String columnLabel : columnLabels) {
				row.put((S) columnLabel, (T)jdbcTable.getObject(columnLabel));
			}
		}
		return rows;
	}


	/**
	 * Executes any kind of SQL query statement (select, info, ...), returning the
	 * query result as a list of row maps.
	 * @param jdbcConnection the JDBC connection
	 * @param sql the SQL statement
	 * @param arguments the statement arguments, optionally including {@code null} values
	 * @return the list of row maps
	 * @throws NullPointerException if the given JDBC connection or SQL statement is {@code null}
	 * @throws SQLException if there is an SQL related problem
	 */
	@SuppressWarnings("exports")
	static public List<Map<String,Object>> executeQuery (final Connection jdbcConnection, final String sql, final Object... arguments) throws NullPointerException, SQLException {
		try (PreparedStatement jdbcStatement = jdbcConnection.prepareStatement(sql)) {
			for (int index = 0; index < arguments.length; ++index)
				jdbcStatement.setObject(index + 1, arguments[index]);

			try (ResultSet jdbcTable = jdbcStatement.executeQuery()) {
				return RelationalDatabases.toRowMaps(jdbcTable, true);
			}
		}
	}


	/**
	 * Executes any kind of SQL insert statement, optionally returning an
	 * auto-generated key.
	 * @param jdbcConnection the JDBC connection
	 * @param sql the SQL statement
	 * @param arguments the statement arguments, optionally including {@code null} values
	 * @return the auto-generated key, or {@code null} for none
	 * @throws NullPointerException if the given JDBC connection or SQL statement is {@code null}
	 * @throws IllegalStateException if the insert statement created more or less than one row,
	 * 									or if more than one column was auto-generated 
	 * @throws SQLException if there is an SQL related problem
	 */
	@SuppressWarnings("exports")
	static public Object executeInsert (final Connection jdbcConnection, final String sql, final Object... arguments) throws NullPointerException, IllegalStateException, SQLException {
		try (PreparedStatement jdbcStatement = jdbcConnection.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
			for (int index = 0; index < arguments.length; ++index)
				jdbcStatement.setObject(index + 1, arguments[index]);

			final int modifiedRowCount = jdbcStatement.executeUpdate();
			if (modifiedRowCount != 1) throw new IllegalStateException();

			try (ResultSet jdbcTable = jdbcStatement.getGeneratedKeys()) {
				final int columnCount = jdbcTable.getMetaData().getColumnCount();
				if (columnCount > 1) throw new IllegalStateException();
				if (columnCount == 0 || !jdbcTable.next()) return null;
				return jdbcTable.getObject(1);
			}
		}
	}


	/**
	 * Executes any kind of SQL update statement, returning the number of modified rows.
	 * @param jdbcConnection the JDBC connection
	 * @param sql the SQL statement
	 * @param arguments the statement arguments, optionally including {@code null} values
	 * @return the number of modified rows
	 * @throws NullPointerException if the given JDBC connection or SQL statement is {@code null}
	 * @throws SQLException if there is an SQL related problem
	 */
	@SuppressWarnings("exports")
	static public long executeUpdate (final Connection jdbcConnection, final String sql, final Object... arguments) throws NullPointerException, SQLException {
		try (PreparedStatement jdbcStatement = jdbcConnection.prepareStatement(sql)) {
			for (int index = 0; index < arguments.length; ++index)
				jdbcStatement.setObject(index + 1, arguments[index]);

			jdbcStatement.executeUpdate();
			return jdbcStatement.getLargeUpdateCount();
		}
	}


	/**
	 * Executes any kind of SQL delete statement, returning the number of deleted rows.
	 * @param jdbcConnection the JDBC connection
	 * @param sql the SQL statement
	 * @param arguments the statement arguments, optionally including {@code null} values
	 * @return the number of deleted rows
	 * @throws NullPointerException if the given JDBC connection or SQL statement is {@code null}
	 * @throws SQLException if there is an SQL related problem
	 */
	@SuppressWarnings("exports") 
	static public long executeDelete (final Connection jdbcConnection, final String sql, final Object... arguments) throws NullPointerException, SQLException {
		return executeUpdate(jdbcConnection, sql, arguments);
	}
	
	

	/**
	 * Execute any kind of SQL procedure calls with given dynamic parameters
	 * @param jdbcConnection
	 * @param arguments
	 * @return result
	 * @throws NullPointerException
	 * @throws IllegalStateException 
	 * @throws SQLException
	 * @author Omid Malekzadeh Eshtajarani
	 */
	@SuppressWarnings("exports")
	static public Object executeProcedure (final Connection jdbcConnection, final String databaseName, final String procedureName, final Object... arguments) throws NullPointerException, IllegalStateException, SQLException {
		final StringBuilder questionSymbols = new StringBuilder();
		for (int i = 0; i < arguments.length; i++) {
			questionSymbols.append(i == arguments.length - 1 ? "?" : "?, ");
		}		
		try (final CallableStatement callableStatement = jdbcConnection.prepareCall("{CALL "+databaseName+"."+procedureName+"("+questionSymbols+")}")) {			
			for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
				callableStatement.setString(argumentIndex + 1, Objects.toString(arguments[argumentIndex]));
			}			
			try (final ResultSet jdbcTable = callableStatement.executeQuery()) {	
				final int columnCount = jdbcTable.getMetaData().getColumnCount();
				if (columnCount == 0 || !jdbcTable.next()) return null;	
				return jdbcTable.getObject(1);
			}
		}
	}
}