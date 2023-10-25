package it.exolab.exobank.models;

import java.io.Serializable;

public class StatoEmail implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String statoEmail;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatoEmail() {
		return statoEmail;
	}
	public void setStatoEmail(String statoEmail) {
		this.statoEmail = statoEmail;
	}
	
	
}
