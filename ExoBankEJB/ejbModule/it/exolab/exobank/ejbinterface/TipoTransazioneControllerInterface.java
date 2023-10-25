package it.exolab.exobank.ejbinterface;

import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.models.TipoTransazione;

@Local
public interface TipoTransazioneControllerInterface {

	List <TipoTransazione> findAllStatiTransazione() throws Exception ;
}
