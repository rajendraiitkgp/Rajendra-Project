package com.icelero.ias.as.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icelero.ias.as.database.util.DatabaseUtilities;
import com.icelero.ias.as.domain.User;

public class UserDAO {
	private static Logger LOGGER = Logger.getLogger(UserDAO.class);

	public User read(Connection connection, String userID) {
		User user = null;
		if (!StringUtils.isBlank(userID)) {
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String query = "SELECT userid, password, email FROM user WHERE userid = ?";
			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, userID);
				rset = pstmt.executeQuery();
				while (rset.next()) {
					user = new User();
					user.setUserid(rset.getString("userid"));
					user.setPassword(rset.getString("password"));
					user.setEmail(rset.getString("email"));
				}
			} catch (SQLException e) {
				LOGGER.error("Error fetching User with userid [" + userID + "]", e);
			} finally {
				DatabaseUtilities.closeResultSet(rset);
				DatabaseUtilities.closeStatement(pstmt);
			}
		}
		return user;
	}
	
	public User readWithEmail(Connection connection, String email) {
		User user = null;
		if (!StringUtils.isBlank(email)) {
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String query = "SELECT userid, password, email FROM user WHERE email = ?";
			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, email);
				rset = pstmt.executeQuery();
				while (rset.next()) {
					user = new User();
					user.setUserid(rset.getString("userid"));
					user.setPassword(rset.getString("password"));
					user.setEmail(rset.getString("email"));
				}
			} catch (SQLException e) {
				LOGGER.error("Error fetching User with email [" + email + "]", e);
			} finally {
				DatabaseUtilities.closeResultSet(rset);
				DatabaseUtilities.closeStatement(pstmt);
			}
		}
		return user;
	}

	public boolean create(Connection connection, User user) {
		boolean result = false;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO user (userid, password, email) values (?, ?, ?)";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			int rowCount = pstmt.executeUpdate();
			result = rowCount != 0;
			LOGGER.debug("User [" + user.getUserID() + ":" + user.getPassword() + ";" + user.getEmail() + "] created");
		} catch (SQLException e) {
			LOGGER.error("Error creating user [" + user.getUserID() + ":" + user.getPassword() + ";" + user.getEmail() + "]", e);
		} finally {
			DatabaseUtilities.closeStatement(pstmt);
		}
		return result;
	}

}