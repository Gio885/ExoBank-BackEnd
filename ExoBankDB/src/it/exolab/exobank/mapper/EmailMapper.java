package it.exolab.exobank.mapper;

import java.util.List;

import it.exolab.exobank.models.Email;

public interface EmailMapper {

	List<Email> listaEmail();

	int insertEmail(Email email);
	
	Email findLastInsertEmail();
	
	int updateEmail(Email email);
}
