package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.scacchiera.ex.MossaNonConsentita;

public class ValidaMosseScacchi {

	public boolean mossaConsentitaPerPezzo(ParametriValidatoreDto parametri) throws MossaNonConsentita {
		if (isDestinazioneFuoriScacchiera(parametri.getxDestinazione(), parametri.getyDestinazione(), parametri.getGriglia())) {
			throw new MossaNonConsentita("La destinazione è fuori dalla scacchiera.");
		}

		if (isDestinazioneStessaPosizione(parametri.getxPartenza(), parametri.getyPartenza(), parametri.getxDestinazione(), parametri.getyDestinazione())) {
			throw new MossaNonConsentita("La destinazione è la stessa posizione di partenza.");
		}

		if (isDestinazioneStessoColore(parametri.getxDestinazione(), parametri.getyDestinazione(), parametri.getGriglia(), parametri.getPezzo())) {
			throw new MossaNonConsentita("Il pezzo di destinazione è dello stesso colore del pezzo in movimento.");
		}

		ValidatoreMovimentiPezzi validatoreMovimento = new ValidatoreMovimentiPezzi();

		if (parametri.getPezzo().getTipo().equals(Tipo.TORRE)) {
			return validatoreMovimento.validaMovimentoTorre(parametri);
		} else if (parametri.getPezzo().getTipo().equals(Tipo.ALFIERE)) {
			return validatoreMovimento.validaMovimentoAlfiere(parametri);
		} else if (parametri.getPezzo().getTipo().equals(Tipo.REGINA)) {
			return validatoreMovimento.validaMovimentoRegina(parametri);
		} else if (parametri.getPezzo().getTipo().equals(Tipo.PEDONE)) {
			return validatoreMovimento.validaMovimentoPedone(parametri);
		} else if (parametri.getPezzo().getTipo().equals(Tipo.CAVALLO)) {
			return validatoreMovimento.validaMovimentoCavallo(parametri);
		} else if (parametri.getPezzo().getTipo().equals(Tipo.RE)) {
			return validatoreMovimento.validaMovimentoRe(parametri);
		}

		return false;
	}



	private boolean isDestinazioneFuoriScacchiera(int xDestinazione, int yDestinazione, Pezzo[][] scacchiera) {
		return xDestinazione < 0 || xDestinazione >= scacchiera.length || yDestinazione < 0 || yDestinazione >= scacchiera[0].length;
	}

	private boolean isDestinazioneStessaPosizione(int xPartenza, int yPartenza, int xDestinazione, int yDestinazione) {
		return xPartenza == xDestinazione && yPartenza == yDestinazione;
	}

	private boolean isDestinazioneStessoColore(int xDestinazione, int yDestinazione, Pezzo[][] scacchiera, Pezzo pezzo) {
		Pezzo pezzoDestinazione = scacchiera[xDestinazione][yDestinazione];
		return null != pezzoDestinazione && pezzoDestinazione.getColore().equals(pezzo.getColore());
	}

}
