package it.exolab.exobank.dto;

import java.io.Serializable;


public class Dto<Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8580207070851718529L;
	private boolean success;
	private String errore;
	private Object data;

//	public DTO<Object> setDTO(DTO<Object> dto,Object oggetto) {
//		dto.setData(oggetto);   
//		dto.setSuccess(true);         NON NECESSARIO IN QUANTO CE GIA IL SETDATA
//		return dto;  
//	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
		success = false;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
		this.success = true;
	}

}
