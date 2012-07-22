package com.icelero.ias.as.service.response;

import javax.xml.bind.annotation.XmlRootElement;

import com.icelero.ias.as.utilities.StatusCode;

@XmlRootElement(name = "clientRegResponse")
public class RegistrationResponse {

	private String clientId;
	private int clientState;
	private ReturnStatus returnStatus;

	public RegistrationResponse() {
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getClientState() {
		return clientState;
	}

	public void setClientState(int clientState) {
		this.clientState = clientState;
	}

	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	@XmlRootElement(name = "returnStatus")
	public static class ReturnStatus {

		public static final ReturnStatus CLIENT_REG_SUCCESS_RESPONSE = new ReturnStatus(StatusCode.CLIENT_REG_SUCCESS);
		public static final ReturnStatus CLIENT_REG_FAILURE_USERNAME_PASSWORD_RESPONSE = new ReturnStatus(
				StatusCode.CLIENT_REG_FAILURE_USERNAME_PASSWORD);
		public static final ReturnStatus CLIENT_REG_FAILURE_ACCOUNT_STATUS_RESPONSE = new ReturnStatus(
				StatusCode.CLIENT_REG_FAILURE_ACCOUNT_STATUS);
		public static final ReturnStatus CLIENT_REG_UNRECOVERABLE_FAILURE_RESPONSE = new ReturnStatus(
				StatusCode.CLIENT_REG_UNRECOVERABLE_FAILURE);

		public static final ReturnStatus[] ALL = new ReturnStatus[] { CLIENT_REG_SUCCESS_RESPONSE,
				CLIENT_REG_FAILURE_USERNAME_PASSWORD_RESPONSE, CLIENT_REG_FAILURE_ACCOUNT_STATUS_RESPONSE,
				CLIENT_REG_UNRECOVERABLE_FAILURE_RESPONSE };
		private int statusCode;
		private String message;

		public ReturnStatus() {
		}

		public ReturnStatus(StatusCode statusCode) {
			this.statusCode = statusCode.getId();
			this.message = statusCode.getMessage();

		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public static ReturnStatus getResponseFor(StatusCode statusCode) {
			int id = statusCode.getId();
			for (ReturnStatus response : ALL) {
				if (response.getStatusCode() == id) {
					return response;
				}
			}
			return CLIENT_REG_UNRECOVERABLE_FAILURE_RESPONSE;
		}
	}

}