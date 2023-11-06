package it.exolab.scacchiera.timer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Timer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1870060318924075186L;
	
	private Calendar tempo = Calendar.getInstance();
	
	public Date creazioneTimer(int secondi, int minuti, Date tempoGiocatore) throws Exception {
		try {
			tempo.set(Calendar.HOUR_OF_DAY, 0);
			tempo.set(Calendar.MINUTE, minuti);
			tempo.set(Calendar.SECOND, secondi);
			tempoGiocatore = new Date();
			tempoGiocatore = tempo.getTime();
			return tempoGiocatore;
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("errore nella creazione del timer");
		}
	}
}
