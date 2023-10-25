package it.exolab.exobank.sendemail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.costanti.CostantiEmail;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Utente;

public class SendEmail {

	
	//DEFINITE NELLE COSTANTI
	//private static final String USERNAME = "tuoindirizzo@gmail.com";  Sostituisci con il tuo indirizzo Gmail
	//private static final String PASSWORD = "tua_password";  Sostituisci con la tua password Gmail
	
	/*
	 * API JavaMail, che è una libreria Java utilizzata per l'invio e la ricezione di e-mail tramite protocolli standard come SMTP, IMAP e POP3. 
	 * SONO protocolli standard utilizzati nella gestione delle email e nella comunicazione tra client di posta elettronica e server di posta elettronica. 
	 * SMTP (Simple Mail Transfer Protocol):
	   -----SMTP è un protocollo utilizzato per l'invio di email da un client di posta elettronica (mittente) a un server di posta elettronica (destinatario)
	   o tra server di posta elettronica.Il client SMTP è responsabile dell'invio dell'email al server SMTP, che quindi instrada l'email al server di 
	   posta elettronica del destinatario o lo consegna direttamente al destinatario se si trova sullo stesso server.
       LE email inviate raggiungano il destinatario corretto, o restituiscano un messaggio di errore in caso di problemi.
       -----IMAP è un protocollo utilizzato per recuperare le email da un server di posta elettronica remoto e sincronizzare la casella di posta elettronica
       tra il client e il server.Con IMAP, le email rimangono sul server di posta elettronica, consentendo ai client di accedere alle email da più dispositivi.
       -----POP3 è un protocollo utilizzato per recuperare le email da un server di posta elettronica remoto e scaricarle sul client.
	   A differenza di IMAP, con POP3 le email vengono solitamente scaricate sul client e rimosse dal server. 
	   	
 * Gli oggetti della libreria JavaMail (javax.mail) agiscono come interfacce verso i protocolli di posta elettronica, 
 * consentendo alle applicazioni Java di comunicare con i server di posta elettronica per l'invio e la ricezione di email. 
 * Per comprendere meglio il meccanismo dietro questi oggetti, vediamo come funziona il processo generale di invio di una email:

1. *Session*: La classe `javax.mail.Session` rappresenta un'istanza di sessione di posta elettronica. 
Puoi creare una sessione specificando le proprietà come l'host del server SMTP, la porta, le credenziali, la sicurezza, ecc. 
La sessione è l'oggetto principale per inizializzare la comunicazione con il server di posta.

2. *Message*: La classe `javax.mail.Message` rappresenta il messaggio di posta elettronica da inviare. 
Puoi creare un oggetto `Message` per impostare i dettagli del messaggio, come il mittente, i destinatari, l'oggetto, 
il testo e gli allegati.

3. *Transport*: La classe `javax.mail.Transport` gestisce l'invio effettivo del messaggio di posta. 
Puoi ottenere un oggetto `Transport` dalla sessione, collegarlo al server SMTP e quindi utilizzarlo per inviare il messaggio.

4. *Store*: Per la ricezione di email, la classe `javax.mail.Store` gestisce la connessione a un server di posta utilizzando i 
protocolli come IMAP o POP3. Puoi ottenere un oggetto `Store` dalla sessione e utilizzarlo per accedere alla tua casella di posta elettronica e recuperare i messaggi in arrivo.

5. *Folder e Message (ricezione)*: Una volta connesso al server di posta, puoi utilizzare gli oggetti `Folder` per accedere 
alle cartelle della tua casella di posta e gli oggetti `Message` per leggere, scrivere o gestire i messaggi.

Il meccanismo generale coinvolge la creazione di una sessione per inizializzare la comunicazione con il server di posta, 
la creazione di oggetti `Message` per la composizione del messaggio e l'uso di `Transport` per l'invio. 
Per la ricezione, utilizzi un oggetto `Store` per connetterti al server, gli oggetti `Folder` per accedere alle cartelle e gli oggetti 
`Message` per leggere i messaggi.

Gli oggetti JavaMail astraggono gran parte dei dettagli complessi dei protocolli di posta e forniscono un'interfaccia Java per 
semplificare l'invio e la ricezione di email all'interno delle applicazioni Java.	
 */
    public void sendEmail(String destinatario, String oggetto, String testoMessaggio) {
    	
    	
    	//Configuri il client JavaMail (MITTENTE) con le proprietà SMTP corrette (props).
    	//Imposti l'indirizzo email del mittente nell'oggetto Message.
    	//Queste proprietà sono utilizzate per configurare la connessione al server SMTP, che è responsabile per l'invio delle email. 
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");     //mail.smtp.auth: Questo valore è impostato su "true" per indicare che l'autenticazione SMTP è abilitata. Gmail richiede l'autenticazione per inviare email attraverso il suo server SMTP. Quando impostato su "true", il client JavaMail invierà le credenziali dell'account Gmail per autenticarsi con il server SMTP.
        props.put("mail.smtp.starttls.enable", "true"); //mail.smtp.starttls.enable: StartTLS è un protocollo che consente di avviare una connessione sicura (TLS o SSL) durante una connessione SMTP iniziale. Gmail richiede che questa opzione sia abilitata. Impostando questo valore su "true", la connessione SMTP iniziale verrà aggiornata a una connessione sicura tramite TLS.
        props.put("mail.smtp.host", "out.virgilio.it"); //mail.smtp.host: Questo valore specifica l'hostname del server SMTP di Gmail. Per l'invio di email tramite Gmail, l'hostname è "smtp.gmail.com". Il client JavaMail utilizzerà questo hostname per connettersi al server SMTP di Gmail.
        props.put("mail.smtp.port", "587"); //mail.smtp.port: Questo valore specifica la porta su cui il server SMTP di Gmail ascolta. Per Gmail, la porta è di solito "587". Quando il client JavaMail si connette al server SMTP di Gmail, utilizzerà questa porta.

        /*
    * Session: 
* Session è una classe fornita dall'API JavaMail che rappresenta una sessione di comunicazione tra il client di posta elettronica 
* (il tuo programma Java) e il server di posta elettronica (ad esempio, il server SMTP di Gmail). La sessione contiene informazioni sulla 
* configurazione e le impostazioni di comunicazione.

Session.getInstance(props, authenticator): Questo costruttore statico della classe Session viene utilizzato per creare un'istanza di Session. 
Prende due parametri:
props: Un oggetto Properties che contiene le proprietà di configurazione per la sessione. 
Queste proprietà sono utilizzate per configurare la connessione al server SMTP, come l'hostname del server, la porta e altre impostazioni di 
connessione.
authenticator: Un oggetto Authenticator personalizzato che viene utilizzato per l'autenticazione presso il server SMTP. 
L'autenticatore contiene un metodo getPasswordAuthentication() che restituisce le credenziali (indirizzo email e password) necessarie per 
l'autenticazione presso il server SMTP.
Authenticator: Authenticator è una classe fornita da JavaMail che consente di personalizzare il processo di autenticazione per l'invio delle email. 
In questo caso, stai estendendo la classe Authenticator in un oggetto anonimo e sovrascrivendo il metodo getPasswordAuthentication() per restituire 
le credenziali di autenticazione.
         */
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Costanti.EMAIL, Costanti.PASSWORD);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Costanti.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); //Oggetto messaggio che prende una session
            message.setSubject(oggetto);													//set email destinatario, set oggetto // set testo
            message.setText(testoMessaggio);
            Transport.send(message);														//transport per inviare il messaggio
            System.out.println("Email inviata con successo a " + destinatario);
        } catch (MessagingException e) {
        	throw new RuntimeException("Errore nell'invio dell'email: " + e.getMessage(), e);
        }
        
    }
    
    
  
    
    
    
    
    
    public String emailBenvenuto(Utente utente) {
    	return String.format(CostantiEmail.EMAIL_TESTO_BENVENUTO, utente.getNome(),utente.getCognome());
    }
	
    public String emailRichiestaAperturaConto(Utente utente) {
    	return String.format(CostantiEmail.EMAIL_TESTO_APERTURA, utente.getNome(),utente.getCognome());
    }
	
    public String emailStatoConto(ContoCorrente conto) {
    	return String.format(CostantiEmail.EMAIL_TESTO_AGGIORNAMENTO_STATO_CONTO, conto.getUtente().getNome(),conto.getUtente().getCognome(),conto.getStato().getStatoContoCorrente());	
    	
    }
	
	
}
