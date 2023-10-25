package it.exolab.exobank.bean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import it.exolab.exobank.controller.ContoCorrenteController;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Utente;

@Named
@SessionScoped
public class ContoCorrenteBean implements Serializable {

	@EJB
	private ContoCorrenteController contoController;
	private ContoCorrente contoCorrente;
	private boolean apriConto;

	public ContoCorrente getContoCorrente() {
		return contoCorrente;
	}

	public void setContoCorrente(ContoCorrente contoCorrente) {
		this.contoCorrente = contoCorrente;
	}

	public boolean isApriConto() {
		return apriConto;
	}

	public void setApriConto(boolean apriConto) {
		this.apriConto = apriConto;
	}

	public void apriConto() {
		apriConto = true;
	}

	public void findContoUtente(Utente utente) {
		try {
			Dto<ContoCorrente> contoUtente = contoController.findContoByIdUtente(utente);
			contoCorrente = contoUtente.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
