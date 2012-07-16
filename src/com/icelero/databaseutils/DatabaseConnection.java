package com.icelero.databaseutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DatabaseConnection {

	private static Logger logger = Logger.getLogger(DatabaseConnection.class);

	private DatabaseConnection() {
	}

	public static Connection getConnection() {
		Context initContext;
		Connection conn = null;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/iceleroDB");
			conn = ds.getConnection();
			logger.debug("connection created : " + conn);
		} catch (NamingException e) {
			logger.error(e);
		} catch (SQLException e) {
			logger.error(e);
		}
		return conn;
	}

	// public static Connection getConnection() {
	// Connection connection = null;
	// try {
	// Class.forName("com.mysql.jdbc.Driver");
	// logger.debug("In database class to get a connection");
	// connection = DriverManager
	// .getConnection("jdbc:mysql://localhost:3306/icelero",
	// "root", "telugudb");
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return connection;
	// }

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

	public static void setAutoCommitTrue(Connection connection) {
		try {
			connection.setAutoCommit(true);
		} catch (Exception ignore) {

		}
	}
}