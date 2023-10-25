package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.StatoTransazioneMapper;
import it.exolab.exobank.models.StatoTransazione;

public class StatoTransazioneCrud {

	public List<StatoTransazione> findStatiTransazione(StatoTransazioneMapper mapper) throws Exception {
		try {
			List<StatoTransazione> listaStatiTransazioni = mapper.findStatiTransazione();
			return listaStatiTransazioni;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findStatiTransazione ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

}
