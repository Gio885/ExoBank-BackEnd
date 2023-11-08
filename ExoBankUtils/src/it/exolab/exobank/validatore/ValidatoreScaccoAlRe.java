package it.exolab.exobank.validatore;

import java.util.ArrayList;
import java.util.List;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.scacchiera.ex.MossaNonConsentita;

public class ValidatoreScaccoAlRe {

	private List<Pezzo> minacceDirette = new ArrayList<Pezzo>();

	public boolean isScacco(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Colore coloreGiocatore = re.getColore();
		boolean scacco = false;
		minacceDirette.removeAll(minacceDirette);
		try {
			List<Pezzo> minacce = trovaMinacceOAlleati(coloreGiocatore, scacchiera, false);
			ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
			ParametriValidatoreDto parametri = new ParametriValidatoreDto();
			for(Pezzo minaccia : minacce) {
				try {
					compilaDtoPerMinacceOAlleati(parametri, minaccia, re, scacchiera);
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
			ValidaMosseScacchi validaMosse = new ValidaMosseScacchi();
			ParametriValidatoreDto parametri = new ParametriValidatoreDto();
			for(Pezzo minaccia : minacceDirette) {
				try {
					compilaDtoPerMinacceOAlleati(parametri, minaccia, re, scacchiera);
					Integer minacciaPosXOriginale = minaccia.getPosizioneX();
					Integer minacciaPosYOriginale = minaccia.getPosizioneY();
					if(validaMosse.mossaConsentitaPerPezzo(parametri)) {
						List<Pezzo> alleati = trovaMinacceOAlleati(coloreGiocatore, scacchiera, true);
						for(Pezzo alleato : alleati) {
							try {
								compilaDtoPerMinacceOAlleati(parametri, alleato, minaccia, scacchiera);
								Integer alleatoPosXOriginale = alleato.getPosizioneX();
								Integer alleatoPosYOriginale = alleato.getPosizioneY();
								if(validaMosse.mossaConsentitaPerPezzo(parametri)) {
									scacchiera[minaccia.getPosizioneX()][minaccia.getPosizioneY()] = alleato;
									if(!isScacco(re, scacchiera)) {
										salvo = true;
										resetPosizione(alleato, alleatoPosXOriginale, alleatoPosYOriginale, scacchiera);
										resetPosizione(minaccia, minacciaPosXOriginale, minacciaPosYOriginale, scacchiera);
										return salvo;
									}
									resetPosizione(alleato, alleatoPosXOriginale, alleatoPosYOriginale, scacchiera);
									resetPosizione(minaccia, minacciaPosXOriginale, minacciaPosYOriginale, scacchiera);
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
			throw new Exception(Costanti.MOSSA_NON_CONSENTITA);
		}
	}

	public boolean negaScaccoMuovendoIlRe(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Scacchiera tabella = new Scacchiera();
		tabella.setScacchiera(creaCopiaScacchiera(scacchiera));
		Pezzo reProxy = new Pezzo();
		reProxy.setColore(re.getColore());
		reProxy.setEsiste(re.isEsiste());
		reProxy.setId(re.getId());
		reProxy.setPosizioneX(re.getPosizioneX());
		reProxy.setPosizioneY(re.getPosizioneY());
		reProxy.setTipo(re.getTipo());
		

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
								Pezzo[][] griglia = tabella.getGriglia();
								griglia[reProxy.getPosizioneX()][reProxy.getPosizioneY()] = reProxy; 
								if(null != griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()]) {
									Pezzo appoggio = new Pezzo();
									appoggio.setColore(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].getColore());
									appoggio.setEsiste(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].isEsiste());
									appoggio.setId(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].getId());
									appoggio.setPosizioneX(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].getPosizioneX());
									appoggio.setPosizioneY(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].getPosizioneY());
									appoggio.setTipo(griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()].getTipo());
									griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = reProxy;
									griglia[re.getPosizioneX()][re.getPosizioneY()] = null;
									if(!isScacco(reProxy, griglia)) {
										salvo = true;
										griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = appoggio;
										griglia[re.getPosizioneX()][re.getPosizioneY()] = re;
										return salvo;
									}else {
										griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = appoggio;
										griglia[re.getPosizioneX()][re.getPosizioneY()] = re;
									}
								}else {
									griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = reProxy;
									griglia[re.getPosizioneX()][re.getPosizioneY()] = null;
									if(!isScacco(reProxy, griglia)) {
										salvo = true;
										griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = null;
										griglia[re.getPosizioneX()][re.getPosizioneY()] = re;
										return salvo;
									}else {
										griglia[parametriDto.getxDestinazione()][parametriDto.getyDestinazione()] = null;
										griglia[re.getPosizioneX()][re.getPosizioneY()] = re;
									}
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
			throw new Exception(Costanti.MOSSA_NON_CONSENTITA);
		}

	}
	
	private Pezzo[][] creaCopiaScacchiera(Pezzo[][] grigliaOriginale) {
		Pezzo[][] grigliaCopia = new Pezzo[8][8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Pezzo pezzoOriginale = new Pezzo();
				pezzoOriginale = grigliaOriginale[x][y];
				if (null != pezzoOriginale && pezzoOriginale.isEsiste()) {
					grigliaCopia[x][y] = new Pezzo(pezzoOriginale.getId(), pezzoOriginale.getTipo(), pezzoOriginale.getColore(), x, y, true);
				}
			}
		}

		return grigliaCopia;
	}


	public boolean puoInterporreTraReEMinaccia(Pezzo re, Pezzo[][] scacchiera) throws Exception {
		Colore coloreGiocatore = re.getColore();
		boolean pezzoFrapposto = false;

		try {
			List<Pezzo> alleati = trovaMinacceOAlleati(coloreGiocatore, scacchiera, true);

			// Rimuovi il re dalla lista dei pezzi alleati
			alleati.removeIf(pezzo -> pezzo.getTipo() == Tipo.RE && pezzo.getColore() == coloreGiocatore);

			for (Pezzo pezzo : alleati) {
				try {
					if (puoEsserePosizionatoNellaCroceDiagonale(re, pezzo, scacchiera)) {
						pezzoFrapposto = true;
						break;
					}
				}catch (MossaNonConsentita mn) {
					continue;
				}
			}
		} catch (Exception e) {
			throw new Exception(Costanti.MOSSA_NON_CONSENTITA);
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
	
	private void compilaDtoPerMinacceOAlleati(ParametriValidatoreDto dto, Pezzo pezzo, Pezzo pezzoDiArrivo, Pezzo[][] scacchiera) {
		dto.setPezzo(pezzo);
		dto.setxPartenza(pezzo.getPosizioneX());
		dto.setyPartenza(pezzo.getPosizioneY());
		dto.setxDestinazione(pezzoDiArrivo.getPosizioneX());
		dto.setyDestinazione(pezzoDiArrivo.getPosizioneY());
		dto.setColore(pezzo.getColore());
		dto.setGriglia(scacchiera);
	}
	
	private void resetPosizione(Pezzo pezzo, Integer posX, Integer posY, Pezzo[][] scacchiera) {
		scacchiera[posX][posY] = pezzo;
	}


	public boolean isScaccoMatto(Pezzo re, Pezzo[][] scacchiera) throws Exception {

		try {
			if (puoInterporreTraReEMinaccia(re, scacchiera)) {
				return false;
			}
			if (negaScaccoMuovendoIlRe(re, scacchiera)) {
				return false; 
			}
			if (negaScaccoMangiandoMinaccia(re, scacchiera)) {
				return false;
			}
	
	
	
			return true;
		}catch(Exception e) {
			throw new Exception();
		}
	}

}
