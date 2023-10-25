package it.exolab.exobank.doc;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;



import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;
import it.exolab.exobank.models.Utente;

public class PdfFileCreator extends PdfPageEventHelper {
	
	/*
	 * 
	 * https://javadoc.io/doc/com.github.librepdf/openpdf/1.1.0/index.html
		Viene creato un documento PDF vuoto con il formato A4.
		creation of the document with a certain size and certain margins
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try { 
		creation of the different writers 
		HtmlWriter.getInstance(document , System.out);
		PdfWriter.getInstance(document , new FileOutputStream("text.pdf"));
		we add some meta information to the document
	    document.addAuthor("Bruno Lowagie"); 
	    document.addSubject("This is the result of a Test."); 
	    we open the document for writing
	    document.open(); 
	    document.add(new Paragraph("Hello world"));
	    } catch(DocumentException de) {
	    System.err.println(de.getMessage());
	    }
	    document.close();
	    
	    public class PdfPTable
		extends Object
		implements LargeElement
		This is a table that can be put at an absolute position but can also be added to the document as the class Table. In the last case when crossing pages the table always break at full rows; if a row is bigger than the page it is dropped silently to avoid infinite loops.
		A PdfPTableEvent can be associated to the table to do custom drawing when the table is rendered.
		
		Fields inherited from class com.lowagie.text.Rectangle
		backgroundColor, border, borderColor, borderColorBottom, borderColorLeft, borderColorRight, borderColorTop, borderWidth, borderWidthBottom, 
		borderWidthLeft, borderWidthRight, borderWidthTop, BOTTOM, BOX, LEFT, llx, lly, NO_BORDER, RIGHT, TOP, UNDEFINED, urx, ury, useVariableBorders
		
		Fields inherited from interface com.lowagie.text.Element
		ALIGN_BASELINE, ALIGN_BOTTOM, ALIGN_CENTER, ALIGN_JUSTIFIED, ALIGN_JUSTIFIED_ALL, ALIGN_LEFT, ALIGN_MIDDLE, ALIGN_RIGHT, ALIGN_TOP, 
		
		
		CHUNK:
		UNITA BASE DI TESTO,SINGOLA RIGA DI TESTO CON STILI SPECIFICI, POSSIAMO USARE PIU CHUNK E AGGIUNGERLI AD UN PHRASE
		PHRASE:
		E UNA SEQUENZA DI CHUNK E VIENE USATO PER APPLICARE LO STESSO STILE A UN GRUPPO DI CHUNK, PUO CONTENERE PIU RIGHE
		MA OGNI RIGA INIZIA SU UNA NUOVA LINEA, VIENE USATO PER CREARE PARAGRAFI CON STILI UNIFORMI
		PARAGRAPH:
		X CREARE BLOCCHI DI TESTO E PUO ESTENDERSI SI PIU RIGHE E PARAGRAFI, USATO PER FORMATTARE IL TESTO ES ALLINEAMENTO
		PUO ESSERE COSTRUITO CON CHUNK E PHRASE 

		
		Paragraph paragraph = new Paragraph();
		Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
		Phrase phrase = new Phrase();
		Chunk chunk1 = new Chunk("Testo in grassetto", font);
		Chunk chunk2 = new Chunk("Testo normale");
		phrase.add(chunk1);
		phrase.add(chunk2);
		paragraph.add(phrase);
		
		Paragraph paragraph1 = new Paragraph("Primo paragrafo");
		Paragraph paragraph2 = new Paragraph("Secondo paragrafo");
		paragraph1.add(paragraph2); // Aggiungi il secondo paragrafo al primo
				
	 */

