package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.ScacchieraControllerInterface;
import it.exolab.scacchiera.ex.MossaNonConsentita;
import it.exolab.scacchiera.ex.Scacco;
import it.exolab.scacchiera.ex.ScaccoMatto;
import it.exolab.scacchiera.timer.Timer;

/**
* Bean class for chess game
* made with JSF components
*
* @author  Armandone
* @version 0.1
* @since   24-10-2023 
*/

@Named("scacchieraBean")
@SessionScoped
public class ScacchieraBean implements Serializable {

	private static final long serialVersionUID = 4821479503167476166L;
	
	@EJB
	private ScacchieraControllerInterface scacchieraController;
	
	private List<Pezzo> listaPezziMangiatiBianchi;
	private List<Pezzo> listaPezziMangiatiNeri;
	private boolean nuovoGioco;
	private Scacchiera scacchiera;
	private Pezzo[][] griglia;
	private Pezzo pezzo;
	private Pezzo pezzoAggiornato;
	private Integer turno;
	private Date tempoGiocatore1;
	private Date tempoGiocatore2;
	private Calendar cal;
	private boolean giocaGiocatore1;
	private boolean giocaGiocatore2;
	private boolean stopTimer;
	private boolean partitaTerminata;
	private boolean ultimaPosizione;
	private Integer tipoPartitaScelta;
	private Tipo nuovoTipo;
	

	//INIZIALIZZAZIONE SCACCHIERA
	public void scacchieraInit() {
		try {
			if(null != scacchiera) {
				resetGame();
			}
			Scacchiera scacchieraOriginale = scacchieraController.scacchieraIniziale();
			scacchiera = new Scacchiera();
			listaPezziMangiatiBianchi = new ArrayList<Pezzo>();
			listaPezziMangiatiNeri = new ArrayList<Pezzo>();
			scacchiera.setScacchiera(scacchieraOriginale.getGriglia());
			griglia = scacchiera.getGriglia();
			tipoPartitaScelta = null;
			giocaGiocatore1 = true;
			giocaGiocatore2 = false;
			partitaTerminata = false;
			ultimaPosizione = false;
			turno = 1;
			
		} catch(Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_FATAL, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		}
	}
	
	//METODO PER BOTTONE SCELTA LUNGHEZZA PARTITA
	public void sceltaLungezzaPartita(int scelta) {
		try {
			cal = Calendar.getInstance();
			tempoGiocatore1 = new Date();
			tempoGiocatore1 = new Date();

			if(scelta == 1) {
				tipoPartitaScelta = scelta;
				tempoGiocatore1 = new Timer().creazioneTimer(0, 10, 0, cal);
				tempoGiocatore2 = new Timer().creazioneTimer(0, 10, 0, cal);
				
			} else if(scelta == 2) {
				tipoPartitaScelta = scelta;
				tempoGiocatore1 = new Timer().creazioneTimer(0, 59, 59, cal);
				tempoGiocatore2 = new Timer().creazioneTimer(0, 59, 59, cal);
			
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);

		}
	}
	
	//AGGIORNA VARIABILE STOPTIMER PER FERMARE O FAR RIPARTIRE IL TIMER
	public void start() {
		stopTimer = !stopTimer;
		if(turno == 1) {
			FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_INFO, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tocca al bianco!", null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_INFO);
		}
	}
	
	//METODO PER GESTIONE TIMER
	public void timer() {
		if(stopTimer) {
			if(giocaGiocatore1) {
				tempoGiocatore1 = handleTimer(tempoGiocatore1);
				
			} else if(giocaGiocatore2) {
				tempoGiocatore2 = handleTimer(tempoGiocatore2);
			}
		}	
	}
	
