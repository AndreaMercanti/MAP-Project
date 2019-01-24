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

import java.io.Serializable;
import java.util.Set;

/**
 * <p>Modella un generico item che consiste in una coppia attributo-valore, come 
 * ad esempio Outlook="Sunny".
 * @author Andrea Mercanti
 */
public abstract class Item implements Serializable{
    /**attributo coinvolto nell'item*/
    Attribute attribute;
    /**valore assegnato all'attributo*/
    Object value;
    
    /**
     * <p>Inizializza i valori descriventi l'item con i valori {@code attribute}
     * e {@code value} passati come parametri.
     * @param attribute tipo di attributo: discreto o continuo.
     * @param value valore che assume l'item tra quelli nel dominio dell'attributo.
     */
    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }
    
    /**
     * <p>Restituisce il tipo di attributo che descrive l'item.
     * @return il tipo di attributo.
     */
    Attribute getAttribute() {
        return attribute;
    }
    
    /**
     * <p>Restituisce il valore che assume l'item di quel tipo di attributo.
     * @return il valore dell'item.
     */
    Object getValue() {
        return value;
    }

    /**
     * <p>Restituisce la stringa rappresentante lo stato dell'oggetto item, che
     * consiste esclusivamente nel valore ch'esso assume.
     * @return la stringa che modella lo stato dell'oggetto.
     */
    @Override
    public String toString() {
        return value.toString();
    }
    
    /**
     * <p>Implementa la distanza di Hamming tra il valore dell'item corrente e 
     * il valore dell'item {@code a} passato in input.
     * @param a item da confrontare.
     * @return 0 se i due valori sono uguali (getValue().equals(a)), 1 altrimenti.
     */
    abstract double distance(Object a);
    
    /**
     * <p>Modifica il membro value, assegnandogli il valore restituito da
     * {@code data.computePrototype(clusteredData,attribute)}.
     * @param data matrice di riferimento.
     * @param clusteredData insieme di indici delle righe della matrice in data che formano il cluster.
     */
    public void update(Data data, Set<Integer> clusteredData) {
        value = data.computePrototype(clusteredData, attribute);
    }
}
