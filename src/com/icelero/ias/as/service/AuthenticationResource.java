package com.icelero.ias.as.service;

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

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icelero.ias.as.database.dao.ClientDAO;
import com.icelero.ias.as.database.util.DatabaseUtilities;
import com.icelero.ias.as.domain.Client;
import com.icelero.ias.as.membase.MemBase;
import com.icelero.ias.as.service.response.AuthenticationResponse;
import com.icelero.ias.as.utilities.ClientState;
import com.icelero.ias.as.utilities.StatusCode;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/auth")
public class AuthenticationResource {
	private static Logger LOGGER = Logger.getLogger(AuthenticationResource.class);

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public AuthenticationResponse authentication(@FormParam("payload") String payload) {

		Connection connection = null;
		try {
			if (StringUtils.isBlank(payload)) {
				throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_UNRECOVERABLE_FAILURE));
			}
			String clientID = readPayload(payload);
			if (clientID == null) {
				throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_UNRECOVERABLE_FAILURE));
			}

			connection = DatabaseUtilities.getReadConnection();
			ClientDAO clientDAO = new ClientDAO();

			Client databaseClient = clientDAO.read(connection, clientID);

			if (databaseClient == null) {
				DatabaseUtilities.closeConnection(connection);
				connection = DatabaseUtilities.getReadWriteConnection();
				databaseClient = clientDAO.read(connection, clientID);
				if (databaseClient == null) {
					throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_FAILURE_ACCOUNT_STATUS));
				}
			}
			MemcachedClient memBase = MemBase.getMembaseConnection();
			if (memBase == null) {
				throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_UNRECOVERABLE_FAILURE));
			}
			String sessionIdFromMembase = MemBase.getValue(clientID, memBase);
			if (!StringUtils.isBlank(sessionIdFromMembase)) {
				return getSuccessResponse(sessionIdFromMembase);
			}
			String sessionID = UUID.randomUUID().toString();
			if (!StringUtils.isBlank(sessionID)) {
				LOGGER.debug("session ID : " + sessionID);
				if (!MemBase.setValue(clientID, sessionID, memBase)) {
					LOGGER.debug("sessionID not set for : " + clientID);
					throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_UNRECOVERABLE_FAILURE));
				}
			}
			MemBase.shutdownClient(memBase);
			return getSuccessResponse(sessionID);
		} catch (Exception e) {
			LOGGER.error(e);
			if (e instanceof WebApplicationException) {
				throw (WebApplicationException) e;
			}
			throw new WebApplicationException(getFailureResponse(StatusCode.AUTH_UNRECOVERABLE_FAILURE));
		} finally {
			DatabaseUtilities.closeConnection(connection);
		}
	}

	private String readPayload(String payload) throws UnsupportedEncodingException {
		/*
		 * Example Payload <?xml version="1.0" encoding="UTF-8"
		 * standalone="yes"?> <CID> c714d71154004dc1af95829d0ce25a5b </CID>
		 */
		String clientID = null;
		try {
			payload = payload.toLowerCase();
			payload = StringUtils.deleteWhitespace(payload);
			String value = StringUtils.substringBetween(payload, "<cid>", "</cid>");
			if (!StringUtils.isBlank(value)) {
				String stripped = StringUtils.remove(value, '-');
				if (32 == stripped.length()) {
					if (UUID.fromString(value) != null) {
						clientID = value;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Unable to parse following payload:\n" + payload, e);
		}
		return clientID;
	}

	private AuthenticationResponse getSuccessResponse(String sessionID) {
		AuthenticationResponse response = new AuthenticationResponse();
		response.setSessionID(sessionID);
		response.setClientState(ClientState.ACTIVE);
		// TODO replace this with a configurable address
		// Move them to the database, cache these properties, and keep
		// refreshing them once every n seconds. These properties can be
		// modified from a dashboard
		response.setMediaServerFarmAddress("176.34.121.182:11211");
		response.setMediaServerFarmPort(11211);
		// End configurable properties

		response.setStatusCode(StatusCode.AUTH_SUCCESS.getId());
		response.setMessage(StatusCode.AUTH_SUCCESS.getMessage());
		return response;
	}

	private Response getFailureResponse(StatusCode statusCode) {
		Status status = Status.BAD_REQUEST;
		AuthenticationResponse payload = new AuthenticationResponse();
		payload.setSessionID("S_GLOABL_INVALID");
		payload.setClientState(ClientState.NON_OPERATIONAL);
		payload.setStatusCode(statusCode.getId());
		payload.setMessage(statusCode.getMessage());
		return Response.status(status).entity(payload).type(MediaType.APPLICATION_XML).build();
	}

}
