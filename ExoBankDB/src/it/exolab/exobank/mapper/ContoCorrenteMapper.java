package it.exolab.exobank.mapper;

import java.util.List;

import it.exolab.exobank.models.ContoCorrente;

public interface ContoCorrenteMapper {

	ContoCorrente findContoByIdUtente(Integer id);

	List<ContoCorrente> findAllConti();

	boolean insertContoCorrente(ContoCorrente conto);

	boolean updateConto(ContoCorrente conto);

	int contoRigheAggiornate();
}
