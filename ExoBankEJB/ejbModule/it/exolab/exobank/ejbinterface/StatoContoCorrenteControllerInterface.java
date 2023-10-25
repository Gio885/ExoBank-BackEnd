package it.exolab.exobank.ejbinterface;

import java.util.List;

import javax.ejb.Local;

import it.exolab.exobank.models.StatoContoCorrente;

@Local
public interface StatoContoCorrenteControllerInterface {

	
	List<StatoContoCorrente> findAllStatiConto() throws Exception;

	
}
