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

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p> Classe che realizza l'accesso ossia la connessione alla base di dati per 
 * mezzo del driver apposito per MySQL attraverso il sottoprotocollo adatto, del 
 * protocollo JDBC.
 * @author Andrea Mercanti 
 */
public class DbAccess {
    /**Driver per il collegamento alla piattaforma/DBMS MySQL*/
    private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
    /**Sottoprotocollo del protocollo JDBC per la connettività alla piattaforma/DMBS MySQL*/
    private final String DBMS = "jdbc:mysql";
    /**Contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost)*/
    private final String SERVER = "localhost";
    /**Contiene il nome della base di dati*/
    private final String DATABASE = "MapDB";
    /**La porta su cui il DBMS MySQL accetta le connessioni*/
    private final String PORT = "3306";
    /**Contiene il nome dell’utente per l’accesso alla base di dati*/
    private final String USER_ID = "MapUser";
    /**Contiene la password di autenticazione per l’utente identificato da USER_ID*/
    private final String PASSWORD = "map";
    /**Gestisce una connessione*/
    Connection conn;
    
    /**
     * <p>Impartisce al class loader l’ordine di caricare il driver per MySQL ed 
     * inizializza la connessione riferita da {@code conn}.
     * @throws DatabaseConnectionException in caso di fallimento nella connessione 
     *                                     al database.
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        String DB_URL = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
        try {
            conn = DriverManager.getConnection(DB_URL, USER_ID, PASSWORD);
        } catch (SQLException ex) {
            throw new DatabaseConnectionException();
        }
    }
    
    /**
     * <p>Restituisce la connessione instaurata con il database.
     * @return la connessione {@code conn}.
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * <p>Chiude la connessione rappresentata da {@code conn}.
     * @throws SQLException nel caso in cui la chiusura della connessione fallisca.
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
