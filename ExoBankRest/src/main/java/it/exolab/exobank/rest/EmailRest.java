package it.exolab.exobank.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.ibatis.session.SqlSession;

import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.CostantiEmail;
import it.exolab.exobank.crud.EmailCrud;
import it.exolab.exobank.ejbinterface.EmailControllerInterface;
import it.exolab.exobank.mapper.EmailMapper;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Email;
import it.exolab.exobank.models.Utente;
import it.exolab.exobank.sendemail.SendEmail;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

@Path("/emailRest")
public class EmailRest {

	@POST
	@Path("/emailBenvenuto")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response insertAndSendEmail(Utente utente) {
		try {
			EmailControllerInterface serviceEmail = new EJBFactory<EmailControllerInterface>(EmailControllerInterface.class).getEJB();
			Email email = serviceEmail.insertAndSendEmail(utente,utente.getContoCorrente(), CostantiEmail.EMAIL_ID_REGISTRAZIONE);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/emailRichiestaAperturaConto")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response emailRichiestaAperturaConto(Utente utente) {
		try {
			EmailControllerInterface serviceEmail = new EJBFactory<EmailControllerInterface>(EmailControllerInterface.class).getEJB();
			Email email = serviceEmail.insertAndSendEmail(utente,utente.getContoCorrente(), CostantiEmail.EMAIL_ID_APERTURA_CONTO);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/emailAggiornamentoConto")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response emailAggiornamentoConto(ContoCorrente conto) {
		try {
			Utente utente= conto.getUtente();
			EmailControllerInterface serviceEmail = new EJBFactory<EmailControllerInterface>(EmailControllerInterface.class).getEJB();
			Email email = serviceEmail.insertAndSendEmail(utente,conto, CostantiEmail.EMAIL_ID_AGGIORNAMENTO_CONTO);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public void LIST() {
		try {
			EmailCrud crud = new EmailCrud();
			SqlMapFactory factory = SqlMapFactory.instance();
			SqlSession session = factory.openSession();
			EmailMapper mapper = session.getMapper(EmailMapper.class);
			List<Email> listaEmail = mapper.listaEmail();
			System.out.println("ciao");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
		}

	}

}
