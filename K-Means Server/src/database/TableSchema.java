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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Classe modellante lo schema di una qualsiasi tabella della base di dati 
 * relazionale.
 * @author Andrea Mercanti 
 */
public class TableSchema implements Iterable<TableSchema.Column>{
    /**Accesso alla base di dati*/
    private DbAccess db;
    /**Schema della tabella modellato come una lista di oggetti di tipo {@code Column}*/
    private List<Column> tableSchema = new ArrayList<Column>();

    /**
     * <p>Classe modellante la generica entità attributo, per come si intende 
     * nell'ambito delle basi di dati, che identifica quindi una colonna 
     * all'interno di una tabella di una qualsiasi base di dati relazionale. 
     * Questa classe è infatti composta da un nome/titolo e dal tipo di valori 
     * del dominio che tratta (numerici o litterali).
     */
    public class Column{
        /**Titolo della colonna o altresì il nome dell'attributo*/
        private String name;
        /**Tipo di dato che manipola la colonna, cioè numerico (continuo) o litterale (discreto)*/
        private String type;
        
        /**
         * <p>Costruisce ed inizializza l'oggetto dandone un titolo e la tipologia 
         * del dominio rispettivamente in base ai parametri {@code name} e {@code type}.
         * @param name titolo della colonna.
         * @param type tipo di valori del dominio.
         */
        Column(String name, String type){
            this.name = name;
            this.type = type;
        }

        /**
         * <p>Restituisce il titolo della colonna, ossia il nome dell'attributo.
         * @return il nome dell'attributo.
         */
        public String getColumnName(){
            return name;
        }

        /**
         * <p>Verifica se la colonna ovvero l'attributo puntato da {@code this} è di tipo
         * numerico o meno.
         * @return {@code true} se il dominio è di tipo numerico, {@code false} 
         * se è di tipo litterale.
         */
        public boolean isNumber(){
            return type.equals("number");
        }
        
        /**
         * <p>Restuisce la stringa rappresentante lo stato dell'oggetto cioè il 
         * titolo seguito dal tipo dell'oggetto puntato da {@code this}.
         * @return la stringa che modella lo stato dell'oggetto.
         */
        @Override
        public String toString(){
            return name + ":" + type;
        }
    }

    /**
     * <p>Costruisce lo schema della tabella, chiedendo di volta in volta al 
     * database, per ogni colonna, la tipologia dei dati che questa manipola, 
     * mappandola in un oggetto della classe {@code Column} quindi traducendo i 
     * diversi formati numerici e litterali dell'SQL rispettivamente in "number" 
     * e "string" per poter essere manipolati facilmente in Java.
     * @param db il mezzo per accedere alla base di dati.
     * @param tableName nome della tabella.
     * @throws SQLException 
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException{
        this.db = db;
        HashMap<String,String> mapSQL_JAVATypes = new HashMap<String, String>();
        //http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        Connection con = db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(res.getString("COLUMN_NAME"), 
                                mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
                                );
        }
        res.close();
    }

    /**
     * <p>Restituisce l'attributo nella posizione indicata da {@code index} 
     * nello schema della tabella. L'indice deve partire da 0.
     * @param index indice nello schema.
     * @return l'attributo in posizione {@code index}.
     */
    public Column getColumn(int index){
        return tableSchema.get(index);
    }
    
    /**
     * <p>Restituisce la lunghezza dello schema della tabella che equivale al numero
     * di colonne o attributi componenti lo schema stesso.
     * @return il numero di attributi dello schema.
     */
    public int getNumberOfAttributes(){
        return tableSchema.size();
    }
    
    /**
     * <p>Restituisce un iteratore per l'oggetto corrente, visto come una 
     * collection, così da poter scandire gli oggetti {@code Column} che memorizza.
     * @return l'iteratore per l'oggetto corrente;
     */
    @Override
    public Iterator<Column> iterator() {
        return tableSchema.iterator();
    }
}
