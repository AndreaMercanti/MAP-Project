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
import database.DatabaseConnectionException;
import database.NoValueException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mining.KMeansMiner;

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
     * @param s socket del client connesso.
     * @throws IOException per un qualsiasi errore di input/output.
     */
    public ServerOneClient(Socket s) throws IOException {
        c_socket = s;
        out = new ObjectOutputStream(new BufferedOutputStream(c_socket.getOutputStream()));
        out.flush();
        in = new ObjectInputStream(new BufferedInputStream(c_socket.getInputStream()));
//        System.out.println("Starting dialog...");
        start();    //avvia il thread e chiama run()
    }
    
    /**
     * <p>Riscrive il metodo {@code run} della superclasse {@code Thread} al fine 
     * di gestire le richieste del client.
     */
    @Override
    public void run() {
        try {
            do {
//                System.out.println("server.ServerOneClient.run()");
                int menuAnswer = (Integer) in.readObject();
//                synchronized(this){
//                    while((menuAnswer = (Integer) in.readObject()) == 0) {                        
//                        wait(5000);
//                    }
//                }
                switch(menuAnswer) {
                    case 1:     /*FETCHING DATA FROM FILE*/
                        kmeans = new KMeansMiner((String) in.readObject());
                        out.writeObject(kmeans);
                        break;
                    case 2:     /*MINING DATA FROM DB*/
//                        System.out.println("Receiving table's name");
                        Data data = new Data((String) in.readObject());
                        System.out.println(data);

                        kmeans = new KMeansMiner((Integer) in.readObject());
                        try {
                            int numIter = kmeans.kmeans(data);
                            out.writeObject("Numero di Iterazioni: " + numIter);
                            out.writeObject(kmeans.getC().toString(data));

                            String fileName = (String) in.readObject();
                            System.out.print("Nome file di backup: " + fileName);
                            System.out.println("Salvataggio in " + fileName);
                            try {
                                kmeans.salva(fileName);
                            } catch (IOException e) {
                                out.writeObject(e);
                            }
                            out.writeObject("Fine operazioni di salvataggio!");
                        } catch(OutOfRangeSampleSize e) {
                            out.writeObject(e);
                        }
                        break;
                }
            } while("y".equals((String)in.readObject()));
        } catch(IOException | ClassNotFoundException | SQLException | NoValueException | DatabaseConnectionException ex) {
            Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                c_socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerOneClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
