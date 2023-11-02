package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.costanti.Costanti;

public class ValidatoreMovimentiPezzi {
	
    public boolean validaMovimentoPedone(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, String colore, Pezzo[][] griglia) throws Exception {
        if (xPartenza != xDestinazione || yPartenza != yDestinazione) {
            if (colore.equals(Costanti.BIANCO)) {
                return validaMovimentoPedoneBianco(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } else if (colore.equals(Costanti.NERO)) {
                return validaMovimentoPedoneNero(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            }
        }

        throw new Exception("Movimento non valido per il pedone.");
    }

    private boolean validaMovimentoPedoneBianco(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) {
        if (xDestinazione == xPartenza + 1) {
            if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null) {
                return true;
            } else if (Math.abs(yDestinazione - yPartenza) == 1 && griglia[xDestinazione][yDestinazione] != null) {
                return true;
            }
        } else if (xDestinazione == xPartenza + 2 && xPartenza == 1) {
            if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null
                    && griglia[xPartenza + 1][yPartenza] == null) {
                return true;
            }
        }
        return false;
    }

    private boolean validaMovimentoPedoneNero(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) {
        if (xDestinazione == xPartenza - 1) {
            if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null) {
                return true;
            } else if (Math.abs(yDestinazione - yPartenza) == 1 && griglia[xDestinazione][yDestinazione] != null) {
                return true;
            }
        } else if (xDestinazione == xPartenza - 2 && xPartenza == 6) {
            if (yDestinazione == yPartenza && griglia[xDestinazione][yDestinazione] == null
                    && griglia[xPartenza - 1][yPartenza] == null) {
                return true;
            }
        }
        return false;
    }

    public boolean validaMovimentoTorre(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
        if (xPartenza != xDestinazione && yPartenza != yDestinazione) {
            throw new Exception("La torre può muoversi solo in orizzontale o verticale.");
        }

        if (xPartenza.equals(xDestinazione)) {
            return validaMovimentoTorreVerticale(xPartenza, yPartenza, yDestinazione, griglia);
        } else if (yPartenza.equals(yDestinazione)) {
            return validaMovimentoTorreOrizzontale(xPartenza, xDestinazione, yPartenza, griglia);
        }

        throw new Exception("Movimento non valido per la torre.");
    }

    private boolean validaMovimentoTorreVerticale(Integer xPartenza, Integer yPartenza, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
        int minY = Math.min(yPartenza, yDestinazione);
        int maxY = Math.max(yPartenza, yDestinazione);
        for (int i = minY + 1; i < maxY; i++) {
            if (griglia[xPartenza][i] != null) {
            	throw new Exception("C'è un pezzo prima della destinazione.");
            }
        }
        return true;
    }

    private boolean validaMovimentoTorreOrizzontale(Integer xPartenza, Integer xDestinazione, Integer yPartenza, Pezzo[][] griglia) throws Exception {
        int minX = Math.min(xPartenza, xDestinazione);
        int maxX = Math.max(xPartenza, xDestinazione);
        for (int i = minX + 1; i < maxX; i++) {
            if (griglia[i][yPartenza] != null) {
            	throw new Exception("C'è un pezzo prima della destinazione.");
            }
        }
        return true;
    }

    public boolean validaMovimentoAlfiere(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
        if (Math.abs(xDestinazione - xPartenza) != Math.abs(yDestinazione - yPartenza)) {
            throw new Exception("L'alfiere deve muoversi lungo diagonali.");
        }

        int deltaX = (xDestinazione - xPartenza) > 0 ? 1 : -1;
        int deltaY = (yDestinazione - yPartenza) > 0 ? 1 : -1;

        for (int i = xPartenza + deltaX, j = yPartenza + deltaY; i != xDestinazione && j != yDestinazione; i += deltaX, j += deltaY) {
            if (griglia[i][j] != null) {
            	throw new Exception("C'è un pezzo prima della destinazione.");
            }
        }

        return true;
    }

    public boolean validaMovimentoRegina(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
        int deltaX = Math.abs(xDestinazione - xPartenza);
        int deltaY = Math.abs(yDestinazione - yPartenza);

        if ((deltaX == 0 && deltaY != 0) || (deltaX != 0 && deltaY == 0)) {
            try {
                // Movimento orizzontale o verticale, quindi validaMovimentoTorre
                return validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } catch (Exception e) {
                // Personalizza il messaggio di errore se il movimento della torre non è valido
                throw new Exception("Movimento non valido per la regina");
            }
        } else if (deltaX == deltaY) {
            try {
                // Movimento diagonale, quindi validaMovimentoAlfiere
                return validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } catch (Exception e) {
                // Personalizza il messaggio di errore se il movimento dell'alfiere non è valido
                throw new Exception("Movimento non valido per la regina");
            }
        }

        return false;
    }

    public boolean validaMovimentoCavallo(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione) {
        boolean isValid = false;
        if ((xDestinazione == xPartenza + 2 && yDestinazione == yPartenza + 1)
                || (xDestinazione == xPartenza + 2 && yDestinazione == yPartenza - 1)
                || (xDestinazione == xPartenza - 2 && yDestinazione == yPartenza + 1)
                || (xDestinazione == xPartenza - 2 && yDestinazione == yPartenza - 1)
                || (xDestinazione == xPartenza + 1 && yDestinazione == yPartenza - 2)
                || (xDestinazione == xPartenza + 1 && yDestinazione == yPartenza + 2)
                || (xDestinazione == xPartenza - 1 && yDestinazione == yPartenza - 2)
                || (xDestinazione == xPartenza - 1 && yDestinazione == yPartenza + 2)) {
            isValid = true;
        }
        return isValid;
    }

    public boolean validaMovimentoRe(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
        boolean isValid = false;

        int deltaX = Math.abs(xDestinazione - xPartenza);
        int deltaY = Math.abs(yDestinazione - yPartenza);

        // Il re può muoversi solo in queste 8 posizioni intorno a lui
        if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
            isValid = true;
        }

        if (isValid) {
            // Verifica se la destinazione è una posizione vuota o contiene un pezzo avversario
            if (griglia[xDestinazione][yDestinazione] == null || !griglia[xDestinazione][yDestinazione].getColore().equals(griglia[xPartenza][yPartenza].getColore())) {
                return true;
            }
        }

        throw new Exception("Movimento non valido per il re.");
    }
}