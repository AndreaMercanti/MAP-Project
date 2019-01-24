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

import data.Data;
import data.OutOfRangeSampleSize;
import java.io.*;

/**
 * <p>Include l'implementazione dell’algoritmo K-Means.
 * @author Andrea Mercanti
 */
public class KMeansMiner {
    /**Insieme dei cluster*/
    private ClusterSet C;
    
    /**
     * <p>Crea la struttura per l'insieme dei {@code k} cluster da popolare.
     * @param k numero di cluster da generare.
     */
    public KMeansMiner(int k) {
        C = new ClusterSet(k);
    }
    
    /**
     * <p>Apre il file identificato da {@code fileName}, legge l'oggetto ivi 
     * memorizzato e lo assegna a {@code C}; in questo modo la computazione può
     * partire da un punto definito in precedenza, salvato nel file {@code fileName}
     * che si sta leggendo.
     * @param fileName percorso + nome del file dal quale recuperare l'oggetto serializzato.
     * @throws FileNotFoundException nel caso in cui il nome del file non indichi 
     *                               un file reale nel file system.
     * @throws IOException per un qualsiasi errore di input/output.
     * @throws ClassNotFoundException nel caso in cui il percorso non indichi una 
     *                                cartella reale nel file system.
     */
    public KMeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        BufferedInputStream inBuffer = new BufferedInputStream(new FileInputStream(fileName));
        ObjectInputStream inStream = new ObjectInputStream(inBuffer);
        C = (ClusterSet) inStream.readObject();
        inStream.close();
    }
    
    /**
     * <p>Restituisce l'insieme dei cluster.
     * @return {@code C} l'insieme dei cluster.
     */
    public ClusterSet getC() {
        return C;
    }
    
    /**
     * <p>Esegue l’algoritmo k-means, riferendosi alla tabella descritta da 
     * {@code data}, eseguendo i passi dello pseudo-codice:
     * <ol>
     * <li>Scelta casuale di centroidi per k clusters;</li>
     * <li>Assegnazione di ciascuna riga della matrice in data al cluster avente centroide più vicino all'esempio;</li>
     * <li>Calcolo dei nuovi centroidi per ciascun cluster;</li>
     * <li>Ripetere i passi 2 e 3 finché due iterazioni consecuitive non restituiscono centroidi uguali.</li>
     * </ol>
     * @param data tabella con i dati da modellare.
     * @return il numero di iterazioni servite per assestare la configurazione 
     *         dei cluster rispetto alla matrice data.
     * @throws data.OutOfRangeSampleSize nel caso in cui si sta cercando di creare 
     *                                  un numero di cluster non permesso.
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize{
        int numberOfIterations = 0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster = false;
        do{
            numberOfIterations++;
            //STEP 2
            changedCluster = false;
            for(int i = 0; i < data.getNumberOfExamples(); i++){
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if(currentChange)
                    changedCluster = true;
                //rimuovo la tupla dal vecchio cluster
                if(currentChange && oldCluster != null)
                //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //STEP 3
            C.updateCentroids(data);
        } while(changedCluster);
        return numberOfIterations;
    }
    
    /**
     * Apre il file identificato da {@code fileName} e salva l'oggetto riferito 
     * da {@code C} in tale file.
     * @param fileName percorso + nome del file in cui serializzare l'oggetto {@code this}.
     * @throws FileNotFoundException nel caso in cui il nome del file non indichi
     *                               un file reale nel file system o si tenti di
     *                               scrivere qualora il file è di sola lettura.
     * @throws IOException per un qualsiasi errore di input/output.
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        BufferedOutputStream outBuffer = new BufferedOutputStream(new FileOutputStream(fileName));
        ObjectOutputStream outStream = new ObjectOutputStream(outBuffer);
        outStream.writeObject(C);
        outStream.close();
    }

    /**
     * <p>Restituisce la stringa che rappresenta lo stato dell'oggetto cioè
     * l'insieme dei centroidi dei cluster da cui è formato l'oggetto.
     * @return la stringa che rappresenta i centroidi di ciascun cluster.
     */
    @Override
    public String toString() {
        return C.toString();
    }
}
