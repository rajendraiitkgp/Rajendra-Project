package com.icelero.ias.as.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a User
 * 
 * @author Vamshi
 * 
 */
public class User {
	/**
	 * The user selected user name
	 */
	private String userID;
	/**
	 * User selected password. The password is in an encrypted form.
	 */
	private transient String password;
	/**
	 * The contact email of the user
	 */
	private String email;
	/**
	 * Set of 128 bit UUID/GUID that are generated by the server to represent
	 * devices that the user installs the iCelero client on
	 */
	private Set<String> clientIDs;

	/**
	 * The default no_args constructor
	 */
	public User() {
	}

	/**
	 * 
	 * @param userID
	 *            the unique user name for this user
	 * @param password
	 *            the encrypted password for this user.
	 * @param email
	 *            the contact email for this user
	 */
	public User(String userID, String password, String email) {
		this.userID = userID;
		this.password = password;
		this.email = email;
	}

	/**
	 * Returns the unique user name for this user
	 * 
	 * @return the unique user name for this user
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the unique user name for this user. The user name is selected by the
	 * user.
	 * 
	 * @param userID
	 *            the unique user name for this user. The user name is selected
	 *            by the user.
	 */
	public void setUserid(String userID) {
		this.userID = userID;
	}

	/**
	 * Returns the encrypted password for this user. The encrypted password is
	 * stored in the database/data store
	 * 
	 * @return the encrypted password for this user. The encrypted password is
	 *         stored in the database/data store
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the encrypted password for this user. The encrypted password is
	 * stored in the database/data store
	 * 
	 * @param password
	 *            the encrypted password for this user. The encrypted password
	 *            is stored in the database/data store
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the contact email for this user
	 * 
	 * @return the contact email for this user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the contact email for this user
	 * 
	 * @param email
	 *            the contact email for this user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Return an unmodifiable copy of the set of clientIDs, that represent
	 * devices on which the user has installed the iCelero client
	 * 
	 * @return an unmodifiable copy of the set of clientIDs, that represent
	 *         devices on which the user has installed the iCelero client
	 */
	public Set<String> getClientIDs() {
		if (clientIDs == null) {
			clientIDs = new HashSet<String>();
		}
		return Collections.unmodifiableSet(clientIDs);
	}

	/**
	 * Sets the clientIDs associated with the devices on which the user has
	 * installed the iCelero client. All the <code>ClientIDs</code> passed in
	 * are copied to an internal <code>Set</code>
	 * 
	 * @param clientIDs
	 *            the clientIDs associated with the devices on which the user
	 *            has installed the iCelero client. All the
	 *            <code>ClientIDs</code> passed in are copied to an internal
	 *            <code>Set</code>
	 */
	public void setClientIDs(Set<String> clientIDs) {
		getClientIDs().clear();
		for (String clientID : clientIDs) {
			getClientIDs().add(clientID);
		}
	}

	/**
	 * Adds the <code>clientID</code> to the list of client ids for this user.
	 * 
	 * @param clientID
	 *            adds the <code>clientID</code> to the list of client ids for
	 *            this user. If the passed in client id is <code>null</code>
	 *            does not do anything
	 */
	public void addClientID(String clientID) {
		if (clientID != null) {
			getClientIDs().add(clientID);
		}
	}

	/**
	 * Verifies if a client id is in the set of clientIDs associated with this
	 * user
	 * 
	 * @param clientID
	 *            the client id to check
	 * @return <code>true</code> if this client id matches one of the client ids
	 *         associated with this user. Else returns <code>false</code>
	 */
	public boolean hasClientID(String clientID) {
		return getClientIDs().contains(clientID);
	}

}