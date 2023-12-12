package it.exolab.exobank.crud;

import java.lang.reflect.Method;
import java.util.ArrayList;

import it.exolab.exobank.models.Utente;

	public class MyClass {
	    public void myMethod(String message) {
	        System.out.println("Message: " + message);
	    }
	

	
	    public static void main(String[] args) {
	     ArrayList<Utente> listaUtente = new ArrayList<Utente>();
	     Utente utente = new Utente();
	     utente.setId(1);
	     listaUtente.add(null);
	     listaUtente.add(utente);
	     for(Utente u : listaUtente) {
	    	 System.out.println(u);
	     }
	    
	}
}
