package it.exolab.exobank.conf;

import java.sql.SQLException;
import java.util.List;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.ContoCorrenteControllerInterface;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Utente;

public class ContoCorrenteService {

	private EJBService<ContoCorrenteControllerInterface> service = new EJBService<ContoCorrenteControllerInterface>();

	

	

	public Dto<ContoCorrente> updateConto(ContoCorrente conto) throws SQLException, Exception {
		return service.getServizioContoCorrenteControllerInterface().updateConto(conto);
	}
	
	public Dto<ContoCorrente> findContoByIdUtente(Utente utente) throws SQLException, Exception {
		return service.getServizioContoCorrenteControllerInterface().findContoByIdUtente(utente);
	}
	
	public  Dto<ContoCorrente> insertContoCorrente(Utente utente) throws Exception {
		return service.getServizioContoCorrenteControllerInterface().insertContoCorrente(utente);
	}
	
	public Dto <List<ContoCorrente>> findAllConto() throws SQLException, Exception {
		return service.getServizioContoCorrenteControllerInterface().listaConti();
	}

}
