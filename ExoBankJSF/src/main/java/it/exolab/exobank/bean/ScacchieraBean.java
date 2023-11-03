package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.controller.ScacchieraController;

/**
* Bean class for chess game
* made with JSF component
*
* @author  Armandone
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
		
	@EJB
	ScacchieraController scacchieraController = new ScacchieraController();
	private List<Pezzo> listaPezziMangiati;
	private boolean nuovoGioco;
	private Scacchiera scacchiera;
	private Pezzo[][] griglia;
	private Pezzo pezzo;
	private Pezzo pezzoAggiornato;
	private Integer turno;
	private Date tempoGiocatore1;
	private Date tempoGiocatore2;
	private boolean giocaGiocatore1;
	private boolean giocaGiocatore2;
	private boolean stopTimer;
	private boolean partitaTerminata;
	private boolean ultimaPosizione;
	private Tipo nuovoTipo;
	

	//INIZIALIZZAZIONE SCACCHIERA
	public void scacchieraInit() {
		try {
			if(scacchiera != null ) {
				resetGame();
			}
			
			Scacchiera scacchieraOriginale = scacchieraController.scacchieraIniziale();
			scacchiera = new Scacchiera();
			scacchiera.setScacchiera(scacchieraOriginale.getGriglia());
			griglia = scacchiera.getGriglia();
			creazioneTimer(30, 10);			
			giocaGiocatore1 = true;
			giocaGiocatore2 = false;
			partitaTerminata = false;
			ultimaPosizione = false;
			turno = 1;
			
		} catch(Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void sceltaLungezzaPartita(int scelta) {
		
		if(scelta == 1) {
			creazioneTimer(30, 00);
		} else if(scelta == 2) {
			creazioneTimer(00, 10);
		}
	}
	
	private void creazioneTimer(int secondi, int minuti) {
		try {
			Calendar tempo = Calendar.getInstance();
			tempo.set(Calendar.HOUR_OF_DAY, 0);
			tempo.set(Calendar.MINUTE, minuti);
			tempo.set(Calendar.SECOND, secondi);
			tempoGiocatore1 = new Date();
			tempoGiocatore1 = tempo.getTime();
			tempoGiocatore2 = new Date();
			tempoGiocatore2 = tempo.getTime();
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void start() {
		stopTimer = !stopTimer;
	}
	
	public void timer() {
		if(stopTimer) {
			if(giocaGiocatore1) {
				tempoGiocatore1 = handleTimer(tempoGiocatore1);
				
			} else if(giocaGiocatore2) {
				tempoGiocatore2 = handleTimer(tempoGiocatore2);
			}
		}	
	}
	
	private Date handleTimer(Date tempoGiocatore) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(tempoGiocatore);
		
		if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0){
	        partitaTerminata = true;
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_INFO,
	        		giocaGiocatore1 ? "Il tempo è scaduto, hai perso Giocatore1! :(" : "Il tempo è scaduto hai perso Giocatore2! :(", null));

		} else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) > 0) {
			cal.add(Calendar.SECOND, -1);
			
		} else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) == 0) {
			cal.add(Calendar.MINUTE, -1);
			cal.add(Calendar.SECOND, 59);
			
		} else if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) > 0 ) {
			cal.add(Calendar.SECOND, -1);
			
		} else if(cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == -1) {
	        stopTimer = !stopTimer;
		}
		
		return cal.getTime();
	}
	
	public void mossa (Integer posX, Integer posY) {
		try {
			pezzoAggiornato = aggiornaPosizionePezzoAggiornato(posX, posY);
			pezzo = null;
			scacchiera = scacchieraController.mossaConsentita(pezzoAggiornato);
			
			if(scacchieraController.controlloPedoneUltimaPosizione(pezzoAggiornato)) {
				ultimaPosizione = true;
				PrimeFaces.current().ajax().update("homeForm:modalTrasformazionePedone");
				PrimeFaces.current().executeScript("PF('modalTrasformazionePedone').show()");
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + giocaGiocatore2);
			} else {
				ultimaPosizione = false;
				pezzoAggiornato = null;
				cambiaTurno();
			}
			


		} catch(Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void listaPezziMangiati(){
		try {
			listaPezziMangiati = scacchieraController.listaPezziMangiati();
			
		} catch (Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void selezionaNuovoTipo(String tipo) {
		
		if(Tipo.REGINA.toString().equalsIgnoreCase(tipo)) {
			nuovoTipo = Tipo.REGINA;
			
		} else if(Tipo.TORRE.toString().equalsIgnoreCase(tipo)) {
			nuovoTipo = Tipo.TORRE;
			
		} else if(Tipo.ALFIERE.toString().equalsIgnoreCase(tipo)) {
			nuovoTipo = Tipo.ALFIERE;
			
		} else if(Tipo.CAVALLO.toString().equalsIgnoreCase(tipo)) {
			nuovoTipo = Tipo.CAVALLO;
			
		}
	}
	
	public void confermaNuovoTipo() {
		
		try {
			pezzoAggiornato.setTipo(nuovoTipo);
			griglia = scacchieraController.aggiornamentoTipoPedone(pezzoAggiornato).getGriglia();
			nuovoTipo = null;
			ultimaPosizione = false;
			cambiaTurno();
			
		} catch (Exception e) {
			e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage("messaggioScacchi", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));

		}
	}
	
	public void mostraDialog() {
		ultimaPosizione = true;
	}

	private void cambiaTurno() {
		turno++;
		giocaGiocatore1 = !giocaGiocatore1;
		giocaGiocatore2 = !giocaGiocatore2;
	}
	
	public void resetPezzo() {
		pezzo = null;
	}
	
	//VALORIZZA CAMPI VARIABILE PEZZO CON CAMPI PEZZO SELEZIONATO.
	public void pezzoSelezionato(Pezzo pezzoSelezionato) {	
			pezzo = new Pezzo();
			pezzo = pezzoSelezionato;
			System.out.println(this.pezzo.getId() + " " + this.pezzo.getColore() + " " + this.pezzo.getPosizioneX() + " " + this.pezzo.getPosizioneY());
	}
	
	private Pezzo aggiornaPosizionePezzoAggiornato(Integer posX, Integer posY) throws Exception {
		try {
			pezzoAggiornato = new Pezzo();
			pezzoAggiornato.setColore(pezzo.getColore());
			pezzoAggiornato.setEsiste(pezzo.isEsiste());
			pezzoAggiornato.setId(pezzo.getId());
			pezzoAggiornato.setTipo(pezzo.getTipo());
			pezzoAggiornato.setPosizioneX(posX);
			pezzoAggiornato.setPosizioneY(posY);
			return pezzoAggiornato;
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("errore in aggiornaPezzoAggiornato --> ");
		}
		
	}

	public void gioca() {
		nuovoGioco = !nuovoGioco;
		if(nuovoGioco && null == scacchiera) {
			scacchieraInit();
		} 
		else if(!nuovoGioco){
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
		stopTimer = false;
		nuovoTipo = null;
		turno = 0;		
	}
	
	public List<Pezzo> getListaPezziMangiati() {
		return listaPezziMangiati;
	}

	public void setListaPezziMangiati(List<Pezzo> listaPezziMangiati) {
		this.listaPezziMangiati = listaPezziMangiati;
	}
	
	public boolean isPartitaTerminata() {
		return partitaTerminata;
	}

	public void setPartitaTerminata(boolean partitaTerminata) {
		this.partitaTerminata = partitaTerminata;
	}
	
	public boolean isStopTimer() {
		return stopTimer;
	}

	public void setStopTimer(boolean stopTimer) {
		this.stopTimer = stopTimer;
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

	public boolean isNuovoGioco() {
		return nuovoGioco;
	}

	public void setNuovoGioco(boolean nuovoGioco) {
		this.nuovoGioco = nuovoGioco;
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

	public boolean isUltimaPosizione() {
		return ultimaPosizione;
	}

	public void setUltimaPosizione(boolean ultimaPosizione) {
		this.ultimaPosizione = ultimaPosizione;
	}

	public Tipo getNuovoTipo() {
		return nuovoTipo;
	}

	public void setNuovoTipo(Tipo nuovoTipo) {
		this.nuovoTipo = nuovoTipo;
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
	

}
