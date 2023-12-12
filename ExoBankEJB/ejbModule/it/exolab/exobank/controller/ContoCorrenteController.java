package it.exolab.exobank.controller;

import it.exolab.exobank.convertitore.Convertitore;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.costanti.CostantiEmail;
import it.exolab.exobank.crud.ContoCorrenteCrud;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.ContoCorrenteControllerInterface;
import it.exolab.exobank.mapper.ContoCorrenteMapper;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.StatoContoCorrente;
import it.exolab.exobank.models.Utente;
import it.exolab.exobank.sendemail.SendEmail;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class ContoCorrenteController
 */
@Stateless(name = "ContoCorrenteControllerInterface")
@LocalBean
public class ContoCorrenteController implements ContoCorrenteControllerInterface {

	@Override
	public Dto<ContoCorrente> updateConto(ContoCorrente conto) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<ContoCorrente> dtoConto = new Dto<ContoCorrente>();
		try {
			SqlSession session = factory.openSession();
			ContoCorrenteCrud crud = new ContoCorrenteCrud();
			ContoCorrenteMapper mapper = session.getMapper(ContoCorrenteMapper.class);
			crud.updateConto(conto, mapper);
			ContoCorrente contoAggiornato = crud.findContoByIdUtente(conto.getUtente().getId(), mapper);
			if (null != contoAggiornato) {
				dtoConto.setData(contoAggiornato);
				factory.commitSession();
				return dtoConto;
			} else {
				System.out.println("Errore 0 righe aggiornata conto updateConto ---ContoCorrenteController----");
				throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findContoByIdUtente --SQL EXCEPTION-- ");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findContoByIdUtente --Controller-- ");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}

	}

	@Override
	public Dto<ContoCorrente> findContoByIdUtente(Utente utente) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<ContoCorrente> dtoConto = new Dto<ContoCorrente>();
		try {
			if (null != utente) {
				ContoCorrenteCrud crud = new ContoCorrenteCrud();
				SqlSession session = factory.openSession();
				ContoCorrenteMapper mapper = session.getMapper(ContoCorrenteMapper.class);
				ContoCorrente contoUtente = crud.findContoByIdUtente(utente.getId(), mapper);
				if (null != contoUtente) {
					ContoCorrente contoUtenteDTO = new Convertitore().convertContoToDTO(contoUtente);
					dtoConto.setData(contoUtenteDTO);
					return dtoConto;
				} else {
					dtoConto.setErrore("Non hai un conto corrente");
					return dtoConto;
				}
			} else {
				System.out.println("ERRORE ---ID UTENTE NULL, findContoByIdUtente ---ControllerContoCorrente--");
				throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findContoByIdUtente --ControllerContoCorrente---SQL EXCEPTION-- ");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findContoByIdUtente --ControllerContoCorrente-- ");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

	@Override
	public Dto<ContoCorrente> insertContoCorrente(Utente utente) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<ContoCorrente> dtoConto = new Dto<ContoCorrente>();
		try {
			SqlSession session = factory.openSession();
			ContoCorrenteMapper mapper = session.getMapper(ContoCorrenteMapper.class);
			ContoCorrenteCrud crud = new ContoCorrenteCrud();
			ContoCorrente contoDaInserire = contoBuild(utente);
			ContoCorrente contoInserito = crud.insertContoCorrente(contoDaInserire, mapper);
			ContoCorrente contoCorrenteDto = new Convertitore().convertContoToDTO(contoInserito);
			dtoConto.setData(contoCorrenteDto);
			factory.commitSession();
			return dtoConto;
		} catch (SQLException e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findContoByIdUtente ----ContoCorrenteController----SQL EXCEPTION-- ");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findContoByIdUtente --ContoCorrenteController-- ");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

	@Override
	public Dto<List<ContoCorrente>> listaConti() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<List<ContoCorrente>> dtoListaContiCorrenti = new Dto<List<ContoCorrente>>();
		try {
			ContoCorrenteCrud crud = new ContoCorrenteCrud();
			SqlSession session = factory.openSession();
			ContoCorrenteMapper mapper = session.getMapper(ContoCorrenteMapper.class);
			List<ContoCorrente> listaConti = crud.listaConti(mapper);
			if (null != listaConti && !listaConti.isEmpty()) {
				List<ContoCorrente> listaContiDTO = new Convertitore().convertListaContoToDTO(listaConti);
				dtoListaContiCorrenti.setData(listaContiDTO);
				return dtoListaContiCorrenti;
			} else {
				System.out.println("lista vuota o null");
				dtoListaContiCorrenti.setErrore("Non sono presenti conti");
				return dtoListaContiCorrenti;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findContoByIdUtente ---ControllerConto---SQL EXCEPTION-- ");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findContoByIdUtente --ControllerConto-- ");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

	private static ContoCorrente contoBuild(Utente utente) {
		ContoCorrente contoBuild = new ContoCorrente();
		String numConto = UUID.randomUUID().toString().replaceAll("[^a-f0-9]", "").toUpperCase();
		String numeroConto = numConto.substring(0, 27);
		contoBuild.setUtente(utente);
		StatoContoCorrente statoSospeso = new StatoContoCorrente();
		statoSospeso.setId(Costanti.STATO_SOSPESO);
		contoBuild.setStato(statoSospeso);
		contoBuild.setNumeroConto(numeroConto);
		contoBuild.setSaldo(Costanti.SALDO_CONTO_INIZIALE);
		return contoBuild;
	}

}
