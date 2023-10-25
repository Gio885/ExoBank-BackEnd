package it.exolab.exobank.controller;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.costanti.Costanti;

@Stateless(name="ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface{

	    private MoveValidator validator; // Validatore delle mosse


	    @Override
	    public Scacchiera scacchieraIniziale(Scacchiera scacchieraIniziale) {
	        // Imposta la scacchiera iniziale con i dati ricevuti dal frontend
	        return scacchieraIniziale;
	    }

	    @Override
	    public boolean isMoveValid(Scacchiera scacchieraCorrente, Scacchiera scacchieraAggiornata) {
	        // Verifica se la mossa è valida confrontando le due scacchiere
	        return validator.isValidMove(currentBoard, newBoard);
	    }

	    @Override
	    public ChessBoard makeMove(Scacchiera scacchieraCorrente, Scacchiera scacchieraAggiornata) throws InvalidMoveException {
	        if (isMoveValid(currentBoard, newBoard)) {
	            // Esegui la mossa e aggiorna la scacchiera corrente
	            currentBoard.updateBoard(newBoard);
	            return currentBoard;
	        } else {
	            throw new InvalidMoveException("Mossa non valida");
	        }
	    }
	    
	    private Pezzo determinaMovimento(Scacchiera scacchieraCorrente, Scacchiera scacchieraAggiornata) {
	        for (int riga = 0; riga < scacchieraCorrente.getRows(); riga++) {
	            for (int colonna = 0; colonna < scacchieraCorrente.getColumns(); colonna++) {
	                Pezzo pezzoCorrente = pezzoInPosizione(scacchieraCorrente, riga, colonna);
	                Pezzo nuovoPezzo = pezzoInPosizione(scacchieraAggiornata, riga, colonna);

	                if (pezzoCorrente != null && nuovoPezzo != null && !pezzoCorrente.equals(nuovoPezzo)) {
	                    // Se esiste un pezzo nella posizione corrente della scacchiera
	                    // ed è diverso da quello nella nuova scacchiera, restituisci il pezzo spostato
	                    return pezzoCorrente;
	                }
	            }
	        }

	        return null; // Se nessun pezzo è stato spostato, restituisci null
	    }
	    
	    private Pezzo pezzoInPosizione(Scacchiera scacchiera, int riga, int colonna) {
	    	if (riga < 0 || riga > Costanti.RIGHE || colonna < 0 || colonna > Costanti.COLONNE) {
	    		throw new Exception("La posizione non è valida OUTOFBOUND");
	    	}else {
	    		return scacchiera.getGriglia
	    	}
	    }
}