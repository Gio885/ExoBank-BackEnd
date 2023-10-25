package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import it.exolab.exobank.controller.TransazioneController;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;

@Named
@SessionScoped
public class TransazioneBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@EJB
	private TransazioneController ejb;
	private List<Transazione> listaTransazioni;
	
	@PostConstruct
	public void init() {
		try {
			Dto <List<Transazione>>transazioni = ejb.findAllTransazioni();
			listaTransazioni = transazioni.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Transazione> getListaTransazioni() {
		return listaTransazioni;
	}
	
	
	
}
