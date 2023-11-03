package it.exolab.exobank.validatore;

import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.costanti.Costanti;

public class ValidatoreMovimentiPezzi {
	
    public boolean validaMovimentoPedone(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Colore colore, Pezzo[][] griglia) throws Exception {
        if (xPartenza != xDestinazione || yPartenza != yDestinazione) {
            if (colore.equals(Colore.BIANCO)) {
                return validaMovimentoPedoneBianco(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } else if (colore.equals(Colore.NERO)) {
                return validaMovimentoPedoneNero(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            }
        }

        throw new Exception("Movimento non valido per il pedone.");
    }

    private boolean validaMovimentoPedoneBianco(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
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
        throw new Exception("Pedone bianco mossa non consentita.");
    }

    private boolean validaMovimentoPedoneNero(Integer xPartenza, Integer yPartenza, Integer xDestinazione, Integer yDestinazione, Pezzo[][] griglia) throws Exception {
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
        throw new Exception("Pedone nero mossa non consentita.");
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
        for (int index = minY + 1; index < maxY; index++) {
            if (griglia[xPartenza][index] != null) {
            	throw new Exception("C'è un pezzo prima della destinazione.");
            }
        }
        return true;
    }

    private boolean validaMovimentoTorreOrizzontale(Integer xPartenza, Integer xDestinazione, Integer yPartenza, Pezzo[][] griglia) throws Exception {
        int minX = Math.min(xPartenza, xDestinazione);
        int maxX = Math.max(xPartenza, xDestinazione);
        for (int index = minX + 1; index < maxX; index++) {
            if (griglia[index][yPartenza] != null) {
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

        for (int index = xPartenza + deltaX, j_index = yPartenza + deltaY; index != xDestinazione && j_index != yDestinazione; index += deltaX, j_index += deltaY) {
            if (griglia[index][j_index] != null) {
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
                return validaMovimentoTorre(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } catch (Exception e) {
                throw new Exception("Movimento non valido per la regina");
            }
        } else if (deltaX == deltaY) {
            try {
                return validaMovimentoAlfiere(xPartenza, yPartenza, xDestinazione, yDestinazione, griglia);
            } catch (Exception e) {
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

        if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
            isValid = true;
        }

        if (isValid) {
            if (griglia[xDestinazione][yDestinazione] == null || !griglia[xDestinazione][yDestinazione].getColore().equals(griglia[xPartenza][yPartenza].getColore())) {
                return true;
            }
        }

        throw new Exception("Movimento non valido per il re.");
    }
}