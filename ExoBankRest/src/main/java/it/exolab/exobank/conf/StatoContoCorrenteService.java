package it.exolab.exobank.conf;

import java.util.List;

import it.exolab.exobank.ejbinterface.StatoContoCorrenteControllerInterface;
import it.exolab.exobank.models.StatoContoCorrente;

public class StatoContoCorrenteService {

	
	private EJBService<StatoContoCorrenteControllerInterface> service = new EJBService <StatoContoCorrenteControllerInterface>();
	
	
	
	
	public List<StatoContoCorrente> findAllStatiConto() throws Exception{
		return service.getServizioStatoContoCorrenteControllerInterface().findAllStatiConto();
		
	}

	
	
}
