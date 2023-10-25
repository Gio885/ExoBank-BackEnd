package it.exolab.exobank.rest;

import java.util.List;

import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.StatoTransazioneControllerInterface;
import it.exolab.exobank.models.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path(Costanti.STATO_TRANSAZIONE_REST)
public class StatoTransazioneRest {
	

	
	
	@GET
	@Path(Costanti.FIND_STATI_TRANSAZIONE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findStatiTransazione() {
		try {
			StatoTransazioneControllerInterface statoTransazioneService = new EJBFactory<StatoTransazioneControllerInterface>(StatoTransazioneControllerInterface.class).getEJB();			
			List<StatoTransazione> listaStatiTransazione= statoTransazioneService.findStatiTransazione();
			if(listaStatiTransazione!=null) {
				return Response.status(Status.OK).entity(listaStatiTransazione).build();
			}else {
				return Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	

}
