package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.List;

public class StatoTransazione implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String statoTransazione;
	private List<Transazione> listaTransazione;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatoTransazione() {
		return statoTransazione;
	}
	public void setStatoTransazione(String statoTransazione) {
		this.statoTransazione = statoTransazione;
	}
	public List<Transazione> getListaTransazione() {
		return listaTransazione;
	}
	public void setListaTransazione(List<Transazione> listaTransazione) {
		this.listaTransazione = listaTransazione;
	}
	
	
	
	
}
