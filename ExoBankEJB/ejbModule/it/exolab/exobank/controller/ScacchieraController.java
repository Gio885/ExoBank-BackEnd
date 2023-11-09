package it.exolab.exobank.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.chess.dto.ParametriValidatoreDto;
import it.exolab.exobank.chess.model.Colore;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.chess.model.Tipo;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.ejbinterface.ScacchieraControllerInterface;
import it.exolab.exobank.validatore.ValidaMosseScacchi;
import it.exolab.exobank.validatore.ValidatoreScaccoAlRe;
import it.exolab.scacchiera.ex.MossaNonConsentita;
import it.exolab.scacchiera.ex.Scacco;
import it.exolab.scacchiera.ex.ScaccoMatto;

@Stateless(name = "ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface {

	private Scacchiera scacchieraLavoro = new Scacchiera();
	private List<Pezzo> pezziMangiati = new ArrayList<Pezzo>();
	private boolean reSottoScacco = false;

	@Override
	public Scacchiera scacchieraIniziale() throws Exception {
		try {
			Scacchiera scacchieraIniziale = creazioneScacchieraIniziale();
			scacchieraLavoro.setScacchiera(scacchieraIniziale.getGriglia());
			return scacchieraIniziale;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Costanti.CONTATTA_ASSISTENZA);
		}
	}

	@Override
	public Scacchiera mossaConsentita(Pezzo pezzo) throws Exception {
		Pezzo[][] griglia = scacchieraLavoro.getGriglia();
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		ValidaMosseScacchi validatoreScacchi = new ValidaMosseScacchi();

		try {
			Pezzo pezzoSpecifico = findPezzoById(pezzo, griglia);
			if (null == pezzoSpecifico) {
				throw new Exception(Costanti.NESSUN_PEZZO_TROVATO);
			}

			ParametriValidatoreDto parametri = new ParametriValidatoreDto(pezzoSpecifico, pezzoSpecifico.getPosizioneX(), pezzoSpecifico.getPosizioneY(), pezzo.getPosizioneX(), pezzo.getPosizioneY(), pezzo.getColore(), griglia);
			Colore coloreAlleato = pezzoSpecifico.getColore();
			Colore coloreNemico = Colore.BIANCO.equals(pezzoSpecifico.getColore()) ? Colore.NERO : Colore.BIANCO;
			if (validaScacco.isScacco(trovaRe(coloreAlleato, griglia), griglia)) {
				reSottoScacco = true;
				eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);
			} else {
				eseguiMossa(parametri, scacchieraLavoro, validatoreScacchi);
			}

			if (validaScacco.isScacco(trovaRe(coloreNemico, griglia), griglia)) {
				if (validaScacco.isScaccoMatto(trovaRe(coloreNemico, griglia), griglia)) {
					throw new ScaccoMatto(Costanti.SCACCO_MATTO + (Colore.BIANCO.equals(trovaRe(coloreNemico, griglia).getColore()) ? "Nero." : "Bianco."));
				}
				throw new Scacco("Scacco al RE " + (Colore.BIANCO.equals(pezzoSpecifico.getColore()) ? " Nero." : " Bianco."));
			}



			return scacchieraLavoro;
		} catch (MossaNonConsentita eM) {
			eM.printStackTrace();
			throw new MossaNonConsentita(Costanti.MOSSA_NON_CONSENTITA);
		} catch (Scacco s) {
			s.printStackTrace();
			throw new Scacco(s.getMessage() != null ? s.getMessage() :Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		}catch (ScaccoMatto sm) {
			sm.printStackTrace();
			throw new ScaccoMatto(sm.getMessage() != null ? sm.getMessage() :Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() != null ? e.getMessage() : Costanti.CONTATTA_ASSISTENZA);
		}
	}

	@Override
	public Scacchiera aggiornamentoTipoPedone(Pezzo pezzo) throws Exception {
		Pezzo[][] griglia = scacchieraLavoro.getGriglia();
		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		Colore coloreNemico = Colore.BIANCO.equals(pezzo.getColore()) ? Colore.NERO : Colore.BIANCO;
		try {
			Pezzo pezzoSpecifico = findPezzoById(pezzo, griglia);
			if (null == pezzoSpecifico) {
				throw new Exception(Costanti.NESSUN_PEZZO_TROVATO);
			}
			pezzoSpecifico.setTipo(pezzo.getTipo());
			if (validaScacco.isScacco(trovaRe(coloreNemico, griglia), griglia)) {
				if (validaScacco.isScaccoMatto(trovaRe(coloreNemico, griglia), griglia)) {
					throw new ScaccoMatto(Costanti.SCACCO_MATTO + (Colore.BIANCO.equals(trovaRe(coloreNemico, griglia).getColore()) ? "Nero." : "Bianco."));
				}
				throw new Scacco("Scacco al RE " + (Colore.BIANCO.equals(pezzoSpecifico.getColore()) ? " Nero." : " Bianco."));
			}
			return scacchieraLavoro;
		} catch (Scacco s) {
			s.printStackTrace();
			throw new Scacco(s.getMessage() != null ? s.getMessage() :Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		}catch (ScaccoMatto sm) {
			sm.printStackTrace();
			throw new ScaccoMatto(sm.getMessage() != null ? sm.getMessage() :Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() != null ? e.getMessage() : Costanti.CONTATTA_ASSISTENZA);
		}
	}

	@Override
	public List<Pezzo> listaPezziMangiati() throws Exception {
		return pezziMangiati;
	}

	@Override
	public boolean controlloPedoneUltimaPosizione(Pezzo pezzo) throws Exception {
		try {
			if (pezzo.getColore() == Colore.BIANCO && pezzo.getPosizioneX() == 7 && pezzo.getTipo() == Tipo.PEDONE) {
				return true;
			} else if (pezzo.getColore() == Colore.NERO && pezzo.getPosizioneX() == 0 && pezzo.getTipo() == Tipo.PEDONE) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Costanti.PEDONE_NON_ULTIMA_POS);
		}
	}

	private Scacchiera creazioneScacchieraIniziale() throws Exception {
		try {
			Pezzo[][] griglia = new Pezzo[Costanti.RIGHE][Costanti.COLONNE];
			Tipo[] primaRigaEUltima = {Tipo.TORRE, Tipo.CAVALLO, Tipo.ALFIERE, Tipo.RE, Tipo.REGINA, Tipo.ALFIERE, Tipo.CAVALLO, Tipo.TORRE};
			Tipo[] pedoni = {Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE};
			int idPezzo = 1;

			for (int index = 0; index < Costanti.RIGHE; index++) {
				for (int j_index = 0; j_index < Costanti.COLONNE; j_index++) {
					Tipo tipoCorrente = null;

					if (index == 0 || index == 7) {
						tipoCorrente = primaRigaEUltima[j_index];
					} else if (index == 1 || index == 6) {
						tipoCorrente = pedoni[j_index];
					}

					if (null != tipoCorrente) {
						griglia[index][j_index] = new Pezzo(idPezzo++, tipoCorrente, (index < 2) ? Colore.BIANCO : Colore.NERO, index, j_index, true, false);
					} else {
						griglia[index][j_index] = null;
					}
				}
			}

			pezziMangiati.clear();
			Scacchiera scacchiera = new Scacchiera();
			scacchiera.setScacchiera(griglia);
			return scacchiera;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Costanti.ERRORE_SCACCHIERA_INIZIALE);
		}
	}

	private Pezzo findPezzoById(Pezzo pezzo, Pezzo[][] griglia) throws Exception {
		try {
			int idPezzo = pezzo.getId();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (null != griglia[x][y] && griglia[x][y].getId() == idPezzo) {
						return griglia[x][y];
					}
				}
			}
			throw new Exception(Costanti.NESSUN_PEZZO_TROVATO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Costanti.NESSUN_PEZZO_TROVATO);
		}
	}

	private Pezzo trovaRe(Colore colore, Pezzo[][] griglia) {
		Pezzo reStessoColore = new Pezzo();

		for (int x = 0; x < griglia.length; x++) {
			for (int y = 0; y < griglia[x].length; y++) {
				Pezzo pezzoCorrente = griglia[x][y];
				if (null != pezzoCorrente && pezzoCorrente.getTipo() == Tipo.RE && pezzoCorrente.getColore() == colore) {
					return reStessoColore = pezzoCorrente;
				}
			}
		}
		return reStessoColore;
	}

	private void eseguiMossa(ParametriValidatoreDto parametri, Scacchiera scacchieraLavoro, ValidaMosseScacchi validatoreScacchi) throws Exception {
		// Utilizza il validatoreScacchi per verificare la validità della mossa
		if (validatoreScacchi.mossaConsentitaPerPezzo(parametri)) {
			if (null != parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]) {
				pezziMangiati.add(parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()]);
			}
			if (reSottoScacco && !mossaRimuoveScacco(parametri)) {
				throw new Exception("Mossa non valida: devi rimuovere lo scacco.");
			}

			// Controlla se la mossa elimina lo stato di scacco
			if (!isScaccoDopoMossa(parametri)) {
				reSottoScacco = false;
				if(Tipo.RE.equals(parametri.getPezzo().getTipo())) {
					int spostamentoRe = parametri.getyDestinazione() - parametri.getyPartenza();
					if(!parametri.getPezzo().isSpostato() && Colore.BIANCO.equals(parametri.getPezzo().getColore())) {
						if(parametri.getColore().equals(parametri.getGriglia()[0][0].getColore()) && spostamentoRe < 0 && !parametri.getGriglia()[0][0].isSpostato() && null == parametri.getGriglia()[0][1] && null == parametri.getGriglia()[0][2]) {
							parametri.getGriglia()[0][0].setPosizioneY(2);
							parametri.getGriglia()[0][0].setSpostato(true);
							parametri.getGriglia()[0][2] = parametri.getGriglia()[0][0]; // Sposta il pezzo nella nuova posizione
							parametri.getGriglia()[0][0] = null; // Rimuovi il pezzo dalla posizione precedente
						}

						if(parametri.getColore().equals(parametri.getGriglia()[0][7].getColore()) && spostamentoRe > 0 && !parametri.getGriglia()[0][7].isSpostato() && null == parametri.getGriglia()[0][4] && null == parametri.getGriglia()[0][5] && null == parametri.getGriglia()[0][6]) {
							parametri.getGriglia()[0][7].setPosizioneY(4);
							parametri.getGriglia()[0][7].setSpostato(true);
							parametri.getGriglia()[0][4] = parametri.getGriglia()[0][7]; // Sposta il pezzo nella nuova posizione
							parametri.getGriglia()[0][7] = null; // Rimuovi il pezzo dalla posizione precedente
						}
					}else if(!parametri.getPezzo().isSpostato() && Colore.NERO.equals(parametri.getPezzo().getColore())){
						if(parametri.getColore().equals(parametri.getGriglia()[7][0].getColore()) && spostamentoRe < 0 && !parametri.getGriglia()[7][0].isSpostato() && null == parametri.getGriglia()[7][1] && null == parametri.getGriglia()[7][2]) {
							parametri.getGriglia()[7][0].setPosizioneY(2);
							parametri.getGriglia()[7][0].setSpostato(true);
							parametri.getGriglia()[7][2] = parametri.getGriglia()[0][0]; // Sposta il pezzo nella nuova posizione
							parametri.getGriglia()[7][0] = null; // Rimuovi il pezzo dalla posizione precedente
						}

						if(parametri.getColore().equals(parametri.getGriglia()[7][7].getColore()) && spostamentoRe > 0 && !parametri.getGriglia()[7][7].isSpostato() && null == parametri.getGriglia()[7][4] && null == parametri.getGriglia()[7][5] && null == parametri.getGriglia()[7][6]) {
							parametri.getGriglia()[7][7].setPosizioneY(4);
							parametri.getGriglia()[7][7].setSpostato(true);
							parametri.getGriglia()[7][4] = parametri.getGriglia()[7][7]; // Sposta il pezzo nella nuova posizione
							parametri.getGriglia()[7][7] = null; // Rimuovi il pezzo dalla posizione precedente
						}
					}
				}
				parametri.getPezzo().setSpostato(true);
				parametri.getPezzo().setPosizioneX(parametri.getxDestinazione());
				parametri.getPezzo().setPosizioneY(parametri.getyDestinazione());
				aggiornaScacchiera(parametri);
				scacchieraLavoro.setScacchiera(parametri.getGriglia());
			} else {
				throw new MossaNonConsentita(Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
			}
		} else {
			throw new MossaNonConsentita(Costanti.MOSSA_NON_CONSENTITA);
		}
	}

	private boolean isScaccoDopoMossa(ParametriValidatoreDto parametri) throws Exception {
		// Crea una copia temporanea della scacchiera ed esegui la mossa
		Scacchiera scacchiera = new Scacchiera();
		Pezzo[][] grigliaOriginale = parametri.getGriglia();
		Pezzo[][] grigliaCopia = new Pezzo[Costanti.RIGHE][Costanti.COLONNE];

		grigliaCopia = creaCopiaScacchiera(grigliaOriginale);

		scacchiera.setScacchiera(grigliaCopia);

		// Esegui la mossa sulla copia della griglia
		Pezzo appoggio = new Pezzo();
		appoggio.setColore(parametri.getPezzo().getColore());
		appoggio.setEsiste(parametri.getPezzo().isEsiste());
		appoggio.setId(parametri.getPezzo().getId());
		appoggio.setPosizioneX(parametri.getPezzo().getPosizioneX());
		appoggio.setPosizioneY(parametri.getPezzo().getPosizioneY());
		appoggio.setTipo(parametri.getPezzo().getTipo());
		appoggio.setSpostato(parametri.getPezzo().isSpostato());
		appoggio.setPosizioneX(parametri.getxDestinazione());
		appoggio.setPosizioneY(parametri.getyDestinazione());
		grigliaCopia[parametri.getxDestinazione()][parametri.getyDestinazione()] = appoggio;
		grigliaCopia[parametri.getxPartenza()][parametri.getyPartenza()] = null;

		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();

		// Controlla se il re è in scacco dopo la mossa
		try {
			Pezzo re = trovaRe(appoggio.getColore(), grigliaCopia);
			return validaScacco.isScacco(re, grigliaCopia);
		} catch (MossaNonConsentita s) {
			s.printStackTrace();
			throw new MossaNonConsentita(Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		}
	}

	private boolean mossaRimuoveScacco(ParametriValidatoreDto parametri) throws Exception {
		Pezzo[][] grigliaCopia = creaCopiaScacchiera(parametri.getGriglia());
		Pezzo appoggio = new Pezzo();
		appoggio.setColore(parametri.getPezzo().getColore());
		appoggio.setEsiste(parametri.getPezzo().isEsiste());
		appoggio.setId(parametri.getPezzo().getId());
		appoggio.setPosizioneX(parametri.getPezzo().getPosizioneX());
		appoggio.setPosizioneY(parametri.getPezzo().getPosizioneY());
		appoggio.setTipo(parametri.getPezzo().getTipo());
		appoggio.setSpostato(parametri.getPezzo().isSpostato());
		appoggio.setPosizioneX(parametri.getxDestinazione());
		appoggio.setPosizioneY(parametri.getyDestinazione());
		grigliaCopia[parametri.getxDestinazione()][parametri.getyDestinazione()] = appoggio;
		grigliaCopia[parametri.getxPartenza()][parametri.getyPartenza()] = null;


		ValidatoreScaccoAlRe validaScacco = new ValidatoreScaccoAlRe();
		Pezzo re = trovaRe(appoggio.getColore(), grigliaCopia);

		if (!validaScacco.isScacco(re, grigliaCopia)) {
			return true;
		} else {
			throw new MossaNonConsentita(Costanti.ERRORE_STATO_SCACCO_NON_RIMOSSO);
		}
	}


	private Pezzo[][] creaCopiaScacchiera(Pezzo[][] grigliaOriginale) {
		Pezzo[][] grigliaCopia = new Pezzo[8][8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Pezzo pezzoOriginale = new Pezzo();
				pezzoOriginale = grigliaOriginale[x][y];
				if (null != pezzoOriginale && pezzoOriginale.isEsiste()) {
					grigliaCopia[x][y] = new Pezzo(pezzoOriginale.getId(), pezzoOriginale.getTipo(), pezzoOriginale.getColore(), x, y, true, pezzoOriginale.isSpostato());
				}
			}
		}

		return grigliaCopia;
	}

	private void aggiornaScacchiera(ParametriValidatoreDto parametri) {
		// Aggiorna la scacchiera con la nuova posizione
		parametri.getGriglia()[parametri.getxPartenza()][parametri.getyPartenza()] = null; // Rimuovi il pezzo dalla posizione precedente
		parametri.getGriglia()[parametri.getxDestinazione()][parametri.getyDestinazione()] = parametri.getPezzo(); // Sposta il pezzo nella nuova posizione
		parametri.getPezzo().setPosizioneX(parametri.getxDestinazione()); // Aggiorna le coordinate del pezzo
		parametri.getPezzo().setPosizioneY(parametri.getyDestinazione());
	}
}