package it.exolab.exobank.costanti;

import it.exolab.exobank.chess.model.Tipo;

//import javax.ws.rs.core.Response.Status;

public class Costanti {
	
	
	
	public static final int RUOLO_CLIENTE = 2;
	public static final int RUOLO_AMMINISTRATORE = 1;
	public static final int STATO_SOSPESO = 3;
	public static final double SALDO_CONTO_INIZIALE = 0.0;
	public static final int TRANSAZIONE_APPROVATA = 1;
	public static final int TRANSAZIONE_NEGATA = 2;
	public static final int TRANSAZIONE_IN_CORSO = 3;
	public static final int CONTO_APPROVATO = 1;
	public static final int CONTO_CHIUSO = 2;
	public static final int CONTO_SOSPESO = 3;
	public static final int TIPO_DEPOSITO = 1;
	public static final int TIPO_PRELIEVO = 2;
	public static final int TIPO_BONIFICO= 3;
	public static final int TIPO_RICARICA= 4;
	public static final int TIPO_BOLLETTINO= 5;
	public static final String REGEX_EMAIL = "^(([^<>()[\\\\]\\\\.,;:\\s@\\\"]+(\\.[^<>()[\\\\]\\\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$";
	public static final String REGEX_NOME_COGNOME = "^[A-Za-z0-9]+(?:[ ]?[A-Za-z0-9]+)*$";
    public static final String EMAIL= "g.derenzi96@virgilio.it";
    public static final String PASSWORD= "Metelska73.";
    public static final String MIME_FORMATO_EXCEL= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIME_FORMATO_WORD= "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String CONTENT_DISPOSITION= "Content-Disposition";
    public static final String HEADERS_EXCEL_ALLEGATO_TRANSAZIONE_UTENTE= "attachment; filename=transazioni.xlsx";
    public static final String HEADERS_WORD_ALLEGATO_TRANSAZIONE_UTENTE= "attachment; filename=transazioni.docx";
    public static final String HEADERS_PDF_ALLEGATO_TRANSAZIONE_UTENTE= "attachment; filename=transazioni.pdf";
    public static final String MEDIA_TYPE_FILE_PDF= "application/pdf";
    public static final String ERRORE_CONTATTA_AMMINISTRATORE= "C'è stato un errore, contatta l'amministratore";
    public static final String ERRORE_OPERAZIONE="C'e stato un errore durante l'operazione, contatta l'amministratore";
    public static final String TRANSAZIONE_REST="/transazioneRest";
    public static final String FIND_TRANSAZIONI_UTENTE="/findTransazioniUtente";
    public static final String FIND_ALL_TRANSAZIONI="/findAllTransazioni";
    public static final String INSERT_TRANSAZIONE="/insertTransazione";
    public static final String UPDATE_TRANSAZIONE="/updateTransazione";
    public static final String DOWNLOAD_WORD= "/downloadWord";
    public static final String DOWNLOAD_EXCEL="/downloadExcel";
    public static final String DOWNLOAD_PDF= "/downloadPdf";
    public static final String TIPO_TRANSAZIONE_REST="/tipoTransazioneRest";
    public static final String FIND_TIPI_STATI_TRANSAZIONE="/findAllTipiStatiTransazione";
    public static final String STATO_TRANSAZIONE_REST="/statoTransazioneRest";
    public static final String FIND_STATI_TRANSAZIONE= "/findStatiTransazione";
    public static final String LISTA_STATI_CONTO="/listaStatoConto";
    public static final String STATO_CONTO_CORRENTE="/statoContoCorrenteRest";
    public static final String UPDATE_CONTO_CORRENTE="/updateConto";
    public static final String INSERT_CONTO="/insertConto";
    public static final String FIND_CONTO_BY_ID_UTENTE="/findContoByIdUtente";
    public static final String LISTA_CONTI="/listaConti";
    public static final String CONTO_CORRENTE_REST="/contoCorrenteRest";
    public static final String UTENTE_REST="/utenteRest";
    public static final String LOGIN_UTENTE="/loginUtente";
    public static final String INSERT_UTENTE="/insertUtente";
    public static final String TIPO_TRANSAZIONE="TIPO TRANSAZIONE";
    public static final String DATA_TRANSAZIONE="DATA TRANSAZIONE";
    public static final String IMPORTO_TRANSAZIONE="IMPORTO TRANSAZIONE";
    public static final String CONTO_BENEFICIARIO="CONTO BENEFICIARIO";
    public static final String NOME_TITOLARE="NOME TITOLARE";
    public static final String COGNOME_TITOLARE="COGNOME TITOLARE";
    public static final String NUMERO_CONTO="NUMERO CONTO";
    public static final String FOOTER_PDF = "ExoBank SpA Sede Sociale: Via Alessandro Specchi 16-00186 Roma"
    		+ " Capitale Sociale € 19.647.948,10 Iscrizione al Registro delle Imprese di"
    		+ " Roma, Codice Fiscale e P. IVA no 00348170101 ";
    public static final String RGB_VERDE= "057A1E";
    public static final String RGB_ROSSO= "FF0000";
    public static final String RGB_GRIGIO="E0E0E0";
    public static final String RGB_GIALLO="FFFF00";
    public static final String RGB_NERO = "000000";


    //SCACCHI
    public static final int RIGHE = 8;
    public static final int COLONNE = 8;
    public static final int COMPARATORE_STRINGA_UGUALE = 0;
    
 // Matrice costante per la disposizione iniziale dei pezzi
    public static final Tipo[][] SCACCHIERA_INIZIALE = {
        {Tipo.TORRE, Tipo.CAVALLO, Tipo.ALFIERE, Tipo.RE, Tipo.REGINA, Tipo.ALFIERE, Tipo.CAVALLO, Tipo.TORRE},
        {Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE, Tipo.PEDONE},
        {Tipo.TORRE, Tipo.CAVALLO, Tipo.ALFIERE, Tipo.RE, Tipo.REGINA, Tipo.ALFIERE, Tipo.CAVALLO, Tipo.TORRE}
    };
    
}
