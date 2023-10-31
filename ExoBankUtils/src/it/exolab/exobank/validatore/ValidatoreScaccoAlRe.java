package it.exolab.exobank.validatore;

import java.util.ArrayList;
import java.util.List;

import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.costanti.Costanti;

public class ValidatoreScaccoAlRe {
	
	public boolean isScaccoMatto(Pezzo re, Pezzo[][] scacchiera, String coloreRe) throws Exception {
	    int xRe = re.getPosizioneX();
	    int yRe = re.getPosizioneY();
	    ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
	    // Verifica se il re può muoversi in una posizione sicura
	    for (int deltaX = -1; deltaX <= 1; deltaX++) {
	        for (int deltaY = -1; deltaY <= 1; deltaY++) {
	            if (deltaX == 0 && deltaY == 0) {
	                continue;
	            }

	            int nuovaX = xRe + deltaX;
	            int nuovaY = yRe + deltaY;

	            if (validaMosse.mossaConsentitaPerPezzo(re, xRe, yRe, nuovaX, nuovaY, scacchiera) &&
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

	                        if (validaMosse.mossaConsentitaPerPezzo(pezzo, x, y, nuovaX, nuovaY, scacchiera) &&
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

	public boolean puoiRimuovereScaccoMossaRe(Pezzo re, Integer posizioneXRe, Integer posizioneYRe, Pezzo[][] griglia) throws Exception {
		ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
		for (int deltaX = -1; deltaX <= 1; deltaX++) {
	        for (int deltaY = -1; deltaY <= 1; deltaY++) {
	            if (deltaX == 0 && deltaY == 0) {
	                continue;
	            }

	            Integer nuovaX = posizioneXRe + deltaX;
	            Integer nuovaY = posizioneYRe + deltaY;

	            if (validaMosse.mossaConsentitaPerPezzo(re, posizioneXRe, posizioneYRe, nuovaX, nuovaY, griglia) && !isScacco(nuovaX, nuovaY, griglia, re.getColore())) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	public boolean puoiRimuovereScaccoCatturaMinaccia(Pezzo re, int xRe, int yRe, Pezzo[][] griglia) throws Exception {
	    String coloreRe = re.getColore();
	    ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
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
	                if (validaMosse.mossaConsentitaPerPezzo(pezzo, x, y, minaccia.getPosizioneX(), minaccia.getPosizioneY(), griglia)) {
	                    return true; // Questo pezzo può catturare il pezzo minaccioso.
	                }
	            }
	        }
	    }

	    return false;
	}

	public boolean puoiRimuovereScaccoBloccaMinaccia(Pezzo re, int xRe, int yRe, Pezzo[][] griglia) throws Exception {
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
	    ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
	    // Scorrere la griglia per trovare la minaccia
	    for (int x = 0; x < griglia.length; x++) {
	        for (int y = 0; y < griglia[x].length; y++) {
	            Pezzo pezzo = griglia[x][y];
	            if (pezzo != null && !pezzo.getColore().equals(coloreRe)) {
	                // Verifica se il pezzo può minacciare il re
	                if (validaMosse.mossaConsentitaPerPezzo(pezzo, x, y, xRe, yRe, griglia)) {
	                    return pezzo; // Restituisci il pezzo minaccioso
	                }
	            }
	        }
	    }
	    
	    return null; // Nessuna minaccia trovata
	}

	

	private boolean isScacco(int x, int y, Pezzo[][] scacchiera, String coloreRe) throws Exception {
	    String coloreAvversario = (coloreRe.equals(Costanti.BIANCO)) ? Costanti.NERO : Costanti.BIANCO; // Calcola il colore avversario

	    for (Pezzo minaccia : trovaMinacce(coloreAvversario, scacchiera)) {
	        if (minaccia.getPosizioneX() == x && minaccia.getPosizioneY() == y) {
	            return true;
	        }
	    }

	    return false;
	}
	
	
    public List<Pezzo> trovaMinacce(String colore, Pezzo[][] scacchiera) throws Exception {
        List<Pezzo> minacce = new ArrayList<>();
        ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
        for (int x = 0; x < scacchiera.length; x++) {
            for (int y = 0; y < scacchiera[x].length; y++) {
                Pezzo pezzo = scacchiera[x][y];
                if (pezzo != null && !pezzo.getColore().equals(colore)) {
                    for (int i = 0; i < scacchiera.length; i++) {
                        for (int j = 0; j < scacchiera[i].length; j++) {
                            if (validaMosse.mossaConsentitaPerPezzo(pezzo, x, y, i, j, scacchiera)) {
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

}