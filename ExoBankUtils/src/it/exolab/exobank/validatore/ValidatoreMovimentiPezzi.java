package it.exolab.exobank.validatore;


import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;

public class ValidatoreMovimentiPezzi {

	public boolean validaMovimentoPedone(ParametriValidatoreDto parametri) throws Exception {
		if (parametri.getxPartenza() != parametri.getxDestinazione() || parametri.getyPartenza() != parametri.getyDestinazione()) {
			if (parametri.getColore().equals(Colore.BIANCO)) {
				return validaMovimentoPedoneBianco(parametri);
			} else if (parametri.getColore().equals(Colore.NERO)) {
				return validaMovimentoPedoneNero(parametri);
			}
		}

		throw new Exception("Movimento non valido per il pedone.");
	}


	private boolean validaMovimentoPedoneBianco(ParametriValidatoreDto parametri) throws Exception {
		if (parametri.getxDestinazione() == parametri.getxPartenza() + 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] == null) {
				return true;
			} else if (Math.abs(parametri.getyDestinazione() - parametri.getyPartenza()) == 1 && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] != null) {
				return true;
			}
		} else if (parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getxPartenza() == 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] == null
					&& parametri.getGriglia()[parametri.getxPartenza() + 1][parametri.getyPartenza()] == null) {
				return true;
			}
		}
		throw new Exception("Pedone bianco mossa non consentita.");
	}

	private boolean validaMovimentoPedoneNero(ParametriValidatoreDto parametri) throws Exception {
		if (parametri.getxDestinazione() == parametri.getxPartenza() - 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] == null) {
				return true;
			} else if (Math.abs(parametri.getyDestinazione() - parametri.getyPartenza()) == 1 && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] != null) {
				return true;
			}
		} else if (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getxPartenza() == 6) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] == null
					&& parametri.getGriglia()[parametri.getxPartenza() - 1][parametri.getyPartenza()] == null) {
				return true;
			}
		}
		throw new Exception("Pedone nero mossa non consentita.");
	}

	public boolean validaMovimentoTorre(ParametriValidatoreDto parametri) throws Exception {
		if (parametri.getxPartenza() != parametri.getxDestinazione() && parametri.getyPartenza() != parametri.getyDestinazione()) {
			throw new Exception("La torre può muoversi solo in orizzontale o verticale.");
		}

		if (parametri.getxPartenza().equals(parametri.getxDestinazione())) {
			return validaMovimentoTorreVerticale(parametri);
		} else if (parametri.getyPartenza().equals(parametri.getyDestinazione())) {
			return validaMovimentoTorreOrizzontale(parametri);
		}

		throw new Exception("Movimento non valido per la torre.");
	}

	private boolean validaMovimentoTorreVerticale(ParametriValidatoreDto parametri) throws Exception {
		int minY = Math.min(parametri.getyPartenza(), parametri.getyDestinazione());
		int maxY = Math.max(parametri.getyPartenza(), parametri.getyDestinazione());
		for (int index = minY + 1; index < maxY; index++) {
			if (parametri.getGriglia()[parametri.getxPartenza()][index] != null) {
				throw new Exception("C'è un pezzo prima della destinazione.");
			}
		}
		return true;
	}

	private boolean validaMovimentoTorreOrizzontale(ParametriValidatoreDto parametri) throws Exception {
		int minX = Math.min(parametri.getxPartenza(), parametri.getxDestinazione());
		int maxX = Math.max(parametri.getxPartenza(), parametri.getxDestinazione());
		for (int index = minX + 1; index < maxX; index++) {
			if (parametri.getGriglia()[index][parametri.getyPartenza()] != null) {
				throw new Exception("C'è un pezzo prima della destinazione.");
			}
		}
		return true;
	}

	public boolean validaMovimentoAlfiere(ParametriValidatoreDto parametri) throws Exception {
		if (Math.abs(parametri.getxDestinazione() - parametri.getxPartenza()) != Math.abs(parametri.getyDestinazione() - parametri.getyPartenza())) {
			throw new Exception("L'alfiere deve muoversi lungo diagonali.");
		}

		int deltaX = (parametri.getxDestinazione() - parametri.getxPartenza()) > 0 ? 1 : -1;
		int deltaY = (parametri.getyDestinazione() - parametri.getyPartenza()) > 0 ? 1 : -1;

		for (int index = parametri.getxPartenza() + deltaX, j_index = parametri.getyPartenza() + deltaY; index != parametri.getxDestinazione() && j_index != parametri.getyDestinazione(); index += deltaX, j_index += deltaY) {
			if (parametri.getGriglia()[index][j_index] != null) {
				throw new Exception("C'è un pezzo prima della destinazione.");
			}
		}

		return true;
	}

	public boolean validaMovimentoRegina(ParametriValidatoreDto parametri) throws Exception {
		int deltaX = Math.abs(parametri.getxDestinazione() - parametri.getxPartenza());
		int deltaY = Math.abs(parametri.getyDestinazione() - parametri.getyPartenza());

		if ((deltaX == 0 && deltaY != 0) || (deltaX != 0 && deltaY == 0)) {
			try {
				return validaMovimentoTorre(parametri);
			} catch (Exception e) {
				throw new Exception("Movimento non valido per la regina");
			}
		} else if (deltaX == deltaY) {
			try {
				return validaMovimentoAlfiere(parametri);
			} catch (Exception e) {
				throw new Exception("Movimento non valido per la regina");
			}
		}

		return false;
	}

	public boolean validaMovimentoCavallo(ParametriValidatoreDto parametri) {
		boolean isValid = false;
		if ((parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getyDestinazione() == parametri.getyPartenza() + 1)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getyDestinazione() == parametri.getyPartenza() - 1)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getyDestinazione() == parametri.getyPartenza() + 1)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getyDestinazione() == parametri.getyPartenza() - 1)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() + 1 && parametri.getyDestinazione() == parametri.getyPartenza() - 2)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() + 1 && parametri.getyDestinazione() == parametri.getyPartenza() + 2)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() - 1 && parametri.getyDestinazione() == parametri.getyPartenza() - 2)
				|| (parametri.getxDestinazione() == parametri.getxPartenza() - 1 && parametri.getyDestinazione() == parametri.getyPartenza() + 2)) {
			isValid = true;
		}
		return isValid;
	}

	public boolean validaMovimentoRe(ParametriValidatoreDto parametri) throws Exception {
		boolean isValid = false;

		int deltaX = Math.abs(parametri.getxDestinazione() - parametri.getxPartenza());
		int deltaY = Math.abs(parametri.getyDestinazione() - parametri.getyPartenza());

		if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
			isValid = true;
		}
		if (isValid) {
			if (parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] == null || !parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()].getColore().equals(parametri.getGriglia()[parametri.getxPartenza()][parametri.getyPartenza()].getColore())) {
				return true;
			}
		}

		throw new Exception("Movimento non valido per il re.");
	}

	
}