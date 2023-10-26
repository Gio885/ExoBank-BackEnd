package it.exolab.exobank.controller;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import it.exolab.exobank.chess.model.Pedone;
import it.exolab.exobank.chess.model.Torre;
import it.exolab.exobank.chess.model.Re;
import it.exolab.exobank.chess.model.Regina;
import it.exolab.exobank.chess.model.Cavallo;
import it.exolab.exobank.chess.model.Alfiere;
import it.exolab.exobank.chess.model.Pezzo;
import it.exolab.exobank.chess.model.Scacchiera;
import it.exolab.exobank.costanti.Costanti;

@Stateless(name = "ScacchieraControllerInterface")
@LocalBean
public class ScacchieraController implements ScacchieraControllerInterface {

	private Scacchiera scacchieraLavoro = new Scacchiera();
	@Override
	public Scacchiera scacchieraIniziale() {
		Scacchiera scacchieraIniziale = creazioneScacchieraIniziale();
		return scacchieraIniziale;
	}

//	@Override
//	public boolean mossaConsentita(Pezzo pezzo) {
//		// Richiama il metodo findPezzoById per ottenere l'istanza specifica del pezzo
//		Pezzo pezzoSpecifico = (Pezzo) findPezzoById(pezzo, scacchieraLavoro);
//
//		Pezzo[][] griglia = scacchieraLavoro.getGriglia();
//		int x = pezzoSpecifico.getPosizioneX();
//		int y = pezzoSpecifico.getPosizioneY();
//
//		if (pezzoSpecifico instanceof Torre) {
//			/*Logica per verificare se la mossa è consentita per una Torre
//            //Ad esempio, verifica se la mossa è orizzontale o verticale e se la strada è libera
//
//            int newX = pezzo.getPosizioneX();
//            int newY = pezzo.getPosizioneY();
//
//            if (...) {
//                //Aggiorna la scacchiera con la nuova posizione
//                griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//                griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//                pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//                pezzoSpecifico.setPosizioneY(newY);
//                return true; // La mossa è consentita
//            }*/
//		} else if (pezzoSpecifico instanceof Alfiere) {
//			/*Logica per verificare se la mossa è consentita per un Alfiere
//
//            int newX = pezzo.getPosizioneX();
//            int newY = pezzo.getPosizioneY();
//
//            if (...) {
//                //Aggiorna la scacchiera con la nuova posizione
//                griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//                griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//                pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//                pezzoSpecifico.setPosizioneY(newY);
//                return true; // La mossa è consentita
//            }*/
//		} else if (pezzoSpecifico instanceof Re) {
//			/*Logica per verificare se la mossa è consentita per un Alfiere
//
//            int newX = pezzo.getPosizioneX();
//            int newY = pezzo.getPosizioneY();
//
//            if (...) {
//                //Aggiorna la scacchiera con la nuova posizione
//                griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//                griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//                pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//                pezzoSpecifico.setPosizioneY(newY);
//                return true; // La mossa è consentita
//            }*/
//		}else if (pezzoSpecifico instanceof Regina) {
//			/*Logica per verificare se la mossa è consentita per un Alfiere
//
//            int newX = pezzo.getPosizioneX();
//            int newY = pezzo.getPosizioneY();
//
//            if (...) {
//                //Aggiorna la scacchiera con la nuova posizione
//                griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//                griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//                pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//                pezzoSpecifico.setPosizioneY(newY);
//                return true; // La mossa è consentita
//            }*/
//		}else if (pezzoSpecifico instanceof Cavallo) {
//			/*Logica per verificare se la mossa è consentita per un Alfiere
//
//            int newX = pezzo.getPosizioneX();
//            int newY = pezzo.getPosizioneY();
//
//            if (...) {
//                //Aggiorna la scacchiera con la nuova posizione
//                griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//                griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//                pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//                pezzoSpecifico.setPosizioneY(newY);
//                return true; // La mossa è consentita
//            }*/
//		}else if (pezzoSpecifico instanceof Pedone) {
//			//Logica per verificare se la mossa è consentita per un Alfiere
//
//			int newX = pezzo.getPosizioneX();
//			int newY = pezzo.getPosizioneY();
//
//			if (controllaMovimentoPedone(pezzoSpecifico, newX, newY, scacchieraLavoro)) {
//				//Aggiorna la scacchiera con la nuova posizione
//				griglia[x][y] = null; // Rimuovi il pezzo dalla posizione precedente
//				griglia[newX][newY] = pezzoSpecifico; // Sposta il pezzo nella nuova posizione
//				scacchieraLavoro.setScacchiera(griglia);
//				pezzoSpecifico.setPosizioneX(newX); // Aggiorna le coordinate del pezzo
//				pezzoSpecifico.setPosizioneY(newY);
//				return true; // La mossa è consentita
//			}
//		}
//
//		return false; // Restituisci true se la mossa è consentita, altrimenti false
//	}

