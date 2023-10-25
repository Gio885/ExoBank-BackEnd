package it.exolab.exobank.conf;


import java.sql.SQLException;
import java.util.List;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.TransazioneControllerInterface;
import it.exolab.exobank.models.Transazione;

public class TransazioneService {

	private EJBService<TransazioneControllerInterface> service = new EJBService<TransazioneControllerInterface>();


	public Dto <Transazione> insertTransazione(Transazione transazione) throws SQLException, Exception {
		return service.getServizioTransazioneControllerInterface().insertTransazione(transazione);
	}
	
	public Dto<List<Transazione>> findAllTransazioni() throws SQLException, Exception {
		return service.getServizioTransazioneControllerInterface().findAllTransazioni();
	}
	
	public Dto <Transazione> updateTransazione(Transazione transazione) throws SQLException, Exception {
		return service.getServizioTransazioneControllerInterface().updateTransazione(transazione);
	}
}
