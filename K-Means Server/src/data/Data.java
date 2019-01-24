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

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import java.sql.SQLException;
import java.util.*;
        
/**
 * <p>Questa classe modella l'insieme delle transazioni come tuple di una tabella.
 * @author Andrea Mercanti
 */
public class Data {
    /**
     * <p>Serve a modellare una qualsiasi transazione ovvero una qualsiasi riga 
     * della tabella.
     * @author Andrea Mercanti
     */
//    class Example implements Comparable<Example>{
//        @SuppressWarnings("FieldMayBeFinal")
//        private List<Object> example;   ///lista rappresentante la riga della tabella.
//        
//        /**
//         * <p>Crea una lista di oggetti da popolare.
//         */
//        @SuppressWarnings("Convert2Diamond")
//        public Example() {
//            example = new  ArrayList<Object>();
//        }
//        
//        /**
//         * <p>Aggiunge l'elemento {@code o} in coda alla riga in esame, quindi 
//         * aggiunge una colonna con quell'oggetto.
//         * @param o oggetto modellante la nuova colonna da aggiungere.
//         */
//        void add(Object o) {
//            example.add(o);
//        }
//        
//        /**
//         * <p>Restituisce l'i-esimo riferimento collezionato in {@code Example},
//         * cioè l'oggetto nella i-esima colonna della riga.
//         * @param i numero ordinale della colonna.
//         * @return l'oggetto nella i-esima colonna.
//         */
//        Object get(int i) {
//            try {
//                return ((ArrayList<Object>)example).get(i);
//            } catch (IndexOutOfBoundsException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        /**
//         * <p>Confronta la riga attuale con un'altra riga della tabella {@code Data},
//         * restituendo 0 se le due righe sono uguali, -1 o 1 se la riga attuale
//         * è rispettivamente minore o maggiore della riga {@code ex} chiamando 
//         * {@code compareTo} sulla prima coppia di valori in disaccrodo per la 
//         * stessa colonna.
//         * @param ex riga della tabella da confrontare.
//         * @return 0 se le due righe sono uguali oppure il risultato della 
//         * {@code compareTo} sulla prima coppia di valori in disaccordo.
//         */
//        @Override
//        public int compareTo(Example ex) {
//            Iterator<Object> it = example.iterator();
//            int result = 0, counter = 0;
//            Object next;
//            while (it.hasNext() && result == 0) {
//                next = it.next();
//                if(!next.equals(ex.get(counter)))
//                    result = ((Comparable) next).compareTo(ex.get(counter));
//                counter++;
//            }
//            return result;
//        }
//
//        /**
//         * <p>Restituisce la stringa rappresentante lo stato dell'oggetto cioè 
//         * l'esempio composto dai valori separati da spazio.
//         * @return la stringa modellante lo stato dell'oggetto.
//         */
//        @Override
//        public String toString() {
//            String s = "";
//            for (Object object : example)
//                s += object.toString() + " ";
//            return s;
//        }
//    }
    
    /**Tabella o insieme delle transazioni, modellata come sequenza ordinata di oggetti {@code Example}.*/
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Example> data;
    /**Numero di tuple cioè di righe della tabella.*/
    @SuppressWarnings("FieldMayBeFinal")
    private int numberOfExamples;
    /**Insieme degli attributi cioè delle colonne della tabella.*/
    @SuppressWarnings("FieldMayBeFinal")
    private List<Attribute> explanatorySet;
    
