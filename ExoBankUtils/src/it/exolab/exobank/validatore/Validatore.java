package it.exolab.exobank.validatore;

import java.util.List;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

public class Validatore {

	
	private String messaggioErrore;
	
	public String getMessaggioErrore() {
		return messaggioErrore;
	}


	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}


	//DA USARE SOLO PER CONTROLLO LOGIN IN QUANTO HA LA PASSWORD, DA BACK END NON RESTITUISCO MAI LA PASSWORD
	public boolean controlloUtente(Utente utente) {
		if (null == utente.getNome() || utente.getNome().length()<=3 || utente.getNome().length()>=20 || !(utente.getNome().matches(Costanti.REGEX_NOME_COGNOME)) ) return false;
		if (null == utente.getCognome() || utente.getCognome().length()<=3 || utente.getCognome().length()>=20 || !(utente.getCognome().matches(Costanti.REGEX_NOME_COGNOME))) return false;
		if (null == utente.getEmail() || utente.getEmail().length()>50 || !(utente.getEmail().matches(Costanti.REGEX_EMAIL)) ) return false;
		if (null == utente.getPassword() || utente.getPassword().length()>50 || !(utente.getPassword().matches(Costanti.REGEX_PASSWORD))) return false;
		if (null == utente.getCodiceFiscale() || utente.getCodiceFiscale().length()!=16) return false;
		return true;
	}
	
	public boolean controlloContoCorrente(ContoCorrente conto) {
		if (null == conto.getNumeroConto() && conto.getNumeroConto().length() !=27) return false;
		if (null == conto.getSaldo() && conto.getSaldo()<0) return false;
		if (null == conto.getStato().getId()) return false;
		if (null == conto.getUtente().getId()) return false;
		return true;
	}
	
	public boolean gestoreControlloTransazione(Transazione transazione) throws Exception {
	    try {
	        if (null != transazione.getTipo() && null != transazione.getTipo().getId()) {	            
	            if (transazione.getTipo().getId() == Costanti.TIPO_DEPOSITO) {
	                if (controlloTransazionePositiva(transazione)) {
	                    return true;
	                } else {
	                    System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione DEPOSITO");
	                    throw new Exception("Errore operazione, contattare l'assistenza");
	                }
	            } else if (transazione.getTipo().getId() == Costanti.TIPO_PRELIEVO ||
	            		   transazione.getTipo().getId() == Costanti.TIPO_RICARICA ||
						   transazione.getTipo().getId() == Costanti.TIPO_BONIFICO ||
						   transazione.getTipo().getId() == Costanti.TIPO_BOLLETTINO) {
	                if (controlloTransazioneNegativa(transazione)) {
	                    if (transazione.getTipo().getId() == Costanti.TIPO_PRELIEVO || transazione.getTipo().getId() == Costanti.TIPO_RICARICA) {
	                        return true;
	                    } else if (transazione.getTipo().getId() == Costanti.TIPO_BONIFICO) {
	                        if (null != transazione.getContoBeneficiario()  && null != transazione.getContoBeneficiario().getId()) {
	                            return true;
	                        } else {
	                            System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione BONIFICO ID BENEFICIARIO NULL " + transazione.getContoBeneficiario().getId());
	                            throw new Exception("Conto Exobank non trovato nei nostri sistemi");
	                        }
	                    } else if (transazione.getTipo().getId() == Costanti.TIPO_BOLLETTINO) {
	                        if (null != transazione.getContoBeneficiarioEsterno() && transazione.getContoBeneficiarioEsterno().length() == 27) {
	                            return true;
	                        } else {
	                            System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione RICARICA/BOLLETTINO contoBeneficiarioEsterno null " + transazione.getContoBeneficiarioEsterno());
	                            throw new Exception("Errore operazione, contattare l'assistenza");
	                        }
	                    }
	                }
	                System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione PRELIEVO/BONIFICO/RICARICA/BOLLETTINO");
	                throw new Exception("Errore operazione, contattare l'assistenza");
	            }
	        }       
	        System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione " + transazione.getTipo().getId());
	        throw new Exception("Errore operazione, contattare l'assistenza");
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione transazione");
	        throw new Exception(null != e.getMessage() ? e.getMessage() : "C'è stato un errore, contatta l'amministratore");
	    }
	}

	private boolean controlloTransazionePositiva(Transazione transazione) throws Exception {
		try {	
			if(null == transazione.getConto() && null == transazione.getConto().getId()) {
				System.out.println("Errore controlloTransazionePositiva getConto.getID NULL --> "+transazione.getConto().getId());
				throw new Exception (Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
			if(null == transazione.getImporto() || transazione.getImporto()<=0) {
				System.out.println("Errore controlloTransazionePositiva IMPORTO NEGATIVO --> "+transazione.getImporto());
				throw new Exception ("Errore operazione di deposito, inserisci un importo positivo");
			}
			if (null == transazione.getStatoTransazione() && null == transazione.getStatoTransazione().getId() ) {
				System.out.println("Errore controlloTransazionePositiva getStatoTransazione.getID NULL --> "+transazione.getStatoTransazione().getId());
				throw new Exception (Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println();
			throw new Exception(null!=e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	
	}
	
	private boolean controlloTransazioneNegativa(Transazione transazione) throws Exception {
		try {
			if(null == transazione.getConto() && null == transazione.getConto().getId()  ) {
				System.out.println("Errore controlloTransazioneNegativa getConto.getID NULL --> "+transazione.getConto().getId());
				throw new Exception (Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
			if(null == transazione.getImporto() && null == transazione.getConto().getSaldo() || transazione.getImporto()<=0 || transazione.getImporto()>transazione.getConto().getSaldo()) {
				System.out.println("Errore controlloTransazioneNegativa IMPORTO NULL/NEGATIVO/MAGGIORE DEL SALDO --> IMPORTO TRANSAZIONE: "+transazione.getImporto()+" SALDO CONTO: "+transazione.getConto().getSaldo());
				throw new Exception ("Errore operazione, importo negativo o superiore al saldo");
			}
			if(null == transazione.getStatoTransazione() && null == transazione.getStatoTransazione().getId()) {
				System.out.println("Errore controlloTransazioneNegativa getStatoTransazione.getID NULL --> "+transazione.getStatoTransazione().getId());
				throw new Exception (Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
			}
			return true;		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println();
			throw new Exception(null!=e.getMessage()?e.getMessage():Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}	
	}
	
	

	
//	
//	
//	public boolean gestoreControlloTransazione(Transazione transazione) throws Exception {
//		try {
//			if(null != transazione.getTipo().getId()) {
//				switch (transazione.getTipo().getId()){
//				case Costanti.TIPO_DEPOSITO:{
//					if(controlloTransazionePositiva(transazione)) {
//						return true;
//					}else {
//						System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione DEPOSITO");
//						throw new Exception ("Errore operazione, contattare l'assistenza");
//					}
//				}
//				case Costanti.TIPO_PRELIEVO:
//				case Costanti.TIPO_BONIFICO:
//				case Costanti.TIPO_RICARICA:
//				case Costanti.TIPO_BOLLETTINO:{
//					if(controlloTransazioneNegativa(transazione)) {
//						switch(transazione.getTipo().getId()) {
//						case Costanti.TIPO_PRELIEVO:
//						case Costanti.TIPO_RICARICA:{
//							return true;
//						}
//						case Costanti.TIPO_BONIFICO:{
//							if(null != transazione.getContoBeneficiario().getId()) {
//								return true;
//							}else {
//								System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione BONIFICO ID BENEFICIARIO NULL "+transazione.getContoBeneficiario().getId());
//								throw new Exception ("Conto Exobank non trovato nei nostri sistemi");
//							}
//						}
//						case Costanti.TIPO_BOLLETTINO:{
//							if(null != transazione.getContoBeneficiarioEsterno()) {
//								return true;
//							}else {
//								System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione RICARICA/BOLLETTINO contoBeneficiarioEsterno null "+transazione.getContoBeneficiarioEsterno());
//								throw new Exception ("Errore operazione, contattare l'assistenza");
//							}
//						}
//						}
//						
//					}
//					System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo transazione PRELIEVO/BONIFICO/RICARICA/BOLLETTINO");
//					throw new Exception ("Errore operazione, contattare l'assistenza");
//				}
//					
//				}
//			}
//			System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione tipo tranzione" + transazione.getTipo().getId());
//			throw new Exception ("Errore operazione, contattare l'assistenza");
//		}catch(Exception e) {
//			e.printStackTrace();
//			System.out.println("Errore gestoreControlloTransazione ERRORE --> validazione transazione");
//			throw new Exception( null!= e.getMessage() ? e.getMessage() : "C'è stato un errore, contatta l'amministratore");
//		}
//	}
//	
	
	
}
