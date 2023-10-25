package it.exolab.exobank.ejbinterface;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Utente;

@Local
public interface ContoCorrenteControllerInterface {

	Dto<ContoCorrente> findContoByIdUtente(Utente utente) throws SQLException, Exception;

	Dto<ContoCorrente> insertContoCorrente(Utente utente) throws Exception;

	Dto<List<ContoCorrente>> listaConti() throws SQLException, Exception;

	Dto <ContoCorrente> updateConto(ContoCorrente conto) throws SQLException, Exception;

}
