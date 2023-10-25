package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.List;

public class Ruolo implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipoRuolo;
	private List<Utente> listaUtente;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipoRuolo() {
		return tipoRuolo;
	}
	public void setTipoRuolo(String tipoRuolo) {
		this.tipoRuolo = tipoRuolo;
	}
	public List<Utente> getListaUtente() {
		return listaUtente;
	}
	public void setListaUtente(List<Utente> listaUtente) {
		this.listaUtente = listaUtente;
	}

	
	
	
	
}
