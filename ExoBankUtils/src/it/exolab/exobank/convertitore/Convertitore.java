package it.exolab.exobank.convertitore;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

public class Convertitore {
	
	
	
	//PAGINE RENDERIZZATE CON ALERT // FIND TRANSAZIONI UTENTE // VALIDATORE AVANTI E INDIETRO

	public Utente convertUtenteToDTO(Utente utente) {
		Utente utenteDTO = new Utente();
		utenteDTO.setId(utente.getId());
		utenteDTO.setNome(utente.getNome());
		utenteDTO.setCognome(utente.getCognome());
		utenteDTO.setEmail(utente.getEmail());
		utenteDTO.setRuolo(utente.getRuolo());
		utenteDTO.setCodiceFiscale(utente.getCodiceFiscale());
		return utenteDTO;
	}

	public ContoCorrente convertContoToDTO(ContoCorrente contoUtente) {
		ContoCorrente contoDTO = new ContoCorrente();
		contoDTO.setId(contoUtente.getId());
		contoDTO.setNumeroConto(contoUtente.getNumeroConto());
		contoDTO.setSaldo(contoUtente.getSaldo());
		contoDTO.setStato(contoUtente.getStato());
		contoDTO.setUtente(convertUtenteToDTO(contoUtente.getUtente()));

		return contoDTO;
	}
	public Transazione convertTransazioneToDTO(Transazione transazione) {
		Transazione transazioneDTO = new Transazione();
		transazioneDTO.setId(transazione.getId());
		transazioneDTO.setConto(convertContoToDTO(transazione.getConto()));
		transazioneDTO.setDataTransazione(transazione.getDataTransazione());
		transazioneDTO.setImporto(transazione.getImporto());
		transazioneDTO.setStatoTransazione(transazione.getStatoTransazione());
		transazioneDTO.setTipo(transazione.getTipo());
		if(null != transazione.getContoBeneficiario()) {
			transazioneDTO.setContoBeneficiario(convertContoToDTO(transazione.getContoBeneficiario()));
		}
		if(null != transazione.getContoBeneficiarioEsterno()) {
			transazioneDTO.setContoBeneficiarioEsterno(transazione.getContoBeneficiarioEsterno());
		}
		return transazioneDTO;
	}

	
	public List<ContoCorrente> convertListaContoToDTO(List<ContoCorrente> listaConti){
		List<ContoCorrente> listaDTO = new ArrayList<ContoCorrente>();
		for(ContoCorrente conto : listaConti) {
			ContoCorrente contoDaAggiungere = convertContoToDTO(conto);
			listaDTO.add(contoDaAggiungere);
		}
		return listaDTO;
	}
	
	public List <Transazione> convertListaTransazioneToDTO(List<Transazione> listaTransazioni){
		List<Transazione> listaDTO = new ArrayList<Transazione>();
		for(Transazione transazione : listaTransazioni) {
			Transazione transazioneDaAggiungere = convertTransazioneToDTO(transazione);
			listaDTO.add(transazioneDaAggiungere);
		}
		return listaDTO;
	}
	

}
