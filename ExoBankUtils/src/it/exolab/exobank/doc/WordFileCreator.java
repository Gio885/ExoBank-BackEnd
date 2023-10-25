package it.exolab.exobank.doc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

public class WordFileCreator {

	/*
	 * 
	 *  In un documento Word, il contenuto è organizzato in una struttura ad albero fatta di diversi elementi, ognuno dei quali corrisponde a un 
	 *  aspetto specifico del documento. Ecco alcuni dei principali elementi in un documento Word:
	 *  
	 *  Paragrafo (Paragraph): Un paragrafo in un documento Word è un elemento di base che contiene testo o altri oggetti. 
	 *  È possibile formattare il testo all'interno di un paragrafo, come il tipo di carattere, l'allineamento e gli stili.
	 *  
	 *  Esecuzione (Run): Una run è una sequenza di testo all'interno di un paragrafo con una formattazione uniforme. Ad esempio, un paragrafo può 
	 *  contenere più run se il testo ha stili diversi (grassetto, corsivo, colore, ecc.).
	 *  
	 *  Table: Una tabella è una griglia di celle organizzate in righe e colonne. Ogni cella può contenere testo, immagini o altri oggetti. 
	 *  Le tabelle sono spesso utilizzate per organizzare dati in un documento Word.
	 *  
	 *  Image: Le immagini possono essere inserite all'interno di un documento Word 
	 *  
	 *  Elenco puntato : Questi elementi sono utilizzati per creare elenchi puntati o numerati
	 *  
	 *  Intestazioni e piè di pagina (Header e Footer): Queste sezioni appaiono nella parte superiore (intestazione) e inferiore (piè di pagina) 
	 *  
	 *  Oggetti OLE (OLE Objects): Questi oggetti consentono di incorporare oggetti OLE, come fogli di calcolo Excel o presentazioni 
	 *  PowerPoint, all'interno di un documento Word.
	 *  
	 *  Apache POI fornisce una serie di classi e metodi per manipolare questi elementi in un documento Word. 
	 * 
	 * 
	 * 
	 * https://poi.apache.org/apidocs/dev/index.html
	 * 
	 * addCarriegeReturn per andare a capo in un run
	 */

