package it.exolab.exobank.controller;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.crud.StatoTransazioneCrud;
import it.exolab.exobank.ejbinterface.StatoTransazioneControllerInterface;
import it.exolab.exobank.mapper.StatoTransazioneMapper;
import it.exolab.exobank.models.StatoTransazione;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class StatoTransazioneController
 */
@Stateless(name = "StatoTransazioneControllerInterface")
@LocalBean
public class StatoTransazioneController implements StatoTransazioneControllerInterface {

	@Override
	public List<StatoTransazione> findStatiTransazione() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		try {
			StatoTransazioneCrud crud = new StatoTransazioneCrud();
			SqlSession session = factory.openSession();
			StatoTransazioneMapper mapper = session.getMapper(StatoTransazioneMapper.class);
			List<StatoTransazione> listaStatiTransazioni = crud.findStatiTransazione(mapper);
			return listaStatiTransazioni;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findStatiTransazione --- SQL EXCEPTION---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findStatiTransazione --- Controller---");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}
}
