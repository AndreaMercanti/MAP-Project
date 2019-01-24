/*
 * Copyright (C) 2018 Andrea Mercanti 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import mining.KMeansMiner;
import data.*;
import database.DatabaseConnectionException;
import database.NoValueException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import keyboardinput.Keyboard;

/**
 * <p>Classe principale dalla quale parte l'esecuzione dell'intera applicazione.
 * @author Andrea Mercanti
 */
public class MainTest {
    /**
     * Menù di avvio per dare la possibilità all'utente di caricare i dati da un
     * database o da un file in locale.
     * @return un intero rappresentante la scelta dell'utente.
     */
    private static int menu(){
        int answer;
        System.out.println("Scegli una opzione");
        do{
            System.out.println("(1) Carica Cluster da File");
            System.out.println("(2) Carica Dati");
            System.out.print("Risposta: ");
            answer = Keyboard.readInt();
        } while(answer <= 0 || answer > 2);
        return answer;
    }

    /**
     * Permette di riprendere l'esecuzione del programma da un punto registrato 
     * su un file il cui nome e percorso dovranno essere definiti dall'utente 
     * per mezzo della tastiera.
     * @return l'oggetto della classe {@code KMeansMiner} inizializzato con 
     *         l'oggetto serializzato nel file.
     * @throws FileNotFoundException nel caso in cui il nome del file non indichi 
     *                               un file reale nel file system.
     * @throws IOException per un qualsiasi errore di input/output.
     * @throws ClassNotFoundException nel caso in cui il percorso non indichi una 
     *                                cartella reale nel file system.
     */    
    static KMeansMiner learningFromFile() throws FileNotFoundException, IOException, ClassNotFoundException{
        String fileName = "";
        System.out.print("Nome archivio: ");
        fileName = Keyboard.readString();
        return new KMeansMiner(fileName + ".dmp");
    }
    
    /**
     * Il metodo principale per l'esecuzione dell'intero programma.
     * @param args gli argomenti a linea di comando.
     */
    public static void main(String[] args) {
        do{
            int menuAnswer = menu();
            switch(menuAnswer) {
                case 1:
                    try {
                        KMeansMiner kmeans = learningFromFile();
                        System.out.println(kmeans);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Data data = null;
                    try {
                        data = new Data("playtennis");
                    } catch (SQLException | NoValueException | DatabaseConnectionException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(data);
                    char answer = 'y';
                    do {
                        int k = 1;
                        System.out.print("Inserisci k: ");
                        k = Keyboard.readInt();
                        KMeansMiner kmeans = new KMeansMiner(k);
                        try {
                            int numIter = kmeans.kmeans(data);
                            System.out.println("Numero di Iterazioni: " + numIter);
                            System.out.println(kmeans.getC().toString(data));
                            System.out.print("Nome file di backup: ");
                            String fileName = Keyboard.readString() + ".dmp";
                            System.out.println("Salvataggio in " + fileName);
                            try {
                                kmeans.salva(fileName);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            System.out.println("Fine operazioni di salvataggio!");
                        } catch(OutOfRangeSampleSize e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.print("Vuoi ripetere l'esecuzione?(y/n) ");
                        answer = Keyboard.readChar();
                    } while(answer == 'y');
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }

            System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n) ");
            if(Keyboard.readChar() != 'y')
                break;
        } while(true);
    }
}
