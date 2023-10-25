package it.exolab.exobank.OPEN_PDF_APACHE_POI;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import it.exolab.exobank.doc.Method;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;

public class Pdf {

	

		 public byte[] createPdfFile(Dto<List<Transazione>> transazioni) {
			 

	 /*
		https://javadoc.io/doc/com.github.librepdf/openpdf/1.1.0/index.html
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
		        Document document = new Document(PageSize.A4);
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		        try {
		        	//- Collega il documento al flusso di output.
		            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		            document.open();

		            PdfPTable table = new PdfPTable(7); // Crea una tabella con 7 colonne
		            table.setWidthPercentage(100);	            
		            
		            // Creazione del titolo sopra la tabella
		            Font stileTitolo = new Font();
		            stileTitolo.setFamily("helvetica");
		            stileTitolo.setSize(30);
		            stileTitolo.setColor(20, 30, 50);
		            Paragraph titolo = new Paragraph("Elenco delle Transazioni", stileTitolo);     //nuovo paragrafo prende una stringa e un FONT
		            titolo.setAlignment(Element.ALIGN_CENTER);
		            document.add(titolo);
		            document.add(Chunk.NEWLINE); // Aggiunge un paragrafo vuoto

		            Font stileCella = new Font();
		            stileCella.setSize(10);
		            
		            // Imposta le intestazioni della tabella
		            String[] headerCells = {"NOME TITOLARE", "COGNOME TITOLARE", "NUMERO CONTO", "TIPO T.", "DATA T.", "IMPORTO", "CONTO BEN."};
		            
		            for (String header : headerCells) {
		                Font stileHeader = new Font(Font.HELVETICA, 12, Font.NORMAL);
		                PdfPCell cellRiga1 = new PdfPCell(new Paragraph(header, stileHeader));
		                cellRiga1.setBackgroundColor(new Color(255, 0, 0)); // Colore di sfondo rosso per l'intestazione
		                cellRiga1.setHorizontalAlignment(Element.ALIGN_CENTER); // Allineamento orizzontale al centro
		                table.addCell(cellRiga1);
		            }

		            // Popola la tabella con i dati delle transazioni
		            for (Transazione transazione : transazioni.getData()) {
			            PdfPCell cell0 = new PdfPCell(new Paragraph(transazione.getConto().getUtente().getNome(), stileCella));   // valore / font
			            table.addCell(cell0);
			            PdfPCell cell1 = new PdfPCell(new Paragraph(transazione.getConto().getUtente().getCognome(), stileCella));
			            table.addCell(cell1);
			            PdfPCell cell2 = new PdfPCell(new Paragraph(transazione.getConto().getNumeroConto(), stileCella));
			            table.addCell(cell2);
			            PdfPCell cell3 = new PdfPCell(new Paragraph(transazione.getTipo().getTipoTransazione(), stileCella));
			            table.addCell(cell3);
			            PdfPCell cell4 = new PdfPCell(new Paragraph(new Method().formattaData(transazione.getDataTransazione()), stileCella));
			            table.addCell(cell4);
			            PdfPCell cell5 = new PdfPCell(new Paragraph(new Method().formattaImporto(transazione), stileCella));
			            table.addCell(cell5);
			            if (transazione.getContoBeneficiario() != null) {
				            PdfPCell cell6 = new PdfPCell(new Paragraph(transazione.getContoBeneficiario().getNumeroConto(), stileCella));
		                    table.addCell(cell6);
		                } else {
		                    table.addCell("-----");
		                }
		            }
		            document.add(table);
		            document.close();
		        } catch (DocumentException e) {
		            e.printStackTrace();
		        }

		        return outputStream.toByteArray();
		    }
		
	
	
	
	
	
	
	
}
