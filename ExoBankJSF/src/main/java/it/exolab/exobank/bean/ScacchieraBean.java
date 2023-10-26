package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.controller.ScacchieraController;

@Named
@SessionScoped
public class ScacchieraBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	ScacchieraController scacchieraController = new ScacchieraController();
	private boolean gioca;
	private Pezzo[][] griglia;
	private Pezzo pezzo;
	
	@PostConstruct
	public void scacchieraInit() {
		griglia = new Pezzo[8][8];
		this.griglia = scacchieraController.scacchieraIniziale().getGriglia();
		pezzo = new Pezzo();
	}

//	public ScacchieraBean() {
//		inizializzaScacchiera();
//	}
//
//	private void inizializzaScacchiera() {
//		this.griglia = scacchieraController.scacchieraIniziale().getGriglia();
//
//	}

	public Pezzo[][] getGriglia() {
		return griglia;
	}

	public void setGriglia(Pezzo[][] griglia) {
		this.griglia = griglia;
	}

	public boolean isGioca() {
		return gioca;
	}

	public void setGioca(boolean gioca) {
		this.gioca = gioca;
	}

	public void switchGioca() {
		gioca = !gioca;
	}

	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}
}
