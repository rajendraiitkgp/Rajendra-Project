package com.icelero.ias.as.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.UUID;

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
import com.icelero.ias.as.database.dao.ClientDAO;
import com.icelero.ias.as.database.dao.UserDAO;
import com.icelero.ias.as.database.util.DatabaseUtilities;
import com.icelero.ias.as.domain.Client;
import com.icelero.ias.as.domain.User;
import com.icelero.ias.as.service.payload.SubscriberCredential;
import com.icelero.ias.as.service.response.RegistrationResponse;
import com.icelero.ias.as.service.response.RegistrationResponse.ReturnStatus;
import com.icelero.ias.as.utilities.ClientState;
import com.icelero.ias.as.utilities.StatusCode;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/clientReg")
public class RegistrationResource {
	private static Logger LOGGER = Logger.getLogger(RegistrationResource.class);

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public RegistrationResponse registration(@FormParam("payload") String payload) {

		Connection connection = null;
		try {
			if (StringUtils.isBlank(payload)) {
				throw new WebApplicationException(getFailureResponse(StatusCode.CLIENT_REG_UNRECOVERABLE_FAILURE));
			}
			SubscriberCredential subscriberCredential = readPayload(payload);
			if (!isValid(subscriberCredential)) {
				throw new WebApplicationException(getFailureResponse(StatusCode.CLIENT_REG_UNRECOVERABLE_FAILURE));
			}

			connection = DatabaseUtilities.getReadWriteConnection();
			UserDAO userDAO = new UserDAO();

			User databaseUser = userDAO.read(connection, subscriberCredential.getIceleroId());
			if (databaseUser == null) {
				throw new WebApplicationException(getFailureResponse(StatusCode.CLIENT_REG_FAILURE_USERNAME_PASSWORD));
			}
			if (!CryptUtilities.checkPassword(subscriberCredential.getPassword(), databaseUser.getPassword())) {
				throw new WebApplicationException(getFailureResponse(StatusCode.CLIENT_REG_FAILURE_USERNAME_PASSWORD));
			}
			// TODO what to do with the optional clientID sent by the client?
			String clientID = UUID.randomUUID().toString();

			Client client = createClient(connection, subscriberCredential.getIceleroId(), clientID);
			if (client == null) {
				throw new WebApplicationException(getFailureResponse(StatusCode.CLIENT_REG_UNRECOVERABLE_FAILURE));
			}
			return getSuccessResponse(client);
		} catch (Exception e) {
			LOGGER.error(e);
			if (e instanceof WebApplicationException) {
				throw (WebApplicationException) e;
			}
			throw new WebApplicationException(getFailureResponse(StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE));
		} finally {
			DatabaseUtilities.closeConnection(connection);
		}
	}

	private RegistrationResponse getSuccessResponse(Client client) {
		RegistrationResponse response = new RegistrationResponse();
		response.setClientId(client.getClientID());
		response.setClientState(ClientState.OPERATIONAL);
		response.setReturnStatus(new ReturnStatus(StatusCode.CLIENT_REG_SUCCESS));
		return response;
	}

	private Client createClient(Connection connection, String userID, String clientID) {
		ClientDAO clientDAO = new ClientDAO();
		Client client = new Client();
		client.setUserID(userID);
		client.setClientID(clientID);
		boolean result = clientDAO.create(connection, client);
		if (result) {
			return client;
		}
		return null;
	}

	private boolean isValid(SubscriberCredential subscriberCredential) {
		return subscriberCredential != null && !StringUtils.isBlank(subscriberCredential.getIceleroId())
				&& !StringUtils.isBlank(subscriberCredential.getPassword());
	}

	private SubscriberCredential readPayload(String payload) throws UnsupportedEncodingException {
		SubscriberCredential subscriberCredential = null;
		try {
			InputStream stream = new ByteArrayInputStream(payload.getBytes("UTF-8"));
			subscriberCredential = (SubscriberCredential) JAXB.unmarshal(stream, SubscriberCredential.class);
		} catch (Exception e) {
			LOGGER.error("Unable to parse following payload:\n" + payload, e);
		}
		return subscriberCredential;
	}

	private Response getFailureResponse(StatusCode statusCode) {
		Status status = Status.BAD_REQUEST;
		RegistrationResponse payload = new RegistrationResponse();
		payload.setClientState(ClientState.NON_OPERATIONAL);
		payload.setReturnStatus(new ReturnStatus(statusCode));
		return Response.status(status).entity(payload).type(MediaType.APPLICATION_XML).build();
	}
}