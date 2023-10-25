package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.List;

public class StatoContoCorrente implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String statoContoCorrente;
	private List<ContoCorrente> listaContoCorrente;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatoContoCorrente() {
		return statoContoCorrente;
	}
	public void setStatoContoCorrente(String statoContoCorrente) {
		this.statoContoCorrente = statoContoCorrente;
	}
	public List<ContoCorrente> getListaContoCorrente() {
		return listaContoCorrente;
	}
	public void setListaContoCorrente(List<ContoCorrente> listaContoCorrente) {
		this.listaContoCorrente = listaContoCorrente;
	}
	

	
	
	
}
