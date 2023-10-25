package it.exolab.exobank.controller;

import javax.ejb.Local;

@Local
public interface ScacchieraControllerInterface {
	
	Scacchiera scacchieraIniziale(Scacchiera scacchieraIniziale); // Ottieni la scacchiera iniziale

    boolean movimentoValido(Scacchiera scacchieraCorrente, Scacchiera scacchieraAggiornata);

    Scacchiera faiMovimento(Scacchiera scacchieraCorrente,  Scacchiera scacchieraAggiornata) throws InvalidMoveException;
}