package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.costanti.Costanti;

public class ValidaMosseScacchi {

	public boolean mossaConsentitaPerPezzo(Pezzo pezzo, Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] scacchiera) throws Exception {
		if (isDestinazioneFuoriScacchiera(xDestinazione, yDestinazione, scacchiera)) {
			throw new Exception("La destinazione è fuori dalla scacchiera.");
		}

		if (isDestinazioneStessaPosizione(xPartenza, yPartenza, xDestinazione, yDestinazione)) {
			throw new Exception("La destinazione è la stessa posizione di partenza.");
		}

		if (isDestinazioneStessoColore(xDestinazione, yDestinazione, scacchiera, pezzo)) {
			throw new Exception("Il pezzo di destinazione è dello stesso colore del pezzo in movimento.");
		}

		ValidatoreMovimentiPezzi validatoreMovimento = new ValidatoreMovimentiPezzi();

		if (pezzo.getTipo().equals(Costanti.TORRE)) {
			return validatoreMovimento.validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
		} else if (pezzo.getTipo().equals(Costanti.ALFIERE)) {
			return validatoreMovimento.validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
		} else if (pezzo.getTipo().equals(Costanti.REGINA)) {
			return validatoreMovimento.validaMovimentoRegina(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
		} else if (pezzo.getTipo().equals(Costanti.PEDONE)) {
			return validatoreMovimento.validaMovimentoPedone(xPartenza, yPartenza, xDestinazione, yDestinazione, pezzo.getColore(), scacchiera);
		} else if (pezzo.getTipo().equals(Costanti.CAVALLO)) {
			return validatoreMovimento.validaMovimentoCavallo(xPartenza, yPartenza, xDestinazione, yDestinazione);
		} else if (pezzo.getTipo().equals(Costanti.RE)) {
			return validatoreMovimento.validaMovimentoRe(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
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
