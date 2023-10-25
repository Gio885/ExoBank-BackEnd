package it.exolab.exobank.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class ScacchieraBean implements Serializable {
	
	
	
	private boolean gioca;

	public boolean isGioca() {
		return gioca;
	}

	public void setGioca(boolean gioca) {
		this.gioca = gioca;
	}
	
	
	
	
	
}
