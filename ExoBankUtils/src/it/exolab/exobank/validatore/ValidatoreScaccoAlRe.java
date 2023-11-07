package it.exolab.exobank.validatore;

import java.util.ArrayList;
import java.util.List;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.scacchiera.ex.MossaNonConsentita;

public class ValidatoreScaccoAlRe {
	
	private List<Pezzo> minacceDirette = new ArrayList<Pezzo>();
	
	public boolean isScacco(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Colore coloreGiocatore = re.getColore();
		boolean scacco = false;
		minacceDirette.removeAll(minacceDirette);
		try {
			List<Pezzo> minacce = trovaMinacceOAlleati(coloreGiocatore, scacchiera, false);
			for(Pezzo minaccia : minacce) {
				try {
					ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
					ParametriValidatoreDto parametri = new ParametriValidatoreDto(minaccia, minaccia.getPosizioneX(), minaccia.getPosizioneY(),
							re.getPosizioneX(), re.getPosizioneY(), minaccia.getColore(), scacchiera);
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
	
	public boolean negaScaccoMangiandoMinaccia(Pezzo re, Pezzo[][] scacchiera) throws Exception{
		Colore coloreGiocatore = re.getColore();
		boolean salvo = false;
		try {
			for(Pezzo minaccia : minacceDirette) {
				try {
					ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
					ParametriValidatoreDto parametriMinaccia = new ParametriValidatoreDto(minaccia, minaccia.getPosizioneX(), 
							minaccia.getPosizioneY(), re.getPosizioneX(), re.getPosizioneY(), minaccia.getColore(), scacchiera);
					if(validaMosse.mossaConsentitaPerPezzo(parametriMinaccia)) {
						List<Pezzo> alleati = trovaMinacceOAlleati(coloreGiocatore, scacchiera, true);
						for(Pezzo alleato : alleati) {
							try {
								ParametriValidatoreDto parametriAlleato = new ParametriValidatoreDto(alleato, alleato.getPosizioneX(), 
										alleato.getPosizioneY(), minaccia.getPosizioneX(), minaccia.getPosizioneY(), alleato.getColore(), scacchiera);
								if(validaMosse.mossaConsentitaPerPezzo(parametriAlleato)) {
									minacceDirette.remove(minaccia);
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
			throw new Exception("Mossa non consentita!");
		}
	}
	
	public boolean negaScaccoMuovendoIlRe(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Pezzo reProxy = new Pezzo();
		boolean salvo = false;
		ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
		try {
			ParametriValidatoreDto parametriDto = new ParametriValidatoreDto(re, re.getPosizioneX(), re.getPosizioneY(), re.getPosizioneX(), 
					re.getPosizioneY(), re.getColore(), scacchiera);
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
			throw new Exception("Mossa non consentita!");
		}
				
	}

	
	public boolean puoInterporreTraReEMinaccia(Pezzo re, Pezzo[][] scacchiera) throws Exception {
	    Colore coloreGiocatore = re.getColore();
	    boolean pezzoFrapposto = false;

	    try {
	        List<Pezzo> alleati = trovaMinacceOAlleati(coloreGiocatore, scacchiera, true);

	        // Rimuovi il re dalla lista dei pezzi alleati
	        alleati.removeIf(pezzo -> pezzo.getTipo() == Tipo.RE && pezzo.getColore() == coloreGiocatore);

	        for (Pezzo pezzo : alleati) {
	            if (puoEsserePosizionatoNellaCroceDiagonale(re, pezzo, scacchiera)) {
	                pezzoFrapposto = true;
	                break;
	            }
	        }
	    } catch (Exception e) {
	        throw new Exception("Mossa non consentita!");
	    }

	    return pezzoFrapposto;
	}
	
	private boolean puoEsserePosizionatoNellaCroceDiagonale(Pezzo re, Pezzo alleato, Pezzo[][] scacchiera) throws MossaNonConsentita {
	    int reX = re.getPosizioneX();
	    int reY = re.getPosizioneY();
	    int alleatoX = alleato.getPosizioneX();
	    int alleatoY = alleato.getPosizioneY();

	    // Verifica se l'alleato può essere posizionato nella croce diagonale del re
	    if (Math.abs(reX - alleatoX) == Math.abs(reY - alleatoY)) {
	        // Crea un oggetto ParametriValidatoreDto per verificare la validità della mossa
	        ParametriValidatoreDto parametri = new ParametriValidatoreDto(alleato, alleatoX, alleatoY, reX, reY, alleato.getColore(), scacchiera);
	        ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
	        if (validaMosse.mossaConsentitaPerPezzo(parametri)) {
	            return true;
	        }
	    }

	    return false;
	}

	
	private List<Pezzo> trovaMinacceOAlleati(Colore colore, Pezzo[][] scacchiera, boolean alleato) throws Exception {
		try {
			List<Pezzo> listaPezzi = new ArrayList<>();
			for (int x = 0; x < scacchiera.length; x++) {
				for (int y = 0; y < scacchiera[x].length; y++) {
					Pezzo pezzo = scacchiera[x][y];
					if(alleato) {
						if (null != pezzo && pezzo.getColore().equals(colore)) {
							listaPezzi.add(pezzo);
						}
					}else {
						if(null != pezzo && !pezzo.getColore().equals(colore)) {
							listaPezzi.add(pezzo);
						}
					}
				}
			}
			return listaPezzi;
		}catch(Exception e) {
			throw new Exception();
		}
	}
	

	public boolean isScaccoMatto(Pezzo re, Pezzo[][] scacchiera) throws Exception {

	    if (negaScaccoMangiandoMinaccia(re, scacchiera)) {
	        return false;
	    }

	    if (negaScaccoMuovendoIlRe(re, scacchiera)) {
	        return false; 
	    }

	    if (puoInterporreTraReEMinaccia(re, scacchiera)) {
	        return false;
	    }

	    return true;
	}

}
