package it.exolab.exobank.ejbinterface;

import javax.ejb.Local;

import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Email;
import it.exolab.exobank.models.Utente;

@Local
public interface EmailControllerInterface {

	Email insertAndSendEmail(Utente utente,ContoCorrente conto,Integer tipo) throws Exception;
		
}
