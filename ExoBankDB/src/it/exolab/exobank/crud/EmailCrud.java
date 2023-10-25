package it.exolab.exobank.crud;

import java.util.List;


import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.EmailMapper;
import it.exolab.exobank.models.Email;

public class EmailCrud {

	public List<Email> listaEmailDaReinviare(EmailMapper mapper) throws Exception{
		try {
			List<Email>listaEmailDaInviare = mapper.listaEmail();
			return listaEmailDaInviare;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("ERRORE CRUD EMAIL");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}		
	}
	
	public Email insertEmail(EmailMapper mapper,Email email) throws Exception {
		try {
			mapper.insertEmail(email);
			Email emailInserita = mapper.findLastInsertEmail();
			return emailInserita;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("ERRORE EMAIL CRUD --------- METODO INSERT EMAIL---------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}	
	}
	
	public int updateEmail(EmailMapper mapper,Email email) throws Exception {
		try {
			int righeAggiornate = mapper.updateEmail(email);
			return righeAggiornate;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("ERRORE CRUD EMAIL -------METODO UPDATE EMAIL-------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}
	
	
}
