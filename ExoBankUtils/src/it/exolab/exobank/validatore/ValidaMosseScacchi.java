package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.costanti.Costanti;

public class ValidaMosseScacchi {

	public boolean mossaConsentitaPerPezzo(ParametriValidatoreDto parametri) throws Exception {
		if (isDestinazioneFuoriScacchiera(parametri.getxDestinazione(), parametri.getyDestinazione(), parametri.getGriglia())) {
			throw new Exception("La destinazione è fuori dalla scacchiera.");
		}

		if (isDestinazioneStessaPosizione(parametri.getxPartenza(), parametri.getxPartenza(), parametri.getxDestinazione(), parametri.getyDestinazione())) {
			throw new Exception("La destinazione è la stessa posizione di partenza.");
		}

		if (isDestinazioneStessoColore(parametri.getxDestinazione(), parametri.getyDestinazione(), parametri.getGriglia(), parametri.getPezzo())) {
			throw new Exception("Il pezzo di destinazione è dello stesso colore del pezzo in movimento.");
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

	public boolean puoiRimuovereScacco(Pezzo re, Pezzo[][] griglia, Scacchiera scacchiera) throws Exception {
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();

		return validaScacco.puoiRimuovereScaccoMossaRe(re, re.getPosizioneX(), re.getPosizioneY(), griglia) ||
				validaScacco.puoiRimuovereScaccoCatturaMinaccia(re, re.getPosizioneX(), re.getPosizioneY(), griglia) ||
				validaScacco.puoiRimuovereScaccoBloccaMinaccia(re, re.getPosizioneX(), re.getPosizioneY(), griglia);
	}



	private boolean isDestinazioneFuoriScacchiera(int xDestinazione, int yDestinazione, Pezzo[][] scacchiera) {
		return xDestinazione < 0 || xDestinazione >= scacchiera.length || yDestinazione < 0 || yDestinazione >= scacchiera[0].length;
	}

	private boolean isDestinazioneStessaPosizione(int xPartenza, int yPartenza, int xDestinazione, int yDestinazione) {
		return xPartenza == xDestinazione && yPartenza == yDestinazione;
	}

	private boolean isDestinazioneStessoColore(int xDestinazione, int yDestinazione, Pezzo[][] scacchiera, Pezzo pezzo) {
		Pezzo pezzoDestinazione = scacchiera[xDestinazione][yDestinazione];
		return pezzoDestinazione != null && pezzoDestinazione.getColore().equals(pezzo.getColore());
	}

}
