package it.exolab.exobank.ejbinterface;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

@Local
public interface TransazioneControllerInterface {

	Dto <Transazione> insertTransazione(Transazione transazione) throws SQLException, Exception;
	
	Dto <List<Transazione>> findAllTransazioni() throws SQLException, Exception;
	
	Dto <Transazione> updateTransazione(Transazione transazione) throws SQLException, Exception;
	
	Dto <List<Transazione>> findTransazioniUtente(Utente utente) throws SQLException, Exception;
	
}
