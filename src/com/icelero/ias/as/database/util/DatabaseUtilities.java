package com.icelero.ias.as.database.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DatabaseUtilities {

	private static Logger LOGGER = Logger.getLogger(DatabaseUtilities.class);

	private DatabaseUtilities() {

	}

	private static Connection getConnection() {
		Connection connection = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/ias");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(dataSource.getClass().getName());
			}
			connection = dataSource.getConnection();
		} catch (NamingException e) {
			LOGGER.error("Error fetching connection [" + e.getMessage() + "]", e);
		} catch (SQLException e) {
			LOGGER.error("Error fetching connection [" + e.getMessage() + "]", e);
		}
		return connection;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void closeResultSet(ResultSet rset) {
		try {
			if (rset != null) {
				rset.close();
			}
		} catch (Exception ignore) {

		}
	}

	public static void setAutoCommitTrue(Connection con) {
		try {
			con.setAutoCommit(true);
		} catch (Exception ignore) {

		}
	}

	public static void setAutoCommitFalse(Connection con) {
		try {
			con.setAutoCommit(false);
		} catch (Exception ignore) {

		}
	}

	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (Exception ignore) {

		}
	}

	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (Exception ignore) {

		}
	}

	public static Connection getReadConnection() {
		Connection connection = getConnection();
		print(connection);
		try {
			connection.setReadOnly(true);
			print(connection);
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		}
		return connection;
	}

	public static Connection getReadWriteConnection() {
		Connection connection = getConnection();
		print(connection);
		try {
			connection.setReadOnly(false);
			print(connection);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return connection;
	}

	public static void closeResources(ResultSet resultSet, Statement statement, Connection connection) {
		closeResultSet(resultSet);
		closeStatement(statement);
		closeConnection(connection);
	}

	private static void print(Connection connection) {
		if (LOGGER.isDebugEnabled()) {
			try {
				DatabaseMetaData metadata = connection.getMetaData();
				LOGGER.debug("Returning Connection [" + metadata.getURL() + "] From user [" + metadata.getUserName() + "]");
			} catch (Exception ex) {
				LOGGER.error("Error logging connection details: " + ex.getMessage(), ex);
			}
		}
	}

}
