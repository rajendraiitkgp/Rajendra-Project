package com.icelero.ias.as.service.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "r")
public class AuthenticationResponse {
	@XmlElement(name = "SID")
	String sessionID;

	@XmlElement(name = "cS")
	int clientState;

	@XmlElement(name = "h")
	String mediaServerFarmAddress;

	@XmlElement(name = "p")
	int mediaServerFarmPort;

	@XmlElement(name = "sC")
	int statusCode;

	@XmlElement(name = "m")
	String message;

	public AuthenticationResponse() {
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setClientState(int clientState) {
		this.clientState = clientState;
	}

	public void setMediaServerFarmAddress(String mediaServerFarmAddress) {
		this.mediaServerFarmAddress = mediaServerFarmAddress;
	}

	public void setMediaServerFarmPort(int mediaServerFarmPort) {
		this.mediaServerFarmPort = mediaServerFarmPort;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}