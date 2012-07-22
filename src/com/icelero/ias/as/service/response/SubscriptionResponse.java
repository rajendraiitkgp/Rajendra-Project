package com.icelero.ias.as.service.response;

import javax.xml.bind.annotation.XmlRootElement;

import com.icelero.ias.as.utilities.StatusCode;

@XmlRootElement(name = "returnStatus")
public class SubscriptionResponse {

	public static final SubscriptionResponse SUBSCRIPTION_SUCCESS_RESPONSE = new SubscriptionResponse(StatusCode.SUBSCRIPTION_SUCCESS);
	public static final SubscriptionResponse SUB_ALREADY_EXISTS_RESPONSE = new SubscriptionResponse(StatusCode.SUB_ALREADY_EXISTS);
	public static final SubscriptionResponse SUBSCRIPTION_ID_UNAVAILABLE_RESPONSE = new SubscriptionResponse(
			StatusCode.SUBSCRIPTION_ID_UNAVAILABLE);
	public static final SubscriptionResponse SUBSCRIPTION_PASSWORD_UNAVAILABLE_RESPONSE = new SubscriptionResponse(
			StatusCode.SUBSCRIPTION_PASSWORD_UNAVAILABLE);
	public static final SubscriptionResponse SUBSCRIPTION_UNRECOVERABLE_FAILURE_RESPONSE = new SubscriptionResponse(
			StatusCode.SUBSCRIPTION_UNRECOVERABLE_FAILURE);

	public static final SubscriptionResponse[] ALL = new SubscriptionResponse[] { SUBSCRIPTION_SUCCESS_RESPONSE,
			SUB_ALREADY_EXISTS_RESPONSE, SUBSCRIPTION_ID_UNAVAILABLE_RESPONSE, SUBSCRIPTION_PASSWORD_UNAVAILABLE_RESPONSE,
			SUBSCRIPTION_UNRECOVERABLE_FAILURE_RESPONSE };

	private int statusCode;
	private String message;

	public SubscriptionResponse() {
	}

	public SubscriptionResponse(StatusCode statusCode) {
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

	public static SubscriptionResponse getResponseFor(StatusCode statusCode) {
		int id = statusCode.getId();
		for (SubscriptionResponse response : ALL) {
			if (response.getStatusCode() == id) {
				return response;
			}
		}
		return SUBSCRIPTION_UNRECOVERABLE_FAILURE_RESPONSE;
	}
}