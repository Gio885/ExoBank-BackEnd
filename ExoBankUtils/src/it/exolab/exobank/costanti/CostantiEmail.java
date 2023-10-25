package it.exolab.exobank.costanti;


public class CostantiEmail {

	public static final int EMAIL_ID_REGISTRAZIONE = 1;
	public static final int EMAIL_ID_APERTURA_CONTO = 2;
	public static final int EMAIL_ID_AGGIORNAMENTO_CONTO = 3;
	
	public static final int EMAIL_INVIATA = 1;
	public static final int EMAIL_NON_INVIATA = 2;
	
	public static final String EMAIL_OGGETTO_BENVENUTO = "Benvenuto in ExoBank";
    public static final String EMAIL_TESTO_BENVENUTO = "Benvenuto in ExoBank, %s %s,\nla tua registrazione è andata a buon fine, ora puoi richiedere nell' app l'apertura di un conto per effettuare delle operazioni";
    public static final String EMAIL_OGGETTO_APERTURA_CONTO = "Richiesta apertura conto ExoBank";
    public static final String EMAIL_TESTO_APERTURA = "Complimenti %s %s,\nla richiesta per l'apertura del conto è andata a buon fine, non appena sarà abilitato ti verrà inviata un email con la conferma di attivazione";
    public static final String EMAIL_OGGETTO_AGGIORNAMENTO_STATO_CONTO = "Aggiornamento stato conto Exobank";
    public static final String EMAIL_TESTO_AGGIORNAMENTO_STATO_CONTO = "Ciao %s %s,\n\nvolevamo avvisarti che il tuo conto Exobank è in stato: %s";

}
