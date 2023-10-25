package it.exolab.exobank.models;

import java.io.Serializable;
import java.util.Date;


public class Transazione implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date dataTransazione;
	private Double importo;
	private String contoBeneficiarioEsterno;
	private TipoTransazione tipo;
	private StatoTransazione statoTransazione;
	private ContoCorrente contoBeneficiario;
	private ContoCorrente conto;


	public String getContoBeneficiarioEsterno() {
		return contoBeneficiarioEsterno;
	}
	public void setContoBeneficiarioEsterno(String contoBeneficiarioEsterno) {
		this.contoBeneficiarioEsterno = contoBeneficiarioEsterno;
	}
	public ContoCorrente getContoBeneficiario() {
		return contoBeneficiario;
	}
	public void setContoBeneficiario(ContoCorrente contoBeneficiario) {
		this.contoBeneficiario = contoBeneficiario;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataTransazione() {
		return dataTransazione;
	}
	public void setDataTransazione(Date dataTransazione) {
		this.dataTransazione = dataTransazione;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public TipoTransazione getTipo() {
		return tipo;
	}
	public void setTipo(TipoTransazione tipo) {
		this.tipo = tipo;
	}
	public StatoTransazione getStatoTransazione() {
		return statoTransazione;
	}
	public void setStatoTransazione(StatoTransazione statoTransazione) {
		this.statoTransazione = statoTransazione;
	}
	public ContoCorrente getConto() {
		return conto;
	}
	public void setConto(ContoCorrente conto) {
		this.conto = conto;
	}
	
	
	
}
