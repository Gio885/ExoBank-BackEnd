package it.exolab.exobank.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.StatoContoCorrenteControllerInterface;
import it.exolab.exobank.models.StatoContoCorrente;



@Path(Costanti.STATO_CONTO_CORRENTE)
public class StatoContoCorrenteRest {

	
	
	
	
	
	@GET
	@Path(Costanti.LISTA_STATI_CONTO)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response listaStatoConto() {
		try {
			StatoContoCorrenteControllerInterface statoContoCorrenteService = new EJBFactory<StatoContoCorrenteControllerInterface>(StatoContoCorrenteControllerInterface.class).getEJB();			
			List<StatoContoCorrente> listaStatiConto = statoContoCorrenteService.findAllStatiConto();
			if(null != listaStatiConto) {
				return Response.status(Status.OK).entity(listaStatiConto).build();
			}else {
				return Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}
	
	
	
}
