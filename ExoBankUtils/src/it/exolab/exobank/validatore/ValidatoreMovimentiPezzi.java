package it.exolab.exobank.validatore;


import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.scacchiera.ex.MossaNonConsentita;

public class ValidatoreMovimentiPezzi {

	public boolean validaMovimentoPedone(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (parametri.getxPartenza() != parametri.getxDestinazione() || parametri.getyPartenza() != parametri.getyDestinazione()) {
			if (parametri.getColore().equals(Colore.BIANCO)) {
				return validaMovimentoPedoneBianco(parametri);
			} else if (parametri.getColore().equals(Colore.NERO)) {
				return validaMovimentoPedoneNero(parametri);
			}
		}

		throw new MossaNonConsentita("Movimento non valido per il pedone.");
	}


	private boolean validaMovimentoPedoneBianco(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (parametri.getxDestinazione() == parametri.getxPartenza() + 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && null == parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				return true;
			} else if (Math.abs(parametri.getyDestinazione() - parametri.getyPartenza()) == 1 && null != parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				return true;
			}
		} else if (parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getxPartenza() == 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && null == parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] 
					&& null == parametri.getGriglia()[parametri.getxPartenza() + 1][parametri.getyPartenza()]) {
				return true;
			}
		}
		throw new MossaNonConsentita("Pedone bianco mossa non consentita.");
	}

	private boolean validaMovimentoPedoneNero(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (parametri.getxDestinazione() == parametri.getxPartenza() - 1) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && null == parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				return true;
			} else if (Math.abs(parametri.getyDestinazione() - parametri.getyPartenza()) == 1 && null != parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				return true;
			}
		} else if (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getxPartenza() == 6) {
			if (parametri.getyDestinazione() == parametri.getyPartenza() && null == parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] 
					&& null == parametri.getGriglia()[parametri.getxPartenza() - 1][parametri.getyPartenza()]) {
				return true;
			}
		}
		throw new MossaNonConsentita("Pedone nero mossa non consentita.");
	}

	public boolean validaMovimentoTorre(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (parametri.getxPartenza() != parametri.getxDestinazione() && parametri.getyPartenza() != parametri.getyDestinazione()) {
			throw new MossaNonConsentita("La torre può muoversi solo in orizzontale o verticale.");
		}

		if (parametri.getxPartenza().equals(parametri.getxDestinazione())) {
			return validaMovimentoTorreVerticale(parametri);
		} else if (parametri.getyPartenza().equals(parametri.getyDestinazione())) {
			return validaMovimentoTorreOrizzontale(parametri);
		}

		throw new MossaNonConsentita("Movimento non valido per la torre.");
	}

	private boolean validaMovimentoTorreVerticale(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		int minY = Math.min(parametri.getyPartenza(), parametri.getyDestinazione());
		int maxY = Math.max(parametri.getyPartenza(), parametri.getyDestinazione());
		for (int index = minY + 1; index < maxY; index++) {
			if (null != parametri.getGriglia()[parametri.getxPartenza()][index]) {
				throw new MossaNonConsentita("C'è un pezzo prima della destinazione.");
			}
		}
		return true;
	}

	private boolean validaMovimentoTorreOrizzontale(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		int minX = Math.min(parametri.getxPartenza(), parametri.getxDestinazione());
		int maxX = Math.max(parametri.getxPartenza(), parametri.getxDestinazione());
		for (int index = minX + 1; index < maxX; index++) {
			if (null != parametri.getGriglia()[index][parametri.getyPartenza()]) {
				throw new MossaNonConsentita("C'è un pezzo prima della destinazione.");
			}
		}
		return true;
	}

	public boolean validaMovimentoAlfiere(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (Math.abs(parametri.getxDestinazione() - parametri.getxPartenza()) != Math.abs(parametri.getyDestinazione() - parametri.getyPartenza())) {
			throw new MossaNonConsentita("L'alfiere deve muoversi lungo diagonali.");
		}

		int deltaX = (parametri.getxDestinazione() - parametri.getxPartenza()) > 0 ? 1 : -1;
		int deltaY = (parametri.getyDestinazione() - parametri.getyPartenza()) > 0 ? 1 : -1;

		for (int index = parametri.getxPartenza() + deltaX, j_index = parametri.getyPartenza() + deltaY; index != parametri.getxDestinazione() && j_index != parametri.getyDestinazione(); index += deltaX, j_index += deltaY) {
			if (null != parametri.getGriglia()[index][j_index]) {
				throw new MossaNonConsentita("C'è un pezzo prima della destinazione.");
			}
		}

		return true;
	}

	public boolean validaMovimentoRegina(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		int deltaX = Math.abs(parametri.getxDestinazione() - parametri.getxPartenza());
		int deltaY = Math.abs(parametri.getyDestinazione() - parametri.getyPartenza());

		if ((deltaX == 0 && deltaY != 0) || (deltaX != 0 && deltaY == 0)) {
			try {
				return validaMovimentoTorre(parametri);
			} catch (Exception e) {
				throw new MossaNonConsentita("Movimento non valido per la regina.");
			}
		} else if (deltaX == deltaY) {
			try {
				return validaMovimentoAlfiere(parametri);
			} catch (Exception e) {
				throw new MossaNonConsentita("Movimento non valido per la regina.");
			}
		}

		return false;
	}

	public boolean validaMovimentoCavallo(ParametriValidatoreDto parametri) throws MossaNonConsentita{;
	if ((parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getyDestinazione() == parametri.getyPartenza() + 1)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() + 2 && parametri.getyDestinazione() == parametri.getyPartenza() - 1)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getyDestinazione() == parametri.getyPartenza() + 1)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() - 2 && parametri.getyDestinazione() == parametri.getyPartenza() - 1)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() + 1 && parametri.getyDestinazione() == parametri.getyPartenza() - 2)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() + 1 && parametri.getyDestinazione() == parametri.getyPartenza() + 2)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() - 1 && parametri.getyDestinazione() == parametri.getyPartenza() - 2)
			|| (parametri.getxDestinazione() == parametri.getxPartenza() - 1 && parametri.getyDestinazione() == parametri.getyPartenza() + 2)) {
		return true;
	}
	throw new MossaNonConsentita("Movimento non valido per il cavallo.");
	}

	public boolean validaMovimentoRe(ParametriValidatoreDto parametri) throws Exception {
		boolean isValid = false;
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		int spostamentoRe = parametri.getxDestinazione() - parametri.getxPartenza();
		int deltaX = Math.abs(parametri.getxDestinazione() - parametri.getxPartenza());
		int deltaY = Math.abs(parametri.getyDestinazione() - parametri.getyPartenza());

		if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
			isValid = true;
		}
		
		if(!parametri.getPezzo().isSpostato() && torriPerArroccoSpostate(parametri, spostamentoRe) && deltaX == 2 && deltaY == 0 && !validaScacco.isScacco(parametri.getPezzo(), parametri.getGriglia())) {
			isValid = true;
		}

		if (isValid) {
			if (null == parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] || !parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()].getColore().equals(parametri.getGriglia()[parametri.getxPartenza()][parametri.getyPartenza()].getColore())) {
				return true;
			}
		}

			throw new MossaNonConsentita("Movimento non valido per il re.");
	}



	private boolean torriPerArroccoSpostate (ParametriValidatoreDto parametri, int spostamentoRe) {

		if(parametri.getColore().equals(parametri.getGriglia()[0][0].getColore()) && spostamentoRe < 0 && !parametri.getGriglia()[0][0].isSpostato() && null == parametri.getGriglia()[0][1] && null == parametri.getGriglia()[0][2]) {
			return true;
		}

		if(parametri.getColore().equals(parametri.getGriglia()[0][7].getColore()) && spostamentoRe > 0 && !parametri.getGriglia()[0][7].isSpostato() && null == parametri.getGriglia()[0][4] && null == parametri.getGriglia()[0][5] && null == parametri.getGriglia()[0][6]) {
			return true;
		}

		if(parametri.getColore().equals(parametri.getGriglia()[7][0].getColore()) && spostamentoRe < 0 && !parametri.getGriglia()[7][0].isSpostato() && null == parametri.getGriglia()[7][1] && null == parametri.getGriglia()[7][2]) {
			return true;
		}

		if(parametri.getColore().equals(parametri.getGriglia()[7][7].getColore()) && spostamentoRe > 0 && !parametri.getGriglia()[7][7].isSpostato() && null == parametri.getGriglia()[7][4] && null == parametri.getGriglia()[7][5] && null == parametri.getGriglia()[7][6]) {
			return true;
		}
		
		return false;
	}
	
	

}