	public byte[] createPdfFile(Dto<List<Transazione>> transazioni) throws Exception {
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {

			/*
			 * CREAZIONE OGGETTO WRITER DELLA CLASSE PDFWRITER, RESPONSABILE DELLA SCRITTURA
			 * DEL DOCUMENTO PDF. HO ESTESO LA CLASSE PDF PAGE EVENT HELPER
			 * CHE FORNISCE DEI METODI PER GESTIRE DIVERSI EVENTI
			 * RELATIVE ALLE PAGINE DI UN PDF. HO UTILIZZATO IL METODO ONENDPAGE CHE VIENE
			 * CHIAMATO ALLA FINE DI OGNI PAGINA PDF. NEL METODO MI SONO PRESO IL NUMERO
			 * DELLA PAGINA CORRENTE CON WRITER.GETPAGENUMBER. POI HO FATTO UNA STRINGA E HO UTILIZZATO LA CLASSE
			 * PDF CONTENT BYTE PER POSIZIONARE LA STRINGA  ALL'INTERNO DEL DOCUMENTO
			 */

			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			PdfFileCreator event = new PdfFileCreator(); // Creazione dell'oggetto per il footer
			writer.setPageEvent(event); 				 // Associazione dell'evento al writer
			String[] headerCells = { Costanti.TIPO_TRANSAZIONE, Costanti.DATA_TRANSAZIONE, Costanti.IMPORTO_TRANSAZIONE,
					Costanti.CONTO_BENEFICIARIO };
			
			//public Font(int family, float size, int style, Color color)   
			HashMap<String,Font>fontMap = new HashMap<>();
			fontMap.put("headerFont", new Font(Font.HELVETICA, 10, Font.NORMAL));
			fontMap.put("fontParagrafo", new Font(Font.HELVETICA,11,Font.NORMAL));
			fontMap.put("fontTabella", new Font(Font.HELVETICA,10,Font.NORMAL));
			fontMap.put("fontImportoNegativo", new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(255, 0, 0)));
			fontMap.put("fontImportoPositivo", new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(5, 122, 30)));
			fontMap.put("fontTitolo", new Font(Font.HELVETICA,20,Font.NORMAL));
			fontMap.put("fontExoBank", new Font(Font.HELVETICA,40,Font.ITALIC));
			
			
			//LA LARGHEZZA DELLE COLONNE LO GESTITA GESTENDO I MARGINI DEL DOCUMENTO E CON LA LARGHEZZA DELLA TABELLA setWidthPercentage
			document.setMargins(30f, 30f, 30f, 60f);
			
