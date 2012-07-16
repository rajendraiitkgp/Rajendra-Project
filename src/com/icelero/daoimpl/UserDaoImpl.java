package com.icelero.daoimpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.icelero.dao.UserDAO;
import com.icelero.databaseutils.DatabaseConnection;
import com.icelero.domain.User;

public class UserDaoImpl implements UserDAO {
	private static Logger logger = Logger
			.getLogger("com.icelero.daoimpl.UserDaoImpl");

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		String query = "SELECT id,name,imsi FROM users";
		try {
			connection = DatabaseConnection.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setImsiNumber(rs.getString("imsi"));
				users.add(user);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			DatabaseConnection.closeResultSet(rs);
			DatabaseConnection.closeStatement(stmt);
			DatabaseConnection.closeConnection(connection);
		}
		logger.debug("list size : " + users.size());
		return users;
	}

}