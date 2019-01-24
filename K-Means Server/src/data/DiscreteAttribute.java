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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Modella un attributo di tipo categorico quindi discreto in un certo dominio.
 * @author Andrea Mercanti
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>{
    /**Insieme dei valori costituenti il dominio*/
    private TreeSet<String> values;
    
    /**
     * <p>Inizializza le features descriventi l'attributo con i valori {@code name}, 
     * {@code index} e {@code values}, quest'ultimo rappresentante il dominio, 
     * passati come parametri.
     * @param name nome simbolico dell'attributo.
     * @param index identificativo numerico indicante la posizione ordinale dell'attributo in tabella.
     * @param values insieme dei valori che definiscono il dominio dell'attributo, ordinati per logica lessicografica.
     */
    DiscreteAttribute(String name, int index, TreeSet<String> values) {
        super(name, index);
        this.values = values;
    }
    
    /**
     * <p>Restituisce la dimensione del dominio discreto.
     * @return il numero di valori discreti nel dominio dell'attributo.
     */
    int getNumberOfDistinctValues(){
    	return values.size();
    }
    
    /**
     * <p>Determina il numero di volte che il valore v compare in corrispondenza 
     * dell'attributo corrente (indice di colonna) negli esempi memorizzati in 
     * data e indicizzate (per riga) da idList.
     * @param data tabella su cui lavorare.
     * @param idList insieme degli indici di riga di alcune tuple memorizzate in data.
     * @param v valore discreto da valutare.
     * @return il numero di occorrenze (intero) del valore discreto.
     */
    int frequency(Data data, Set<Integer> idList, String v) {
        int freq = 0;
        for (Iterator<Integer> iterator = idList.iterator(); iterator.hasNext();) {
            if (v.equals((String) data.getAttributeValue(iterator.next(), index)))
                freq++;
        }
        return freq;
    }

    /**
     * <p>Restituisce un iteratore per l'attributo, visto come una 
     * collection, così da poter scandire i valori del dominio.
     * @return l'iteratore all'attributo corrente.
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}