	public byte[] createWordFile(Dto<List<Transazione>> listaTransazioni) throws IOException {
		try {

			XWPFDocument document = new XWPFDocument();
			creazioneParagrafoTitolo(document,UnderlinePatterns.valueOf(1), ParagraphAlignment.LEFT, TextAlignment.AUTO, "ExoBank",true,30,0,200);		
			creazioneParagrafoUtente(listaTransazioni,document,TableRowAlign.RIGHT,ParagraphAlignment.LEFT,TextAlignment.CENTER, true,13,400,400);	
			creazioneParagraIntestazioneTabella(document,listaTransazioni.getData(), UnderlinePatterns.NONE, ParagraphAlignment.CENTER,TextAlignment.CENTER, 
					"Elenco delle Transazioni",true,14,400,200);
			
			XWPFTable tabella = document.createTable(listaTransazioni.getData().size() + 1, 4); // +1 per l'intestazione
			String[] headers = { Costanti.TIPO_TRANSAZIONE, Costanti.DATA_TRANSAZIONE, Costanti.IMPORTO_TRANSAZIONE,
					Costanti.CONTO_BENEFICIARIO };
			tabellaValorizzataConBordi(tabella, headers, 12, Costanti.RGB_GIALLO, true, TextAlignment.CENTER, ParagraphAlignment.CENTER, listaTransazioni,
					Costanti.RGB_ROSSO, Costanti.RGB_VERDE,Costanti.RGB_GRIGIO);
			
			//creazioneBordiTabella(tabella);
			//setCampiTabellaIntestazione(tabella, headers,12,Costanti.RGB_GIALLO,true, TextAlignment.CENTER,ParagraphAlignment.CENTER);
			//setDatiCelleTabella(tabella, listaTransazioni,headers,Costanti.RGB_ROSSO,Costanti.RGB_VERDE,Costanti.RGB_GRIGIO);
			
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
		//https://stackoverflow.com/questions/27410967/apache-poi-how-to-add-a-page-number?rq=1
			addPageNumberFooter(document);
			
			document.write(outputStream);
			return outputStream.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}
	
	private void tabellaValorizzataConBordi(XWPFTable tabella,String [] headers,int dimensioneFontHeader,String coloreCelleHeaders,
			boolean grassettoHeaders,TextAlignment allVertHeaders,ParagraphAlignment allOrizzHeader,Dto<List<Transazione>> listaTransazioni,
			String coloreImportoNegativo,String coloreImportoPositivo,String coloreRigaAlternata) {
			creazioneBordiTabella(tabella);
			setCampiTabellaIntestazione(tabella, headers,12,coloreCelleHeaders,grassettoHeaders, allVertHeaders,allOrizzHeader);
			setDatiCelleTabella(tabella, listaTransazioni,headers,coloreImportoNegativo,coloreImportoPositivo,coloreRigaAlternata);

	}
	
	
	
	//XWPFRun possiamo equiparlarlo ad un chunk di OPENPDF o ITEXT

	private void creazioneParagraIntestazioneTabella(XWPFDocument document,List<Transazione> lista,UnderlinePatterns sottolineatura,ParagraphAlignment all,TextAlignment allVert,String titolo,boolean grassetto,int fontSize,int spazioSuperiore,int spazioInferiore) {
		XWPFParagraph paragrafo = document.createParagraph();
		setStileParagrafo(paragrafo, spazioSuperiore, spazioInferiore, all, allVert);
		Date data = new Date();
		Transazione transazionePiùRecente = null;
	    Date dataPiùRecente = null;
        for (Transazione transazione : lista) {
            Date dataTransazione = transazione.getDataTransazione();
            if (dataPiùRecente == null || dataTransazione.before(dataPiùRecente)) {
                dataPiùRecente = dataTransazione;
                transazionePiùRecente = transazione;
            }
        }
		creaRunConStile(paragrafo, sottolineatura, grassetto, fontSize, titolo+" dal "+new Method().formattaData(transazionePiùRecente.getDataTransazione())+" ad oggi "+new Method().formattaData(data), null);
	}
	
	private XWPFRun creaRunConStile(XWPFParagraph paragraph,UnderlinePatterns sottolineatura, boolean bold, int fontSize, String text, String color) {
	    XWPFRun run = paragraph.createRun();
	    run.setBold(bold);
	    run.setUnderline(sottolineatura);
	    run.setFontSize(fontSize);
	    run.setText(text);
	    if (color != null) {
	        run.setColor(color);
	    }
	    return run;
	}
	
	private XWPFParagraph setStileParagrafo(XWPFParagraph paragrafo,int spazioSuperiore,int spazioInferiore,ParagraphAlignment allOriz,TextAlignment allVert) {
		paragrafo.setSpacingBefore(spazioSuperiore);
		paragrafo.setSpacingAfter(spazioInferiore);
		paragrafo.setAlignment(allOriz);       
		paragrafo.setVerticalAlignment(allVert);
		return paragrafo;

	}

	private void creazioneParagrafoTitolo(XWPFDocument document,UnderlinePatterns sottolineatura,ParagraphAlignment allOriz,TextAlignment allVert,String titolo,boolean grassetto,int fontSize,int spazioSuperiore,int spazioInferiore) {
		XWPFParagraph paragrafo = document.createParagraph();
		setStileParagrafo(paragrafo, spazioSuperiore, spazioInferiore, allOriz, allVert);
		//XWPFRun possiamo equiparlarlo ad un chunk di OPENPDF o ITEXT
		creaRunConStile(paragrafo,sottolineatura, grassetto, fontSize, titolo, null);
	}
	
	/*
	 * PER FARE UN RIQUADRO ATTORNO AI DATI DELL'UTENTE MI SONO FATTO UNA TABELLA CON UNA CELLA ED HO AGGIUNTO UN PARAGRAFO
	 */
	
	private void creazioneParagrafoUtente(Dto<List<Transazione>> listaTransazioni,XWPFDocument document,TableRowAlign allTabella,ParagraphAlignment allOrizz,TextAlignment allVert,boolean grassetto,int fontSize,int spazioSuperiore,int spazioInferiore) {
		
		//XWPFRun possiamo equiparlarlo ad un chunk di OPENPDF o ITEXT

		XWPFTable tabellaUtente = document.createTable(0,0);
		tabellaUtente.setWidth(5500);
		tabellaUtente.setTableAlignment(allTabella);
		XWPFTableRow row = tabellaUtente.getRow(0);
		XWPFTableCell cella = row.getCell(0);	
		XWPFParagraph paragrafoUtente = cella.addParagraph();      //CAPIRE PERCHE ADDPARAGRAPH COSI FUNZIONA CON L'ARGOMENTO PARAGRAFO NO
		setStileParagrafo(paragrafoUtente, spazioSuperiore, spazioInferiore, allOrizz, allVert);
		Utente utente= listaTransazioni.getData().get(0).getConto().getUtente();	
		XWPFRun run = creaRunConStile(paragrafoUtente, UnderlinePatterns.NONE, grassetto, fontSize, "Dati Intestatario:", null);
		run.addCarriageReturn();
		XWPFRun run2 = creaRunConStile(paragrafoUtente, UnderlinePatterns.NONE, false, fontSize, "Nome: " +utente.getNome(), null);
		run2.addCarriageReturn();
		run2.setText("Cognome: "+utente.getCognome());
		run2.addCarriageReturn();
		run2.setText("Codice Fiscale: "+utente.getCodiceFiscale());
		run2.addCarriageReturn();
		run2.setText("Numero Conto: "+listaTransazioni.getData().get(0).getConto().getNumeroConto());
		run2.addCarriageReturn();
		run2.setText("Saldo Disponibile: € "+listaTransazioni.getData().get(0).getConto().getSaldo().toString());
	}
	
	
	/*
	 * CICLO SULLA RIGA 0 DELLA TABELLA GLI HEADERS CHE DEVONO ANDARE AD INSERIRE NELLA TABELLA
	 * QUINDI MI PRENDO ATTRAVERSO LA RIGA LA CELLA (I) E AGGIUNGO UN PARAGRAFO CHE POI ALLA CELLA
	 * IN CUI INSERIRO IL TESTO,COLORE ECC... TRAMITE L'OGGETTO DELLA CLASSE RUN
	 * XWPFRun e una classe utilizzata per gestire i singoli "runs" di testo all'interno di un paragrafo in un documento Word (.docx).
	 *  Un "run" rappresenta una sequenza continua di caratteri con la stessa formattazione nel testo di un paragrafo. 
	 */
	
	
	private void setCampiTabellaIntestazione(XWPFTable tabella, String[] headers,int dimensioneFont,String rgb,boolean grassetto,TextAlignment vertAll,ParagraphAlignment all) {
		XWPFTableRow rigaIntestazione = tabella.getRow(0);
		rigaIntestazione.setHeight(1000);
		 for (int i = 0; i < headers.length; i++) {
		        String[] headerParti = headers[i].split(" ");            //DIVIDO L'HEADERS IN DUE PARTI COSI E POI ME LO CICLO
		        XWPFTableCell cella = rigaIntestazione.getCell(i);		//PER METTERLE IN 2 PARAGRAFI DIVERSI UNO SOTTO L'ALTRO
		        for (String parte : headerParti) {
		            XWPFParagraph paragrafo = cella.addParagraph();
		            setStileParagrafo(paragrafo, 0, 0, all, vertAll);
		            creaRunConStile(paragrafo, UnderlinePatterns.NONE, grassetto, dimensioneFont, parte, null);
		        }
		        cella.setColor(rgb); 
				if(i!=3) {
	            	cella.setWidth(Integer.toString(16));           // QUI TUTTE LE DIMENSIONI DELLE ALTRE COLONNE
	            }else {
	            	cella.setWidth(Integer.toString(30));          //QUI HO GESTITO LA DIMENSIONE DELL'ULTIMA COLONNA LA + LARGA
	            }
		    }
			
		}
	
	private void setDatiCelleTabella(XWPFTable tabella, Dto<List<Transazione>> listaTransazioni, String[] headers,String rgbImportoNegativo,String rgbImportoPositivo,String rgbRigheAlternate) {
    	Boolean rigaPari=true;
	    for (int i = 0; i < listaTransazioni.getData().size(); i++) {
	        rigaPari = !rigaPari;
	        Transazione transazione = listaTransazioni.getData().get(i);
	        XWPFTableRow rigaDati = tabella.getRow(i + 1);   
	        for (int j = 0; j < headers.length; j++) {
	            XWPFTableCell cella = rigaDati.getCell(j);
	            XWPFParagraph paragraph = cella.getParagraphs().get(0); // PRENDE IL PRIMO PARAGRAFO
	            XWPFRun run = paragraph.createRun();
	            switch (j) {
	                case 0:
		                if (transazione.getTipo().getId() != Costanti.TIPO_DEPOSITO) {
		                        run.setColor(rgbImportoNegativo); 
		                    }else {
		                    	run.setColor(rgbImportoPositivo); 
		                    }
		                    run.setText(transazione.getTipo().getTipoTransazione());
	                    break;
	                case 1:
	                    cella.setText(new Method().formattaData(transazione.getDataTransazione()));
	                    break;
	                case 2:
	                    if (transazione.getTipo().getId() != Costanti.TIPO_DEPOSITO) {
	                        run.setColor(rgbImportoNegativo); 
	                    }else {
	                    	run.setColor(rgbImportoPositivo); 
	                    }
	                    run.setText(new Method().formattaImporto(transazione));
	                    break;
	                case 3:
	                    if (transazione.getContoBeneficiario() != null) {
	                        cella.setText(transazione.getContoBeneficiario().getNumeroConto());
	                    } else {
	                        cella.setText("-----");
	                    }
	                    break;
	            }
	            if(j!=3) {
	            	cella.setWidth(Integer.toString(16));
	            }else {
	            	cella.setWidth(Integer.toString(30));
	            	
	            }
	            paragraph.setAlignment(ParagraphAlignment.LEFT);
	            if(rigaPari) {
		        	rigaDati.getCell(j).setColor(rgbRigheAlternate);
		        } 
	        } 
	        
	        
	    }

	}
	
	/*
	 * CTTblBorders E UN OGGETTO CHE RAPPRESENTA I BORDI DI UNA TABELLA IN UN DOCUMENTO WORD.
	 * getCTTbl() RESTITUSCE LA STRUTTURA DELLA TABELLA SOTTOFORMA DI OGGETTO CTTbl
	 * getTblPr() RESTITUISCE LE PROPRIETA DELLA TABELLA 
	 * addNewTblBorders() CREA UN OGGETTO CTTblBorders ALL'INTERNO DELLA TABELLA
	 * 
	 * I 4 tblBorders AGGIUNGO BORDI SU TUTTI E 4 I LATI DI UNA CELLA, UN BORDO SINGOLO PER OGNI LATO
	 */
	private void creazioneBordiTabella(XWPFTable tabella) {
		CTTblBorders tblBorders = tabella.getCTTbl().getTblPr().addNewTblBorders();
		tblBorders.addNewInsideH().setVal(STBorder.SINGLE); // BORDO SINGOLO ORIZZONTALE
		tblBorders.addNewInsideV().setVal(STBorder.SINGLE); // BORDO SINGOLO VERTICALE
		tblBorders.addNewTop().setVal(STBorder.SINGLE); // BORDO SINGOLO SUPERIORE
		tblBorders.addNewBottom().setVal(STBorder.SINGLE); // BORDO SINGOLO INFERIORE
	}

	
	
	//https://stackoverflow.com/questions/27410967/apache-poi-how-to-add-a-page-number?rq=1
	  private void addPageNumberFooter(XWPFDocument document) {
		  
		  /*
		   * CTP ctp = CTP.Factory.newInstance();: Qui stai creando un nuovo oggetto CTP (Common Text Properties) che 
		   * rappresenta le proprietà del testo comuni che possono essere utilizzate all'interno del documento Word.
		   * IL CTP PERMETTE DI MODIFICARE LA STRUTTURA E LA FORMATTAZIONE DI UN DOCUMENTO WORD, TI PERMETTE DI CREARE NUOVI PARAGRAFI
		   * ALLINEAMENTO TESTO, SPAZIATURA ECC...
		   */
		  CTP ctp = CTP.Factory.newInstance();

		  CTText testo = ctp.addNewR().addNewT();  //L'INTERFACCIA CTTTEXT RAPPRESENTA IL TESTO ALL'INTERNO DI UN ELEMENTO XML
		  testo.setStringValue("Page ");				//CREIAMO UN RUN DI TESTO E POI IL TESTO
		  testo.setSpace(SpaceAttribute.Space.PRESERVE);   // EFFETTIVO DOVE INSERIAMO IL TESTO E LO SPAZIO 

		  /*
		   * STIAMO CREANDO TRAMITE IL CTP UN OGGETTO CTR CHE RAPPRENSENTA UN "RUN" DI TESTO, OVVERO UNA SEQUENZA DI CARATTERI
		   * CON LE STESSE PROPRIETA DI FORMATTTAZIONE ALL'INTERNO DI UN PARAGRAFO.
		   * CON addNewFldChar().setFldCharType(STFldCharType.BEGIN) STIAMO AGGIUNGENDO UN CARATTERE DI INIZIO CAMPO CHE SEGNALA
		   * L'INIZIO DI UN CAMPO DI TESTO SPECIALE,OVVERO IL NUMERO DI PAGINA CORRENTE
		   * CON addNewInstrText().setStringValue(" PAGE "); STIAMO DEFINENDO UN CAMPO DI TESTO SPECIALE. WORD RICONOSCE QUEST
		   * ISTRUZIONE COME UNA RICHIESTA PER INSERIRE IL NUMERO DI PAGINA CORRENTE AL MOMENTO DELLA CREAZIONE DEL DOCUMENTO
		   * " PAGE " E " NUMPAGE " SONO DELLE ISTRUZIONI RICONOSCIUTI DA WORD, QUANDO WORD ELABORA IL DOCUMENTO RICONOSCE
		   * QUESTI COMANDI E LI INTERPRETA PER RESTITUIRE IL NUMERO DI PAGINA CORRENTE E IL NUMERO TOTALE DELLE PAGINE
		   */
		  
		  CTR run = ctp.addNewR();	                               	
		  run.addNewFldChar().setFldCharType(STFldCharType.BEGIN);
		  run.addNewInstrText().setStringValue(" PAGE ");
		  run.addNewFldChar().setFldCharType(STFldCharType.END);
		  
		  /*
		   * INSERIAMO CON IL CTP IL TESTO OF
		   */

		  testo = ctp.addNewR().addNewT();
		  testo.setStringValue(" of ");
		  testo.setSpace(SpaceAttribute.Space.PRESERVE);
		  
		  /*
		   * QUI STIAMO FACENDO LA STESSA COSA DI PRIMA OVVERO CREIAMO UN RUN PER DEFINIRE L'INIZIO E LA FINE DI UN CAMPO
		   * SPECIALE OVVERO IL NUMPAGES CHE WORD INTERPRETERA ALL'ELABORAZIONE DEL DOCUMENTO COME IL NUMERO TOTALI DI PAGINE DEL DOC
		   */

		  run = ctp.addNewR();
		  run.addNewFldChar().setFldCharType(STFldCharType.BEGIN);
		  run.addNewInstrText().setStringValue(" NUMPAGES ");
		  run.addNewFldChar().setFldCharType(STFldCharType.END);
		  
		  CTP ctp2 = CTP.Factory.newInstance();
		  CTText testo2 = ctp2.addNewR().addNewT();
		  testo2.setStringValue(Costanti.FOOTER_PDF);
		 
		  
		  /*
		   * CREIAMO UN PARAGRAFO
		   */

		  XWPFParagraph paragrafoFooterNumbPag = new XWPFParagraph(ctp, document);
		  paragrafoFooterNumbPag.setAlignment(ParagraphAlignment.RIGHT);
		  
		  XWPFParagraph paragrafoFooterTesto = new XWPFParagraph(ctp2, document);
		  setStileParagrafo(paragrafoFooterTesto, 100, 150, ParagraphAlignment.CENTER, TextAlignment.CENTER);
		  
		  
//		  XWPFParagraph lineaFooter = new XWPFParagraph(CTP.Factory.newInstance(),document);
//		  XWPFRun runLineaFooter = lineaFooter.createRun();
//		  runLineaFooter.setUnderline(UnderlinePatterns.SINGLE);
//		  runLineaFooter.setBold(true);
//		  runLineaFooter.setColor("000000");
//		  lineaFooter.addRun(runLineaFooter);
//		  lineaFooter.setAlignment(ParagraphAlignment.CENTER);
		  
		  /*
		   * XWPFHeaderFooterPolicy GESTISCE GLI HEADER E I FOOTER DEL DOCUMENTO
		   * VISTO CHE E NULL CREIAMO IL FOOTER CON document.getHeaderFooterPolicy();
		   * CON L'OGGETTO FOOTER CREIAMO IL FOOTER CHE PRENDE DUE PARAMETRI IN INPUT OVVERO
		   * IL TIPO DI PIE DI PAGINA, IL DEFAULT SPECIFICA IL TIPO DI PIE DI PAGINA PREDEFINITO DA UTILIZZARE
		   * E UN ARRAY DI PARAGRAFI IN QUESTO CASO GLI HO PASSATO DUE PARAGRAFI OVVERO UN TESTO E IL NUMERO DI PAGINA
		   * LA LINEA CHE DEFINISCE L'INIZIO DEL FOOTER  E DA FARE
		   */
		  XWPFHeaderFooterPolicy policy = document.getHeaderFooterPolicy();
		  if (policy == null)
		    policy = document.createHeaderFooterPolicy();
		  policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, new XWPFParagraph[] { paragrafoFooterTesto,paragrafoFooterNumbPag });
		}
	

}
