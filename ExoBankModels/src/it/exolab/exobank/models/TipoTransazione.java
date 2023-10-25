package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.List;

public class TipoTransazione implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipoTransazione;
	private List<Transazione> listaTransazione;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipoTransazione() {
		return tipoTransazione;
	}
	public void setTipoTransazione(String tipoTransazione) {
		this.tipoTransazione = tipoTransazione;
	}
	public List<Transazione> getListaTransazione() {
		return listaTransazione;
	}
	public void setListaTransazione(List<Transazione> listaTransazione) {
		this.listaTransazione = listaTransazione;
	}

	
	
}