    /**
     * <p>Crea una tabella sulla base della tabella {@code table} dal database 
     * {@code MapDB}, quindi lo schema sarà composto da 5 attributi litterali e/o
     * numerici e popolata le stesse transazioni della tabella.
     * @param table nome della tabella nel database da cui recuperare le transazioni
     *              di esempio.
     * @throws java.sql.SQLException
     * @throws database.NoValueException
     * @throws database.DatabaseConnectionException
     */
    @SuppressWarnings("Convert2Diamond")
    public Data(String table) throws SQLException, NoValueException, DatabaseConnectionException {
        DbAccess db_access = new DbAccess();
        try {
            db_access.initConnection();  //Instaurazione della connessione
        } catch (DatabaseConnectionException ex) {
            ex.printStackTrace();
        }
        TableData dataFromDB = new TableData(db_access);
        try {
            data = (ArrayList<Example>) dataFromDB.getDistinctTransazioni(table);   //Popolamento della tabella
        } catch (SQLException | EmptySetException ex) {     //multi-catch
            ex.printStackTrace();
        }
        
        numberOfExamples = data.size();
        explanatorySet = new LinkedList<Attribute>();
        TableSchema table_schema = new TableSchema(db_access, table);

        /*Si definiscono gli elementi di explanatorySet, ognuno dei quali viene modellato 
        su necessità da un oggetto della classe DiscreteAttribute o ContinuousAttribute*/
        int i = 0;
        for (TableSchema.Column column : table_schema) {
            if (column.isNumber()) {
                double min_value = (double) dataFromDB.getAggregateColumnValue(table, column, QUERY_TYPE.MIN);
                double max_value = (double) dataFromDB.getAggregateColumnValue(table, column, QUERY_TYPE.MAX);
                explanatorySet.add(new ContinuousAttribute(column.getColumnName(), i, min_value, max_value));
            } else {
                TreeSet<Object> object_values = (TreeSet<Object>) dataFromDB.getDistinctColumnValues(table, column);
                TreeSet<String> values = (TreeSet<String>) object_values.clone();
                explanatorySet.add(new DiscreteAttribute(column.getColumnName(), i, values));
            }
            i++;
        }
        try {
            db_access.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <p>Restituisce la cardinalità dell'insieme delle transazioni ovvero il numero
     * di righe, o tuple, della tabella.
     * @return il numero di righe della tabella.
     */
    public int getNumberOfExamples(){
        return numberOfExamples;
    }

    /**
     * <p>Restituisce la cardinalità dell'insieme degli attributi definenti lo
     * schema della tabella ovvero il numero di colonne di questa.
     * @return il numero di colonne della tabella.
     */
    public int getNumberOfExplanatoryAttributes(){
        return explanatorySet.size();
    }

    /**
     * <p>Restituisce gli attributi nell'ordine esatto in cui definiscono lo schema
     * della tabella.
     * @return lo schema della tabella.
     */
    List<Attribute> getAttributeSchema(){
        return explanatorySet;
    }

    /**
     * <p>Restituisce il valore dell'attributo in posizione {@code attributeIndex} nella 
     * {@code exampleIndex}-esima tupla, o più intuitivamente il valore dell'elemento 
     * in posizione [exampleIndex, attributeIndex] della matrice.
     * @param exampleIndex numero ordinale della riga della tabella.
     * @param attributeIndex indice identificante l'attributo in tabella.
     * @return l'oggetto in posizione [exampleIndex, attributeIndex] nella tabella.
     */
    public Object getAttributeValue(int exampleIndex, int attributeIndex){
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * <p>Restituisce la struttura dell'attributo il cui numero ordinale nello 
     * schema della tabella è index.
     * @param index numero identificativo dell'attributo.
     * @return l'attributo in posizione (con identificativo numerico) index nello schema della tabella.
     */
    Attribute getAttribute(int index){
        return explanatorySet.get(index);
    }

    /**
     * <p>Restuisce la stringa rappresentante lo stato dell'oggetto cioè lo schema 
     * della tabella e le transazioni opportunamente enumerate.
     * @return la stringa che modella lo stato dell'oggetto.
     */
    @Override
    public String toString(){
        String s = "";
        /*Preleva i nomi delle colonne ovvero dei vari attributi della tabella*/
        for(int i = 0; i < explanatorySet.size(); i++){
            if(i == explanatorySet.size() - 1) //last attribute
                s += explanatorySet.get(i).getName();
            else
                s += explanatorySet.get(i).getName() + ", ";
        }
        s+="\n";

        /*Preleva tutte le tuple numerandole opportunamente*/
        for(int i = 0; i < numberOfExamples; i++){
            s+= i+1 + ":";
            for(int j = 0; j < getNumberOfExplanatoryAttributes(); j++)
                s += getAttributeValue(i, j) + ", ";
            s += "\n";
        }

        return s;
    }

    /**
     * <p>Metodo principale per l'esecuzione del file Data.java.
     * @param args insieme dei valori per parametrizzare il comportamento dell'applicazione;
     */
    public static void main(String args[]){
        Data trainingSet = null;
        try {
            trainingSet = new Data("playtennis");
        } catch (SQLException | NoValueException | DatabaseConnectionException ex) {
            ex.printStackTrace();
        }
        System.out.println(trainingSet);
    }
    
    /**
     * <p>Crea una tupla che modella, come sequenza di coppie attributo-valore, 
     * la i-esima riga nella tabella locale {@code data}.
     * @param index indice di riga.
     * @return la tupla in posizione index in tabella.
     */
    public Tuple getItemSet(int index) {
        int size = explanatorySet.size();
        Attribute attribute = null;
        Tuple tuple = new Tuple(size);
        for(int j = 0; j < size; j++) { //scanning each attribute
            attribute = explanatorySet.get(j);  //fetch the j-th attribute or coloumn
            if (attribute instanceof DiscreteAttribute)
                tuple.add(new DiscreteItem((DiscreteAttribute)attribute, (String)data.get(index).get(j)), j);
            else
                tuple.add(new ContinuousItem((ContinuousAttribute)attribute, (Double)data.get(index).get(j)), j);
        }
        return tuple;
    }
    
    /**
     * <p>Crea un array di k interi rappresentanti gli indici di riga in data per 
     * le k tuple inizialmente scelte, in modo casuale, come centroidi.
     * @param k numero di cluster da generare.
     * @return l'array con gli indici delle k righe inizialmente scelti come centroidi.
     * @throws data.OutOfRangeSampleSize
     */
    public int[] sampling(int k) throws OutOfRangeSampleSize{
        if(k <= 0 || k > numberOfExamples)
            throw new OutOfRangeSampleSize(k, "Il numero di cluster inserito non è valido: ");
        int centroidIndexes[] = new int[k];
        //choose k random different centroids in data.
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < k; i++) {   //for each centroid
            boolean found;
            int c;
            do {
                found = false;
                c = rand.nextInt(getNumberOfExamples());
                //verify that the c-th raw is not equal to a centroide already stored in CentroidIndexes
                for(int j = 0; j < i; j++) {
                    if(compare(centroidIndexes[j],c)){
                        found = true;
                        break;
                    }
                }
            } while(found); //a new random number will be generated until found is false
            centroidIndexes[i] = c;
        }
        return centroidIndexes;
    }

    /**
     * <p>Verifica se due righe della tabella, indicizzate da i e j, contengono gli 
     * stessi valori attributo per attributo.
     * @param i indice della prima riga da confrontare.
     * @param j indice della seconda riga da confrontare.
     * @return vero se le due tuple indicizzate da i e j sono identiche, falso altrimenti.
     */
    private boolean compare(int i, int j) {
        Tuple tuple1 = getItemSet(i);
        Tuple tuple2 = getItemSet(j);
        return (tuple1.getDistance(tuple2) == 0.0);
    }
    
    /**
     * <p>Determina il valore che occorre più frequentemente per l'attributo 
     * {@code attribute} nel sottoinsieme di tuple individuato da {@code idList}.
     * @param idList insieme degli indici delle tuple su cui lavorare.
     * @param attribute attributo da valutare.
     * @return il valore più frequente nel dominio di attribute tra l'insieme di tuple.
     */
    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        if (attribute instanceof ContinuousAttribute)
            return computePrototype(idList, (ContinuousAttribute) attribute);
        else
            return computePrototype(idList, (DiscreteAttribute) attribute);
    }
    
    /**
     * <p>Determina il valore che occorre più frequentemente per l'attributo discreto
     * {@code attribute} nel sottoinsieme di tuple individuato da {@code idList}.
     * @param idList insieme degli indici delle tuple su cui lavorare.
     * @param attribute attributo discreto da valutare.
     * @return il valore più frequente nel dominio di attribute tra l'insieme di tuple.
     */
    String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        int max = 0, j;
        String value, result = "";
        for (Iterator<String> iterator = attribute.iterator(); iterator.hasNext();) {
            value = iterator.next();
            j = attribute.frequency(this, idList, value);
            if(max < j) {
                max = j;
                result = value;
            }
        }
        return result;
    }
    
    /**
     * <p>Determina il valore prototipo come media dei valori osservati per 
     * l'attributo continuo {@code attribute} nel sottoinsieme di tuple della 
     * tabella individuato dall'indice di riga in {@code idList}.
     * @param idList insieme degli indici delle tuple su cui lavorare.
     * @param attribute attributo continuo da valutare.
     * @return la media tra i valori dell'insieme di tuple per l'attributo {@code attribute}.
     */
    Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
        Double value = 0.0, result;
        Integer index;
        for (Iterator<Integer> iterator = idList.iterator(); iterator.hasNext();) {
            index = iterator.next();
            value = (Double) data.get(index).get(attribute.getIndex()); //data[index][attribute.getIndex()]
        }
        return result = value/idList.size();
    }
}
