package it.exolab.exobank.controller;

import it.exolab.exobank.convertitore.Convertitore;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.crud.ContoCorrenteCrud;
import it.exolab.exobank.crud.TransazioneCrud;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.TransazioneControllerInterface;
import it.exolab.exobank.mapper.ContoCorrenteMapper;
import it.exolab.exobank.mapper.TransazioneMapper;
import it.exolab.exobank.models.ContoCorrente;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;
import it.exolab.exobank.sqlmapfactory.SqlMapFactory;
import it.exolab.exobank.validatore.Validatore;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.ibatis.session.SqlSession;

/**
 * Session Bean implementation class TransazioneController
 */
@Stateless(name = "TransazioneControllerInterface")
@LocalBean
public class TransazioneController implements TransazioneControllerInterface {

	/**
	 * Default constructor.
	 */
	public TransazioneController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dto<Transazione> insertTransazione(Transazione transazione) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		try {
			if (new Validatore().gestoreControlloTransazione(transazione)) {
				TransazioneCrud crud = new TransazioneCrud();
				Dto<Transazione> dtoTransazione = new Dto<Transazione>();
				SqlSession session = factory.openSession();
				TransazioneMapper mapperTransazione = session.getMapper(TransazioneMapper.class);
				ContoCorrenteMapper mapperContoCorrente = session.getMapper(ContoCorrenteMapper.class);
				Transazione transazioneInserita = crud.insertTransazione(transazione, mapperTransazione);
				if (null != transazioneInserita) {
					if (transazione.getTipo().getId() == Costanti.TIPO_DEPOSITO) {
						int numeroRigheAggiornate = updateContoCorrente(transazione, mapperContoCorrente);
						if (numeroRigheAggiornate != 1) {
							factory.rollbackSession();
							System.out.println("Errore validazione transazione insertTransazione --- ControllerTransazione----");
							throw new Exception(Costanti.ERRORE_OPERAZIONE);
						}
					}
					factory.commitSession();
					dtoTransazione.setData(new Convertitore().convertTransazioneToDTO(transazione));
					return dtoTransazione;
				} else {
					System.out.println("Errore 0 transazioni inserite insertTransazione --- ControllerTransazione----");
					throw new Exception(Costanti.ERRORE_OPERAZIONE);
				}
			} else {
				System.out.println("Errore VALIDAZIONE transazione insertTransazione --- ControllerTransazione----");
				throw new Exception(Costanti.ERRORE_OPERAZIONE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findAllTransazioni ---ControllerTransazione----SQL EXCEPTION---");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore findAllTransazioni ---ControllerTransazione---");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}

	}

	@Override
	public Dto<List<Transazione>> findAllTransazioni() throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<List<Transazione>> dtoListaTransazioni = new Dto<List<Transazione>>();
		try {
			TransazioneCrud crud = new TransazioneCrud();
			SqlSession session = factory.openSession();
			TransazioneMapper mapper = session.getMapper(TransazioneMapper.class);
			List<Transazione> listaTransazioni = crud.findAllTransazioni(mapper);
			if (null != listaTransazioni && !listaTransazioni.isEmpty()) {
				dtoListaTransazioni.setData(new Convertitore().convertListaTransazioneToDTO(listaTransazioni));
				return dtoListaTransazioni;
			} else {
				dtoListaTransazioni.setErrore("Lista transazioni vuota o null");
				return dtoListaTransazioni;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findAllTransazioni ---SQL EXCEPTION---");
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findAllTransazioni ---ControllerTransazione---");
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}

	}

