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

import data.Data;
import data.Item;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Modella il generico concetto di tupla come sequenza di coppie attributo-valore
 * ovvero come sequenza di item.
 * @author Andrea Mercanti
 */
public class Tuple implements Serializable{
    private Item[] tuple;
    
    /**
     * <p>Costruisce la tupla definita dal numero di attributi pari a size.
     * @param size numero di item che costituirà la tupla.
     */
    Tuple(int size) {
        tuple = new Item[size];
    }
    
    /**
     * <p>Restituisce la dimensione della tupla cioè il numero di colonne o attributi
     * che definiscono il suo schema.
     * @return il numero di attributi che definiscono la struttura della tupla.
     */
    public int getLength() {
        return tuple.length;
    }
    
    /**
     * <p>Restituisce l'item ovvero il nome e tipo di attributo, con annesso valore, 
     * in posizione i nella tupla.
     * @param i posizione ordinale dell'item nello schema della tupla.
     * @return l'item in posizione i nello schema della tupla.
     */
    public Item get(int i) {
        return tuple[i];
    }
    
    /**
     * <p>Memorizza l'item c, ovvero aggiunge l'attributo con il valore dedicato, in 
     * posizione i nella tupla.
     * @param c item da aggiungere.
     * @param i posizione in cui aggiungere l'item alla tupla.
     */
    void add(Item c, int i) {
//        if (i <= tuple.length)
            tuple[i] = c;
    }
    
    /**
     * <p>Determina la distanza tra la tupla riferita da {@code obj} e la tupla corrente;
     * la distanza è ottenuta come la somma delle distanze tra gli item in 
     * posizioni eguali nelle due tuple.
     * @param obj tupla a cui riferirsi.
     * @return somma delle singole distanze tra item corrispondenti.
     */
    public double getDistance(Tuple obj) {
        double sum = 0.0;
        for(int i = 0; i < getLength(); i++)
            sum += tuple[i].distance(obj.get(i));
        return sum;
    }
    
    /**
     * <p>Restituisce la media delle distanze tra la tupla corrente e quelle 
     * ottenibili dalle righe della matrice in data aventi indice in clusteredData.
     * @param data tabella dalla quale considerare le tuple da confrontare.
     * @param clusteredData insieme degli indici delle tuple da confrontare.
     * @return la media delle distanze da ogni tupla indicizzata.
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double avg = 0.0, sumD = 0.0;
        for (Iterator<Integer> iterator = clusteredData.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            double d = getDistance(data.getItemSet(next));
            sumD += d;
        }
        avg = sumD/clusteredData.size();
        return avg;
    }
}
