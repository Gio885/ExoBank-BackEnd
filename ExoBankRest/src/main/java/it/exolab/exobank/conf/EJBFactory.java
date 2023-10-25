package it.exolab.exobank.conf;

import javax.naming.InitialContext;

public class EJBFactory<T> {
	
	private final static String PREFIX = "java:global/ExoBankEAR/ExoBankEJB/"; // percorso e nome del progetto con le EJB

	private Class<T> interfaceClass;
	
	// Costruttore parametrico
	public EJBFactory(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	//return getRemote("java:global/ExoBankEAR/ExoBankEJB/UtenteController!it.exolab.exobank.ejbinterface.UtenteControllerInterface");				

	@SuppressWarnings("unchecked")
	public T getEJB() throws Exception {
		InitialContext context = new InitialContext(); //punto iniziale per la risoluzione del naming
		return (T) context.lookup(PREFIX + interfaceClass.getSimpleName() + "!" + interfaceClass.getName());
	}					//restituisce il nome della classe che implementa interfaccia // restituisce nome interfaccia
}