	/*
	 * COMPLETARE GIRO EMAIL
	 * 
	 * download senza salvare il file tempoernaeo APCHE POI DOC X O UN EXCEL
	 * RIEPILOGO MOMVIEMNTI BANCARI NEL DOC X TABELLA E SULL EXCEL XLSX FILE
	 * TEMPORANEO SUL DISCO, SENZA SALVARE IL FILE TEMPORANEO SUL DISCO PERCHE NON
	 * SERMPRE ABBIAMO UN SERVER A DISPOZIONE SU DOVE SALVARE UN FILE
	 */
	@Override
	public Dto<Transazione> updateTransazione(Transazione transazione) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<Transazione> dtoTransazione = new Dto<Transazione>();
		int numeroRigheAggiornate = 0;
		try {
			if (new Validatore().gestoreControlloTransazione(transazione)) {
				TransazioneCrud crudTransazione = new TransazioneCrud();
				SqlSession session = factory.openSession();
				TransazioneMapper mapperTransazione = session.getMapper(TransazioneMapper.class);
				ContoCorrenteMapper mapperContoCorrente = session.getMapper(ContoCorrenteMapper.class);
				numeroRigheAggiornate = crudTransazione.updateTransazione(transazione, mapperTransazione);
				if (numeroRigheAggiornate == 1) {
					if (transazione.getStatoTransazione().getId() == Costanti.TRANSAZIONE_APPROVATA) {
						numeroRigheAggiornate = updateContoCorrente(transazione, mapperContoCorrente);
					}
					factory.commitSession();
					dtoTransazione.setData(new Convertitore().convertTransazioneToDTO(transazione));
					return dtoTransazione;
				} else {
					System.out.println("Errore updateTransazione 0 righe aggiornate transazione in updatetransazione ---ControllerTransazione---");
					throw new Exception(Costanti.ERRORE_OPERAZIONE);
				}
			} else {
				System.out.println("Errore updateTransazione - VALIDAZIONE TRANSAZIONE----");
				throw new Exception(Costanti.ERRORE_OPERAZIONE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore updateTransazione ---SQL EXCEPTION---");
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			factory.rollbackSession();
			System.out.println("Errore updateTransazione ---ControllerTransazione---");
			throw new Exception(null != e.getMessage() ? e.getMessage(): Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} finally {
			factory.closeSession();
		}
	}

	@Override
	public Dto<List<Transazione>> findTransazioniUtente(Utente utente) throws Exception {
		SqlMapFactory factory = SqlMapFactory.instance();
		Dto<List<Transazione>> dtoListaTransazioni = new Dto<List<Transazione>>();
		try {
			SqlSession session = factory.openSession();
			TransazioneCrud crudTransazione = new TransazioneCrud();
			TransazioneMapper mapperTransazione = session.getMapper(TransazioneMapper.class);
			List<Transazione> listaTransazioniUtente = crudTransazione.findTransazioniUtente(mapperTransazione,utente.getId());
			if (null != listaTransazioniUtente && !listaTransazioniUtente.isEmpty()) {
				dtoListaTransazioni.setData(new Convertitore().convertListaTransazioneToDTO(listaTransazioniUtente));
				return dtoListaTransazioni;
			} else {
				dtoListaTransazioni.setErrore("lista transazioni vuota");
				return dtoListaTransazioni;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore findTransazioniUtente ----ControllerTransazioni---SQL EXCEPTION"); // STAMPA PER																						
			throw new SQLException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore findTransazioniUtente ---ControllerTransazioni----"); // STAMPA PER BACK END
			throw new Exception(null != e.getMessage() ? e.getMessage() : Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}
	
	
	
	

	private int updateContoCorrente(Transazione transazione, ContoCorrenteMapper mapper) throws Exception {
		int numeroRigheContiAggiornate = 0;
		try {
			ContoCorrente contoDaAggiornare = transazione.getConto();
			ContoCorrente contoBeneficiario = transazione.getContoBeneficiario();
			ContoCorrenteCrud crud = new ContoCorrenteCrud();
			switch (transazione.getTipo().getId()) {                                    
			case Costanti.TIPO_DEPOSITO: {												
				contoDaAggiornare.setSaldo(contoDaAggiornare.getSaldo() + transazione.getImporto());
				break;
			}
			case Costanti.TIPO_PRELIEVO:
			case Costanti.TIPO_RICARICA:
			case Costanti.TIPO_BOLLETTINO: {
				contoDaAggiornare.setSaldo(contoDaAggiornare.getSaldo() - transazione.getImporto());
				break;
			}
			case Costanti.TIPO_BONIFICO: {
				contoDaAggiornare.setSaldo(contoDaAggiornare.getSaldo() - transazione.getImporto());
				contoBeneficiario.setSaldo(contoBeneficiario.getSaldo() + transazione.getImporto());
				break;
			}
			}
			numeroRigheContiAggiornate = crud.updateConto(contoDaAggiornare, mapper);
			if (numeroRigheContiAggiornate == 1) {
				if (null != contoBeneficiario) {
					numeroRigheContiAggiornate = crud.updateConto(contoBeneficiario, mapper);
					return numeroRigheContiAggiornate;
				}
				return numeroRigheContiAggiornate;
			} else {
				System.out.println("Errore updateContoCorrente  0 righe righe aggiornate conto -----ControllerTransazione-----");
				throw new Exception(Costanti.ERRORE_OPERAZIONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore updateContoCorrente -----ControllerTransazione-----");
			throw new Exception(null != e.getMessage() ? e.getMessage(): Costanti.ERRORE_OPERAZIONE);
		}
	}

}
