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
import data.OutOfRangeSampleSize;
import java.io.Serializable;

/**
 * <p>Modella una qualsiasi tabella di transizioni come un insieme di cluster.
 * @author Andrea Mercanti
 */
public class ClusterSet implements Serializable{
    /**Insieme dei cluster*/
    Cluster C[];
    /**Posizione valida per la memorizzazione di un nuovo cluster in C*/
    int i = 0;
    
    /**
     * <p>Costruisce un insieme che può ospitare {@code k} cluster.
     * @param k numero di cluster in cui scomporre la tabella.
     */
    ClusterSet(int k) {
        C = new Cluster[k];
    }
    
    /**
     * <p>Aggiunge un nuovo cluster all'insieme.
     * @param c cluster da aggiungere.
     */
    void add(Cluster c) {
        C[i] = c;
        i++;    //oppure in maniera più compatta C[i++] = c;
    }
    
    /**
     * <p>Restituisce l'i-esimo cluster aggiunto all'insieme.
     * @param i numero ordinale del cluster aggiunto.
     * @return l'i-esimo cluster.
     */
    Cluster get(int i) {
        return C[i];
    }
    
    /**
     * <p>Crea in modo casuale dei primi centroidi per modellare la tabella {@code data} in 
     * riferimento, e per ognuno di essi crea un cluster che verrà memorizzato 
     * in modo ordinale nell'insieme.
     * @param data tabella da modellare.
     */
    void initializeCentroids(Data data) throws OutOfRangeSampleSize{
        int centroidIndexes[] = data.sampling(C.length);
        for(int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }
    
    /**
     * <p>Calcola la distanza tra la tupla riferita da {@code tuple} ed il centroide di 
     * ciascun cluster dell'insieme, restituendo il cluster più vicino.
     * @param tuple tupla a cui riferirsi.
     * @return il cluster il cui centroide dista meno dalla tupla.
     */
    Cluster nearestCluster(Tuple tuple) {
        double min = 0, distance;
        int j = 0;
        for (int i = 0; i < C.length; i++) {
            distance = C[i].getCentroid().getDistance(tuple);
            if (min > distance || i == 0) {
                min = distance;
                j = i;
            }
        }
        return C[j];
    }
    
    /**
     * <p>Identifica e restituisce il cluster a cui la tupla, rappresentante l'esempio
     * identificato da {@code id}, appartiene; se la tupla non è inclusa in alcun cluster 
     * restituisce null.
     * @param id indice di riga della tupla nella tabella.
     * @return il cluster a cui appartiene la tupla, altrimenti null.
     */
    Cluster currentCluster(int id) {
        for (Cluster cluster : C) {
            if(cluster.contain(id))
                return cluster;
        }
        return null;
    }
    
    /**
     * <p>Calcola il nuovo centro di massa (centroide) per ciascun cluster nell'insieme
     * sulla base della tabella descritta da {@code data}.
     * @param data tabella 
     */
    void updateCentroids(Data data) {
        for (int j = 0; j < C.length; j++)
            C[j].computeCentroid(data);
    }
    
    /**
     * <p>Restituisce la stringa che rappresenta lo stato parziale dell'oggetto cioè
     * l'insieme dei centroidi dei cluster da cui è formato l'insieme.
     * @return la stringa che rappresenta i centroidi di ciascun cluster.
     */
    @Override
    public String toString() {
        String s = "";
        int j = 0;
        for (Cluster cluster : C) {
            s += j + ": " + cluster.toString() + "\n";
            j++;
        }
        return s;
    }
    
    /**
     * <p>Restituisce la stringa che rappresenta lo stato dell'oggetto cioè lo
     * stato di ciascun cluster da cui è formato l'insieme.
     * @param data tabella da cui recuperare la descrizione dei cluster.
     * @return la stringa modellante lo stato completo di ciascun cluster.
     */
    public String toString(Data data) {
        String s = "";
        int j = 0;
        for (Cluster cluster : C) {
            s += j + ": " + cluster.toString(data) + "\n";
            j++;
        }
        return s;
    }
}
