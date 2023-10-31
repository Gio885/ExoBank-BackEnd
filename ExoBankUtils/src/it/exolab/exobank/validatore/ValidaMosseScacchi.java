package it.exolab.exobank.validatore;


import it.exolab.exobank.chess.model.Alfiere;
import it.exolab.exobank.chess.model.Cavallo;
import it.exolab.exobank.chess.model.Pedone;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Re;
import it.exolab.exobank.chess.model.Regina;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Torre;
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

        // Aggiungi la logica specifica per ciascun tipo di pezzo qui.
        if (pezzo instanceof Torre) {
            return validatoreMovimento.validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        } else if (pezzo instanceof Alfiere) {
            return validatoreMovimento.validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        } else if (pezzo instanceof Regina) {
            return validatoreMovimento.validaMovimentoRegina(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        } else if (pezzo instanceof Pedone) {
            return validatoreMovimento.validaMovimentoPedone(xPartenza, yPartenza, xDestinazione, yDestinazione, pezzo.getColore(), scacchiera);
        } else if (pezzo instanceof Cavallo) {
            return validatoreMovimento.validaMovimentoCavallo(xPartenza, yPartenza, xDestinazione, yDestinazione);
        } else if (pezzo instanceof Re) {
            return validatoreMovimento.validaMovimentoRe(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        }

        return false; // Restituisci true se la mossa è consentita, altrimenti false.
    }
	
	public boolean puoiRimuovereScacco(Integer xRe, Integer yRe, Pezzo[][] griglia, Scacchiera scacchiera) throws Exception {
	    Pezzo re = griglia[xRe][yRe];
	    ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
	    if (validaScacco.isScaccoMatto(re, griglia, re.getColore())) {
	        throw new RuntimeException("SCACCO MATTO");
	    }

	    return validaScacco.puoiRimuovereScaccoMossaRe(re, xRe, yRe, griglia) ||
	    		validaScacco.puoiRimuovereScaccoCatturaMinaccia(re, xRe, yRe, griglia) ||
	    		validaScacco.puoiRimuovereScaccoBloccaMinaccia(re, xRe, yRe, griglia);
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
