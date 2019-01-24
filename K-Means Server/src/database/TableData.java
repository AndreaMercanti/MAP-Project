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

import database.TableSchema.Column;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Classe che modella e serve a manipolare specificatamente l'insieme delle 
 * transazioni collezionate in una tabella della base di dati. 
 * La singola transazione è modellata dalla classe {@code Example}.
 * @author Andrea Mercanti 
 */
public class TableData {
    /**Accesso alla base di dati*/
    private DbAccess db;

    /**
     * <p>Crea la tabella contenente i dati nel parametro {@code db}.
     * @param db accesso alla base di dati.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * <p>Ricava lo schema della tabella con nome {@code table}. Esegue quindi 
     * una interrogazione per estrarre le tuple distinte da tale tabella e per 
     * ogni tupla del resultset si crea un oggetto, istanza della classe 
     * {@code Example}, il cui riferimento va incluso nella lista da restituire. 
     * In particolare, per la tupla corrente nel resultset, si estraggono i valori 
     * dei singoli campi (usando {@code getFloat()} o {@code getString()}), e li 
     * si aggiungono all’oggetto istanza della classe {@code Example} che si sta 
     * costruendo.
     * @param table nome della tabella nel database.
     * @return la tabella ristretta alle tuple distinte, dove ognuna è modellata 
     *         da un oggetto {@code Example}.
     * @throws SQLException in presenza di errori nella esecuzione della query.
     * @throws EmptySetException se il resultset è vuoto.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        Connection c = db.getConnection();
        Statement s = c.createStatement();
        ResultSet r;
        try {
            r = s.executeQuery("SELECT DISTINCT * FROM " + table);
        } catch (SQLException e) {
            throw new EmptySetException();
        }
        
        List<Example> examples = new ArrayList<Example>();
        Example example;
        TableSchema tableSchema = new TableSchema(db, table);
        
        while (r.next()) {
            example = new Example();
            for (Column column : tableSchema) {
                if(column.isNumber())
                    example.add(new Double(r.getString(column.getColumnName()))); //popolamento della tupla
                else
                    example.add(r.getString(column.getColumnName())); //popolamento della tupla
            }
            examples.add(example);
        }
        s.close();
        return examples;
    }

    /**
     * <p>Formula ed esegue una interrogazione SQL che consiste in una proiezione
     * sulla colonna {@code column} della tabella {@code table} nel database, per 
     * estrarne i valori distinti e restituirli all'interno di un insieme ordinato
     * ascendentemente.
     * @param table nome della tabella nel database.
     * @param column nome dell'attributo ossia il titolo della colonna in tabella.
     * @return l'insieme dei valori distinti ordinati in modo ascendente per 
     *         l'attributo {@code column} nella tabella {@code table}.
     * @throws SQLException in presenza di errori nella esecuzione della query.
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException, DatabaseConnectionException {
        Connection c = db.getConnection();
        Statement s = c.createStatement();
        ResultSet r = s.executeQuery("SELECT DISTINCT " + column.getColumnName() +
                                     " FROM " + table);
        Set<Object> distinct_values = new TreeSet<>();
        while(r.next())
            distinct_values.add(r.getString(column.getColumnName()));
        s.close();
        return distinct_values;
    }

    /**
     * <p>Formula ed esegue una interrogazione SQL che consiste in una proiezione 
     * sulla colonna {@code column} della tabella {@code table} nel database, per 
     * estrarre il valore aggregato (il minimo o il massimo) di tipo {@code QUERY_TYPE}.
     * @param table nome della tabella nel database.
     * @param column nome dell'attributo ossia titolo della colonna nella tabella.
     * @param aggregate operatore SQL di aggregazione (min/max).
     * @return il valore aggregato (minimo o massimo) della colonna.
     * @throws SQLException in presenza di errori nella esecuzione della query.
     * @throws NoValueException se il resultset è vuoto o il valore calcolato è 
     *                          pari a null.
     */
    public  Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException, DatabaseConnectionException {
        Connection c = db.getConnection();
        Statement s = c.createStatement();
        ResultSet r = s.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ")" +
                                     " FROM " + table);
        Object value;
        if(r.next())
            value = r.getDouble(aggregate + "(" + column.getColumnName() + ")");
        else
            throw new NoValueException();
        s.close();
        return value;
    }
}
