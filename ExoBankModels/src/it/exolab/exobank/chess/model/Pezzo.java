package it.exolab.exobank.chess.model;

import java.io.Serializable;
import java.util.Objects;

public class Pezzo implements Serializable {

	private Integer id;
	private String colore;
	private String tipo;
	private Integer posizioneX;
	private Integer posizioneY;
	private boolean esiste;
	
	
	
	public Pezzo(Integer id, String tipo, String colore, Integer posizioneX, Integer posizioneY, boolean esiste) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.colore = colore;
		this.posizioneX = posizioneX;
		this.posizioneY = posizioneY;
		this.esiste = esiste;
	}
	
	public Pezzo() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getColore() {
		return colore;
	}
	public void setColore(String colore) {
		this.colore = colore;
	}
	
	public Integer getPosizioneX() {
		return posizioneX;
	}
	public void setPosizioneX(Integer posizioneX) {
		this.posizioneX = posizioneX;
	}
	public Integer getPosizioneY() {
		return posizioneY;
	}
	public void setPosizioneY(Integer posizioneY) {
		this.posizioneY = posizioneY;
	}
	public boolean isEsiste() {
		return esiste;
	}
	public void setEsiste(boolean esiste) {
		this.esiste = esiste;
	}
	
	@Override
	public String toString() {
		return "Pezzo [id=" + id + ", colore=" + colore + ", tipo=" + tipo + ", posizioneX=" + posizioneX
				+ ", posizioneY=" + posizioneY + ", esiste=" + esiste + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(colore, esiste, id, posizioneX, posizioneY, tipo);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pezzo other = (Pezzo) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
