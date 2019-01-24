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

import java.sql.SQLException;

/**
 * <p> Classe che modella l'eccezoine qualora ci fosse un fallimento nella 
 * connessione al database.
 * @author Andrea Mercanti 
 */
public class DatabaseConnectionException extends Exception {
    /**
     * <p>Costruisce l'eccezione allegando un messaggio come informazione da 
     * riportare all'utente, se necessario, riguardo la ragione per cui si è verificata. 
     */
    public DatabaseConnectionException() {
        super("Tentativo di connessione al database fallito");
    }
}
