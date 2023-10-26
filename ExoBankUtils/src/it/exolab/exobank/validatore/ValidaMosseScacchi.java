package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.model.Pedone;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.costanti.Costanti;

public class ValidaMosseScacchi {
	// DA FINIRE
//	public boolean validaMossa(Pezzo pezzo, Scacchiera scacchiera, Integer posX, Integer posY) {
//		
//		if (pezzo instanceof Pedone){
//			int posIniX = pezzo.getPosizioneX();
//			int posIniY = pezzo.getPosizioneY();
//			String colore = pezzo.getColore();
//			boolean isValid;
//			switch(colore) {
//				case Costanti.BIANCO:
//					if((posX == posIniX) && (posY == posIniX - 1)) {
//						isValid = true;
//					}else if(posIniX == 6 && ())
//			}
//		}
//	}
	
	public boolean validaPosizione(Pezzo pezzo, Scacchiera scacchiera, Integer posX, Integer posY) throws ArrayIndexOutOfBoundsException{
		
		boolean isValid = false;
		String colore = pezzo.getColore();
		if((posX >= 0 && posX <= 7) && (posY >= 0 && posY <= 7)) {  // inserire le costanti
			Pezzo[][] griglia = scacchiera.getGriglia();
			if((null == griglia[posX][posY]) || (!griglia[posX][posY].getColore().equals(colore))) {
				isValid = true;
			}
		}else {
			throw new ArrayIndexOutOfBoundsException("La posizione selezionata non esiste");
		}
		return isValid;
	}
	
	private boolean validaMovimentoPedone(Pezzo pedone, Scacchiera scacchiera, Integer posX, Integer posY) {
        Integer posIniX = pedone.getPosizioneX();
        Integer posIniY = pedone.getPosizioneY();
        String colore = pedone.getColore();
        boolean isValid = false;

        if (colore.equals(Costanti.BIANCO)) {
            // Pedone bianco può avanzare solo in direzione crescente delle righe
            if (posY == posIniY + 1) {
                // Il pedone può avanzare di una casella in avanti
                if (posX == posIniX) {
                    // La nuova posizione è direttamente in avanti
                    if (validaPosizione(pedone, scacchiera, posX, posY)) {
                        isValid = true;
                    }
                } else if ((posX == posIniX - 1 || posX == posIniX + 1) && validaPosizione(pedone, scacchiera, posX, posY)) {
                    // La nuova posizione è una delle caselle oblique e contiene un pezzo avversario
                    isValid = true;
                }
            } else if (posY == posIniY + 2 && posIniY == 1) {
                // Il pedone può avanzare di due case solo dalla posizione iniziale (riga 1)
                if (posX == posIniX && validaPosizione(pedone, scacchiera, posX, posY) &&
                    validaPosizione(pedone, scacchiera, posX, posY - 1)) {
                    isValid = true;
                }
            }
        } else if (colore.equals(Costanti.NERO)) {
            // Pedone nero può avanzare solo in direzione decrescente delle righe
            if (posY == posIniY - 1) {
                // Il pedone può avanzare di una casella in avanti
                if (posX == posIniX) {
                    // La nuova posizione è direttamente in avanti
                    if (validaPosizione(pedone, scacchiera, posX, posY)) {
                        isValid = true;
                    }
                } else if ((posX == posIniX - 1 || posX == posIniX + 1) && validaPosizione(pedone, scacchiera, posX, posY)) {
                    // La nuova posizione è una delle caselle oblique e contiene un pezzo avversario
                    isValid = true;
                }
            } else if (posY == posIniY - 2 && posIniY == 6) {
                // Il pedone può avanzare di due case solo dalla posizione iniziale (riga 6)
                if (posX == posIniX && validaPosizione(pedone, scacchiera, posX, posY) &&
                    validaPosizione(pedone, scacchiera, posX, posY + 1)) {
                    isValid = true;
                }
            }
        }

        return isValid;
    }
	
	private boolean validaMovimentoTorre(Pezzo torre, Scacchiera scacchiera, Integer posX, Integer posY) {
	    Integer posIniX = torre.getPosizioneX();
	    Integer posIniY = torre.getPosizioneY();
	    String colore = torre.getColore();

	    // Controllo che la torre si stia muovendo sia orizzontalmente sia verticalmente
	    if (posIniX != posX && posIniY != posY || posIniX == posX && posIniY == posY) {
	        return false;
	    }

	    // Controllo delle caselle orizzontali
	    if (posIniY == posY) {
	        int minX = Math.min(posIniX, posX);
	        int maxX = Math.max(posIniX, posX);
	        for (int i = minX + 1; i < maxX; i++) {
	            if (null != scacchiera.getGriglia()[posIniY][i] ) {
	                return false; // Una casella lungo il percorso è occupata, la mossa non è consentita
	            }
	        }
	    }

	    // Controllo delle caselle verticali
	    if (posIniX == posX) {
	        int minY = Math.min(posIniY, posY);
	        int maxY = Math.max(posIniY, posY);
	        for (int i = minY + 1; i < maxY; i++) {
	            if (null != scacchiera.getGriglia()[i][posIniX]  ) {
	                return false; // Una casella lungo il percorso è occupata, la mossa non è consentita
	            }
	        }
	    }

	    // Controllo se la torre sta catturando un pezzo avversario
	    if (null != scacchiera.getGriglia()[posX][posY]  && !scacchiera.getGriglia()[posX][posY].getColore().equals(colore)) {
	        return true; // La torre può catturare un pezzo avversario
	    }

	    return true; // La mossa è consentita
	}
	
	private boolean validaMovimentoAlfiere(Pezzo alfiere, Scacchiera scacchiera, Integer posX, Integer posY) {
	    Integer posIniX = alfiere.getPosizioneX();
	    Integer posIniY = alfiere.getPosizioneY();
	    String colore = alfiere.getColore();
	    
	    // Controlla se l'alfiere si sta muovendo diagonalmente
	    if (Math.abs(posY - posIniY) != Math.abs(posX - posIniX)) {
	        return false; // L'alfiere deve muoversi lungo diagonali, altrimenti la mossa non è consentita
	    }

	    // Calcola la direzione del movimento
	    int deltaX = (posY - posIniY) > 0 ? 1 : -1;
	    int deltaY = (posX - posIniX) > 0 ? 1 : -1;

	    // Controlla le caselle lungo la diagonale
	    for (int i = posIniY + deltaX, j = posIniX + deltaY; i != posY && j != posX; i += deltaX, j += deltaY) {
	        if (scacchiera.getGriglia()[i][j] != null) {
	            return false; // Una casella lungo il percorso è occupata, la mossa non è consentita
	        }
	    }

	    // Controlla se l'alfiere sta catturando un pezzo avversario
	    if (scacchiera.getGriglia()[posX][posY] != null && !scacchiera.getGriglia()[posX][posY].getColore().equals(colore)) {
	        return true; // L'alfiere può catturare un pezzo avversario
	    }

	    return true; // La mossa è consentita
	}

	
	private boolean validaMovimentoRegina(Pezzo regina, Scacchiera scacchiera, Integer posX, Integer posY) {
	    if (validaMovimentoTorre(regina,scacchiera, posX, posY) || validaMovimentoAlfiere(regina, scacchiera, posX, posY)) {
	        return true; // Il movimento è conforme alle regole della torre o dell'alfiere (e quindi della regina)
	    }

	    return false; // La mossa non è consentita
	}
}
