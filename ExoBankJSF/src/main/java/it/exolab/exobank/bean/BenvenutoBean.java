package it.exolab.exobank.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/*
 * @MANAGED BEAN DEPRECATA UTILIZZATA PER VERSIONI JSF INFERIORI ALLA 2.0, PER DI PIU LA SPECIFICA CDI INCLUDE L'ANNOTAZIONE @NAMED PER LA DICHIARAZIONE
 * DEI BEAN GESTITI
 * @Named:
	Questa annotazione è parte delle specifiche di Contexts and Dependency Injection (CDI) in Java EE e viene utilizzata per dichiarare un bean come un 
	componente riconoscibile da CDI.Quando un bean viene annotato con @Named, diventa un componente che può essere iniettato o utilizzato all'interno di 
	altre parti dell'applicazione.
	@SessionScoped:
	Questa annotazione definisce l'ambito di vita del bean, ovvero quanto tempo il bean deve rimanere in memoria.
	indica che una singola istanza del bean verrà creata e condivisa tra tutte le richieste dello stesso utente all'interno di una sessione utente.
	 Ciò significa che lo stato del bean sarà mantenuto durante l'intera sessione utente.
	Questa annotazione è spesso utilizzata per mantenere lo stato e i dati specifici dell'utente durante una sessione.
	
	I BEAN IN JSF FANNO DICHIARATI ALL'INTERNO DEL FILE FACES-CONFIG-XML
 */


@Named
@SessionScoped
public class BenvenutoBean implements Serializable {
	
    private String nome;
    private String messaggio;
    private boolean show;
    
    

    public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String saluta() {
        messaggio = "Benvenuto, " + nome + "!";
        return null; // Resta sulla stessa pagina
    }
    
    public void listaTransazioni() {
    	setShow(!isShow());
    }
    
    
    
}