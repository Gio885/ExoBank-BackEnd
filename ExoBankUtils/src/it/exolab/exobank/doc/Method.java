package it.exolab.exobank.doc;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.models.Transazione;

public class Method {

	public String formattaData(Date data) {
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		String dataFormattata = formatoData.format(data);
		return dataFormattata;
	}

	public String formattaImporto(Transazione transazione) {
		String importoFormattato = String.format("%.2f", transazione.getImporto()); // % inizio formattazione 2 indica
																					// cifre f float decimale
		if (transazione.getTipo().getId() != Costanti.TIPO_DEPOSITO) {
			return "- € " + importoFormattato;
		} else {
			return " € " + importoFormattato;
		}

	}
}
