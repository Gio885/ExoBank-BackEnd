package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.mapper.ContoCorrenteMapper;
import it.exolab.exobank.models.*;
import it.exolab.exobank.costanti.*;
public class ContoCorrenteCrud {

	public ContoCorrente findContoByIdUtente(Integer id, ContoCorrenteMapper mapper) throws Exception {

		try {
			ContoCorrente contoUtente = mapper.findContoByIdUtente(id);
			return contoUtente;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findContoByIdUtente-----CRUD------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public ContoCorrente insertContoCorrente(ContoCorrente conto, ContoCorrenteMapper mapper) throws Exception {

		try {
			mapper.insertContoCorrente(conto);
			ContoCorrente contoInserito = mapper.findContoByIdUtente(conto.getUtente().getId());
			return contoInserito;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore insertContoCorrente-----CRUD------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public List<ContoCorrente> listaConti(ContoCorrenteMapper mapper) throws Exception {
		try {
			List<ContoCorrente> listaConti = mapper.findAllConti();
			return listaConti;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore listaConti-----CRUD------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public int updateConto(ContoCorrente conto, ContoCorrenteMapper mapper) throws Exception {

		try {
			int righeUpdateContoAggiornate = mapper.updateConto(conto) ? 1 : 0;
			return righeUpdateContoAggiornate;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore updateConto-----CRUD------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}

	}
}
