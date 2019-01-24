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

package data;

/**
 * <p>Classe che modella l'eccezione qualora il numero {@code k} di cluster inserito 
 * da tastiera è maggiore del numero di centroidi generabili dall'insieme di 
 * transazioni.
 * @author Andrea Mercanti
 */
public class OutOfRangeSampleSize extends Exception{
    /**Numero di cluster per cui c'è stata l'eccezione*/
    private int number;

    /**
     * <p>Costruisce l'eccezione sulla base del numero {@code numer} di cluster 
     * non valido acquisito da tastiera, allegando la stringa {@code message} 
     * come informazione aggiuntiva da riportare all'utente se necessario.
     * @param number numero di cluster acquisito da tastiera.
     * @param message messaggio da mostrare in output contenente la motivazione dell'eccezione.
     */
    public OutOfRangeSampleSize(int number, String message) {
        super(message);
        this.number = number;
    }
    
    /**
     * <p>Restituisce il numero di cluster che l'utente ha dato in input da tastiera.
     * @return il numero di cluster da input.
     */
    public int getNumber() {
        return number;
    }    
}
