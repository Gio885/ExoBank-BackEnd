package it.exolab.exobank.OPEN_PDF_APACHE_POI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import it.exolab.exobank.doc.Method;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;

public class Word {

	
	public byte[] createWordFile(Dto<List<Transazione>> transazioni) throws IOException {
	    try {
	        // Crea il documento Word in memoria
	        XWPFDocument document = new XWPFDocument();

	        // Crea un paragrafo per il titolo
	        XWPFParagraph titleParagraph = document.createParagraph();
	        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
	        XWPFRun titleRun = titleParagraph.createRun();
	        titleRun.setBold(true);
	        titleRun.setFontSize(16);
	        titleRun.setText("Elenco delle Transazioni");

	        // Crea una tabella per i dati delle transazioni
	        XWPFTable table = document.createTable(transazioni.getData().size() + 1, 7); // +1 per l'intestazione

	        // STILE TABELLA      getCTTBL PRENDE LO STILE DELLA TABLE // getTblPr PRENDE LE PROPRIETA di formattazione DELLA TABELLA
	        // addNewTblBorders aggiunge un elemento TblBorders alla tabella.
	        //OGNI CELLA A IL PROPRIO BORDO
	        CTTblBorders tblBorders = table.getCTTbl().getTblPr().addNewTblBorders();
	        tblBorders.addNewInsideH().setVal(STBorder.SINGLE);     //BORDO SINGOLO ORIZZONTALE
	        tblBorders.addNewInsideV().setVal(STBorder.SINGLE);		//BORDO SINGOLO VERTICALE
	        tblBorders.addNewTop().setVal(STBorder.SINGLE);         //BORDO SINGOLO SUPERIORE
	        tblBorders.addNewBottom().setVal(STBorder.SINGLE);      //BORDO SINGOLO INFERIORE

	        // PRIMA RIGA TABELLA
	        XWPFTableRow headerRow = table.getRow(0);
	        String[] headers = {"NOME TITOLARE", "COGNOME TITOLARE", "NUMERO CONTO", "TIPO T.", "DATA T.", "IMPORTO", "CONTO BEN."};
	        for (int i = 0; i < headers.length; i++) {
	            XWPFTableCell cell = headerRow.getCell(i);
	            cell.setText(headers[i]);
	            cell.setColor("D9D9D9"); // Colore di sfondo grigio per l'intestazione
	            cell.getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER); // Centra il testo nella cella
	        }

	        // CICLO TRANSAZIONI --- RIGA
	        for (int i = 0; i < transazioni.getData().size(); i++) {
	            Transazione transazione = transazioni.getData().get(i);
	            XWPFTableRow dataRow = table.getRow(i + 1);

	            // CICLO OGNI CELLA DELLA RIGA
	            for (int j = 0; j < 7; j++) {
	                XWPFTableCell cell = dataRow.getCell(j);
	                String cellValue = "";
	                switch (j) {
	                    case 0:
	                        cellValue = transazione.getConto().getUtente().getNome();
	                        break;
	                    case 1:
	                        cellValue = transazione.getConto().getUtente().getCognome();
	                        break;
	                    case 2:
	                        cellValue = transazione.getConto().getNumeroConto();
	                        break;
	                    case 3:
	                        cellValue = transazione.getTipo().getTipoTransazione();
	                        break;
	                    case 4:
	                        cellValue = new Method().formattaData(transazione.getDataTransazione());
	                        break;
	                    case 5:
	                        cellValue = new Method().formattaImporto(transazione);
	                        break;
	                    case 6:
	                        cellValue = transazione.getContoBeneficiario() != null ? transazione.getContoBeneficiario().getNumeroConto() : "-----";
	                        break;
	                }
	                cell.setText(cellValue);// SETTA IL VALORE E CENTRA IL CONTENUTO NELLA CELLA
	                cell.getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER); 
	            }
	        }
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        document.write(outputStream);

	        return outputStream.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new IOException("C'è stato un errore nella creazione del documento Word");
	    }
	}
	
	
	
	
	
	
	
	
	
}