			document.open();
			document.add(creazioneParagrafoTopSX("ExoBank",fontMap.get("fontExoBank")));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			PdfPTable paragrafoIntestazioneCliente = creazioneRiquadroParagrafo(
					creazioneParagrafoTopDX(transazioni.getData(), fontMap.get("fontParagrafo")),Element.ALIGN_RIGHT,10f,55f);
			document.add(paragrafoIntestazioneCliente);
			document.add(new Paragraph(" "));
			document.add(creazioneTitoloConStile("Elenco delle Transazioni", fontMap.get("fontTitolo")));
			document.add(new Paragraph(" "));
			PdfPTable tabella = new PdfPTable(headerCells.length);
			tabella.setWidthPercentage(100);
			aggiungiCelleIntestazione(tabella, headerCells, fontMap.get("headerFont"),18);
			creaAggiungiDatiCelle(tabella, transazioni.getData(), fontMap.get("fontTabella"),fontMap.get("fontImportoNegativo"),fontMap.get("fontImportoPositivo"), Element.ALIGN_CENTER, Element.ALIGN_CENTER,15);
			document.add(tabella);
			document.close();
			return outputStream.toByteArray();
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new DocumentException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
		}
	}

	// METODO X LA CREAZIONE CELLE INTESTAZIONE
	private void aggiungiCelleIntestazione(PdfPTable tabella, String[] headerCells, Font headerFont,int altezzaCelle) {
		for (String header : headerCells) {
			PdfPCell cellaIntestazione = creazioneCellaConStile(header, headerFont,
					new Color(168, 167, 167), Element.ALIGN_CENTER, Element.ALIGN_CENTER, altezzaCelle);
			tabella.addCell(cellaIntestazione);
		}
	}

	// METODO X LA CREAZIONE CELLE DATI
	private void creaAggiungiDatiCelle(PdfPTable table, List<Transazione> transazioni, Font cellaFont,
			Font cellaFontImportoNegativo,Font cellaFontImportoPositivo, int allOriz, int allVert,int altezzaCelle) {
		for (int i = 0; i< transazioni.size();i++){
			Color backgroundColor;
			if(i % 2 !=0) {
				backgroundColor = new Color (224,224,224);
			}else {
				backgroundColor =new Color (255,255,255);
			}
			Font cellaFontAttuale = transazioni.get(i).getTipo().getId() != Costanti.TIPO_DEPOSITO ? cellaFontImportoNegativo
					: cellaFontImportoPositivo;
			table.addCell(creazioneCellaConStile(transazioni.get(i).getTipo().getTipoTransazione(), cellaFontAttuale,backgroundColor, allOriz, allVert,altezzaCelle));

			table.addCell(
					creazioneCellaConStile(new Method().formattaData(transazioni.get(i).getDataTransazione()), cellaFont,backgroundColor, allOriz, allVert,altezzaCelle));

			table.addCell(creazioneCellaConStile(new Method().formattaImporto(transazioni.get(i)), cellaFontAttuale,backgroundColor, allOriz, allVert,altezzaCelle));

			String contoBeneficiario = transazioni.get(i).getContoBeneficiario() != null
					? transazioni.get(i).getContoBeneficiario().getNumeroConto()
					: "-----";
			table.addCell(creazioneCellaConStile(contoBeneficiario, cellaFont,backgroundColor, allOriz, allVert,altezzaCelle));
				
		}
			
		
	}
	
	private PdfPCell creazioneCellaConStile(String text, Font font, Color backgroundColor, int allOriz, int allVert,int altezzaCelle) {
	    PdfPCell cella = new PdfPCell(new Paragraph(text, font));
	    cella.setHorizontalAlignment(allOriz);
	    cella.setVerticalAlignment(allVert);
	    cella.setBackgroundColor(backgroundColor);
	    cella.setFixedHeight(altezzaCelle);
	    return cella;
	}

	// METODO X LA CREAZIONE DEL PARAGRAFO, RAPPRENSENTA IL TITOLO
	private Paragraph creazioneTitoloConStile(String titolo, Font fontTitolo) {
		Paragraph paragrafoTitolo = new Paragraph(titolo, fontTitolo);
		paragrafoTitolo.setAlignment(Element.ALIGN_CENTER);
		return paragrafoTitolo;
	}

	// METODO X LA CREAZIONE DEL PARAGRAFO
	/*
	 * Paragraph è una classe che rappresenta un paragrafo completo, mentre Phrase è una sequenza di Chunk. 
	 * Quando aggiungi un Chunk direttamente a un Paragraph, questo Chunk è considerato come parte di quel paragrafo e quindi c'è uno 
	 * spazio sopra e sotto il paragrafo stesso.Invece quando crei un Phrase e ci aggiungi un Chunk, il Chunk è ancora parte del Phrase,
	 *  ma quando successivamente crei un Paragraph utilizzando questo Phrase, non è considerato come parte del paragrafo, 
	 *  quindi non vi è alcuno spazio sopra il paragrafo stesso.
	 */
	private Paragraph creazioneParagrafoTopSX(String titolo,Font fontTitolo) {
		Chunk chunk = new Chunk(titolo);
		chunk.setFont(fontTitolo);
		chunk.setUnderline(3.0f, -5.0f); // PRIMO VALORE SPESSORE RIGA SECONDO VALORE LO SPAZIO TRA LA SCRITTA E LA SOTTOLINEATURA
		chunk.setUnderline(3.0f, -10.0f);
		Phrase phrase = new Phrase();
		phrase.add(chunk);
		Paragraph paragrafoTitolo = new Paragraph(phrase);     //CAPIRE LA DIFFERENZA TRA PHRASE E PRAGRAPH, SE AGGIUNGO IL CHUNK AL 
		paragrafoTitolo.setAlignment(Element.ALIGN_LEFT);	//PARAGRAPH MI LASCIA LO SPAZIO SOPRA, SE LO AGGIUNGO AL PHRASE E POI IL PHRASE
															//AL PARAGRAPH NON MI LASCIA LO SPAZIO
		return paragrafoTitolo;
	}

	// METODO X LA CREAZIONE DEL PARAGRAFO
	private Paragraph creazioneParagrafoTopDX(List<Transazione> transazioni, Font cellFont) {
		Utente utente = transazioni.get(0).getConto().getUtente();

		Paragraph paragrafo = new Paragraph();
		paragrafo.add("Dati Intestatario:\n");
		paragrafo.add(Chunk.NEWLINE); 
		paragrafo.add(Chunk.NEWLINE); 
		paragrafo.add(new Chunk("Nome: "+utente.getNome()+"\n",cellFont));
		paragrafo.add(new Chunk("Cognome: "+utente.getCognome()+"\n",cellFont));
		paragrafo.add(new Chunk("Codice Fiscale: "+utente.getCodiceFiscale()+"\n",cellFont));
		paragrafo.add(new Chunk("Numero Conto: "+transazioni.get(0).getConto().getNumeroConto()+"\n",cellFont));
		paragrafo.add(new Chunk("Saldo Disponibile: € "+String.valueOf(transazioni.get(0).getConto().getSaldo())+"\n",cellFont));
		paragrafo.setAlignment(Element.ALIGN_RIGHT);
		return paragrafo;
	}
	
	

	// METODO RIQUADRO PARAGRAFO
	private PdfPTable creazioneRiquadroParagrafo(Paragraph paragrafo,int allOriz,float padding,float profondita) {
		PdfPTable tabella = new PdfPTable(1);
		PdfPCell cella = new PdfPCell(paragrafo);
		cella.setPadding(padding);
		tabella.setWidthPercentage(profondita);
		tabella.addCell(cella);
		tabella.setHorizontalAlignment(allOriz);
		return tabella;
	}

	
	@Override    //DA USARE PER RIPETERE IL NOME DELLA BANCA E I DATI DELL'UTENTE OGNI PAGINA
	 public void onStartPage(PdfWriter writer,Document document) {
		
	    }

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		
		/*
		 * METODO DELLA CLASSE PDF PAGE EVENT HELPER
		 * CHE FORNISCE DEI METODI PER GESTIRE DIVERSI EVENTI
		 * RELATIVE ALLE PAGINE DI UN PDF. HO UTILIZZATO IL METODO ONENDPAGE CHE VIENE
		 * CHIAMATO ALLA FINE DI OGNI PAGINA PDF. NEL METODO MI SONO PRESO IL NUMERO
		 * DELLA PAGINA CORRENTE CON WRITER.GETPAGENUMBER. POI HO FATTO UNA STRINGA E HO UTILIZZATO LA CLASSE
		 * PDF CONTENT BYTE PER POSIZIONARE LA STRINGA  ALL'INTERNO DEL DOCUMENTO
		 */

		try {
			// Crea un oggetto Phrase con il numero di pagina
			
			String pageText = "Pagina " + writer.getPageNumber();
			Phrase numeroPagina = new Phrase(pageText, new Font(Font.HELVETICA,10,Font.NORMAL));

			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, numeroPagina, document.right(), +15, 0);
			
			Chunk chunk = new Chunk();
			chunk.append(Costanti.FOOTER_PDF);
			chunk.setFont(new Font(Font.HELVETICA, 7, Font.NORMAL));
			chunk.setUnderline(2, 15);
			Phrase phrase = new Phrase(chunk);
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, phrase, (document.left()+document.right())/2, +30, 0);


			/*
			 * PDF CONTENT BYTE E UNA CLASSE UTLIZZATA PER MANIPOLARE CONTENUTO DEI PDF, CON WRITER CHE E UNA CLASSE CHE SI OCCUPA
			 * DELLA SCRITTURA DEL DOCUMENTO MI PRENDO UN ISTANZA DI PDFCONTENTBYTE ASSOCIATA A QUEL DOCUMENTO PERMETTENDOMI DI SCRIVERE
			 * NELLA PAGINA.
			 * BEGIN TEXT QUESTO METODO INIZIA UN NUOVO OGGETTO TESTO ALL'INTERNO DEL DOCUMENTO, PREPARANDOLO PER L'AGGIUNTA CHE FARO
			 * DOPO CON SHOW TEXTALIGNED 
			 */
