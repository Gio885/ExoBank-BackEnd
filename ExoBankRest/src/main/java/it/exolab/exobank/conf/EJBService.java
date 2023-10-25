package it.exolab.exobank.conf;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBService<T> {

	public T getRemote(String jndi) throws NamingException {
		Properties props = new Properties();
        InitialContext ctx;
        T ret=null;
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
     
        ctx = new InitialContext(props);
           

        ret = (T) ctx.lookup(jndi);
             
                 
        return ret;
        
		
	}

	/*java:global/ExoBankEAR/ExoBankEJB/UtenteControllerInterface!it.exolab.exobank.controller.UtenteController
	 * java:global/ExoBankEAR/ExoBankEJB/UtenteControllerInterface
	 */
	public T getServizioUtenteControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/UtenteController!it.exolab.exobank.ejbinterface.UtenteControllerInterface");				
	}					  
	
	public T getServizioRuoloControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/RuoloController!it.exolab.exobank.ejbinterface.RuoloControllerInterface");				
	}
	
	public T getServizioContoCorrenteControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/ContoCorrenteController!it.exolab.exobank.ejbinterface.ContoCorrenteControllerInterface");				
	}
	
	public T getServizioTransazioneControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/TransazioneController!it.exolab.exobank.ejbinterface.TransazioneControllerInterface");				
	}	
	public T getServizioStatoTransazioneControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/StatoTransazioneController!it.exolab.exobank.ejbinterface.StatoTransazioneControllerInterface");				
	}	
	public T getServizioStatoContoCorrenteControllerInterface() throws NamingException {
		return getRemote("java:global/ExoBankEAR/ExoBankEJB/StatoContoCorrenteController!it.exolab.exobank.ejbinterface.StatoContoCorrenteControllerInterface");				
	}
	
	
	
}

