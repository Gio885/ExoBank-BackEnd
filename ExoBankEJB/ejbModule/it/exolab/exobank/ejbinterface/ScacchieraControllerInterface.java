package it.exolab.exobank.ejbinterface;

import javax.ejb.Local;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;

@Local
public interface ScacchieraControllerInterface {
	
	Scacchiera scacchieraIniziale() throws Exception;

	Scacchiera mossaConsentita(Pezzo pezzo) throws Exception;

	Scacchiera aggiornamentoTipoPedone(Pezzo pezzo) throws Exception;

	
}