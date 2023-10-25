package it.exolab.exobank.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import it.exolab.exobank.conf.EJBFactory;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.doc.ExcelFileCreator;
import it.exolab.exobank.doc.PdfFileCreator;
import it.exolab.exobank.doc.WordFileCreator;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.ejbinterface.TransazioneControllerInterface;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

@Path(Costanti.TRANSAZIONE_REST)
public class TransazioneRest {

	
	@POST
	@Path(Costanti.FIND_TRANSAZIONI_UTENTE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findTransazioniUtente(Utente utente) {
		try {
			TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();			
			Dto<List<Transazione>> dtoListaTransazioni = transazioneService.findTransazioniUtente(utente);  
			if (dtoListaTransazioni.isSuccess()) {
				return Response.status(Status.OK).entity(dtoListaTransazioni).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoListaTransazioni.getErrore()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}
	
 	

	@GET
	@Path(Costanti.FIND_ALL_TRANSAZIONI)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findAllTransazioni() {
		try {
			TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();			
			Dto<List<Transazione>> dtoListaTransazioni = transazioneService.findAllTransazioni();  
			if (dtoListaTransazioni.isSuccess()) {
				return Response.status(Status.OK).entity(dtoListaTransazioni).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoListaTransazioni.getErrore()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(Costanti.INSERT_TRANSAZIONE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response insertTransazione(Transazione transazione) {
		try {
			TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();			
			Dto<Transazione> dtoTransazione = transazioneService.insertTransazione(transazione);
			if (dtoTransazione.isSuccess()) {
				return Response.status(Status.OK).entity(dtoTransazione).build();
			} else {
				return Response.status(Status.NO_CONTENT).entity(dtoTransazione).build();     //DA TOGLIERE
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(Costanti.UPDATE_TRANSAZIONE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateTransazione(Transazione transazione) {
		try {
			TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();			
			Dto<Transazione> dtoTransazione = transazioneService.updateTransazione(transazione);
			if (dtoTransazione.isSuccess()) {
				return Response.status(Status.OK).entity(dtoTransazione).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(dtoTransazione).build();   //DA TOGLIERE
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	
	/*BACK END:
	 * ByteArrayOutputStream è una classe in Java inclusa nel package java.io. Questa classe estende OutputStream ed è utilizzata per 
	 * scrivere dati in un array di byte in memoria anziché in un file o un altro stream di output.ByteArrayOutputStream 
	 * per scrivere dati "in memoria" significa che i dati verranno temporaneamente conservati nella RAM, 
	 * il che è utile per manipolare o elaborare i dati prima di salvarli su disco o trasmetterli su una rete.
	 *REST:
	 *Produces (file binario) indica che il metodo puo produrre una risposta del media specificato in questo caso binario
	 *Costruiamo una risposta 200 con ResponseBuilder (COSTRUTTORE CON OGGETTO/MEDIA TYPE OGGETTO) dove inseriamo nella risposta l'oggetto da restituire e il MIME
	 *(Multipurpose Internet Mail Extensions) è utilizzata per indicare al browser come interpretare il contenuto
	 *della risposta HTTP.in questo caso sto comunicando al browser il formato Office Open XML (XLSX).
	 *Content-Disposition": Questa è la chiave dell'intestazione HTTP. È un'intestazione standard utilizzata per 
	 *controllare il comportamento del browser quando si tratta di risposte HTTP.
	 *"attachment": Questo è il valore dell'intestazione "Content-Disposition". Indica al browser che la risposta deve essere trattata
	 * come un allegato scaricabile. 
	 * "filename=transazioni.xlsx": Questa parte dell'intestazione specifica il nome del file che verrà suggerito al browser quando si scarica il contenuto. 
	 * REACT:
axios.post(URL_DOWNLOAD_LISTA_TRANSAZIONI_EXCEL, utente, { responseType: 'blob' })  LA RISPOSTA (RESPONSETYPE) DEVE ESSERE TRATTATA	 
.then(response => {																	COME UN OGGETTO BINARIO
const url = window.URL.createObjectURL(new Blob([response.data]));  STIAMO CREANDO UN URL TEMPORANEO CON L'OGGETTO BLOB CHE INCAPSULA IL FILE
const a = document.createElement('a');                            CREIAMO UN ELEMENTO HTML A OVVERO UN COLLEGAMENTO
a.href = url;													LO COLLEGHIAMO ALLA URL TEMPORANEO DELL'OGGETTO BLOB
a.download = 'transazioni.xlsx';  								INDICA IL NOME DEL FILE SCARICATO 
document.body.appendChild(a);                           USATO PER AGGIUNGERE A OVVERO IL COLLEGAMENTO PER LO SCARICAMENTO DEL FILE AL CORPO DEL DOC HTML
a.click();                                             è una chiamata JavaScript comune per avviare il download di un file
window.URL.revokeObjectURL(url);   : Dopo aver attivato il download, è buona pratica rilasciare (revocare) l'URL temporaneo che 
									è stato creato. Questo libera le risorse del browser che erano utilizzate per l'URL temporaneo.

	 */
		
	
	
	
	
	@POST
	@Path(Costanti.DOWNLOAD_WORD) 
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })        
	public Response downloadWord(Utente utente) {
	    try {
	        TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();
	        Dto<List<Transazione>> dtoListaTransazioni = transazioneService.findTransazioniUtente(utente);
	        if (dtoListaTransazioni.isSuccess()) {
	            byte[] fileWord = new WordFileCreator().createWordFile(dtoListaTransazioni); 
	            ResponseBuilder response = Response.ok(fileWord, Costanti.MIME_FORMATO_WORD);                       //CONTENUTO RISPOSTA oggetto / formato
	            response.header(Costanti.CONTENT_DISPOSITION, Costanti.HEADERS_WORD_ALLEGATO_TRANSAZIONE_UTENTE); 
	            return response.build();
	        }
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@POST
    @Path(Costanti.DOWNLOAD_EXCEL)
 	@Produces({MediaType.APPLICATION_OCTET_STREAM})      
    public Response downloadExcel(Utente utente) {
 		try {
 			TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();			
 			Dto<List<Transazione>> dtoListaTransazioni = transazioneService.findTransazioniUtente(utente);
 			if(dtoListaTransazioni.isSuccess()) {
 		        byte[] fileExcel = new ExcelFileCreator().createExcelFile(dtoListaTransazioni); 
 		        ResponseBuilder response = Response.ok(fileExcel, Costanti.MIME_FORMATO_EXCEL);      
 		        response.header(Costanti.CONTENT_DISPOSITION, Costanti.HEADERS_EXCEL_ALLEGATO_TRANSAZIONE_UTENTE);
 		        return response.build();
 			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
 		}catch(Exception e) {
 			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
 		}
    }
	
	@POST
	@Path(Costanti.DOWNLOAD_PDF) 
	@Produces({Costanti.MEDIA_TYPE_FILE_PDF})
	public Response downloadPdf(Utente utente) {
	    try {
	        TransazioneControllerInterface transazioneService = new EJBFactory<TransazioneControllerInterface>(TransazioneControllerInterface.class).getEJB();
	        Dto<List<Transazione>> dtoListaTransazioni = transazioneService.findTransazioniUtente(utente);
	        if (dtoListaTransazioni.isSuccess()) {
	            byte[] filePdf = new PdfFileCreator().createPdfFile(dtoListaTransazioni);
	            ResponseBuilder response = Response.ok(filePdf);
	            response.header(Costanti.CONTENT_DISPOSITION, Costanti.HEADERS_PDF_ALLEGATO_TRANSAZIONE_UTENTE); 
	            return response.build();
	        }
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
	

}
