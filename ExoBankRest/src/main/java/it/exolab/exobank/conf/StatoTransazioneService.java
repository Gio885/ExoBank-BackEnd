package it.exolab.exobank.conf;

import java.util.List;

import it.exolab.exobank.ejbinterface.StatoTransazioneControllerInterface;
import it.exolab.exobank.models.StatoTransazione;

public class StatoTransazioneService {

	private EJBService<StatoTransazioneControllerInterface> service = new EJBService<StatoTransazioneControllerInterface>();

	public List<StatoTransazione> findStatiTransazione() throws Exception {
		return service.getServizioStatoTransazioneControllerInterface().findStatiTransazione();
	}
	
}
