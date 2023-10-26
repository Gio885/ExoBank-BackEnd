package it.exolab.exobank.controller;

import javax.ejb.Local;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;

@Local
public interface ScacchieraControllerInterface {
	
	Scacchiera scacchieraIniziale();

//	boolean mossaConsentita(Pezzo pezzo);

	
}