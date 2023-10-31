package it.exolab.exobank.bean;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.controller.ScacchieraController;

/**
* Bean class for chess game
* made with JSF component
*
* @author  Armando
* @version 0.1
* @since   24-10-2023 
*/

@Named
@SessionScoped
public class ScacchieraBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO EXCEPTION HANDLING
	
	@EJB
	ScacchieraController scacchieraController = new ScacchieraController();
	private boolean gioca;
	private Scacchiera scacchiera;
	private Pezzo[][] griglia;
	private Pezzo pezzo;
	private Pezzo pezzoAggiornato;
	private Integer turno=2;


	@PostConstruct
	public void scacchieraInit() {
		try {
			Scacchiera scacchieraOriginale = scacchieraController.scacchieraIniziale();
			scacchiera = new Scacchiera();
			scacchiera.setScacchiera(scacchieraOriginale.getGriglia());
			griglia = scacchiera.getGriglia();

		} catch (Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
		}
	}
	
	public void cambiaTurno() {
		turno++;
	}
	
	public void resetPezzo() {
		pezzo = null;
		System.out.println(pezzo);
	}
	
	//VALORIZZA CAMPI VARIABILE PEZZO CON CAMPI PEZZO SELEZIONATO.
	//SE DIVERSO DA NULL AGGIORNA ALTRIMENTI CREA PEZZO E AGGIORNA
	//SE COLORE UGUALE AGGIORNA
	//SE COLORE DIVERSO AGGIORNA POSIZOINE (STA CERCANDO DI MANGIARE UN PEZZO AVVERSARIO)
	public void pezzoSelezionato(Pezzo pezzoSelezionato) {	
			pezzo = new Pezzo();
			pezzo = pezzoSelezionato;
			System.out.println(this.pezzo.getId() + " " + this.pezzo.getColore() + " " + this.pezzo.getPosizioneX() + " " + this.pezzo.getPosizioneY());
	}
	
//	//METODO PER AGGIORNARE POSIZIONE DEL PEZZO
//	//SE DIVERSO DA NULL AGGIORNA POSIZOINE
//	//ALTRIMENTI MANDA UN MESSAGGIO DI ERRORE
//	public void nuovaPosizioneSelezionata(Integer posX, Integer posY) {
//		System.out.println("posX " + posX + " posY " + posY);
//		try {
//			if(null != pezzo) {
//				aggiornaPosizionePezzo(posX, posY);
//				
//			} else {
//		        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO, "Seleziona prima un pezzo", null));
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
//
//		}
//	}
	
	public void mossa (Integer posX, Integer posY) {
		try {
			pezzoAggiornato = new Pezzo();
			pezzoAggiornato.setColore(pezzo.getColore());
			pezzoAggiornato.setEsiste(pezzo.isEsiste());
			pezzoAggiornato.setId(pezzo.getId());
			pezzoAggiornato.setPosizioneX(posX);
			pezzoAggiornato.setPosizioneY(posY);
			System.out.println(pezzoAggiornato.toString());
			pezzo = null;
			scacchiera = scacchieraController.mossaConsentita(pezzoAggiornato);
			pezzoAggiornato=null;
			//TODO AGGIORNA GRIGLIA TRAMITE METODO CONTROLLER
		} catch(Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
		}
	}
	
	public void switchGioca() {
		gioca = !gioca;
		if(gioca && null == scacchiera) {
			scacchieraInit();
		} 
		else if(!gioca){
			resetGame();
		}	
	}
	
	@PreDestroy
	public void resetGame() {
		this.scacchiera = null;
		this.griglia = null;
		this.pezzo = null;
		this.pezzoAggiornato = null;
	}

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

	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}
	
	public Pezzo getPezzoAggiornato() {
		return pezzoAggiornato;
	}

	public void setPezzoAggiornato(Pezzo pezzoAggiornato) {
		this.pezzoAggiornato = pezzoAggiornato;
	}

	public Scacchiera getScacchiera() {
		return scacchiera;
	}

	public void setScacchiera(Scacchiera scacchiera) {
		this.scacchiera = scacchiera;
	}


	public Integer getTurno() {
		return turno;
	}


	public void setTurno(Integer turno) {
		this.turno = turno;
	}
	
	
	
	
//	public void trovaPezzo(Integer id) {
//	for(Pezzo[] arrayPezzi : griglia) {
//		for(Pezzo pezzo : arrayPezzi) {
//			if(pezzo.getId() == id && null != pezzo) {
//				this.pezzo = pezzo;
//			}
//		}
//	}
//}
	
//	public void azione() {
//        // Recupera i parametri passati dalla chiamata AJAX
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
//        
//        // Recupera le coordinate di partenza e destinazione e l'ID del pezzo
//        String coordinataPartenza = params.get("coordinataPartenza");
//        String coordinataDestinazione = params.get("coordinataDestinazione");
//        String idPezzo = params.get("idPezzo");
//
//        // Fai qualcosa con le coordinate e l'ID del pezzo
//        System.out.println("Coordinate di partenza: " + coordinataPartenza);
//        System.out.println("Coordinate di destinazione: " + coordinataDestinazione);
//        System.out.println("ID del pezzo: " + idPezzo);
//
//        // Esegui altre azioni o aggiorna il tuo modello dati
//    }

}
