package it.exolab.exobank.conf;

import java.sql.SQLException;
import java.util.List;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.UtenteControllerInterface;
import it.exolab.exobank.models.Utente;

public class UtenteService {

	private EJBService<UtenteControllerInterface> service = new EJBService<UtenteControllerInterface>();

	public Dto<List<Utente>> findAllUtente() throws Exception {
		return service.getServizioUtenteControllerInterface().findAll();
	}

	public Dto<Utente> findUtenteByEmailPassword(Utente utente) throws Exception {
		return service.getServizioUtenteControllerInterface().findUtenteByEmailPassword(utente);
	}

	public Dto<Utente> insertUtente(Utente utente) throws SQLException, Exception {
		return service.getServizioUtenteControllerInterface().insertUtente(utente);
	}

}
