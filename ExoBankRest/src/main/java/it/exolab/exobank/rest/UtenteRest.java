package it.exolab.exobank.rest;


import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.exolab.exobank.models.*;
import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.UtenteControllerInterface;


@Path(Costanti.UTENTE_REST)
public class UtenteRest {

	
	@POST
	@Path(Costanti.LOGIN_UTENTE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response loginUtente(Utente utente) {
		
		try {
			UtenteControllerInterface utenteService = new EJBFactory<UtenteControllerInterface>(UtenteControllerInterface.class).getEJB();			
			Dto <Utente> dtoUtente = utenteService.findUtenteByEmailPassword(utente);
			if (dtoUtente.isSuccess()) {
				return Response.status(Status.OK).entity(dtoUtente.getData()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(dtoUtente.getErrore()).build();
			}
		} catch (Exception e) {
			 if (e instanceof NamingException) {
			        // Crea una nuova istanza di NamingException con il messaggio personalizzato
			        NamingException namingException = new NamingException("Messaggio personalizzato per NamingException");
			        namingException.initCause(e); // Mantieni il riferimento all'eccezione originale
			        e.printStackTrace();
			        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(namingException.getMessage()).build();
			    }
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}


	@POST
	@Path(Costanti.INSERT_UTENTE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response insertUtente(Utente utente) {
		try {
			UtenteControllerInterface utenteService = new EJBFactory<UtenteControllerInterface>(UtenteControllerInterface.class).getEJB();			
			Dto <Utente> dtoUtente = utenteService.insertUtente(utente);	
			if (dtoUtente.isSuccess()) {
				return Response.status(Status.OK).entity(dtoUtente.getData()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(dtoUtente).build();    
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	




}
