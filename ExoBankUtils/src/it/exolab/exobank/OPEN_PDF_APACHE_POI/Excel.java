package it.exolab.exobank.OPEN_PDF_APACHE_POI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.doc.Method;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;

public class Excel {

	//woorkbook e un interfaccia implementata da xssfworkbook
	//XSSFWorkbook è una classe fornita da Apache POI per rappresentare un workbook Excel in formato XLSX.
	//Workbook workbook = new XSSFWorkbook(): Qui viene creato un nuovo oggetto workbook di tipo XSSFWorkbook. 
	//Questo oggetto rappresenta il workbook Excel 
	//heet sheet = workbook.createSheet("Dati"): Dopo aver creato il workbook, stai creando un nuovo foglio di lavoro (worksheet) 
	//all'interno del workbook 
/*
1. *HSSFWorkbook / XSSFWorkbook (Workbook)*: Questi oggetti rappresentano il libro Excel nel formato binario (XLS) e nel formato OOXML (XLSX),
 rispettivamente. Un oggetto Workbook è il contenitore principale per fogli di calcolo, stili, formule e altre informazioni relative a un 
 documento Excel.
 
2. *HSSFSheet / XSSFSheet (Sheet)*: Questi oggetti rappresentano fogli di calcolo all'interno di un libro Excel nel formato binario (HSSF)
e nel formato OOXML (XSSF). Un oggetto Sheet contiene celle e dati effettivi.

3. *HSSFRow / XSSFRow (Row)*: Questi oggetti rappresentano righe all'interno di un foglio di calcolo HSSFSheet o XSSFSheet. 
Possono contenere celle con dati.

4. *HSSFCell / XSSFCell (Cell)*: Questi oggetti rappresentano celle all'interno di una riga HSSFRow o XSSFRow. 
Le celle contengono i dati effettivi, come numeri, testo o formule.

5. *HSSFCellStyle / XSSFCellStyle (CellStyle)*: Questi oggetti gestiscono stili e formattazione per le celle all'interno di un foglio 
di calcolo. Puoi utilizzare CellStyle per applicare stili come colori di sfondo, colori del testo e allineamento.

6. *HSSFRichTextString / XSSFRichTextString (RichTextString)*: Questi oggetti gestiscono testo ricco all'interno di una cella, 
consentendo la formattazione del testo con diversi stili e colori.

7. *HSSFFormulaEvaluator / XSSFFormulaEvaluator (FormulaEvaluator)*: Questi oggetti valutano le formule all'interno delle celle di un 
foglio di calcolo, calcolando i risultati delle formule.

8. *HSSFPicture / XSSFPicture (Picture)*: Questi oggetti rappresentano immagini all'interno di un foglio di calcolo. 
Possono essere utilizzati per aggiungere, leggere o manipolare immagini in un foglio di calcolo.

Il meccanismo dietro a questi oggetti coinvolge la creazione di un Workbook per rappresentare un documento Excel, 
l'aggiunta di fogli di calcolo al Workbook, la creazione di righe e celle nei fogli di calcolo e la manipolazione dei dati all'interno 
delle celle. Gli oggetti di stile, formula e immagine consentono di gestire stili, formule e immagini all'interno del foglio di calcolo.
 */   
	public byte[] createExcelFile(Dto<List<Transazione>> lista) throws IOException {
	    try {
	        // Crea il file Excel in memoria
	        Workbook progettoExcel = new XSSFWorkbook();
	        Sheet foglioExcel = progettoExcel.createSheet("Lista transazioni");

	        String[] headerCells = {"NOME TITOLARE", "COGNOME TITOLARE", "NUMERO CONTO", "TIPO TRANSAZIONE", "DATA TRANSAZIONE", "IMPORTO", "CONTO BENEFICIARIO"};

	        
 // NB creazione oggetto riga con il foglio excel e dall'oggetto riga creo la cella che poi andro ad utilizzare per settare il valore e lo stile
	        
	     // Aggiungi un titolo sopra la tabella
	        Row titoloRow = foglioExcel.createRow(0);				// aggiungo un riga al foglio excel per il titolo
	        titoloRow.setHeightInPoints(30);                       // altezza riga del titolo

	        CellStyle stileTitolo = progettoExcel.createCellStyle();       //creo uno stile con l'oggetto progetto di tipo cellstyle 
	        Font fontTitolo = progettoExcel.createFont();                  //creo un font con l'oggetto progetto di tipo font
	        fontTitolo.setBold(true);
	        fontTitolo.setFontHeightInPoints((short) 16);           // Imposta la dimensione del font per il titolo
	        stileTitolo.setFont(fontTitolo);
	        stileTitolo.setAlignment(HorizontalAlignment.CENTER);
	        stileTitolo.setVerticalAlignment(VerticalAlignment.CENTER);

	        Cell titoloCell = titoloRow.createCell(3);         // Crea una cella nella riga del titolo colonna 3
	        titoloCell.setCellValue("Elenco Transazioni");      // Testo del titolo
	        titoloCell.setCellStyle(stileTitolo);         
	        
	        //STILE RIGA INTESTAZIONE
	        CellStyle stileIntestazione = progettoExcel.createCellStyle();					//CREAZIONE STILE CELLA
	        stileIntestazione.setFillForegroundColor(IndexedColors.YELLOW.getIndex());    	//SFONDO GIALLO
	        stileIntestazione.setFillPattern(FillPatternType.SOLID_FOREGROUND);        //RIMPIEMPIMENTO SOLID
	        stileIntestazione.setVerticalAlignment(VerticalAlignment.CENTER);          //ALLINEAMENTO VERTICALE DEL CONTENUTO
	        stileIntestazione.setAlignment(HorizontalAlignment.CENTER);     //ALLINEAMENTO ORIZZONTALE DEL CONTENUTO
	        Font fontIntestazione = progettoExcel.createFont();               //CREAZIONE FONT CON ASSEGNAZIONE ALLA CELLA
	        fontIntestazione.setBold(true);
	        stileIntestazione.setFont(fontIntestazione);

	        //STILE CELLE DATI
	        CellStyle stileCelle = progettoExcel.createCellStyle();
	        stileCelle.setAlignment(HorizontalAlignment.CENTER);
	        stileCelle.setVerticalAlignment(VerticalAlignment.CENTER);

	        //LARGHEZZA COLONNE
	        for (int i = 0; i < headerCells.length; i++) {
	            foglioExcel.setColumnWidth(i, 30 * 256); 
	        }
	        
	        //CICLO LA RIGA_INTESTAZIONE E GLI ASSEGNO LO STILE E IL CONTENUTO CHE SI TROVA NELL ARRAY DI STRINGHE
	        Row rigaIntestazione = foglioExcel.createRow(1);
	        for (int i = 0; i < headerCells.length; i++) {
	            Cell cell = rigaIntestazione.createCell(i);
	            cell.setCellValue(headerCells[i]);
	            cell.setCellStyle(stileIntestazione);
	        }

	        //RIEMPIO LA TABELLA CON I DATI DELLA LISTA E GLI SETTO LO STILE
	        for (int i = 0; i < lista.getData().size(); i++) {
	            Transazione transazione = lista.getData().get(i);
	            Row riga = foglioExcel.createRow(i + 2);
	            if (transazione.getTipo().getId() != Costanti.TIPO_DEPOSITO) {
	                riga.createCell(5).setCellValue(new Method().formattaImporto(transazione));
	            }
	            riga.createCell(0).setCellValue(transazione.getConto().getUtente().getNome());
	            riga.getCell(0).setCellStyle(stileCelle);
	            riga.createCell(1).setCellValue(transazione.getConto().getUtente().getCognome());
	            riga.getCell(1).setCellStyle(stileCelle);
	            riga.createCell(2).setCellValue(transazione.getConto().getNumeroConto());
	            riga.getCell(2).setCellStyle(stileCelle);
	            riga.createCell(3).setCellValue(transazione.getTipo().getTipoTransazione());
	            riga.getCell(3).setCellStyle(stileCelle);
	            riga.createCell(4).setCellValue(new Method().formattaData(transazione.getDataTransazione()));
	            riga.getCell(4).setCellStyle(stileCelle);
	            riga.createCell(5).setCellValue(new Method().formattaImporto(transazione));
	            riga.getCell(5).setCellStyle(stileCelle);
	            if (null != transazione.getContoBeneficiario()) {
	                riga.createCell(6).setCellValue(transazione.getContoBeneficiario().getNumeroConto());
	            } else {
	                riga.createCell(6).setCellValue("-----");
	            }
	            riga.getCell(6).setCellStyle(stileCelle);
	        }

	        // VIENE APERTO UN CANALE DI STREAM DI OUTPUT DI BYTE
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        progettoExcel.write(outputStream);

	        return outputStream.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new IOException("C'è stato un errore nella creazione dell'Excel");
	    }
	}
    
	
	
	
	
	
	
}
