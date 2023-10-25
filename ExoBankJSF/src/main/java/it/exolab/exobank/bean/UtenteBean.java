package it.exolab.exobank.bean;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.primefaces.PrimeFaces;

import it.exolab.exobank.controller.UtenteController;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Utente;

@Named
@SessionScoped
public class UtenteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UtenteController utenteController;
	@Inject
	private ContoCorrenteBean contoCorrenteBean;
	private Utente utente;
	private Utente utenteLoggato;
	private boolean login;
	private boolean registrazione;

	@PostConstruct
	public void init() {
		utenteLoggato = new Utente();
		utente = new Utente();
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	public void setUtenteLoggato(Utente utenteLoggato) {
		this.utenteLoggato = utenteLoggato;
	}


	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	
	public boolean isRegistrazione() {
		return registrazione;
	}

	public void setRegistrazione(boolean registrazione) {
		this.registrazione = registrazione;
	}

	public void login() {
		try {
			Dto<Utente> utenteDaTrovare = utenteController.findUtenteByEmailPassword(utenteLoggato);
			if (null != utenteDaTrovare.getData()) {
				utenteLoggato = utenteDaTrovare.getData();
				contoCorrenteBean.findContoUtente(utenteLoggato);	
		        FacesContext.getCurrentInstance().addMessage("loginForm:messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Credenziali valide.", null));
		        login = true;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			resetUtente(utenteLoggato);
	        FacesContext.getCurrentInstance().addMessage("loginForm:messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenziali non valide.", null));
		}
	}
	

	
	public void insertUtente() {
		String regole = "Compila i campi correttamente:" +
				   "- Il nome non può contenere numeri o caratteri speciali" +
				   "- Il cognome non può contenere numeri o caratteri speciali"+
	               "- La password deve contenere almeno 8 caratteri" +
	               "- La password deve contenere almeno un carattere maiuscolo" +
	               "- La password deve contenere almeno un carattere minuscolo";
		try {
			Dto<Utente>utenteDto = utenteController.insertUtente(utente);
			if(null != utenteDto.getData()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrazione effettuata!", null));
				setRegistrazione(false);
				setUtenteLoggato(utente);
				login();
				resetUtente(utente);
			}
		} catch (Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, regole, null));
		}
	}

	public void logout() {
		resetUtente(utenteLoggato);
		login = false;
	}
	
	public void switchLoginRegister() {
		this.registrazione = registrazione ? false : true ;
	}
	private void resetUtente(Utente utente) {    //non svuota con new utente   DEVO RESETTARE CONTO ALLA LOGOUT
		utente.setCodiceFiscale(null);
		utente.setCognome(null);
		utente.setContoCorrente(null);
		utente.setEmail(null);
		utente.setId(null);
		utente.setNome(null);
		utente.setPassword(null);
		utente.setRuolo(null);
	}

}
