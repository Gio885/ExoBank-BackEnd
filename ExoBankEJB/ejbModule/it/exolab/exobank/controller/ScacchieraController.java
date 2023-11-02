package it.exolab.exobank.controller;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.ScacchieraControllerInterface;
import it.exolab.exobank.validatore.ValidaMosseScacchi;
import it.exolab.exobank.validatore.ValidatoreScaccoAlRe;

@Stateless(name = "ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface {

	private Scacchiera scacchieraLavoro = new Scacchiera();
	private ValidaMosseScacchi validatoreScacchi = new ValidaMosseScacchi();

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

		try {
			Pezzo pezzoSpecifico = findPezzoById(pezzo, griglia);
			if (pezzoSpecifico == null) {
				throw new Exception("Nessun pezzo con l'ID specifico è stato trovato.");
			}
			Integer xPartenza = pezzoSpecifico.getPosizioneX();
			Integer yPartenza = pezzoSpecifico.getPosizioneY();
			Integer xDestinazione = pezzo.getPosizioneX();
			Integer yDestinazione = pezzo.getPosizioneY();

			if(validaScacco.isScacco(trovaRe(pezzoSpecifico, griglia).getPosizioneX(), trovaRe(pezzoSpecifico, griglia).getPosizioneY(), griglia, trovaRe(pezzoSpecifico, griglia).getColore())) {

				// Utilizza il validatoreScacchi per verificare la validità della mossa
				if (validatoreScacchi.mossaConsentitaPerPezzo(pezzoSpecifico, xPartenza, yPartenza, xDestinazione, yDestinazione, griglia)) {
					if (null != griglia[xDestinazione][yDestinazione]) {
						griglia[xDestinazione][yDestinazione].setEsiste(false); // setto esiste a false così dico che il pezzo non esiste più sulla scacchiera
						griglia[xDestinazione][yDestinazione] = null; // setto a null quella cella della griglia così da dire che il pezzo è stato mangiato
						// Aggiorna la scacchiera con la nuova posizione
						griglia[xPartenza][yPartenza] = null; // Rimuovi il pezzo dalla posizione precedente
						griglia[xDestinazione][yDestinazione] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
						pezzoSpecifico.setPosizioneX(xDestinazione); // Aggiorna le coordinate del pezzo
						pezzoSpecifico.setPosizioneY(yDestinazione);
					} else {
						// Aggiorna la scacchiera con la nuova posizione
						griglia[xPartenza][yPartenza] = null; // Rimuovi il pezzo dalla posizione precedente
						griglia[xDestinazione][yDestinazione] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
						pezzoSpecifico.setPosizioneX(xDestinazione); // Aggiorna le coordinate del pezzo
						pezzoSpecifico.setPosizioneY(yDestinazione);
					}
					scacchieraLavoro.setScacchiera(griglia);
				} else {
					throw new Exception("Mossa non consentita");
				}
			}else {
				if(validatoreScacchi.puoiRimuovereScacco(trovaRe(pezzoSpecifico, griglia), griglia, scacchieraLavoro)) {
					// Utilizza il validatoreScacchi per verificare la validità della mossa
					if (validatoreScacchi.mossaConsentitaPerPezzo(pezzoSpecifico, xPartenza, yPartenza, xDestinazione, yDestinazione, griglia)) {
						if (null != griglia[xDestinazione][yDestinazione]) {
							griglia[xDestinazione][yDestinazione].setEsiste(false); // setto esiste a false così dico che il pezzo non esiste più sulla scacchiera
							griglia[xDestinazione][yDestinazione] = null; // setto a null quella cella della griglia così da dire che il pezzo è stato mangiato
							// Aggiorna la scacchiera con la nuova posizione
							griglia[xPartenza][yPartenza] = null; // Rimuovi il pezzo dalla posizione precedente
							griglia[xDestinazione][yDestinazione] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
							pezzoSpecifico.setPosizioneX(xDestinazione); // Aggiorna le coordinate del pezzo
							pezzoSpecifico.setPosizioneY(yDestinazione);
						} else {
							// Aggiorna la scacchiera con la nuova posizione
							griglia[xPartenza][yPartenza] = null; // Rimuovi il pezzo dalla posizione precedente
							griglia[xDestinazione][yDestinazione] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
							pezzoSpecifico.setPosizioneX(xDestinazione); // Aggiorna le coordinate del pezzo
							pezzoSpecifico.setPosizioneY(yDestinazione);
						}
						scacchieraLavoro.setScacchiera(griglia);
					} else {
						throw new Exception("Mossa non consentita");
					}
				}else {
					throw new Exception("SCACCO MATTO!");
				}
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

	// Metodo per creare la scacchiera iniziale
	private Scacchiera creazioneScacchieraIniziale() throws Exception {
		try {
			Pezzo[][] griglia = new Pezzo[8][8];
			int idPezzo = 1;

			// Inizializza la prima riga (0) con i pezzi iniziali (torre, cavallo, alfiere, re, regina, alfiere, cavallo, torre)
			griglia[0][0] = new Pezzo(idPezzo++, Costanti.TORRE, Costanti.BIANCO, 0, 0, true);
			griglia[0][1] = new Pezzo(idPezzo++, Costanti.CAVALLO, Costanti.BIANCO, 0, 1, true);
			griglia[0][2] = new Pezzo(idPezzo++, Costanti.ALFIERE, Costanti.BIANCO, 0, 2, true);
			griglia[0][3] = new Pezzo(idPezzo++, Costanti.RE, Costanti.BIANCO, 0, 3, true);
			griglia[0][4] = new Pezzo(idPezzo++, Costanti.REGINA, Costanti.BIANCO, 0, 4, true);
			griglia[0][5] = new Pezzo(idPezzo++, Costanti.ALFIERE, Costanti.BIANCO, 0, 5, true);
			griglia[0][6] = new Pezzo(idPezzo++, Costanti.CAVALLO, Costanti.BIANCO, 0, 6, true);
			griglia[0][7] = new Pezzo(idPezzo++, Costanti.TORRE, Costanti.BIANCO, 0, 7, true);

			// Inizializza la seconda riga (1) con i pedoni bianchi
			for (int colonna = 0; colonna < 8; colonna++) {
				griglia[1][colonna] = new Pezzo(idPezzo++, Costanti.PEDONE, Costanti.BIANCO, 1, colonna, true);
			}

			// Inizializza le righe 2-5 con caselle vuote
			for (int riga = 2; riga < 6; riga++) {
				for (int colonna = 0; colonna < 8; colonna++) {
					griglia[riga][colonna] = null; // Casella vuota
				}
			}

			// Inizializza la sesta riga (6) con i pedoni neri
			for (int colonna = 0; colonna < 8; colonna++) {
				griglia[6][colonna] = new Pezzo(idPezzo++, Costanti.PEDONE, Costanti.BIANCO, 6, colonna, true);
			}

			// Inizializza l'ultima riga (7) con i pezzi iniziali neri (torre, cavallo, alfiere, re, regina, alfiere, cavallo, torre)
			griglia[7][0] = new Pezzo(idPezzo++, Costanti.TORRE, Costanti.NERO, 7, 0, true);
			griglia[7][1] = new Pezzo(idPezzo++, Costanti.CAVALLO, Costanti.NERO, 7, 1, true);
			griglia[7][2] = new Pezzo(idPezzo++, Costanti.ALFIERE, Costanti.NERO, 7, 2, true);
			griglia[7][3] = new Pezzo(idPezzo++, Costanti.RE, Costanti.NERO, 7, 3, true);
			griglia[7][4] = new Pezzo(idPezzo++, Costanti.REGINA, Costanti.NERO, 7, 4, true);
			griglia[7][5] = new Pezzo(idPezzo++, Costanti.ALFIERE, Costanti.NERO, 7, 5, true);
			griglia[7][6] = new Pezzo(idPezzo++, Costanti.CAVALLO, Costanti.NERO, 7, 6, true);
			griglia[7][7] = new Pezzo(idPezzo++, Costanti.TORRE, Costanti.NERO, 7, 7, true);
			
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
			// Gestisci l'eccezione qui
			e.printStackTrace();
			throw new Exception("Pezzo non trovato"); // Restituisci un valore adeguato in caso di errore
		}
	}

	private Pezzo trovaRe(Pezzo pezzo, Pezzo[][] griglia) {
		Pezzo reStessoColore = null;

		for (int x = 0; x < griglia.length; x++) {
			for (int y = 0; y < griglia[x].length; y++) {
				Pezzo pezzoCorrente = griglia[x][y];
				if (pezzoCorrente != null && pezzoCorrente.getTipo().equals(Costanti.RE) && pezzoCorrente.getColore().equals(pezzo.getColore())) {
					return reStessoColore = pezzoCorrente;
				}
			}
		}
		return reStessoColore;
	}
}