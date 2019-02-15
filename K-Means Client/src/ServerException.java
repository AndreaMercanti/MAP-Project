
import com.sun.javafx.scene.DirtyBits;

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

/**
 * <p> Classe che modella un qualsiasi errore verificatosi nel server e inoltrato 
 * da questo.
 * @author Andrea Mercanti 
 */
public class ServerException extends Exception {
    /**
     * <p>Costruisce l'eccezione con l'informazione riguardo la motivazione, così 
     * da poterla riportare all'utente se necessario.
     * 
     * @param s la stringa contenente il messaggio di errore ricevuto dal server
     */
    public ServerException(String s) {
        super(s);
    }
    
    /**
     * <p>Costruisce l'eccezione sulla base di una eccezione inoltrata dal server,
     * definendo la tipologia di eccezione e la causa che l'ha causata, così 
     * da poter riportare all'utente tutte le informazioni utili se necessario.
     * 
     * @param e l'eccezione sollevata e inviata dal server, da modellare
     */
    public ServerException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
