package it.exolab.exobank.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	private Integer turno;
	private Date tempoGiocatore1;
	private Date tempoGiocatore2;
	private boolean giocaGiocatore1;
	private boolean giocaGiocatore2;
	private boolean start;
	private boolean partitaTerminata;
	

	public void scacchieraInit() {
		try {
			if(scacchiera != null ) {
				resetGame();
			}
			Scacchiera scacchieraOriginale = scacchieraController.scacchieraIniziale();
			scacchiera = new Scacchiera();
			scacchiera.setScacchiera(scacchieraOriginale.getGriglia());
			griglia = scacchiera.getGriglia();
			creazioneTimer();			
			giocaGiocatore1=true;
			giocaGiocatore2=false;
			partitaTerminata=false;
			turno=1;
		} catch (Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null));
		}
	}
	
	private void creazioneTimer() {
		try {
			Calendar tempo = Calendar.getInstance();
			tempo.set(Calendar.HOUR_OF_DAY, 0);
			tempo.set(Calendar.MINUTE, 0);
			tempo.set(Calendar.SECOND, 20);
			tempoGiocatore1 = new Date();
			tempoGiocatore1 = tempo.getTime();
			tempoGiocatore2 = new Date();
			tempoGiocatore2 = tempo.getTime();
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void start() {
		start = !start;
	}
	
	public void timer() {
		Calendar cal = Calendar.getInstance();
		if(start) {
			if(giocaGiocatore1) {
				cal.setTime(tempoGiocatore1);
				if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0){
			        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Il tempo è scaduto, hai perso Giocatore1", null));
			        start=!start;
			        partitaTerminata=true;
				}
				else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) > 0) {
					cal.add(Calendar.SECOND, -1);
				}
				else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) == 0) {
					cal.add(Calendar.MINUTE, -1);
					cal.add(Calendar.SECOND, 59);
				}
				else if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) > 0 ) {
					cal.add(Calendar.SECOND, -1);
				}
				tempoGiocatore1 = cal.getTime();
			}
			else if(giocaGiocatore2) {
				cal.setTime(tempoGiocatore2);
				if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0){
			        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_FATAL,"Il tempo è scaduto, hai perso Giocatore2", null));
			        start=!start;
			        partitaTerminata=true;
				}
				else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) > 0) {
					cal.add(Calendar.SECOND, -1);
				}
				else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) == 0) {
					cal.add(Calendar.MINUTE, -1);
					cal.add(Calendar.SECOND, 59);
				}
				else if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) > 0 ) {
					cal.add(Calendar.SECOND, -1);
				}
				tempoGiocatore2 = cal.getTime();
			}
		}	
	}

	public void cambiaTurno() {
		turno++;
		giocaGiocatore1 = !giocaGiocatore1;
		giocaGiocatore2 = !giocaGiocatore2;
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
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
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
	
	public void resetGame() {
		scacchiera = null;
		griglia = null;
		pezzo = null;
		pezzoAggiornato = null;
		tempoGiocatore1 = null;
		tempoGiocatore2 = null;
		giocaGiocatore1 = false;
		giocaGiocatore2 = false;
		start = false;
		turno = 0;		
	}
	
	public boolean isPartitaTerminata() {
		return partitaTerminata;
	}

	public void setPartitaTerminata(boolean partitaTerminata) {
		this.partitaTerminata = partitaTerminata;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isGiocaGiocatore1() {
		return giocaGiocatore1;
	}

	public void setGiocaGiocatore1(boolean giocaGiocatore1) {
		this.giocaGiocatore1 = giocaGiocatore1;
	}

	public boolean isGiocaGiocatore2() {
		return giocaGiocatore2;
	}

	public void setGiocaGiocatore2(boolean giocaGiocatore2) {
		this.giocaGiocatore2 = giocaGiocatore2;
	}

	public Date getTempoGiocatore1() {
		return tempoGiocatore1;
	}

	public void setTempoGiocatore1(Date tempoGiocatore1) {
		this.tempoGiocatore1 = tempoGiocatore1;
	}

	public Date getTempoGiocatore2() {
		return tempoGiocatore2;
	}

	public void setTempoGiocatore2(Date tempoGiocatore2) {
		this.tempoGiocatore2 = tempoGiocatore2;
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
