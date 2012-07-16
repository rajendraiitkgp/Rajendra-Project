package com.icelero.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User implements Serializable {

	private static final long serialVersionUID = -2306466156340602784L;

	private int id;
	private String name;
	private String imsiNumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImsiNumber() {
		return imsiNumber;
	}

	public void setImsiNumber(String imsiNumber) {
		this.imsiNumber = imsiNumber;
	}
}