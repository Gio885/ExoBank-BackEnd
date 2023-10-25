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
import it.exolab.exobank.ejbinterface.TipoTransazioneControllerInterface;
import it.exolab.exobank.models.TipoTransazione;

@Path(Costanti.TIPO_TRANSAZIONE_REST)
public class TipoTransazioneRest {

	@GET
	@Path(Costanti.FIND_TIPI_STATI_TRANSAZIONE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findAllStatiTransazione(){
		try {
			TipoTransazioneControllerInterface tipoTransazioneService = new EJBFactory<TipoTransazioneControllerInterface>(TipoTransazioneControllerInterface.class).getEJB();
			List<TipoTransazione> listaTipoTransazione= tipoTransazioneService.findAllStatiTransazione();
			if(listaTipoTransazione!=null) {
				return Response.status(Status.OK).entity(listaTipoTransazione).build();
			}else {
				return Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

}
