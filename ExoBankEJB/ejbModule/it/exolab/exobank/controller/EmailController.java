package it.exolab.exobank.controller;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.costanti.CostantiEmail;
import it.exolab.exobank.crud.EmailCrud;
import it.exolab.exobank.ejbinterface.EmailControllerInterface;
import it.exolab.exobank.mapper.EmailMapper;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Email;
import it.exolab.exobank.models.StatoEmail;
import it.exolab.exobank.models.Utente;
import it.exolab.exobank.sendemail.SendEmail;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class EmailController
 */
@Singleton (name = "EmailControllerInterface")
@LocalBean
@Startup
public class EmailController implements EmailControllerInterface {

	
	/*
	 * 
     * @Hello - Se sostituisci @Singleton con @Stateless perderai anche @Startup poiché è disponibile solo sui bean @Singleton. 
     * Ciò potrebbe importare o meno per te, anche se non avrà alcuna influenza sul metodo @Schedule poiché verrà visto e "impostato" al momento 
     * della distribuzione dell'applicazione.
     * 
     * Se utilizzi @Stateless, l'istanza del bean che esegue il metodo annotato sarà completamente autonoma e non potrà essere referenziata dal 
     * resto della tua applicazione. Il contenitore creerà il timer al momento della distribuzione e richiamerà la richiamata in base alla 
     * pianificazione. Se provi a iniettare un'istanza di questo bean, ne otterrai un'altra copia (senza una pianificazione di esecuzione).
     * Se usi un bean @Singleton puoi iniettarlo e modificarne lo stato, eventualmente influenzando il comportamento della richiamata del timer.
     * 
     * persistent
     * true: il timer è persistente, che sopravviverà ai riavvii del server. Il timer verra recuparato nel momento in cui il server viene riavviato
     * false: il timer è non persistente, il che significa che verrà rimosso se il server viene arrestato e riavviato.
     * 
	 * @Resource 
	 * VIENE UTILIZZATA X INIETTARE IL SERVIZIO TIMER SERVICE ALL'INTERNO DELLA CLASSE EJB. TIME SERVICE E FORNITO DALL'EJB CONTARINER PER LA
	 * GESTIONE DEL TIMER
	 * @PostCostruct
	 * ANNOTAZIONE UTILIZZATA PER INVOCARE IL METODO INIT DOPO CHE L'ISTANZA EJB E STATA CREATA E PRIMA CHE VENGA MESSA IN SERVIZIO
	 * @TIME OUT 
	 * E UN ANNOTAZIONE CHE VIENE MESSA SOPRA UN METODO E CHE VIENE INVOCATO ALLO SCADERE DEL TIMER
	 * 
	 * TIMER CONFIG E UN OGGETTO CHE VIENE UTILIZZATO PER CONFIGURARE IL TIMER
	 * TIMER E UN OGGETTO CHE RAPPRESENTA UN ATTIVITA PIANIFICATA E VIENE CREATO UTILIZZANDO TIMER SERVICE CHE PRENDE DUE PARAMENTRI IN INPUT
	 * TIMER CONFI OVVERO LA CONFIGURAZIONE DEL TIMER E UN OGGETTO SE SCHEDULE EXPRESSION UILIZZATO X DEF L'INTERVALLO DI TEMPO DEL TIMER. 
	 * L'OGGETTO TIMER E L'OGGETTO CHE  VIENE PASSATO AL TIMEOUT E CHE DEFINISCE L'INTERVALLO DI TEMPO IN CUI VA INVOCATO IL METODO CON L'ANNOTAZIONE TIMEOUT
	 * 
	 * SCHEDULE EXPRESSION E UNA CLASSE UTILIZZATA X DEFINIRE LA PIANIFICAZIONE DEI TIMER NEI SERVIZI EJB
	 */
	
	
	
	@Resource
	private TimerService timerService;	
	private Timer timer;


	@PostConstruct
	private void init() {
		TimerConfig timerConfig = new TimerConfig(null, false); // info e persist
		//schedularExpression per definire la pianificazione del timer
		ScheduleExpression se = new ScheduleExpression().second("0").minute("0").hour("*");
		timer = timerService.createCalendarTimer(se, timerConfig);
		System.out.println("IL TIMER ---- " + timer.getTimeRemaining() +"--------");
	}

