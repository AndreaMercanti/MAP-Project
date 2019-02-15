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

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Classe che modella la parte server di una architettura client-server; server
 * in grado di gestire l'accesso concorrente di più client per volta, quindi multi-client.
 * @author Andrea Mercanti 
 */
public class MultiServer {
    /**Porta in cui risiede il programma server in ascolto*/
    private int PORT = 8080;
    
    /**
     * <p>Costruisce la struttura per il server multi-client, inizializzando la 
     * porta ed invocando il metodo {@code run()}.
     * @param port porta in cui risiedera' il programma server
     */
    public MultiServer(int port) {
        PORT = port;
        try {
            run();
        } catch (IOException ex) {
            Logger.getLogger(MultiServer.class.getName()).log(Level.SEVERE, null, ex);  //oppure System.out.println(ex.getMessage())
        }
    }

    /**
     * <p>Metodo principale per l'esecuzione del file {@code MultiServer.java}, 
     * in cui viene semplicemente istanziato un oggetto di tipo MultiServer.
     * @param args gli argomenti a linea di comando.
     */
    public static void main(String[] args) {
        MultiServer server2manyClients = new MultiServer(8080);
    };
    
    /**
     * <p>Istanzia un oggetto della classe {@code ServerSocket} che pone in attesa 
     * di richiesta di connessioni da parte del client. Ad ogni nuova richiesta 
     * di connessione viene istanziato un oggetto della classe {@code ServerOneClient}.
     * @throws IOException per un qualsiasi errore di input/output.
     */
    private void run() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            do {
//                System.out.println("Listening...");
                //si blocca su accept() fin quando non c'è una richiesta di connessione
                Socket client = server.accept();    //connessione stabilita e recupero del riferimento alla client socket
//                System.out.println("Accepted client " + client);
                try {
                    new ServerOneClient(client);
                } catch (IOException e) {
                    client.close(); //chiusura della connessione client
                }
            } while(true);
        } finally {
            server.close(); //chiusura del server
        }
    }
}
