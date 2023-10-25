package it.exolab.exobank.controller;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.crud.StatoContoCorrenteCrud;
import it.exolab.exobank.ejbinterface.StatoContoCorrenteControllerInterface;
import it.exolab.exobank.mapper.StatoContoCorrenteMapper;
import it.exolab.exobank.models.StatoContoCorrente;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class StatoContoCorrenteController
 */
@Stateless(name = "StatoContoCorrenteControllerInterface")

@LocalBean
public class StatoContoCorrenteController implements StatoContoCorrenteControllerInterface {

	/**
	 * Default constructor.
	 */
	public StatoContoCorrenteController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<StatoContoCorrente> findAllStatiConto() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		try {
			StatoContoCorrenteCrud crud = new StatoContoCorrenteCrud();
			SqlSession session = factory.openSession();
			StatoContoCorrenteMapper mapper = session.getMapper(StatoContoCorrenteMapper.class);
			List<StatoContoCorrente> listaStatiConto = crud.listaStatiConto(mapper);
			return listaStatiConto;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findAllStatiConto --- SQL EXCEPTION---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllStatiConto --- Controller---");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}
}
