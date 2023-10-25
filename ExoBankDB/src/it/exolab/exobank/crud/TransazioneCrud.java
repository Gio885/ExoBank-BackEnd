package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.TransazioneMapper;
import it.exolab.exobank.models.Transazione;

public class TransazioneCrud {

	public Transazione insertTransazione(Transazione transazione, TransazioneMapper mapper) throws Exception {
		try {
			mapper.insertTransazione(transazione);
			Transazione transazioneInserita = mapper.findLastTransazione();
			return transazioneInserita;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore insertTransazione ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public List<Transazione> findAllTransazioni(TransazioneMapper mapper) throws Exception {
		try {
			List<Transazione> listaTransazioni = mapper.findAllTransazioni();
			return listaTransazioni;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllTransazioni ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}

	}

	public List<Transazione> findTransazioniUtente(TransazioneMapper mapper, Integer idUtente) throws Exception {
		try {
			List<Transazione> listaTransazioni = mapper.findTransazioniUtente(idUtente);
			return listaTransazioni;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findTransazioniUtente ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}

	}

	public int updateTransazione(Transazione transazione, TransazioneMapper mapper) throws Exception {
		try {
			int numeroRigheAggiornate=mapper.updateTransazione(transazione)?1:0;
			return numeroRigheAggiornate;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore updateTransazione ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

}
