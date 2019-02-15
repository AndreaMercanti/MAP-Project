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

package server;

import data.Data;
import data.OutOfRangeSampleSize;
import mining.KMeansMiner;
import database.DatabaseConnectionException;
import database.NoValueException;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Classe che modella un canale di comunicazione riservato per un solo client,
 * dunque gestisce la connessione tra il server e un client, con tutti gli 
 * accorgimenti per la gestione delle concorrenze tra le altre connessioni.
 * @author Andrea Mercanti 
 */
public class ServerOneClient extends Thread {
    /***Socket del client*/
    private Socket c_socket;
    /***Stream per ricevere dal client*/
    private ObjectInputStream in;
    /***Stream per inviare al client*/
    private ObjectOutputStream out;
    /***Risultato della computazione di clusetring*/
    private KMeansMiner kmeans;
    
    /**
     * <p>Costruttore di classe. Inizializza gli attributi socket, in e out ed avvia il thread.
     * 
     * @param s socket del client connesso.
     * 
     * @throws IOException per un qualsiasi errore di input/output.
     */
    public ServerOneClient(Socket s) throws IOException {
        c_socket = s;
        out = new ObjectOutputStream(c_socket.getOutputStream());
        in = new ObjectInputStream(c_socket.getInputStream());
        super.start();    //avvia il thread e chiama run()
    }
    
    /**
     * <p>Riscrive il metodo {@code run} della superclasse {@code Thread} al fine 
     * di gestire le richieste del client che sono suddivise in
     * due categorie:
     * <ul><li>Una di scoperta dei cluster sul database;</li>
     * <li>Una di lettura di un risultato precedente di scoperta da file.</li></ul>
     *  Nel primo caso il client invia una serie di comandi che il server gestirà 
     * in questo modo:
     * <ol><li>Viene ricevuto il comando 0 e si procede con la creazione dell'oggetto 
     * {@link data.Data};</li>
     * <li>Viene letto il comando 1, inviato dal client, e si effettua la vera e 
     * propria attività di scoperta tramite {@link mining.KmeansMiner#kmeans(Data)}.</li>
     * <li>Viene ricevuto il comando 2 e si procede con il salvataggio della 
     * scoperta su file.</li></ol>
     * Nel secondo caso (richiesta di lettura di una precendente computazione di 
     * clustering su file) il client invia un'altra serie di comandi così gestiti:
     * <ol><li>Viene ricevuto il comando 3 e si inizializza l'attributo {@link #kmeans}
     * attraverso il costruttore {@link mining.KmeansMiner#KmeansMiner(String)}</li></ol>
     * <br>
     * Se una qualunque operazione delle due attività non va a buon fine, il server 
     * si occuperà di scrivere sullo stream di output il relativo messaggio di 
     * errore; viceversa, per ogni operazione andata a buon fine, scrive sullo 
     * stream di output il messaggio "OK".
     */
    @Override
    public void run() {
        Data data = null;
        try {
            while (true) {                
                int menuAnswer = (Integer) in.readObject();
                switch(menuAnswer) {
                    case 0:
                        try{
                            data = new Data((String) in.readObject());
                            System.out.println(data);
                        } catch(SQLException e){
                            out.writeObject("Errore! - Nome Tabella Errato!");
                            break;
                        } catch (DatabaseConnectionException | NoValueException e) {
                            out.writeObject(e.getMessage());
                            break;
                        }
                        out.writeObject("OK");
                        break;
                    case 1:
                        kmeans = new KMeansMiner((Integer) in.readObject());
                        int numIter = 0;
                        try {
                            numIter = kmeans.kmeans(data);
                        } catch(OutOfRangeSampleSize e) {
                            out.writeObject(e.getMessage());
                            break;
                        }
                        out.writeObject("OK");
                        
                        out.writeObject(numIter);
                        out.writeObject(kmeans.getC().toString(data));
                        break;
                    case 2:
                        String fileName = (String) in.readObject();
                        System.out.println("Nome file di backup: " + fileName);
                        System.out.println("Salvataggio in " + fileName);
                        try {
                            kmeans.salva(fileName);
                        } catch (IOException e) {
                            Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, e);
                            out.writeObject(e.getMessage());
                            break;
                        }
                        out.writeObject("OK");
                        break;
                    case 3:
                        try{
                            kmeans = new KMeansMiner((String) in.readObject());
                        } catch(IOException | ClassNotFoundException e){
                            out.writeObject(e.getMessage());
                            break;
                        }
                        out.writeObject("OK");
                        out.writeObject(kmeans.toString());
                        break;
                }
//            out.flush();
            }
        } catch(IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                c_socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
