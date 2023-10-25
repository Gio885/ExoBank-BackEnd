package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.TipoTransazioneMapper;
import it.exolab.exobank.models.*;

public class TipoTransazioneCrud {

	public List<TipoTransazione> findAllTipoTransazione(TipoTransazioneMapper mapper) throws Exception {
		try {
			List<TipoTransazione> listaTipoTransazione = mapper.findAllTipoTransazione();
			return listaTipoTransazione;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllTipoTransazione-----CRUD------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}

	}

}
