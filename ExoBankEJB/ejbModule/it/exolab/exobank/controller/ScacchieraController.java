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
import it.exolab.exobank.validatore.ValidaMosseScacchi;

@Stateless(name = "ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface {

	private Scacchiera scacchieraLavoro = new Scacchiera();
	private ValidaMosseScacchi validatoreScacchi = new ValidaMosseScacchi();
	
	
	@Override
	public Scacchiera scacchieraIniziale() {
		Scacchiera scacchieraIniziale = creazioneScacchieraIniziale();
		return scacchieraIniziale;
	}

	@Override
	public boolean mossaConsentita(Pezzo pezzo) {
	    Pezzo[][] griglia = scacchieraLavoro.getGriglia();

	    // Trova il pezzo specifico sulla scacchiera usando l'ID
	    Pezzo pezzoSpecifico = (Pezzo) findPezzoById(pezzo, griglia);

	    if (pezzoSpecifico == null) {
	        return false; // Nessun pezzo con l'ID specifico è stato trovato.
	    }

	    Integer xPartenza = pezzoSpecifico.getPosizioneX();
	    Integer yPartenza = pezzoSpecifico.getPosizioneY();
	    Integer xDestinazione = pezzo.getPosizioneX();
	    Integer yDestinazione = pezzo.getPosizioneY();
	    // Verifica se la destinazione è all'interno della scacchiera
	    if (xDestinazione < 0 || xDestinazione >= griglia.length || yDestinazione < 0 || yDestinazione >= griglia[0].length) {
	        return false; // La destinazione è fuori dalla scacchiera.
	    }

	    // Verifica se la destinazione è la stessa posizione di partenza
	    if (xPartenza.equals(xDestinazione) && yPartenza.equals(yDestinazione)) {
	        return false; // La destinazione è la stessa posizione di partenza.
	    }

	    Pezzo pezzoDestinazione = griglia[xDestinazione][yDestinazione];

	    // Verifica se il pezzo di destinazione è dello stesso colore del pezzo in movimento
	    if (pezzoDestinazione != null && pezzoDestinazione.getColore().equals(pezzoSpecifico.getColore())) {
	        return false; // Il pezzo di destinazione è dello stesso colore del pezzo in movimento.
	    }

	    // Utilizza il validatoreScacchi per verificare la validità della mossa
	    return validatoreScacchi.mossaValida(pezzoSpecifico, xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
	}

	// Metodo per creare la scacchiera iniziale
	private Scacchiera creazioneScacchieraIniziale() {
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
	}

	public Object findPezzoById(Pezzo pezzo, Pezzo[][] griglia) {
		Integer idPezzo = pezzo.getId();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (null != griglia[x][y] && griglia[x][y].getId() == idPezzo) {
					if (griglia[x][y] instanceof Torre) {
						return (Torre) griglia[x][y];
					} else if (griglia[x][y] instanceof Alfiere) {
						return (Alfiere) griglia[x][y];
					}  else if (griglia[x][y] instanceof Re) {
						return (Re) griglia[x][y];
					}  else if (griglia[x][y] instanceof Regina) {
						return (Regina) griglia[x][y];
					}  else if (griglia[x][y] instanceof Cavallo) {
						return (Cavallo) griglia[x][y];
					}  else if (griglia[x][y] instanceof Pedone) {
						return (Pedone) griglia[x][y];
					} 
				}
			}
		}
		return null; // Se nessun pezzo con l'ID specifico è stato trovato
	} 


	
}