	//@Schedule(configurazione timer)     --questo al posto di timeout mi risparmia l'init per la configurazione del timer
	@Timeout
	public void scheduledTimeout(Timer timer) throws Exception {
		try {
			EmailCrud crud = new EmailCrud();
			SqlMapFactory factory = SqlMapFactory.instance();
			SqlSession session = factory.openSession();
			EmailMapper mapper = session.getMapper(EmailMapper.class);
			List<Email> listaEmailDaInviare = null;
			listaEmailDaInviare = crud.listaEmailDaReinviare(mapper);
			for (Email e : listaEmailDaInviare) {
				new SendEmail().sendEmail(e.getDestinatario(), e.getOggettoEmail(), e.getTestoEmail());
				StatoEmail stato = new StatoEmail();
				stato.setId(CostantiEmail.EMAIL_INVIATA);
				e.setStatoEmail(stato);
				crud.updateEmail(mapper, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore metodo scheduledTimeout");
			throw new Exception (null != e.getMessage() ? e.getMessage() : "Errore Scheduled");
		}
	}

	@PreDestroy
	private void shutDown() {
		timer.cancel();
	}
	
	public Email insertAndSendEmail(Utente utente,Integer tipo) throws Exception {
		return insertAndSendEmail(utente, utente.getContoCorrente(),tipo);
	}
			
         
	@Override
	public Email insertAndSendEmail(Utente utente, ContoCorrente conto, Integer tipo) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Email email = null;
		try {
			if(null != utente && null != tipo && null != utente.getEmail()) {
				SqlSession session = factory.openSession();
				EmailCrud crud = new EmailCrud();
				EmailMapper mapper = session.getMapper(EmailMapper.class);
				if(CostantiEmail.EMAIL_ID_REGISTRAZIONE == tipo) {
					email = buildEmail(utente.getEmail(), CostantiEmail.EMAIL_OGGETTO_BENVENUTO, new SendEmail().emailBenvenuto(utente), utente);
				}
				else if(CostantiEmail.EMAIL_ID_APERTURA_CONTO == tipo) {
					email = buildEmail(utente.getEmail(), CostantiEmail.EMAIL_OGGETTO_APERTURA_CONTO, new SendEmail().emailRichiestaAperturaConto(utente), utente);
				}
				else if(CostantiEmail.EMAIL_ID_AGGIORNAMENTO_CONTO == tipo ) {
					email = buildEmail(utente.getEmail(), CostantiEmail.EMAIL_OGGETTO_AGGIORNAMENTO_STATO_CONTO, new SendEmail().emailStatoConto(conto), utente);
				}
				email = crud.insertEmail(mapper,email);
				if(Objects.nonNull(email)) {
					factory.commitSession();
					new SendEmail().sendEmail(email.getDestinatario(), email.getOggettoEmail(), email.getTestoEmail());
					StatoEmail stato = new StatoEmail();
					stato.setId(CostantiEmail.EMAIL_INVIATA);
					email.setStatoEmail(stato);
					crud.updateEmail(mapper,email);
				}
			}
			return email;
		}catch(Exception e) {
			e.printStackTrace();			
			factory.rollbackSession();
			System.out.println("ERRORE CONTROLLER EMAIL ----INSERT------");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}finally {
			factory.closeSession();
		}
	}
	
	
	private Email buildEmail(String email,String oggettoEmail,String testoEmail,Utente utente) {
		Email emailDaInserire = new Email();
		StatoEmail statoEmail = new StatoEmail();
		statoEmail.setId(CostantiEmail.EMAIL_NON_INVIATA);
		emailDaInserire.setDestinatario(email);
		emailDaInserire.setOggettoEmail(oggettoEmail);
		emailDaInserire.setTestoEmail(testoEmail);
		emailDaInserire.setUtente(utente);
		emailDaInserire.setStatoEmail(statoEmail);		
		return emailDaInserire;
	}
    
    
    
    
    

}
