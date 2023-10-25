package it.exolab.exobank.models;

import java.io.Serializable;



public class Utente implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private Ruolo ruolo;
	private String codiceFiscale;
	private ContoCorrente contoCorrente;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Ruolo getRuolo() {
		return ruolo;
	}
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public ContoCorrente getContoCorrente() {
		return contoCorrente;
	}
	public void setContoCorrente(ContoCorrente contoCorrente) {
		this.contoCorrente = contoCorrente;
	}
		
	
	
	
	
}
