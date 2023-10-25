package it.exolab.exobank.ejbinterface;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Utente;

@Local
public interface UtenteControllerInterface {

	
	Dto<List<Utente>> findAll() throws Exception;
	Dto<Utente> insertUtente(Utente utente) throws SQLException, Exception;
	Dto<Utente> findUtenteByEmailPassword(Utente utente) throws Exception;
	
}
