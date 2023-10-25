package it.exolab.exobank.mapper;

import java.util.List;

import it.exolab.exobank.models.Transazione;

public interface TransazioneMapper {

	List<Transazione> findAllTransazioni();

	List<Transazione> findTransazioniUtente(Integer idUtente);

	Transazione findLastTransazione();

	boolean insertTransazione(Transazione transazione);

	boolean updateTransazione(Transazione transazione);

	int contoRigheAggiornate();
}
