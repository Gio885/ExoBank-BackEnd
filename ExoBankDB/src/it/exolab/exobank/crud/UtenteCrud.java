package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.UtenteMapper;
import it.exolab.exobank.models.Utente;

public class UtenteCrud {

	public List<Utente> findAllUtente(UtenteMapper mapper) throws Exception {
		try {
			List<Utente> listaUtenti = mapper.findAllUtente();
			return listaUtenti;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllUtente ----CRUD----");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public Utente findUtenteByEmailPassword(Utente utente, UtenteMapper mapper) throws Exception {
		try {
			Utente utenteDaTrovare = mapper.findUtenteByEmailPassword(utente);
			return utenteDaTrovare;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findUtenteByEmailPassword ---CRUD---- ");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public Utente insertUtente(Utente utente, UtenteMapper mapper) throws Exception {
		try {
			mapper.insertUtente(utente);
			Utente utenteInserito = mapper.findUtenteByEmailPassword(utente);
			return utenteInserito;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findUtenteByEmailPassword ---CRUD----");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	public Utente findUtenteById(Utente utente, UtenteMapper mapper) throws Exception {
		try {
			Utente utenteDaTrovare = mapper.findUtenteById(utente);
			return utenteDaTrovare;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findUtenteById ---CRUD----");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

}
