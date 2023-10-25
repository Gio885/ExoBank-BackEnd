package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.List;

public class ContoCorrente implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String numeroConto;
	private Double saldo;
	private StatoContoCorrente stato;
	private Utente utente;
	private List<Transazione> listaTransazioni;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroConto() {
		return numeroConto;
	}
	public void setNumeroConto(String numeroConto) {
		this.numeroConto = numeroConto;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public StatoContoCorrente getStato() {
		return stato;
	}
	public void setStato(StatoContoCorrente stato) {
		this.stato = stato;
	}
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public List<Transazione> getListaTransazioni() {
		return listaTransazioni;
	}
	public void setListaTransazioni(List<Transazione> listaTransazioni) {
		this.listaTransazioni = listaTransazioni;
	}
	
	
	
	
}
