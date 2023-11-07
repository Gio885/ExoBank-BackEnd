package it.exolab.scacchiera.timer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Timer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1870060318924075186L;
		
	public Date creazioneTimer(int ore, int minuti, int secondi, Calendar tempo) throws Exception {
		try {
			tempo.set(Calendar.HOUR_OF_DAY, ore);
			tempo.set(Calendar.MINUTE, minuti);
			tempo.set(Calendar.SECOND, secondi);
			return tempo.getTime();
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("errore nella creazione del timer");
		}
	}
}
