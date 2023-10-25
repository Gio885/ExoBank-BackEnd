package it.exolab.exobank.chess.model;

import java.io.Serializable;
import java.util.Arrays;

public class Scacchiera implements Serializable {

	private Pezzo[][] griglia = new Pezzo[8][8];

	public Pezzo[][] getGriglia() {
		return griglia;
	}

	public void setScacchiera(Pezzo[][] griglia) {
		this.griglia = griglia;
	}

	@Override
	public String toString() {
		return "Scacchiera [scacchiera=" + Arrays.toString(griglia) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(griglia);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scacchiera other = (Scacchiera) obj;
		return Arrays.deepEquals(griglia, other.griglia);
	}
	
	
}
