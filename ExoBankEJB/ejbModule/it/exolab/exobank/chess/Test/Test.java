package it.exolab.exobank.chess.Test;

import it.exolab.exobank.controller.ScacchieraController;
import it.exolab.exobank.costanti.Costanti;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;

import java.util.Scanner;

public class Test {

    private static void visualizzaScacchiera(Scacchiera scacchiera) {
        Pezzo[][] griglia = scacchiera.getGriglia();

        System.out.println("Scacchiera attuale:");
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Pezzo pezzo = griglia[x][y];
                if (pezzo == null) {
                    System.out.print("-----\t");
                } else {
                    System.out.print(pezzo.getId() + " " + pezzo.getPosizioneX() + " " + pezzo.getPosizioneY() + "\t");
                }
            }
            System.out.println();
        }
    }

    private static boolean partitaTerminata(Scacchiera scacchiera) {
        // Implementa la logica per verificare se la partita è terminata
        // Ad esempio, verifica se uno dei re è stato catturato
        return false; // Modificare questa logica
    }

    public static void main(String[] args) {
        ScacchieraController scacchieraController = new ScacchieraController();
        Scacchiera scacchiera;
        try {
            scacchiera = scacchieraController.scacchieraIniziale();
        } catch (Exception e) {
            e.printStackTrace();
            return; // In caso di errore nella creazione della scacchiera, termina il programma
        }

        boolean isTurnWhite = true; // True se è il turno del giocatore bianco, altrimenti è il turno del giocatore nero
        Scanner scanner = new Scanner(System.in);

        System.out.println("Benvenuti in questa simulazione di una partita di scacchi!");

        while (true) {
            // Mostra la scacchiera attuale
            visualizzaScacchiera(scacchiera);

            // Determina il colore del giocatore di turno
            String coloreGiocatore = isTurnWhite ? Costanti.BIANCO : Costanti.NERO;

            System.out.println(coloreGiocatore + ", inserisci la tua mossa (esempio: '1 2 3' per spostare il pezzo 1 a (2, 3)): ");
            int pezzoId = scanner.nextInt();
            int xDestinazione = scanner.nextInt();
            int yDestinazione = scanner.nextInt();
            
            // Verifica se la mossa è consentita
            Pezzo pezzo = new Pezzo(coloreGiocatore, xDestinazione, yDestinazione, pezzoId, true);
            try {
                scacchiera = scacchieraController.mossaConsentita(pezzo);
                System.out.println("Mossa consentita. Eseguo la mossa...");
            } catch (Exception e) {
                System.out.println("Mossa non consentita. Riprova.");
                continue; // Salta il resto del ciclo e chiedi un'altra mossa
            }

            // Controlla se la partita è terminata (ad esempio, uno dei re è stato catturato)
            if (partitaTerminata(scacchiera)) {
                visualizzaScacchiera(scacchiera);
                System.out.println("La partita è terminata. " + coloreGiocatore + " vince!");
                break;
            }

            isTurnWhite = !isTurnWhite; // Cambia il turno
        }

        scanner.close();
    }
}