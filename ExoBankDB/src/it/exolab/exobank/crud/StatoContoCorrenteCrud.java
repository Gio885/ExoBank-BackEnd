package it.exolab.exobank.crud;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.mapper.StatoContoCorrenteMapper;
import it.exolab.exobank.models.StatoContoCorrente;

public class StatoContoCorrenteCrud {

	public List<StatoContoCorrente> listaStatiConto(StatoContoCorrenteMapper mapper) throws Exception {
		try {
			List<StatoContoCorrente> listaStatiConto = mapper.listaStatiConto();
			return listaStatiConto;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore listaStatiConto ---CRUD---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

}