	// Metodo per creare la scacchiera iniziale
	private Scacchiera creazioneScacchieraIniziale() {
		Pezzo[][] griglia = new Pezzo[8][8];
		int idPezzo = 1;

		// Inizializza la prima riga (0) con i pezzi iniziali (torre, cavallo, alfiere, re, regina, alfiere, cavallo, torre)
		griglia[0][0] = new Torre(Costanti.BIANCO, 0, 0, idPezzo++, true);
		griglia[0][1] = new Cavallo(Costanti.BIANCO, 0, 1, idPezzo++, true);
		griglia[0][2] = new Alfiere(Costanti.BIANCO, 0, 2, idPezzo++, true);
		griglia[0][3] = new Re(Costanti.BIANCO, 0, 3, idPezzo++, true);
		griglia[0][4] = new Regina(Costanti.BIANCO, 0, 4, idPezzo++, true);
		griglia[0][5] = new Alfiere(Costanti.BIANCO, 0, 5, idPezzo++, true);
		griglia[0][6] = new Cavallo(Costanti.BIANCO, 0, 6, idPezzo++, true);
		griglia[0][7] = new Torre(Costanti.BIANCO, 0, 7, idPezzo++, true);

		// Inizializza la seconda riga (1) con i pedoni bianchi
		for (int colonna = 0; colonna < 8; colonna++) {
			griglia[1][colonna] = new Pedone(Costanti.BIANCO, 1, colonna, idPezzo++, true);
		}

		// Inizializza le righe 2-5 con caselle vuote
		for (int riga = 2; riga < 6; riga++) {
			for (int colonna = 0; colonna < 8; colonna++) {
				griglia[riga][colonna] = null; // Casella vuota
			}
		}

		// Inizializza la sesta riga (6) con i pedoni neri
		for (int colonna = 0; colonna < 8; colonna++) {
			griglia[6][colonna] = new Pedone(Costanti.NERO, 6, colonna, idPezzo++, true);
		}

		// Inizializza l'ultima riga (7) con i pezzi iniziali neri (torre, cavallo, alfiere, re, regina, alfiere, cavallo, torre)
		griglia[7][0] = new Torre(Costanti.NERO, 7, 0, idPezzo++, true);
		griglia[7][1] = new Cavallo(Costanti.NERO, 7, 1, idPezzo++, true);
		griglia[7][2] = new Alfiere(Costanti.NERO, 7, 2, idPezzo++, true);
		griglia[7][3] = new Re(Costanti.NERO, 7, 3, idPezzo++, true);
		griglia[7][4] = new Regina(Costanti.NERO, 7, 4, idPezzo++, true);
		griglia[7][5] = new Alfiere(Costanti.NERO, 7, 5, idPezzo++, true);
		griglia[7][6] = new Cavallo(Costanti.NERO, 7, 6, idPezzo++, true);
		griglia[7][7] = new Torre(Costanti.NERO, 7, 7, idPezzo++, true);

		Scacchiera scacchiera = new Scacchiera();
		scacchiera.setScacchiera(griglia);
		scacchieraLavoro.setScacchiera(griglia);
		return scacchiera;
	}

