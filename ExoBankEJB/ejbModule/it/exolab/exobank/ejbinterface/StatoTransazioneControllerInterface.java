package it.exolab.exobank.ejbinterface;

import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.models.StatoTransazione;

@Local
public interface StatoTransazioneControllerInterface {

	List<StatoTransazione> findStatiTransazione() throws Exception;
		
}
