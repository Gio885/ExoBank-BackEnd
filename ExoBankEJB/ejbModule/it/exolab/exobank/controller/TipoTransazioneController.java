package it.exolab.exobank.controller;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.crud.TipoTransazioneCrud;
import it.exolab.exobank.ejbinterface.TipoTransazioneControllerInterface;
import it.exolab.exobank.mapper.TipoTransazioneMapper;
import it.exolab.exobank.models.TipoTransazione;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class TipoTransazioneController
 */
@Stateless(name = "TipoTransazioneControllerInterface")
@LocalBean
public class TipoTransazioneController implements TipoTransazioneControllerInterface {

	/**
	 * Default constructor.
	 */
	public TipoTransazioneController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TipoTransazione> findAllStatiTransazione() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		try {
			TipoTransazioneCrud crud = new TipoTransazioneCrud();
			SqlSession session = factory.openSession();
			TipoTransazioneMapper mapper = session.getMapper(TipoTransazioneMapper.class);
			List<TipoTransazione> listaTipoTransazione = crud.findAllTipoTransazione(mapper);
			return listaTipoTransazione;
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
