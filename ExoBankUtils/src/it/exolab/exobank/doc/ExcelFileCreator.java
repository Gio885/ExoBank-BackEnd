package it.exolab.exobank.doc;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.dto.Dto;
import it.exolab.exobank.models.Transazione;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelFileCreator {
	
	
	
	/*
	 * 	//woorkbook e un interfaccia implementata da xssfworkbook
	//XSSFWorkbook è una classe fornita da Apache POI per rappresentare un workbook Excel in formato XLSX.
	//Workbook workbook = new XSSFWorkbook(): Qui viene creato un nuovo oggetto workbook di tipo XSSFWorkbook. 
	//Questo oggetto rappresenta il workbook Excel 
	//heet sheet = workbook.createSheet("Dati"): Dopo aver creato il workbook, stai creando un nuovo foglio di lavoro (worksheet) 
	//all'interno del workbook 

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
	        Workbook progettoExcel = new XSSFWorkbook();
	        Sheet foglioExcel = progettoExcel.createSheet("Lista transazioni");

	        String[] headerCells = {Costanti.NOME_TITOLARE,Costanti.COGNOME_TITOLARE,Costanti.NUMERO_CONTO, Costanti.TIPO_TRANSAZIONE,Costanti.DATA_TRANSAZIONE,Costanti.IMPORTO_TRANSAZIONE,Costanti.CONTO_BENEFICIARIO};

	        setLarghezzaColonne(foglioExcel,headerCells,35);
      
	        Row rigaTitolo = foglioExcel.createRow(0);			//CREO UNA RIGA SUL FOGLIO 	
	        rigaTitolo.setHeightInPoints(30);      
	        Font fontTitolo = progettoExcel.createFont();             //CREO UN FONT PER IL TITOLO
	        fontTitolo.setFontHeightInPoints((short) 16); 
	        fontTitolo.setBold(true);      
	        Font fontBold = progettoExcel.createFont();               //CREAZIONE FONT PER LE CELLE D'INTESTAZIONE DELLA TABELLA
	        fontBold.setBold(true);
	        	        
	        CellStyle stileTitolo = creazioneStileCelle(progettoExcel,fontTitolo,IndexedColors.WHITE,FillPatternType.NO_FILL, VerticalAlignment.CENTER, HorizontalAlignment.CENTER); //CREO STILE TITOLO
	        Cell cellaTitolo = rigaTitolo.createCell(3);             //CREO LA CELLA PER IL TITOLO E SETTO LO STILE E IL VALORE
	        cellaTitolo.setCellValue("Elenco Transazioni");
	        cellaTitolo.setCellStyle(stileTitolo);               
	        CellStyle stileIntestazione = creazioneStileCelle(progettoExcel,fontBold, IndexedColors.YELLOW, FillPatternType.SOLID_FOREGROUND, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
	        creazioneCelleIntestazioneConStile(foglioExcel,stileIntestazione);     //SOPRA CREO LO STILE E SOTTO CREO LE CELLE CON LO STILE
	        CellStyle stileCelleDati = creazioneStileCelle(progettoExcel,fontBold ,IndexedColors.AUTOMATIC,FillPatternType.NO_FILL, VerticalAlignment.CENTER, HorizontalAlignment.JUSTIFY);
	        creazioneCelleDatiConStile(foglioExcel, lista, stileCelleDati);

	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        progettoExcel.write(outputStream);
	        return outputStream.toByteArray();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new IOException(Costanti.ERRORE_CONTATTA_AMMINISTRATORE);
	    }
	}
	

      
	private CellStyle creazioneStileCelle(Workbook progettoExcel,Font font,IndexedColors sfondoCella,FillPatternType tipoRiempimento,VerticalAlignment allineamentoVerticale,HorizontalAlignment allineamentoOrizzontale) {
		CellStyle stileCella = progettoExcel.createCellStyle();                 //CREAZIONE STILE CELLE INTESTAZIONE TABELLA
		stileCella.setFillBackgroundColor(sfondoCella.getIndex());            	//SFONDO CELLE INTESTAZIONE
		stileCella.setFillPattern(tipoRiempimento);							//TIPO RIMPIEMPIMENTO 
		stileCella.setVerticalAlignment(allineamentoVerticale);				//ALLINEAMENTO VERTICALE
		stileCella.setAlignment(allineamentoOrizzontale);                	//ALLINEAMENTO ORIZZONTALE
		stileCella.setFont(font);
		return stileCella;
	}
	
	
	//PER OGNI COLONNA DOPO UNA DIMENSIONE E MOLTIPLICO PER 256, 256 IN EXCEL RAPPRESENTA LA LUNGHEZZA DI UN CARATTERE, E UN UNITA
	//DI MISURA
	private void setLarghezzaColonne(Sheet foglioExcel,String[] headerCells,int dimensioneSpazioDopoUltimoCarattere) {
	    for (int i = 0; i < headerCells.length; i++) {
	        foglioExcel.setColumnWidth(i, dimensioneSpazioDopoUltimoCarattere * 256);
	    }
	}
	
	private void creazioneCelleIntestazioneConStile(Sheet foglioExcel, CellStyle stileIntestazione) {
	    String[] headerCells = {Costanti.NOME_TITOLARE,Costanti.COGNOME_TITOLARE,Costanti.NUMERO_CONTO, Costanti.TIPO_TRANSAZIONE,Costanti.DATA_TRANSAZIONE,Costanti.IMPORTO_TRANSAZIONE,Costanti.CONTO_BENEFICIARIO};
	    Row rigaIntestazione = foglioExcel.createRow(1);
	    for (int i = 0; i < headerCells.length; i++) {
	        Cell cell = rigaIntestazione.createCell(i);
	        cell.setCellValue(headerCells[i]);
	        cell.setCellStyle(stileIntestazione);
	    }
	}
    
	private void creazioneCelleDatiConStile(Sheet foglioExcel, Dto<List<Transazione>> lista, CellStyle stileCelle) {
	    for (int i = 0; i < lista.getData().size(); i++) {
	        Transazione transazione = lista.getData().get(i);
	        Row riga = foglioExcel.createRow(i + 2);
	        if (transazione.getTipo().getId() != Costanti.TIPO_DEPOSITO) {
	            riga.createCell(5).setCellValue(new Method().formattaImporto(transazione));
	           // riga.createCell(5).setCellStyle(new CellStyle().setFont(new XSSFFont().setThemeColor(HSSFColor.HSSFColorPredefined.AUTOMATIC)));
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
	}
    
    
    

    	
    	
    
    
    
    
}