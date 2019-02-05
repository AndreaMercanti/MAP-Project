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

//import mining.KMeansMiner;
//import data.*;
//import database.DatabaseConnectionException;
//import database.NoValueException;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import keyboardinput.Keyboard;

/**
 * <p>Classe principale dalla quale parte l'esecuzione dell'intera applicazione.
 * @author Andrea Mercanti
 */
public class MainTest {
    /***Stream per ricevere dal server*/
    private static ObjectInputStream in;
    /***Stream per inviare al server*/
    private static ObjectOutputStream out;
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
    static String learningFromFile() throws FileNotFoundException, IOException, ClassNotFoundException{
        String fileName = "";
        System.out.print("Nome archivio: ");
        fileName = Keyboard.readString();
        return fileName + ".dmp";
    }
    
    /**
     * Il metodo principale per l'esecuzione dell'intero programma.
     * @param args gli argomenti a linea di comando.
     */
    public static void main(String[] args) {
        InetAddress addr;
        Socket socket = null;

        try {
            addr = InetAddress.getByName(null); 
            socket = new Socket(addr, 8080);
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            do{
                int menuAnswer = menu();
                out.writeInt(menuAnswer);
                switch(menuAnswer) {
                    case 1:
                        try {
                            out.writeObject(learningFromFile());
                            System.out.println(in.readObject());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        out.writeObject("playtennis");   //invia il nome della tabella da cui prelevare i dati
                        
                        char answer = 'y';
                        do {
                            System.out.print("Inserisci k: ");
                            out.writeInt(Keyboard.readInt());
                            try {
                                //lettura risultato computazione
                                System.out.println((String) in.readObject());
                                System.out.println(in.readObject());
                                
                                System.out.print("Nome file di backup: ");
                                String fileName = Keyboard.readString() + ".dmp";
                                System.out.println("Salvataggio in " + fileName);
                                out.writeObject(fileName);
                                System.out.println((String) in.readObject());
                            } catch(IOException | ClassNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.print("Vuoi ripetere l'esecuzione?(y/n) ");
                            answer = Keyboard.readChar();
                            out.writeObject(answer);
                        } while(answer == 'y');
                        break;
                    default:
                        System.out.println("Opzione non valida!");
                }

                System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n) ");
                if(Keyboard.readChar() != 'y')
                    break;
            } while(true);
        }catch(IOException e) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
