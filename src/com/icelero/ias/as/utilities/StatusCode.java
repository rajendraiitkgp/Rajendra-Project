package com.icelero.ias.as.utilities;

public final class StatusCode {

	private int id;
	private String message;

	public static final StatusCode INVALID_PAYLOAD_FORMAT = new StatusCode(-1, "Invalid format of request parameters");
	public static final StatusCode SUBSCRIPTION_SUCCESS = new StatusCode(0, "Subscription successful");
	public static final StatusCode SUB_ALREADY_EXISTS = new StatusCode(1, "Subscriber already exists");
	public static final StatusCode SUBSCRIPTION_ID_UNAVAILABLE = new StatusCode(2, "Subscription id unavailable");
	public static final StatusCode SUBSCRIPTION_PASSWORD_UNAVAILABLE = new StatusCode(3, "Subscription password unavailable");
	public static final StatusCode SUBSCRIPTION_UNRECOVERABLE_FAILURE = new StatusCode(4, "Unrecoverable failure during subscription");
	public static final StatusCode CLIENT_REG_SUCCESS = new StatusCode(5, "Client registration successful");
	public static final StatusCode CLIENT_REG_FAILURE_USERNAME_PASSWORD = new StatusCode(6,
			"Client registration failure for username and password");
	public static final StatusCode CLIENT_REG_FAILURE_ACCOUNT_STATUS = new StatusCode(7,
			"Client registration failed because of invalid account status");
	public static final StatusCode CLIENT_REG_UNRECOVERABLE_FAILURE = new StatusCode(8, "Unrecoverable failure during client registration");
	public static final StatusCode AUTH_SUCCESS = new StatusCode(9, "Authorisation successful");
	public static final StatusCode AUTH_SUCCESS_POLICY_UPDATE = new StatusCode(10, "Authorisation succeeded; updated policy available");
	public static final StatusCode AUTH_SUCCESS_SW_UPDATE = new StatusCode(11, "Authorisation succeeded; software update available");
	public static final StatusCode AUTH_FAILURE_ACCOUNT_SUSPEND = new StatusCode(12, "Authorisation failed because account is suspended");
	public static final StatusCode AUTH_FAILURE_ACCOUNT_BLACK_LIST = new StatusCode(13,
			"Authorisation failed because account is blacklisted");
	public static final StatusCode AUTH_FAILURE_ACCOUNT_STATUS = new StatusCode(14,
			"Authorisation failed because of inactive account status");
	public static final StatusCode AUTH_UNRECOVERABLE_FAILURE = new StatusCode(15, "Unrecoverable failure during authentication");

	private StatusCode(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof StatusCode) {
			return this.id == ((StatusCode) obj).getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
