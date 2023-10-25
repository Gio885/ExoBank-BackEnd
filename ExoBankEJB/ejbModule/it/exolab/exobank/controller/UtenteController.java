package it.exolab.exobank.controller;

import it.exolab.exobank.convertitore.Convertitore;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.costanti.CostantiEmail;
import it.exolab.exobank.validatore.Validatore;
import it.exolab.exobank.crud.UtenteCrud;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.UtenteControllerInterface;
import it.exolab.exobank.mapper.UtenteMapper;
import it.exolab.exobank.models.Utente;
import it.exolab.exobank.sendemail.SendEmail;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class UtenteController
 */
@Stateless(name = "UtenteControllerInterface")
@LocalBean
public class UtenteController implements UtenteControllerInterface { // MANCA DTO DA FRONT END

	/**
	 * Default constructor.
	 */
	public UtenteController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dto<List<Utente>> findAll() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<List<Utente>> dtoListaUtente = new Dto<List<Utente>>();
		try {
			UtenteCrud crud = new UtenteCrud();
			SqlSession session = factory.openSession();
			UtenteMapper mapper = session.getMapper(UtenteMapper.class);
			List<Utente> listaUtente = crud.findAllUtente(mapper);
			if (null != listaUtente && !listaUtente.isEmpty()) {
				dtoListaUtente.setData(listaUtente);
				return dtoListaUtente;
			} else {
				System.out.println("lista vuota o null");
				dtoListaUtente.setErrore("Non sono presenti conti");
				return dtoListaUtente;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findAllUtente ----SQL Exception UTENTE CONTROLLER------");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllUtente ----ControllerUtente------");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}finally {
			factory.closeSession();
		}

	}

	public Dto<Utente> findUtenteByEmailPassword(Utente utente) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<Utente> dtoUtente = new Dto<Utente>();
		try {
			if (null != utente.getEmail() && null != utente.getPassword()) {
				UtenteCrud crud = new UtenteCrud();
				SqlSession session = factory.openSession();
				UtenteMapper mapper = session.getMapper(UtenteMapper.class);
				Utente utenteDaTrovare = crud.findUtenteByEmailPassword(utente, mapper);
				if (null != utenteDaTrovare) {
					Utente utenteDTO = new Convertitore().convertUtenteToDTO(utenteDaTrovare);
					dtoUtente.setData(utenteDTO);
					return dtoUtente;
				} else {
					System.out.println("Errore findUtenteByEmailPassword --ControllerUtente-- UTENTE NON TROVATO");
					throw new Exception("Credenziali di accesso errate");
				}
			} else {
				System.out.println("Errore findUtenteByEmailPassword --ControllerUtente-- email o password non inserita");
				throw new Exception("Errore inserisci email e password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findUtenteByEmailPassword ---ControllerUtente---SQL EXCEPTION--");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findUtenteByEmailPassword  --ControllerUtente--");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

	@Override
	public Dto<Utente> insertUtente(Utente utente) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<Utente> dtoUtente = new Dto<Utente>();
		try {
			if (new Validatore().controlloUtente(utente)) {
				SqlSession session = factory.openSession();
				UtenteMapper mapper = session.getMapper(UtenteMapper.class);
				UtenteCrud crud = new UtenteCrud();
				Utente utenteInserito = crud.insertUtente(utente, mapper);
				Utente utenteDTO = new Convertitore().convertUtenteToDTO(utenteInserito);
				dtoUtente.setData(utenteDTO);
				factory.commitSession();
				return dtoUtente;
			} else {
				System.out.println("Errore insertUtente --ControllerUtente-- ERRORE VALIDAZIONE UTENTE");
				throw new Exception("Registrazione non effettuata, compila correttamente tutti i campi");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore insertUtente --ControllerUtente---SQL EXCEPTION---");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore insertUtente ---ControllerUtente---");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

}
