package it.exolab.exobank.chess.dto;

import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;

public class ParametriValidatoreDto {
	private Pezzo pezzo;
	private Integer xPartenza;
	private Integer yPartenza;
	private Integer xDestinazione;
	private Integer yDestinazione;
	private Colore colore;
	private Pezzo[][] griglia;

	public ParametriValidatoreDto() {
		// Costruttore vuoto
	}

	public ParametriValidatoreDto(Pezzo pezzo, Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Colore colore, Pezzo[][] griglia) {
		this.pezzo = pezzo;
		this.xPartenza = xPartenza;
		this.yPartenza = yPartenza;
		this.xDestinazione = xDestinazione;
		this.yDestinazione = yDestinazione;
		this.colore = colore;
		this.griglia = griglia;
	}
	
	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}

	public Integer getxPartenza() {
		return xPartenza;
	}

	public void setxPartenza(Integer xPartenza) {
		this.xPartenza = xPartenza;
	}

	public Integer getyPartenza() {
		return yPartenza;
	}

	public void setyPartenza(Integer yPartenza) {
		this.yPartenza = yPartenza;
	}

	public Integer getxDestinazione() {
		return xDestinazione;
	}

	public void setxDestinazione(Integer xDestinazione) {
		this.xDestinazione = xDestinazione;
	}

	public Integer getyDestinazione() {
		return yDestinazione;
	}

	public void setyDestinazione(Integer yDestinazione) {
		this.yDestinazione = yDestinazione;
	}

	public Colore getColore() {
		return colore;
	}

	public void setColore(Colore colore) {
		this.colore = colore;
	}

	public Pezzo[][] getGriglia() {
		return griglia;
	}


	public void setGriglia(Pezzo[][] griglia) {
		this.griglia = griglia;
	}
}
