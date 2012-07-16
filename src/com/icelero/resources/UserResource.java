package com.icelero.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.icelero.dao.UserDAO;
import com.icelero.daoimpl.UserDaoImpl;
import com.icelero.domain.User;

@Path("/users")
public class UserResource {
	private static Logger logger = Logger
			.getLogger("com.icelero.resources.UserResource");

	@GET
	@Produces("application/xml")
	@Path("/getallusers/")
	public List<User> getAllUsersInfo() {
		List<User> users = null;
		try {
			UserDAO userDAO = new UserDaoImpl();
			users = userDAO.getAllUsers();
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("size : " + users.size());
		return users;
	}
}