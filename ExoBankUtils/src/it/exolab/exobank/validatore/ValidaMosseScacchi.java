package it.exolab.exobank.validatore;

import java.util.ArrayList;
import java.util.List;

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
	
	private boolean validaMovimentoPedone(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, String colore, Pezzo[][] griglia) throws Exception {
	    if (xPartenza != xDestinazione || yPartenza != yDestinazione) {
	        if (colore.equals(Costanti.BIANCO)) {
	            // Controllo per il pedone bianco
	            if (xDestinazione == xPartenza + 1) {
	                if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null) {
	                    return true;
	                } else if (yDestinazione == yPartenza + 1 && griglia[xDestinazione][yDestinazione] != null) {
	                    return true;
	                }
	            } else if (xDestinazione == xPartenza + 2 && xPartenza == 1) {
	                if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null
	                        && griglia[xPartenza + 1][yPartenza] == null) {
	                    return true;
	                }
	            }
	        } else if (colore.equals(Costanti.NERO)) {
	            // Controllo per il pedone nero
	            if (xDestinazione == xPartenza - 1) {
	                if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null) {
	                    return true;
	                } else if (yDestinazione == yPartenza - 1 && griglia[xDestinazione][yDestinazione] != null) {
	                    return true;
	                }
	            } else if (xDestinazione == xPartenza - 2 && xPartenza == 6) {
	                if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null
	                        && griglia[xPartenza - 1][yPartenza] == null) {
	                    return true;
	                }
	            }
	        }
	    }
	    
	    throw new Exception("Movimento non valido per il pedone.");
	}

	private boolean validaMovimentoTorre(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
	    if (xPartenza != xDestinazione && yPartenza != yDestinazione) {
	        throw new Exception("La torre può muoversi solo in orizzontale o verticale.");
	    }

	    if (xPartenza.equals(xDestinazione)) {
	        int minY = Math.min(yPartenza, yDestinazione);
	        int maxY = Math.max(yPartenza, yDestinazione);
	        for (int i = minY + 1; i < maxY; i++) {
	            if (griglia[xPartenza][i] != null) {
	                throw new Exception("Una casella lungo il percorso è occupata, la mossa non è consentita.");
	            }
	        }
	    } else if (yPartenza.equals(yDestinazione)) {
	        int minX = Math.min(xPartenza, xDestinazione);
	        int maxX = Math.max(xPartenza, xDestinazione);
	        for (int i = minX + 1; i < maxX; i++) {
	            if (griglia[i][yPartenza] != null) {
	                throw new Exception("Una casella lungo il percorso è occupata, la mossa non è consentita.");
	            }
	        }
	    }
	    
	    if (griglia[xDestinazione][yDestinazione] != null) {
	        return true;
	    }
	    
	    throw new Exception("La mossa non è consentita.");
	}

	private boolean validaMovimentoAlfiere(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
	    if (Math.abs(xDestinazione - xPartenza) != Math.abs(yDestinazione - yPartenza)) {
	        throw new Exception("L'alfiere deve muoversi lungo diagonali.");
	    }

	    int deltaX = (xDestinazione - xPartenza) > 0 ? 1 : -1;
	    int deltaY = (yDestinazione - yPartenza) > 0 ? 1 : -1;

	    for (int i = xPartenza + deltaX, j = yPartenza + deltaY; i != xDestinazione && j != yDestinazione; i += deltaX, j += deltaY) {
	        if (griglia[i][j] != null) {
	            throw new Exception("Una casella lungo il percorso è occupata, la mossa non è consentita.");
	        }
	    }

	    if (griglia[xDestinazione][yDestinazione] != null) {
	        return true;
	    }

	    throw new Exception("La mossa non è consentita.");
	}

	private boolean controllaMovimentoRegina(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
	    if (validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia) || validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia)) {
	        return true;
	    }
	    
	    throw new Exception("Il movimento non è conforme alle regole della torre o dell'alfiere (e quindi della regina).");
	}
	
	private boolean validaMovimentoCavallo(Pezzo cavallo, Scacchiera scacchiera, Integer posX, Integer posY) {
		
		Integer posIniX = cavallo.getPosizioneX();
		Integer posIniY = cavallo.getPosizioneY();
		boolean isValid = false;
		
		if(validaPosizione(cavallo, scacchiera, posX, posY)){
			if((posX == posIniX + 2 && posY == posIniY + 1) 
				|| (posX == posIniX + 2 && posY == posIniY - 1) 
				|| (posX == posIniX - 2 && posY == posIniY + 1)
				|| (posX == posIniX - 2 && posY == posIniY - 1) 
				|| (posX == posIniX + 1 && posY == posIniY - 2)
				|| (posX == posIniX + 1 && posY == posIniY + 2)
				|| (posX == posIniX - 1 && posY == posIniY - 2)
				|| (posX == posIniX - 1 && posY == posIniY + 2)){
				
				isValid = true;
			}
		}
		return isValid;
	}
	
	public boolean puoiRimuovereScacco(Integer xRe, Integer yRe, Pezzo[][] griglia, Scacchiera scacchiera) throws Exception {
	    Pezzo re = griglia[xRe][yRe];

	    if (isScaccoMatto(re, griglia, re.getColore())) {
	        throw new RuntimeException("SCACCO MATTO");
	    }

	    return puoiRimuovereScaccoMossaRe(re, xRe, yRe, griglia) ||
	        puoiRimuovereScaccoCatturaMinaccia(re, xRe, yRe, griglia) ||
	        puoiRimuovereScaccoBloccaMinaccia(re, xRe, yRe, griglia);
	}

	private boolean puoiRimuovereScaccoMossaRe(Pezzo re, Integer posizioneXRe, Integer posizioneYRe, Pezzo[][] griglia) {
	    for (int deltaX = -1; deltaX <= 1; deltaX++) {
	        for (int deltaY = -1; deltaY <= 1; deltaY++) {
	            if (deltaX == 0 && deltaY == 0) {
	                continue;
	            }

	            Integer nuovaX = posizioneXRe + deltaX;
	            Integer nuovaY = posizioneYRe + deltaY;

	            if (isMossaValida(re, posizioneXRe, posizioneYRe, nuovaX, nuovaY, griglia) && !isScacco(nuovaX, nuovaY, griglia, re.getColore())) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	private boolean puoiRimuovereScaccoCatturaMinaccia(Pezzo re, int xRe, int yRe, Pezzo[][] griglia) throws Exception {
	    String coloreRe = re.getColore();

	    // Trova il pezzo minaccioso
	    Pezzo minaccia = trovaMinacciaRe(xRe, yRe, griglia);

	    if (minaccia == null) {
	        return false; // Nessuna minaccia trovata.
	    }

	    // Ora scorri tutti i pezzi dello stesso colore del re
	    for (int x = 0; x < griglia.length; x++) {
	        for (int y = 0; y < griglia[x].length; y++) {
	            Pezzo pezzo = griglia[x][y];
	            if (pezzo != null && pezzo.getColore().equals(coloreRe)) {
	                if (mossaValida(pezzo, x, y, minaccia.getPosizioneX(), minaccia.getPosizioneY(), griglia)) {
	                    return true; // Questo pezzo può catturare il pezzo minaccioso.
	                }
	            }
	        }
	    }

	    return false;
	}

	private boolean puoiRimuovereScaccoBloccaMinaccia(Pezzo re, int xRe, int yRe, Pezzo[][] griglia) {
	    String coloreRe = re.getColore();
	    List<Pezzo> pedineStessoColore = trovaPedineStessoColore(coloreRe, griglia);

	    for (Pezzo minaccia : trovaMinacce(coloreRe, griglia)) {
	        for (Pezzo pedina : pedineStessoColore) {
	            if (puoiMetterePedinaTraReEMinaccia(pedina, xRe, yRe, minaccia, griglia)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	private List<Pezzo> trovaPedineStessoColore(String colore, Pezzo[][] griglia) {
	    List<Pezzo> pedineStessoColore = new ArrayList<>();
	    for (int x = 0; x < griglia.length; x++) {
	        for (int y = 0; y < griglia[0].length; y++) {
	            Pezzo pezzo = griglia[x][y];
	            if (pezzo != null && pezzo.getColore().equals(colore)) {
	                pedineStessoColore.add(pezzo);
	            }
	        }
	    }
	    return pedineStessoColore;
	}

	private boolean puoiMetterePedinaTraReEMinaccia(Pezzo pedina, int xRe, int yRe, Pezzo minaccia, Pezzo[][] griglia) {
	    int xPedina = pedina.getPosizioneX();
	    int yPedina = pedina.getPosizioneY();

	    // Calcola la differenza tra le posizioni del re, della minaccia e della pedina
	    int diffReMinacciaX = minaccia.getPosizioneX() - xRe;
	    int diffReMinacciaY = minaccia.getPosizioneY() - yRe;
	    int diffPedinaReX = xRe - xPedina;
	    int diffPedinaReY = yRe - yPedina;

	    // Verifica se la pedina può bloccare la minaccia in orizzontale
	    if (diffReMinacciaX == 0 && Math.abs(diffReMinacciaY) > 1 && diffPedinaReX == 0) {
	        int passo = (diffReMinacciaY > 0) ? 1 : -1;
	        for (int y = yRe + passo; y != minaccia.getPosizioneY(); y += passo) {
	            if (griglia[xRe][y] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in orizzontale
	        return true;
	    }

	    // Verifica se la pedina può bloccare la minaccia in verticale
	    if (diffReMinacciaY == 0 && Math.abs(diffReMinacciaX) > 1 && diffPedinaReY == 0) {
	        int passo = (diffReMinacciaX > 0) ? 1 : -1;
	        for (int x = xRe + passo; x != minaccia.getPosizioneX(); x += passo) {
	            if (griglia[x][yRe] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in verticale
	        return true;
	    }

	    if (diffReMinacciaX == diffReMinacciaY && Math.abs(diffReMinacciaX) > 1 && diffPedinaReX == diffPedinaReY) {
	        int passoX = (diffReMinacciaX > 0) ? 1 : -1;
	        int passoY = (diffReMinacciaY > 0) ? 1 : -1;
	        for (int x = xRe + passoX, y = yRe + passoY; x != minaccia.getPosizioneX() && y != minaccia.getPosizioneY(); x += passoX, y += passoY) {
	            if (griglia[x][y] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in diagonale in alto a sinistra
	        return true;
	    }

	    // Verifica se la pedina può bloccare la minaccia in diagonale in alto a destra
	    if (diffReMinacciaX == -diffReMinacciaY && Math.abs(diffReMinacciaX) > 1 && diffPedinaReX == -diffPedinaReY) {
	        int passoX = (diffReMinacciaX > 0) ? 1 : -1;
	        int passoY = (diffReMinacciaY < 0) ? 1 : -1;
	        for (int x = xRe + passoX, y = yRe + passoY; x != minaccia.getPosizioneX() && y != minaccia.getPosizioneY(); x += passoX, y += passoY) {
	            if (griglia[x][y] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in diagonale in alto a destra
	        return true;
	    }

	    // Verifica se la pedina può bloccare la minaccia in diagonale in basso a sinistra
	    if (diffReMinacciaX == -diffReMinacciaY && Math.abs(diffReMinacciaX) > 1 && diffPedinaReX == -diffPedinaReY) {
	        int passoX = (diffReMinacciaX < 0) ? 1 : -1;
	        int passoY = (diffReMinacciaY > 0) ? 1 : -1;
	        for (int x = xRe + passoX, y = yRe + passoY; x != minaccia.getPosizioneX() && y != minaccia.getPosizioneY(); x += passoX, y += passoY) {
	            if (griglia[x][y] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in diagonale in basso a sinistra
	        return true;
	    }

	    // Verifica se la pedina può bloccare la minaccia in diagonale in basso a destra
	    if (diffReMinacciaX == diffReMinacciaY && Math.abs(diffReMinacciaX) > 1 && diffPedinaReX == diffPedinaReY) {
	        int passoX = (diffReMinacciaX < 0) ? 1 : -1;
	        int passoY = (diffReMinacciaY < 0) ? 1 : -1;
	        for (int x = xRe + passoX, y = yRe + passoY; x != minaccia.getPosizioneX() && y != minaccia.getPosizioneY(); x += passoX, y += passoY) {
	            if (griglia[x][y] != null) {
	                // La pedina non può andare tra il re e la minaccia
	                return false;
	            }
	        }
	        // La pedina può bloccare la minaccia in diagonale in basso a destra
	        return true;
	    }

	    // Se nessuna delle condizioni sopra è soddisfatta, la pedina non può bloccare la minaccia.
	    return false;
	}
	
	private Pezzo trovaMinacciaRe(int xRe, int yRe, Pezzo[][] griglia) throws Exception {
	    String coloreRe = griglia[xRe][yRe].getColore();
	    
	    // Scorrere la griglia per trovare la minaccia
	    for (int x = 0; x < griglia.length; x++) {
	        for (int y = 0; y < griglia[x].length; y++) {
	            Pezzo pezzo = griglia[x][y];
	            if (pezzo != null && !pezzo.getColore().equals(coloreRe)) {
	                // Verifica se il pezzo può minacciare il re
	                if (mossaValida(pezzo, x, y, xRe, yRe, griglia)) {
	                    return pezzo; // Restituisci il pezzo minaccioso
	                }
	            }
	        }
	    }
	    
	    return null; // Nessuna minaccia trovata
	}

	private boolean isScaccoMatto(Pezzo re, Pezzo[][] scacchiera, String coloreRe) {
	    int xRe = re.getPosizioneX();
	    int yRe = re.getPosizioneY();

	    // Verifica se il re può muoversi in una posizione sicura
	    for (int deltaX = -1; deltaX <= 1; deltaX++) {
	        for (int deltaY = -1; deltaY <= 1; deltaY++) {
	            if (deltaX == 0 && deltaY == 0) {
	                continue;
	            }

	            int nuovaX = xRe + deltaX;
	            int nuovaY = yRe + deltaY;

	            if (isMossaValida(re, xRe, yRe, nuovaX, nuovaY, scacchiera) &&
	                !isScacco(nuovaX, nuovaY, scacchiera, coloreRe)) {
	                return false;
	            }
	        }
	    }

	    // Verifica se è possibile bloccare le minacce avverse
	    for (int x = 0; x < scacchiera.length; x++) {
	        for (int y = 0; y < scacchiera[x].length; y++) {
	            Pezzo pezzo = scacchiera[x][y];
	            if (pezzo != null && !pezzo.getColore().equals(re.getColore())) {
	                // Verifica se il pezzo può muoversi in modo da bloccare le minacce avverse
	                for (int deltaX = -1; deltaX <= 1; deltaX++) {
	                    for (int deltaY = -1; deltaY <= 1; deltaY++) {
	                        if (deltaX == 0 && deltaY == 0) {
	                            continue;
	                        }

	                        int nuovaX = x + deltaX;
	                        int nuovaY = y + deltaY;

	                        if (isMossaValida(pezzo, x, y, nuovaX, nuovaY, scacchiera) &&
	                            !isScacco(nuovaX, nuovaY, scacchiera, coloreRe)) {
	                            return false;
	                        }
	                    }
	                }
	            }
	        }
	    }

	    return true; // Se il re non può muoversi in posizioni sicure o bloccare le minacce, è scacco matto.
	}

	private boolean isScacco(int x, int y, Pezzo[][] scacchiera, String coloreRe) {
	    String coloreAvversario = (coloreRe.equals(Costanti.BIANCO)) ? Costanti.NERO : Costanti.BIANCO; // Calcola il colore avversario

	    for (Pezzo minaccia : trovaMinacce(coloreAvversario, scacchiera)) {
	        if (minaccia.getPosizioneX() == x && minaccia.getPosizioneY() == y) {
	            return true;
	        }
	    }

	    return false;
	}
	
	public boolean isMossaValida(Pezzo pezzo, int xPartenza, int yPartenza, int xDestinazione, int yDestinazione, Pezzo[][] scacchiera) {
        if (xDestinazione < 0 || xDestinazione >= scacchiera.length || yDestinazione < 0 || yDestinazione >= scacchiera[0].length) {
            return false; // La destinazione è fuori dalla scacchiera.
        }

        if (xPartenza == xDestinazione && yPartenza == yDestinazione) {
            return false; // La destinazione è la stessa posizione di partenza.
        }

        Pezzo pezzoDestinazione = scacchiera[xDestinazione][yDestinazione];
        if (pezzoDestinazione != null && pezzoDestinazione.getColore().equals(pezzo.getColore())) {
            return false; // Il pezzo di destinazione è dello stesso colore del pezzo in movimento.
        }

        // Implementa la logica per verificare se la mossa è valida per il tipo di pezzo specifico.
        // Ad esempio, considera le regole di movimento specifiche per ciascun pezzo (re, regina, torre, ecc.).
        return true; // Cambia questa logica in base alle regole degli scacchi.
    }

    public List<Pezzo> trovaMinacce(String colore, Pezzo[][] scacchiera) {
        List<Pezzo> minacce = new ArrayList<>();

        for (int x = 0; x < scacchiera.length; x++) {
            for (int y = 0; y < scacchiera[x].length; y++) {
                Pezzo pezzo = scacchiera[x][y];
                if (pezzo != null && !pezzo.getColore().equals(colore)) {
                    for (int i = 0; i < scacchiera.length; i++) {
                        for (int j = 0; j < scacchiera[i].length; j++) {
                            if (isMossaValida(pezzo, x, y, i, j, scacchiera)) {
                                minacce.add(pezzo);
                            }
                        }
                    }
                }
            }
        }

        return minacce;
    }

    public List<int[]> trovaTraiettoriaMinacciaRe(Pezzo minaccia, Pezzo re, Pezzo[][] scacchiera) {
        List<int[]> traiettoria = new ArrayList<>();
        int xMinaccia = minaccia.getPosizioneX();
        int yMinaccia = minaccia.getPosizioneY();
        int xRe = re.getPosizioneX();
        int yRe = re.getPosizioneY();

        if (xMinaccia == xRe) {
            // Minaccia sulla stessa riga
            int step = (yMinaccia < yRe) ? 1 : -1;
            for (int y = yMinaccia + step; y != yRe; y += step) {
                traiettoria.add(new int[] { xMinaccia, y });
            }
        } else if (yMinaccia == yRe) {
            // Minaccia sulla stessa colonna
            int step = (xMinaccia < xRe) ? 1 : -1;
            for (int x = xMinaccia + step; x != xRe; x += step) {
                traiettoria.add(new int[] { x, yMinaccia });
            }
        }

        return traiettoria;
    }

    public boolean mossaValida(Pezzo pezzo, Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] scacchiera) throws Exception {
        if (xDestinazione < 0 || xDestinazione >= scacchiera.length || yDestinazione < 0 || yDestinazione >= scacchiera[0].length) {
            return false; // La destinazione è fuori dalla scacchiera.
        }

        if (xPartenza == xDestinazione && yPartenza == yDestinazione) {
            return false; // La destinazione è la stessa posizione di partenza.
        }

        Pezzo pezzoDestinazione = scacchiera[xDestinazione][yDestinazione];
        if (pezzoDestinazione != null && pezzoDestinazione.getColore().equals(pezzo.getColore())) {
            return false; // Il pezzo di destinazione è dello stesso colore del pezzo in movimento.
        }

        // Aggiungi la logica specifica per ciascun tipo di pezzo qui.
        if (pezzo instanceof Torre) {
            return validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        } else if (pezzo instanceof Alfiere) {
            return validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        
        } else if (pezzo instanceof Regina) {
            return controllaMovimentoRegina(xPartenza, yPartenza, xDestinazione, yDestinazione, scacchiera);
        } else if (pezzo instanceof Pedone) {
            return validaMovimentoPedone(xPartenza, yPartenza, xDestinazione, yDestinazione, pezzo.getColore(), scacchiera);
        }

        return false; // Restituisci true se la mossa è consentita, altrimenti false.
    }
}
