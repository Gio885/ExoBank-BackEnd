package it.exolab.exobank.mapper;

import java.util.List;

import it.exolab.exobank.models.*;

public interface UtenteMapper {

	List<Utente> findAllUtente();

	Utente findUtenteByEmailPassword(Utente utente);

	Utente findUtenteById(Utente utente);

	boolean insertUtente(Utente utente);

}
