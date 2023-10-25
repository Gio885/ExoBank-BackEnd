package it.exolab.exobank.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.exolab.exobank.models.*;
import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.ContoCorrenteControllerInterface;

@Path(Costanti.CONTO_CORRENTE_REST)
public class ContoCorrenteRest {


	@GET
	@Path(Costanti.LISTA_CONTI)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findAllConti() {
		try {
			ContoCorrenteControllerInterface contoService = new EJBFactory<ContoCorrenteControllerInterface>(ContoCorrenteControllerInterface.class).getEJB();			
			Dto<List<ContoCorrente>> dtoListaConti = contoService.listaConti();
			if (dtoListaConti.isSuccess()) {
				return Response.status(Status.OK).entity(dtoListaConti).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoListaConti).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(Costanti.FIND_CONTO_BY_ID_UTENTE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findContoByIdUtente(Utente utente) {

		try {
			ContoCorrenteControllerInterface contoService = new EJBFactory<ContoCorrenteControllerInterface>(ContoCorrenteControllerInterface.class).getEJB();			
			Dto<ContoCorrente> dtoConto = contoService.findContoByIdUtente(utente);
			if (dtoConto.isSuccess()) {
				return Response.status(Status.OK).entity(dtoConto).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoConto).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(Costanti.INSERT_CONTO)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response insertConto(Utente utente) {
		try {
			ContoCorrenteControllerInterface contoService = new EJBFactory<ContoCorrenteControllerInterface>(ContoCorrenteControllerInterface.class).getEJB();			
			Dto<ContoCorrente> dtoConto = contoService.insertContoCorrente(utente);
			if (dtoConto.isSuccess()) {
				return Response.status(Status.OK).entity(dtoConto).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoConto).build();      
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@PUT
	@Path(Costanti.UPDATE_CONTO_CORRENTE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateConto(ContoCorrente conto) {
		try {
			ContoCorrenteControllerInterface contoService = new EJBFactory<ContoCorrenteControllerInterface>(ContoCorrenteControllerInterface.class).getEJB();			
			Dto<ContoCorrente> dtoConto = contoService.updateConto(conto);
			if(dtoConto.isSuccess()) {
				return Response.status(Status.OK).entity(dtoConto.getData()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(dtoConto).build();      
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

}
