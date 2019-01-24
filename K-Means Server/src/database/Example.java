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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Serve a modellare una qualsiasi transazione ovvero una qualsiasi riga 
 * della tabella.
 * @author Andrea Mercanti
 */
public class Example implements Comparable<Example>{
    /**Lista rappresentante la riga della tabella.*/
    private List<Object> example = new ArrayList<Object>();

    /**
     * <p>Aggiunge l'elemento {@code o} in coda alla riga in esame, quindi 
     * aggiunge una colonna con quell'oggetto.
     * @param o oggetto modellante la nuova colonna da aggiungere.
     */
    public void add(Object o){
        example.add(o);
    }

    /**
     * <p>Restituisce l'i-esimo riferimento collezionato in {@code Example},
     * cioè l'oggetto nella i-esima colonna della riga. 
     * L'indice deve partire da 0.
     * @param i numero ordinale (sottratto di 1) della colonna.
     * @return l'oggetto nella i-esima colonna.
     */
    public Object get(int i){
        return example.get(i);
    }
    
    /**
     * <p>Confronta la riga attuale con un'altra riga della tabella {@code Data},
     * restituendo 0 se le due righe sono uguali, -1 o 1 se la riga attuale
     * è rispettivamente minore o maggiore della riga {@code ex} chiamando 
     * {@code compareTo} sulla prima coppia di valori in disaccrodo per la 
     * stessa colonna.
     * @param ex riga della tabella da confrontare.
     * @return 0 se le due righe sono uguali oppure il risultato della 
     * {@code compareTo} sulla prima coppia di valori in disaccordo.
     */
    @Override
    public int compareTo(Example ex) {
        int i = 0;
        for(Object o : ex.example){
            if(!o.equals(this.example.get(i)))
                return ((Comparable) o).compareTo(example.get(i));
            i++;
        }
        return 0;
    }
    
    /**
     * <p>Restituisce la stringa rappresentante lo stato dell'oggetto cioè 
     * l'esempio composto dai valori separati da spazio.
     * @return la stringa modellante lo stato dell'oggetto.
     */
    @Override
    public String toString(){
        String str = "";
        for(Object o : example)
            str += o.toString() + " ";
        return str;
    }
}