	private Object findPezzoById(Pezzo pezzo, Scacchiera scacchiera) {
		Pezzo[][] griglia = scacchiera.getGriglia();
		Integer idPezzo = pezzo.getId();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (null != griglia[x][y] && griglia[x][y].getId() == idPezzo) {
					if (griglia[x][y] instanceof Torre) {
						return (Torre) griglia[x][y];
					} else if (griglia[x][y] instanceof Alfiere) {
						return (Alfiere) griglia[x][y];
					}  else if (griglia[x][y] instanceof Re) {
						return (Re) griglia[x][y];
					}  else if (griglia[x][y] instanceof Regina) {
						return (Regina) griglia[x][y];
					}  else if (griglia[x][y] instanceof Cavallo) {
						return (Cavallo) griglia[x][y];
					}  else if (griglia[x][y] instanceof Pedone) {
						return (Pedone) griglia[x][y];
					} 
				}
			}
		}
		return null; // Se nessun pezzo con l'ID specifico è stato trovato
	} 


//	private boolean controllaMovimentoPedone(Pezzo pedone, Integer newX, Integer newY, Scacchiera scacchiera) {
//		Integer x = pedone.getPosizioneX();
//		Integer y = pedone.getPosizioneY();
//		String colore = pedone.getColore();
//		if(x != newX && y != newY) {
//			if (colore.equals(Costanti.BIANCO)) {
//				// Pedone bianco può avanzare solo in direzione crescente delle righe
//				if (newX == x + 1) {
//					// Il pedone può avanzare di una casa in avanti
//					if (newY == y) {
//						// La nuova posizione è direttamente in avanti
//						if (validaPosizione(pedone, scacchiera, newX, newY)) {
//							return true;
//						}
//					} else if ((newY == y - 1 || newY == y + 1) && validaPosizione(pedone, scacchiera, newX, newY)) {
//						// La nuova posizione è una delle caselle oblique e contiene un pezzo avversario
//						return true;
//					}
//				} else if (newX == x + 2 && x == 1) {
//					// Il pedone può avanzare di due case solo dalla posizione iniziale (riga 1)
//					if (newY == y && validaPosizione(pedone, scacchiera, newX, newY) &&
//							validaPosizione(pedone, scacchiera, newX - 1, newY)) {
//						return true;
//					}
//				}
//			} else if (colore.equals(Costanti.NERO)) {
//				// Pedone nero può avanzare solo in direzione decrescente delle righe
//				if (newX == x - 1) {
//					// Il pedone può avanzare di una casa in avanti
//					if (newY == y) {
//						// La nuova posizione è direttamente in avanti
//						if (validaPosizione(pedone, scacchiera, newX, newY)) {
//							return true;
//						}
//					} else if ((newY == y - 1 || newY == y + 1) && validaPosizione(pedone, scacchiera, newX, newY)) {
//						// La nuova posizione è una delle caselle oblique e contiene un pezzo avversario
//						return true;
//					}
//				} else if (newX == x - 2 && x == 6) {
//					// Il pedone può avanzare di due case solo dalla posizione iniziale (riga 6)
//					if (newY == y && validaPosizione(pedone, scacchiera, newX, newY) &&
//							validaPosizione(pedone, scacchiera, newX + 1, newY)) {
//						return true;
//					}
//				}
//			}
//		}else {
//			throw new Exception ("Non puoi non fare movimenti"); 
//		}
//
//		return false; // La mossa non è consentita
//	}
//	
//	private boolean controllaMovimentoTorre(Torre torre, Integer newX, Integer newY, Scacchiera scacchiera) {
//	    Integer x = torre.getPosizioneX();
//	    Integer y = torre.getPosizioneY();
//	    String colore = torre.getColore();
//
//	    // Controllo che la torre si stia muovendo sia orizzontalmente sia verticalmente
//	    if ((!x.equals(newX) && !y.equals(newY)) || (x.equals(newX) && y.equals(newY))) {
//	        return false;
//	    }
//
//	    // Controllo delle caselle orizzontali
//	    if (x.equals(newX)) {
//	        int minY = Math.min(y, newY);
//	        int maxY = Math.max(y, newY);
//	        for (int i = minY + 1; i < maxY; i++) {
//	            if (scacchiera.getGriglia()[x][i] != null) {
//	                return false; // Una casella lungo il percorso è occupata, la mossa non è consentita
//	            }
//	        }
//	    }
//
//	    // Controllo delle caselle verticali
//	    if (y.equals(newY)) {
//	        int minX = Math.min(x, newX);
//	        int maxX = Math.max(x, newX);
//	        for (int i = minX + 1; i < maxX; i++) {
//	            if (scacchiera.getGriglia()[i][y] != null) {
//	                return false; // Una casella lungo il percorso è occupata, la mossa non è consentita
//	            }
//	        }
//	    }
//
//	    // Controllo se la torre sta catturando un pezzo avversario
//	    if (scacchiera.getGriglia()[newX][newY] != null && !scacchiera.getGriglia()[newX][newY].getColore().equals(colore)) {
//	        return true; // La torre può catturare un pezzo avversario
//	    }
//
//	    return true; // La mossa è consentita
//	}
//
//




	// // Metodo per verificare se una posizione è libera o occupata
	//    private boolean posizioneLiberaOOccupata(Integer x, Integer y, String colore, Scacchiera scacchiera) throws Exception {
	//        boolean ritorno = false;
	//        
	//        // Verifica se le coordinate x e y sono all'interno della scacchiera (8x8)
	//        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
	//            ritorno = false; // La posizione è fuori dalla scacchiera
	//        }
	//
	//        // Ottiene la griglia della scacchiera
	//        Pezzo[][] griglia = scacchiera.getGriglia();
	//
	//        // Itera attraverso tutte le celle della scacchiera
	//        for (int riga = 0; riga < 8; riga++) {
	//            for (int colonna = 0; colonna < 8; colonna++) {
	//                // Verifica se la cella contiene un pezzo
	//                if (null != griglia[riga][colonna]) {
	//                    // Verifica se le coordinate x e y non corrispondono a questa cella
	//                    if (riga != x || colonna != y) {
	//                        // Le coordinate corrispondono a questa cella
	//                        // Verifica se il colore del pezzo è diverso da quello specificato
	//                        if (!griglia[riga][colonna].getColore().equals(colore)) {
	//                            griglia[riga][colonna].setEsiste(false); // Imposta esiste su false
	//                            ritorno = true; // La posizione è occupata da un pezzo avversario
	//                        } else {
	//                            ritorno = false; // La posizione è occupata da un tuo pezzo
	//                        }
	//                    } else {
	//                        throw new Exception("Non ti sei spostato");
	//                    }
	//                } else {
	//                    ritorno = true; // La posizione è libera (nessun pezzo)
	//                }
	//            }
	//        }
	//        
	//        return ritorno;
	//    }
	//    // Esempio di implementazione del metodo per spostare un pezzo
	//    private void spostaPezzo(Pezzo pezzo, int newX, int newY) {
	//        int oldX = pezzo.getX();
	//        int oldY = pezzo.getY();
	//
	//        // Rimuovi il pezzo dalla posizione precedente
	//        scacchiera[oldX][oldY] = null;
	//
	//        // Sposta il pezzo nella nuova posizione
	//        scacchiera[newX][newY] = pezzo;
	//
	//        // Aggiorna le coordinate del pezzo
	//        pezzo.setX(newX);
	//        pezzo.setY(newY);
	//    }
	//
	//    // Esempio di implementazione del metodo per verificare se un Re è sotto scacco
	//    private boolean reSottoScacco(String coloreRe) {
	//        // Implementa la logica per verificare se il Re di un certo colore è sotto scacco
	//        int xRe = -1;
	//        int yRe = -1;
	//
	//        // Trova la posizione del Re del colore specificato
	//        for (int i = 0; i < 8; i++) {
	//            for (int j = 0; j < 8; j++) {
	//                Pezzo pezzo = scacchiera[i][j];
	//                if (pezzo instanceof Re && pezzo.getColore().equals(coloreRe)) {
	//                    xRe = i;
	//                    yRe = j;
	//                    break;
	//                }
	//            }
	//        }
	//
	//        // Implementa la logica per verificare se il Re è minacciato
	//        // Utilizza calcolaMossePossibili degli avversari per determinare le minacce
	//        // Se una mossa minaccia la posizione del Re, il Re è sotto scacco
	//
	//        return false; // Restituisci true se il Re è sotto scacco, altrimenti false
	//    }
}