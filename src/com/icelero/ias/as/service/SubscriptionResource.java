package com.icelero.ias.as.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icelero.ias.as.cryptography.CryptUtilities;
import com.icelero.ias.as.database.dao.UserDAO;
import com.icelero.ias.as.database.util.DatabaseUtilities;
import com.icelero.ias.as.domain.User;
import com.icelero.ias.as.service.payload.Subscriber;
import com.icelero.ias.as.service.response.SubscriptionResponse;
import com.icelero.ias.as.utilities.StatusCode;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/subscribeTrial")
public class SubscriptionResource {
	private static Logger LOGGER = Logger.getLogger(SubscriptionResource.class);

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public SubscriptionResponse subscribe(@FormParam("payload") String payload) {
		Connection connection = null;
		try {
			if (StringUtils.isBlank(payload)) {
				throw new WebApplicationException(getResponse(StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE));
			}
			Subscriber subscriber = readPayload(payload);
			if (!isValid(subscriber)) {
				throw new WebApplicationException(getResponse(StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE));
			}

			connection = DatabaseUtilities.getReadWriteConnection();
			UserDAO userDAO = new UserDAO();

			User databaseUser = userDAO.read(connection, subscriber.getIceleroId());
			if (databaseUser != null) {
				throw new WebApplicationException(getResponse(StatusCode.SUBSCRIPTION_ID_UNAVAILABLE));
			}
			databaseUser = userDAO.readWithEmail(connection, subscriber.getEmail());
			if (databaseUser != null) {
				throw new WebApplicationException(getResponse(StatusCode.SUB_ALREADY_EXISTS));
			}

			databaseUser = createUser(userDAO, connection, subscriber);

			if (databaseUser == null) {
				throw new WebApplicationException(getResponse(StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE));
			}
		} catch (Exception e) {
			LOGGER.error(e);
			if (e instanceof WebApplicationException) {
				throw (WebApplicationException) e;
			}
			throw new WebApplicationException(getResponse(StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE));
		} finally {
			DatabaseUtilities.closeConnection(connection);
		}
		return SubscriptionResponse.SUBSCRIPTION_SUCCESS_RESPONSE;
	}

	private User createUser(UserDAO userDAO, Connection connection, Subscriber subscriber) {
		User user = new User(subscriber.getIceleroId(), CryptUtilities.encryptPassword(subscriber.getPassword()), subscriber.getEmail());
		boolean result = userDAO.create(connection, user);
		if (result) {
			return user;
		}
		return null;
	}

	private boolean isValid(Subscriber subscriber) {
		return subscriber != null && !StringUtils.isBlank(subscriber.getIceleroId()) && !StringUtils.isBlank(subscriber.getPassword())
				&& !StringUtils.isBlank(subscriber.getEmail());
	}

	private Subscriber readPayload(String payload) throws UnsupportedEncodingException {
		Subscriber subscriber = null;
		try {
			InputStream stream = new ByteArrayInputStream(payload.getBytes("UTF-8"));
			subscriber = (Subscriber) JAXB.unmarshal(stream, Subscriber.class);
		} catch (Exception e) {
			LOGGER.error("Unable to parse following payload:\n" + payload, e);
		}
		return subscriber;
	}

	private Response getResponse(StatusCode statusCode) {
		Status status = Status.BAD_REQUEST;
		SubscriptionResponse payload = SubscriptionResponse.getResponseFor(statusCode);
		if (StatusCode.SUBSCRIPTION_SUCCESS.getId() == statusCode.getId()) {
			status = Status.OK;
		}
		return Response.status(status).entity(payload).type(MediaType.APPLICATION_XML).build();

	}

}