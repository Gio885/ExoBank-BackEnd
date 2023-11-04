package it.exolab.exobank.controller;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.ScacchieraControllerInterface;
import it.exolab.exobank.validatore.ValidaMosseScacchi;
import it.exolab.exobank.validatore.ValidatoreScaccoAlRe;

@Stateless(name = "ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface {

	private Scacchiera scacchieraLavoro = new Scacchiera();
	private List<Pezzo> pezziMangiati = new ArrayList<Pezzo>();

	@Override
	public Scacchiera scacchieraIniziale() throws Exception{
		try {
			Scacchiera scacchieraIniziale = creazioneScacchieraIniziale();
			scacchieraLavoro = scacchieraIniziale;
			return scacchieraIniziale;
		} catch (Exception e) {
			// Gestisci l'eccezione qui
			e.printStackTrace();
			throw new Exception("Contatta assistenza non ti si è creata la scacchiera iniziale");
		}
	}

	@Override
	public Scacchiera mossaConsentita(Pezzo pezzo) throws Exception {
		Pezzo[][] griglia = scacchieraLavoro.getGriglia();
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		ValidaMosseScacchi validatoreScacchi = new ValidaMosseScacchi();
		try {
			Pezzo pezzoSpecifico = findPezzoById(pezzo, griglia);
			if (pezzoSpecifico == null) {
				throw new Exception("Nessun pezzo con l'ID specifico è stato trovato.");
			}
			ParametriValidatoreDto parametri = new ParametriValidatoreDto(pezzoSpecifico, pezzoSpecifico.getPosizioneX(), pezzoSpecifico.getPosizioneY(), pezzo.getPosizioneX(), pezzo.getPosizioneY(), pezzo.getColore(), griglia);
			if (validaScacco.isScacco(trovaRe(pezzoSpecifico, griglia), griglia)) {
				if (validaScacco.isScaccoMatto(trovaRe(pezzoSpecifico, griglia), griglia)) {
					throw new Exception("SCACCO MATTO!! Ha vinto il giocatore " + (trovaRe(pezzoSpecifico, griglia).getColore().equals(Colore.BIANCO) ? "Bianco." : "Nero."));
				} else {
					if (validaScacco.negaScaccoMangiandoMinaccia(pezzo, griglia)) {
						eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);
					} else if (validaScacco.negaScaccoMuovendoIlRe(pezzo, griglia)) {
						eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);
					} else if (validaScacco.puoInterporreTraReEMinaccia(pezzo, griglia)) {
						eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);
					} else {
						throw new Exception("Mossa non consentita non esci dallo scacco");	                }
				}
			} else {
				eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);    
			}
			return scacchieraLavoro;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() != null ? e.getMessage() : "Contatta l'assistenza, si è verificato un errore.");
		}
	}

	@Override
	public Scacchiera aggiornamentoTipoPedone(Pezzo pezzo) throws Exception {
		Pezzo[][] griglia = scacchieraLavoro.getGriglia();

		try {
			Pezzo pezzoSpecifico = findPezzoById(pezzo, griglia);
			if (pezzoSpecifico == null) {
				throw new Exception("Nessun pezzo con l'ID specifico è stato trovato.");
			}
			pezzoSpecifico.setTipo(pezzo.getTipo());
			return scacchieraLavoro;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() != null ? e.getMessage() : "Contatta l'assistenza, si è verificato un errore.");
		}
	}


	@Override
	public List<Pezzo> listaPezziMangiati() throws Exception {
	    if (pezziMangiati == null) {
	        throw new Exception("Nessun pezzo mangiato per ora.");
	    }
	    
	    return pezziMangiati;
	}


	@Override
	public boolean controlloPedoneUltimaPosizione(Pezzo pezzo) {
		if(pezzo.getColore().compareTo(Colore.BIANCO) == Costanti.COMPARATORE_STRINGA_UGUALE
				&& pezzo.getPosizioneX() == 7
				&& pezzo.getTipo().compareTo(Tipo.PEDONE) == Costanti.COMPARATORE_STRINGA_UGUALE) {

			return true;

		} else if(pezzo.getColore().compareTo(Colore.NERO) == Costanti.COMPARATORE_STRINGA_UGUALE
				&& pezzo.getPosizioneX() == 0
				&& pezzo.getTipo().compareTo(Tipo.PEDONE) == Costanti.COMPARATORE_STRINGA_UGUALE) {

			return true;
		} else {
			return false;
		}
	}


	// Metodo per creare la scacchiera iniziale
	private Scacchiera creazioneScacchieraIniziale() throws Exception {
		try {
			Pezzo[][] griglia = new Pezzo[Costanti.RIGHE][Costanti.COLONNE];
			int idPezzo = 1;


			for (int riga = 0; riga < Costanti.RIGHE; riga++) {
				for (int colonna = 0; colonna < Costanti.COLONNE; colonna++) {
					Tipo tipoPezzo = Costanti.SCACCHIERA_INIZIALE[riga][colonna];
					if (tipoPezzo != null) {
						griglia[riga][colonna] = new Pezzo(idPezzo++, tipoPezzo, (riga < 2) ? Colore.BIANCO : Colore.NERO, riga, colonna, true);
					} else {
						griglia[riga][colonna] = null;
					}
				}
			}


			Scacchiera scacchiera = new Scacchiera();
			scacchiera.setScacchiera(griglia);
			scacchieraLavoro.setScacchiera(griglia);
			return scacchiera;
		} catch (Exception e) {
			// Gestisci l'eccezione qui
			e.printStackTrace();
			throw new Exception("Griglia non creata contatta assistenza"); // Restituisci un valore adeguato in caso di errore
		}
	}

	private Pezzo findPezzoById(Pezzo pezzo, Pezzo[][] griglia) throws Exception {
		try {
			Integer idPezzo = pezzo.getId();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (null != griglia[x][y] && griglia[x][y].getId() == idPezzo) {
						return griglia[x][y];
					}
				}
			}
			return null; // Se nessun pezzo con l'ID specifico è stato trovato
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Pezzo non trovato"); // Restituisci un valore adeguato in caso di errore
		}
	}

	private Pezzo trovaRe(Pezzo pezzo, Pezzo[][] griglia) {
		Pezzo reStessoColore = null;

		for (int x = 0; x < griglia.length; x++) {
			for (int y = 0; y < griglia[x].length; y++) {
				Pezzo pezzoCorrente = griglia[x][y];
				if (pezzoCorrente != null && pezzoCorrente.getTipo().equals(Tipo.RE) && pezzoCorrente.getColore().equals(pezzo.getColore())) {
					return reStessoColore = pezzoCorrente;
				}
			}
		}
		return reStessoColore;
	}


	private void eseguiMossa(ParametriValidatoreDto parametri, Scacchiera scacchieraLavoro, ValidaMosseScacchi validatoreScacchi) throws Exception {
		// Utilizza il validatoreScacchi per verificare la validità della mossa
		if (validatoreScacchi.mossaConsentitaPerPezzo(parametri)) {
			if (null != parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()].setEsiste(false);
				pezziMangiati.add(parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]);
				System.out.println(pezziMangiati.size() + " " + pezziMangiati.get(0).getTipo() + " " + pezziMangiati.get(0).getColore() + " " + pezziMangiati.get(0).getId());
				parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] = null;
				// Controlla se la mossa elimina lo stato di scacco
				if (!isScaccoDopoMossa(parametri)) {
					aggiornaScacchiera(parametri);
					scacchieraLavoro.setScacchiera(parametri.getGriglia());
				} else {
					throw new Exception("Mossa non rimuove lo stato di scacco");
				}
			} else {
				// Controlla se la mossa elimina lo stato di scacco
				if (!isScaccoDopoMossa(parametri)) {
					aggiornaScacchiera(parametri);
					scacchieraLavoro.setScacchiera(parametri.getGriglia());
				} else {
					throw new Exception("Mossa non rimuove lo stato di scacco");
				}
			}
		} else {
			throw new Exception("Mossa non consentita");
		}
	}

	private boolean isScaccoDopoMossa(ParametriValidatoreDto parametri) {
		// Crea una copia temporanea della scacchiera e esegui la mossa
		Pezzo[][] griglia = parametri.getGriglia();
		griglia[parametri.getxDestinazione()][parametri.getyDestinazione()] = parametri.getPezzo();
		griglia[parametri.getxPartenza()][parametri.getyPartenza()] = null;
		parametri.getPezzo().setPosizioneX(parametri.getxDestinazione());
		parametri.getPezzo().setPosizioneY(parametri.getyDestinazione());
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		// Controlla se il re è in scacco dopo la mossa
		try {
			Pezzo re = trovaRe(parametri.getPezzo(), griglia);
			return validaScacco.isScacco(re, griglia);
		} catch (Exception e) {
			e.printStackTrace();
			return true; // Se si verifica un'eccezione, considera che lo stato di scacco persiste
		}
	}

	private void aggiornaScacchiera(ParametriValidatoreDto parametri) {
		// Aggiorna la scacchiera con la nuova posizione
		parametri.getGriglia()[parametri.getxPartenza()][parametri.getyPartenza()] = null; // Rimuovi il pezzo dalla posizione precedente
		parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] = parametri.getPezzo(); // Sposta il pezzo nella nuova posizione
		parametri.getPezzo().setPosizioneX(parametri.getxDestinazione()); // Aggiorna le coordinate del pezzo
		parametri.getPezzo().setPosizioneY(parametri.getyDestinazione());
	}
}