package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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
	
	public void trovaPezzo(Integer id) {
		for(Pezzo[] arrayPezzi : griglia) {
			for(Pezzo pezzo : arrayPezzi) {
				if(pezzo.getId() == id && null != pezzo) {
					this.pezzo = pezzo;
				}
			}
		}
	}
	
	@PreDestroy
	public void resetGame() {
		this.griglia = null;
		this.pezzo = null;
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
	
	public void azione() {
        // Recupera i parametri passati dalla chiamata AJAX
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        
        // Recupera le coordinate di partenza e destinazione e l'ID del pezzo
        String coordinataPartenza = params.get("coordinataPartenza");
        String coordinataDestinazione = params.get("coordinataDestinazione");
        String idPezzo = params.get("idPezzo");

        // Fai qualcosa con le coordinate e l'ID del pezzo
        System.out.println("Coordinate di partenza: " + coordinataPartenza);
        System.out.println("Coordinate di destinazione: " + coordinataDestinazione);
        System.out.println("ID del pezzo: " + idPezzo);

        // Esegui altre azioni o aggiorna il tuo modello dati
    }

}