//			PdfContentByte cb = writer.getDirectContent();
//			cb.beginText();
//			cb.setColorFill(color);
//			BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//			cb.setFontAndSize(helvetica, 10);
//			// public void showTextAligned(int alignment, String text, float x, float y, float rotation)
//			cb.showTextAligned(Element.ALIGN_CENTER, pageText, x, y, 0);    //AGGIUNTA NUMERO PAGINA
//			cb.endText();

			/*https://www.tabnine.com/code/java/methods/com.lowagie.text.pdf.PdfContentByte/showTextAligned
			 * document.open();
			This sample uses the "GOTHIC.TTF" font file located in the "Template" package
			BaseFont bf = BaseFont.createFont(GUI.class.getClass().getResource("/Template/GOTHIC.TTF") + "", BaseFont.WINANSI, BaseFont.EMBEDDED);	
			//set font type, size and color
			Font font = new Font(bf, 13.5f);	
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginText();
			canvas.setFontAndSize(bf, 10);
			//Method Usage: showTextAligned(Align, String, x, y, rotation);
			canvas.showTextAligned(Element.ALIGN_TOP, "My Text Here", 75, 40, 0);
			canvas.endText();			
			document.close();
			BASE FONT E UNA CLASSE DI BASE CHE RAPPRESENTA UN TIPO DI CARATTERE DI BASE, AD ES HELEVETICA, DEFINISCE QUINDI PROPRIETA
			BASE DI UN TIPO DI CARATTERE COME IL NOME DEL TIPO DI CARATTERE E L'ENCODING DEI CARATTERI (ASSOCIAZIONE TRA CODICI E CARATTERI)
			FONT E UNA CLASSE CHE RAPPRESENTA UN TIPO DI CARATTERE FORMATTATO CON ATTRIBUTI AGGIUNTIVI COME STILE,DIMENSIONE
			 */
	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


}
