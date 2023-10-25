package it.exolab.exobank.models;

import java.io.Serializable;

public class Email implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String destinatario;
	private String oggettoEmail;
	private String testoEmail;
	private Utente utente;
	private StatoEmail statoEmail;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getOggettoEmail() {
		return oggettoEmail;
	}
	public void setOggettoEmail(String oggettoEmail) {
		this.oggettoEmail = oggettoEmail;
	}
	public String getTestoEmail() {
		return testoEmail;
	}
	public void setTestoEmail(String testoEmail) {
		this.testoEmail = testoEmail;
	}
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public StatoEmail getStatoEmail() {
		return statoEmail;
	}
	public void setStatoEmail(StatoEmail statoEmail) {
		this.statoEmail = statoEmail;
	}
	
	
	
	
}