	//METODO PER GESTIONE/AGGIORNAMENTO TIMER
	private Date handleTimer(Date tempoGiocatore) {
		
		cal.setTime(tempoGiocatore);
		
		if (cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0){
	        stopTimer = !stopTimer;
	        partitaTerminata = true;
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	        		giocaGiocatore1 ? Costanti.TEMPO_SCADUTO_GIOCATORE1 : Costanti.TEMPO_SCADUTO_GIOCATORE2, null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM);
			giocaGiocatore1 = !giocaGiocatore1;
			giocaGiocatore2 = !giocaGiocatore2;
			PrimeFaces.current().executeScript(Costanti.SCRIPT_SHOW_MODAL_VITTORIA);

		} else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) > 0 
					|| cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) > 0 ) {
			cal.add(Calendar.SECOND, -1);
			
		} else if (cal.get(Calendar.MINUTE) > 0 && cal.get(Calendar.SECOND) == 0) {
			cal.add(Calendar.MINUTE, -1);
			cal.add(Calendar.SECOND, 59);

		}
		
		return cal.getTime();
	}
	
	//METODO PER GESTIRE MOSSA: PRENDE NUOVE POSIZIONI AGGIORNA IL PEZZO CON NUOVE POSIZIONI E AGGIORNA SCACCHIERA
	public void mossa (Integer posX, Integer posY) throws Exception {
		try {
			System.out.println("posX: " + posX + " posY: " + posY);
			pezzoAggiornato = aggiornaPosizionePezzoAggiornato(posX, posY);
			pezzo = null;
			scacchiera = scacchieraController.mossaConsentita(pezzoAggiornato);
			gestionePedoneUltimaPosizione();
			aggiornaListePezziMangiati();
			
		} catch(MossaNonConsentita mossaNonConsentita) {
			pezzoAggiornato = null;
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, mossaNonConsentita.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		
		} catch(ScaccoMatto scaccoMatto) {
			stopTimer = false;
			partitaTerminata = true;
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, scaccoMatto.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM);
			PrimeFaces.current().executeScript(Costanti.SCRIPT_SHOW_MODAL_VITTORIA);

				
		} catch(Scacco scacco) {
			gestionePedoneUltimaPosizione();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, scacco.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		
		} catch(Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		}
	}
	
	private void gestionePedoneUltimaPosizione() throws Exception {
		try {
			if(scacchieraController.controlloPedoneUltimaPosizione(pezzoAggiornato)) {
				ultimaPosizione = true;
				PrimeFaces.current().ajax().update(Costanti.ID_MODAL_TRASFORMA_PEDONE);
				PrimeFaces.current().executeScript(Costanti.SCRIPT_SHOW_MODAL_TRASFORMAZIONE);
				
			} else {
				ultimaPosizione = false;
				pezzoAggiornato = null;
				cambiaTurno();
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		}
	}
	
	//METODO PER AGGIORNARE LISTE DEI PEZZI MANGIATI
	private void aggiornaListePezziMangiati(){
		try {	
			List<Pezzo> listaPezziMangiati = scacchieraController.listaPezziMangiati();
			
			for(Pezzo pezzo : listaPezziMangiati) {
				if(Colore.BIANCO == pezzo.getColore() && !listaPezziMangiatiBianchi.contains(pezzo)) {
					listaPezziMangiatiBianchi.add(pezzo);
					
				} else if(Colore.NERO == pezzo.getColore() && !listaPezziMangiatiNeri.contains(pezzo)) {
					listaPezziMangiatiNeri.add(pezzo);
				}
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		}
	}
	
	//METODO PER TRASFORMAZIONE PEDONE: PRENDE PARAMETRO IN INPUT E RIEMPIE VARIABILE NUOVOTIPO
	public void selezionaNuovoTipo(String tipo) {
		try {
			for(Tipo tipoPezzo : Tipo.values()) {
				if(tipoPezzo.toString().equalsIgnoreCase(tipo)) {
					nuovoTipo = tipoPezzo;
				}
			}
		} catch(Exception exception) {
			exception.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
		}
	}
	
	//METODO PER TRASFORMAZIONE PEDONE: INVIA PEZZO CON TIPO AGGIORNATO
	public void confermaNuovoTipo() {
		try {
			pezzoAggiornato.setTipo(nuovoTipo);
			griglia = scacchieraController.aggiornamentoTipoPedone(pezzoAggiornato).getGriglia();
			nuovoTipo = null;
			ultimaPosizione = false;
			aggiornaListePezziMangiati();
			cambiaTurno();
			
		} catch(ScaccoMatto scaccoMatto) {
			partitaTerminata = true;
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, scaccoMatto.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);
			PrimeFaces.current().executeScript(Costanti.SCRIPT_SHOW_MODAL_VITTORIA);

		} catch (Scacco scacco) {
			nuovoTipo = null;
			ultimaPosizione = false;
			aggiornaListePezziMangiati();
			cambiaTurno();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, scacco.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);

		} catch (Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);

		}
	}
	
	//CAMBIO TURNO
	private void cambiaTurno() {
		turno++;
		giocaGiocatore1 = !giocaGiocatore1;
		giocaGiocatore2 = !giocaGiocatore2;
		String message = giocaGiocatore1 ? Costanti.TOCCA_BIANCO : Costanti.TOCCA_NERO;
        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_INFO, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
		PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_INFO);

	}
	
	//SVUOTA IL PEZZO
	public void resetPezzo() {
		pezzo = null;
	}
	
	//VALORIZZA CAMPI VARIABILE PEZZO CON CAMPI PEZZO SELEZIONATO.
	public void pezzoSelezionato(Pezzo pezzoSelezionato) {	
			pezzo = new Pezzo();
			pezzo = pezzoSelezionato;
			System.out.println(pezzoSelezionato.toString());
	}
	
	//COSTRUISCO L'OGGETTO CON POSIZIONI AGGIORNATE DA MANDARE A BACK END
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
			
		} catch(Exception exception) {
			exception.printStackTrace();
			throw new Exception(Costanti.ERRORE_AGGIORNA_POSIZIONE_PEZZO);
		}
		
	}
	
	//METODO PER INIZIALIZZARE NUOVO GIOCO
	public void gioca() {
		try {
			nuovoGioco = !nuovoGioco;
			
			if(nuovoGioco && null == scacchiera) {
				scacchieraInit();
				
			} else if(!nuovoGioco){
				resetGame();
			}
			
		}catch(Exception exception) {
			exception.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(Costanti.ID_COMPONENTE_MESSAGGIO_ERRORE, new FacesMessage(FacesMessage.SEVERITY_FATAL, exception.getMessage(), null));
			PrimeFaces.current().ajax().update(Costanti.ID_HOMEFORM_MESSAGGIO_ERRORE);

		}
	}
	
	//SVUOTO LE VARIABILI
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
		listaPezziMangiatiBianchi = null;
		listaPezziMangiatiNeri = null;
		tipoPartitaScelta = null;
		turno = 0;
	}
	
	/**
	 * GETTERS E SETTERS
	 * 
	 */
	
	public List<Pezzo> getListaPezziMangiatiBianchi() {
		return listaPezziMangiatiBianchi;
	}

	public void setListaPezziMangiatiBianchi(List<Pezzo> listaPezziMangiatiBianchi) {
		this.listaPezziMangiatiBianchi = listaPezziMangiatiBianchi;
	}

	public List<Pezzo> getListaPezziMangiatiNeri() {
		return listaPezziMangiatiNeri;
	}

	public void setListaPezziMangiatiNeri(List<Pezzo> listaPezziMangiatiNeri) {
		this.listaPezziMangiatiNeri = listaPezziMangiatiNeri;
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

	public Integer getTipoPartitaScelta() {
		return tipoPartitaScelta;
	}

	public void setTipoPartitaScelta(Integer tipoPartitaScelta) {
		this.tipoPartitaScelta = tipoPartitaScelta;
	}
}
