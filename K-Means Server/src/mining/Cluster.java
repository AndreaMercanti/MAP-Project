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
package mining;

import data.Tuple;
import data.Data;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Modella un cluster cioè un agglomerato di oggetti, in questo caso di 
 * <i>tuple</i>, aventi stesse proprietà e caratteristiche comuni, attorno ad un
 * centro di massa chiamato <i>centroide</i> il quale può risultare essere una 
 * tupla dell'insieme oppure una tupla i cui valori sono in un qualche modo 
 * funzione di quelli delle tuple dell'insieme.
 * @author Andrea Mercanti
 */
public class Cluster implements Serializable{
    /**tupla rappresentante le caratteristiche del cluster*/
    private Tuple centroid;
    /**insieme degli indici delle tuple componenti il cluster*/
    private Set<Integer> clusteredData;
    
    
    Cluster() {}
    
    /**
     * <p>Costruisce il cluster in base alla tupla {@code centroid} parametrizzata.
     * @param centroid centro di massa intorno al quale raggruppare l'insieme di oggetti.
     */
    Cluster(Tuple centroid){
        this.centroid = centroid;
        clusteredData = new HashSet<Integer>();
    }
    
    /**
     * <p>Restituisce il centro di massa del cluster riferito da {@code this}.
     * @return il centroide del cluster.
     */
    Tuple getCentroid(){
        return centroid;
    }
    
    /**
     * <p>Costruisce il centro di massa sulla base dell'insime non vuoto di oggetti.
     * @param data tabella sulla quale calcolare il centroide.
     */
    void computeCentroid(Data data){
        for(int i = 0; i < centroid.getLength(); i++)
            centroid.get(i).update(data,clusteredData);
    }
    
    /**
     * <p>Verifica se la tupla da aggiungere, indicizzata da {@code id}, appartiene 
     * o meno al cluster: 
     * <ul>
     * <li>se non è già presente la aggiunge e restituisce <i>true</i>;</li>
     * <li>altrimenti non fa nulla e restituisce <i>false</i>.</li>
     * </ul>
     * <p>Quindi ritorna un boolenano indicante se il cluster è stato modificato.
     * @param id indice di riga della tupla da aggiungere.
     * @return Vero se il cluster è mutato, falso altrimenti.
     */
    boolean addData(int id){
        return clusteredData.add(id);
    }
    
    /**
     * <p>Verifica se una transazione, cioè una tupla della tabella, è nel corrente 
     * cluster ovvero nell'attuale insieme delle transazioni agglomerate.
     * @param id indice della tupla (transazione) da verificare.
     * @return Vero se la tupla appartiene, falso altrimenti.
     */
    boolean contain(int id){
        return clusteredData.contains(id);
    }
    
    /**
     * <p>Rimuove una tupla, identificata dal numero di riga id nella tabella, dal 
     * cluster.
     * @param id indice di riga della tupla da eliminare.
     */
    void removeTuple(int id){
        clusteredData.remove(id);
    }
    
    /**
     * <p>Restuisce la stringa rappresentante lo stato parziale dell'oggetto cioè 
     * la tupla modellante il centro di massa (centroide) del cluster.
     * @return la stringa che rappresenta il centroide.
     */
    @Override
    public String toString(){
        String str = "Centroid=(";
        for(int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")";
        return str;

    }
    
    /**
     * <p>Restuisce la stringa rappresentante lo stato dell'oggetto cioè la tupla 
     * rappresentante il centro di massa (centroide) del cluster, nonché l'insieme 
     * delle transazioni appartenenti con, ciascuna, relativa distanza dal centroide.
     * @param data tabella dalla quale prelevare le tuple appartenenti al cluster.
     * @return la stringa che modella l'intero stato del cluster.
     */
    public String toString(Data data){
        String str = "Centroid=(";
        for(int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i)+ " ";
        str += ")\nExamples:\n";
        /*for each cluster element*/
        for(Iterator<Integer> iterator = clusteredData.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            str += "[";
            for(int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++)
                str += data.getAttributeValue(next, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(next)) + "\n";
        }
        str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        return str;
    }

}
