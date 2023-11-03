package it.exolab.exobank.validatore;

import java.util.ArrayList;
import java.util.List;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.costanti.Costanti;

public class ValidatoreScaccoAlRe {
	
	private List<Pezzo> minacceDirette = null;
	
	public boolean isScacco(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Colore coloreGiocatore = re.getColore();
		boolean scacco = false;
		try {
			List<Pezzo> minacce = trovaMinacce(coloreGiocatore, scacchiera);
			for(Pezzo minaccia : minacce) {
				try {
					ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
					ParametriValidatoreDto parametri = new ParametriValidatoreDto(minaccia, minaccia.getPosizioneX(), minaccia.getPosizioneY(), re.getPosizioneX(), re.getPosizioneY(), minaccia.getColore(), scacchiera);
					if(validaMosse.mossaConsentitaPerPezzo(parametri)) {
						scacco = true;
						minacceDirette.add(minaccia);
						break;
					}
				}catch(Exception e) {
					continue;
				}
			}
			return scacco;
			
		}catch(Exception e) {
			throw new Exception();
		}
	}
	
	private boolean negaScaccoMangiandoMinaccia(Pezzo re, Pezzo[][] scacchiera) throws Exception{
		Colore coloreGiocatore = re.getColore();
		boolean salvo = false;
		try {
			for(Pezzo minaccia : minacceDirette) {
				try {
					ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
					ParametriValidatoreDto parametriMinaccia = new ParametriValidatoreDto(minaccia, minaccia.getPosizioneX(), minaccia.getPosizioneY(), re.getPosizioneX(), re.getPosizioneY(), minaccia.getColore(), scacchiera);
					if(validaMosse.mossaConsentitaPerPezzo(parametriMinaccia)) {
						List<Pezzo> alleati = trovaAlleati(coloreGiocatore, scacchiera);
						for(Pezzo alleato : alleati) {
							try {
								ParametriValidatoreDto parametriAlleato = new ParametriValidatoreDto(alleato, alleato.getPosizioneX(), alleato.getPosizioneY(), minaccia.getPosizioneX(), minaccia.getPosizioneY(), alleato.getColore(), scacchiera);
								if(validaMosse.mossaConsentitaPerPezzo(parametriAlleato)) {
									salvo = true;
									return salvo;
								}
								
							}catch(Exception e) {
								continue;
							}
						}
					}
				}catch(Exception e) {
					continue;
				}
			}
			return salvo;
			
		}catch(Exception e) {
			throw new Exception();
		}
	}
	
	private boolean negaScaccoMuovendoIlRe(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Pezzo reProxy = new Pezzo();
		boolean salvo = false;
		ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
		try {
			ParametriValidatoreDto parametriDto = new ParametriValidatoreDto(re, re.getPosizioneX(), re.getPosizioneY(), re.getPosizioneX(), re.getPosizioneY(), re.getColore(), scacchiera);
			for(int righe = - 1; righe <= 1; righe++) {
				for(int colonne = - 1; colonne <= 1; colonne++) {
					try {
						if(righe == 0 && colonne == 0) {
							continue;
						}else {
							parametriDto.setxDestinazione(re.getPosizioneX() + righe);
							parametriDto.setyDestinazione(re.getPosizioneY() + colonne);
							if(validaMosse.mossaConsentitaPerPezzo(parametriDto)) {
								reProxy.setPosizioneX(parametriDto.getxDestinazione());
								reProxy.setPosizioneY(parametriDto.getyDestinazione());
								reProxy.setColore(parametriDto.getColore());
								if(!isScacco(reProxy, scacchiera)) {
									salvo = true;
									return salvo;
								}
							}
						}
					}catch (Exception e) {
						continue;
					}
				}
			}
			return salvo;		
					
		}catch(Exception e) {
			throw new Exception();
		}
				
	}
	
	public boolean puoInterporreTraReEMinaccia(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Colore coloreGiocatore = re.getColore();
		boolean pezzoFrapposto = false;
		try {
			for(Pezzo pezzoMinaccia : minacceDirette) {
				try {
					ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
					List<Pezzo> alleati = trovaAlleati(coloreGiocatore, scacchiera);
					for(Pezzo pezzo : alleati) {
						ParametriValidatoreDto parametri = new ParametriValidatoreDto(pezzo, pezzo.getPosizioneX(), pezzo.getPosizioneY(), re.getPosizioneX(), re.getPosizioneY(), pezzo.getColore(), scacchiera);
						if(validaMosse.mossaConsentitaPerPezzo(parametri)) {
							pezzoFrapposto = true;
							minacceDirette.remove(pezzoMinaccia);
							break;
						}
					}
				}catch(Exception e) {
					continue;
				}
			}
		}catch(Exception e) {
			throw new Exception();
		}
		return pezzoFrapposto;
	}
	
	private List<Pezzo> trovaMinacce(Colore colore, Pezzo[][] scacchiera) throws Exception {
		try {
			List<Pezzo> minacce = new ArrayList<>();
			for (int x = 0; x < scacchiera.length; x++) {
				for (int y = 0; y < scacchiera[x].length; y++) {
					Pezzo pezzo = scacchiera[x][y];
					if (null != pezzo && !pezzo.getColore().equals(colore)) {
						minacce.add(pezzo);
					}
				}
			}
			return minacce;
		}catch(Exception e) {
			throw new Exception();
		}
	}
	
	private List<Pezzo> trovaAlleati(Colore colore, Pezzo[][] scacchiera) throws Exception {
		try {
			List<Pezzo> alleati = new ArrayList<>();
			for (int x = 0; x < scacchiera.length; x++) {
				for (int y = 0; y < scacchiera[x].length; y++) {
					Pezzo pezzo = scacchiera[x][y];
					if (null != pezzo && pezzo.getColore().equals(colore)) {
						alleati.add(pezzo);
					}
				}
			}
			return alleati;
		}catch(Exception e) {
			throw new Exception();
		}
	}

	public boolean isScaccoMatto(Pezzo re, Pezzo[][] scacchiera, Colore coloreRe) throws Exception {
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


	public boolean puoiRimuovereScaccoCatturaMinaccia(Pezzo re, int xRe, int yRe, Pezzo[][] griglia) throws Exception {
	    Colore coloreRe = re.getColore();
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
	    Colore coloreRe = re.getColore();
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

	private List<Pezzo> trovaPedineStessoColore(Colore colore, Pezzo[][] griglia) {
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
	    Colore coloreRe = griglia[xRe][yRe].getColore();
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
