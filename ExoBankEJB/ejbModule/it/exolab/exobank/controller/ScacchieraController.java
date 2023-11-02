package it.exolab.exobank.controller;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.chess.model.Pedone;
import it.exolab.exobank.chess.model.Torre;
import it.exolab.exobank.chess.model.Re;
import it.exolab.exobank.chess.model.Regina;
import it.exolab.exobank.chess.model.Cavallo;
import it.exolab.exobank.chess.model.Alfiere;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.ScacchieraControllerInterface;
import it.exolab.exobank.validatore.ValidaMosseScacchi;

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

		try {
			Pezzo pezzoSpecifico = (Pezzo) findPezzoById(pezzo, griglia);
			if (pezzoSpecifico == null) {
				throw new Exception("Nessun pezzo con l'ID specifico è stato trovato.");
			}

			

			if(validatoreScacchi.puoiRimuovereScacco(trovaRe(pezzoSpecifico, griglia), griglia, scacchieraLavoro)) {
				Integer xPartenza = pezzoSpecifico.getPosizioneX();
				Integer yPartenza = pezzoSpecifico.getPosizioneY();
				Integer xDestinazione = pezzo.getPosizioneX();
				Integer yDestinazione = pezzo.getPosizioneY();

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
			}
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
			griglia[0][0] = new Torre(Costanti.BIANCO, 0, 0, idPezzo++, true);
			griglia[0][1] = new Cavallo(Costanti.BIANCO, 0, 1, idPezzo++, true);
			griglia[0][2] = new Alfiere(Costanti.BIANCO, 0, 2, idPezzo++, true);
			griglia[0][3] = new Re(Costanti.BIANCO, 0, 3, idPezzo++, true);
			griglia[0][4] = new Regina(Costanti.BIANCO, 0, 4, idPezzo++, true);
			griglia[0][5] = new Alfiere(Costanti.BIANCO, 0, 5, idPezzo++, true);
			griglia[0][6] = new Cavallo(Costanti.BIANCO, 0, 6, idPezzo++, true);
			griglia[0][7] = new Torre(Costanti.BIANCO, 0, 7, idPezzo++, true);

			// Inizializza la seconda riga (1) con i pedoni bianchi
			for (int colonna = 0; colonna < 8; colonna++) {
				griglia[1][colonna] = new Pedone(Costanti.BIANCO, 1, colonna, idPezzo++, true);
			}

			// Inizializza le righe 2-5 con caselle vuote
			for (int riga = 2; riga < 6; riga++) {
				for (int colonna = 0; colonna < 8; colonna++) {
					griglia[riga][colonna] = null; // Casella vuota
				}
			}

			// Inizializza la sesta riga (6) con i pedoni neri
			for (int colonna = 0; colonna < 8; colonna++) {
				griglia[6][colonna] = new Pedone(Costanti.NERO, 6, colonna, idPezzo++, true);
			}

			// Inizializza l'ultima riga (7) con i pezzi iniziali neri (torre, cavallo, alfiere, re, regina, alfiere, cavallo, torre)
			griglia[7][0] = new Torre(Costanti.NERO, 7, 0, idPezzo++, true);
			griglia[7][1] = new Cavallo(Costanti.NERO, 7, 1, idPezzo++, true);
			griglia[7][2] = new Alfiere(Costanti.NERO, 7, 2, idPezzo++, true);
			griglia[7][3] = new Re(Costanti.NERO, 7, 3, idPezzo++, true);
			griglia[7][4] = new Regina(Costanti.NERO, 7, 4, idPezzo++, true);
			griglia[7][5] = new Alfiere(Costanti.NERO, 7, 5, idPezzo++, true);
			griglia[7][6] = new Cavallo(Costanti.NERO, 7, 6, idPezzo++, true);
			griglia[7][7] = new Torre(Costanti.NERO, 7, 7, idPezzo++, true);

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

	public Object findPezzoById(Pezzo pezzo, Pezzo[][] griglia) throws Exception {
		try {
			Integer idPezzo = pezzo.getId();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (null != griglia[x][y] && griglia[x][y].getId() == idPezzo) {
						if (griglia[x][y] instanceof Torre) {
							return (Torre) griglia[x][y];
						} else if (griglia[x][y] instanceof Alfiere) {
							return (Alfiere) griglia[x][y];
						} else if (griglia[x][y] instanceof Re) {
							return (Re) griglia[x][y];
						} else if (griglia[x][y] instanceof Regina) {
							return (Regina) griglia[x][y];
						} else if (griglia[x][y] instanceof Cavallo) {
							return (Cavallo) griglia[x][y];
						} else if (griglia[x][y] instanceof Pedone) {
							return (Pedone) griglia[x][y];
						}
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
				if (pezzoCorrente != null && pezzoCorrente instanceof Re && pezzoCorrente.getColore().equals(pezzo.getColore())) {
					return reStessoColore = pezzoCorrente;
				}
			}
		}
		return reStessoColore;
	}
}