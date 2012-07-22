package com.icelero.ias.as.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icelero.ias.as.database.util.DatabaseUtilities;
import com.icelero.ias.as.domain.Client;
import com.icelero.ias.as.domain.User;

public class ClientDAO {
	private static Logger LOGGER = Logger.getLogger(ClientDAO.class);

	public ClientDAO() {
	}

	public Client read(Connection connection, String clientID) {
		Client client = null;
		if (!StringUtils.isBlank(clientID)) {
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String query = "SELECT userid, clientid FROM client WHERE clientid = ?";
			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, clientID);
				rset = pstmt.executeQuery();
				while (rset.next()) {
					client = new Client();
					client.setUserID(rset.getString("userid"));
					client.setClientID(rset.getString("clientid"));
				}
			} catch (SQLException e) {
				LOGGER.error("Error fetching Client with clientID [" + clientID + "]", e);
			} finally {
				DatabaseUtilities.closeResultSet(rset);
				DatabaseUtilities.closeStatement(pstmt);
			}
		}
		return client;
	}

	public Set<Client> readWithUser(Connection connection, User user) {
		String userID = user.getUserID();
		Set<Client> clients = new HashSet<Client>();
		if (user != null && !StringUtils.isBlank(userID)) {
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String query = "SELECT userid, clientid FROM client WHERE userid = ?";
			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, userID);
				rset = pstmt.executeQuery();
				while (rset.next()) {
					Client client = new Client();
					client.setUserID(rset.getString("userid"));
					client.setClientID(rset.getString("clientid"));
					clients.add(client);
				}
			} catch (SQLException e) {
				LOGGER.error("Error fetching Clients for User with userID [" + userID + "]", e);
			} finally {
				DatabaseUtilities.closeResultSet(rset);
				DatabaseUtilities.closeStatement(pstmt);
			}
		}
		return clients;
	}

	public boolean create(Connection connection, Client client) {
		boolean result = false;
		PreparedStatement pstmt = null;
		String query = "INSERT INTO client (userid, clientid) VALUES (?, ?)";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, client.getUserID());
			pstmt.setString(2, client.getClientID());
			int rowCount = pstmt.executeUpdate();
			result = rowCount != 0;
			LOGGER.debug("Client [" + client.getUserID() + ":" + client.getClientID() + "]" + " created");
		} catch (SQLException e) {
			LOGGER.error("Error creating Client [" + client.getUserID() + ":" + client.getClientID() + "]");
		} finally {
			DatabaseUtilities.closeStatement(pstmt);
		}
		return result;
	}

}
