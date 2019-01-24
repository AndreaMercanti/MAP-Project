package data;

import java.io.Serializable;

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

/**
 * <p>Modella la gengerica entità <i>attributo</i> di una transazione, cioè di 
 * una tupla, come una coppia di valori dove uno è il nome simbolico e l'altro 
 * è un identificativo numerico rappresentante la posizione ordinale che 
 * l'attributo in questione ha nello schema della tabella.
 * @author Andrea Mercanti
 */
public class Attribute implements Serializable{
    /**Nome simbolico/titolo dell'attributo*/
    protected String name;
    /**Identificativo numerico*/
    protected int index;

    /**
     * <p>Inizializza la coppia descrivente l'attributo con i valori {@code name}
     *  e {@code index} passati come parametri.
     * @param name nome simbolico dell'attributo.
     * @param index identificativo numerico indicante la posizione ordinale 
     *              dell'attributo nello schema della tabella.
     */
    Attribute(String name, int index){
        this.name = name;
        this.index = index;
    }

    /**
     * <p>Restituisce il nome simbolico dell'attributo.
     * @return il nome identificante l'attributo.
     */
    String getName(){
        return name;
    }

    /**
     * <p>Restituisce l'identificativo numerico, cioè la posizione ordinale 
     * dell'attributo in tabella.
     * @return la posizione ordinale dell'attributo in tabella.
     */
    int getIndex(){
        return index;
    }

    /**
     * <p>Restuisce la stringa rappresentante lo stato dell'oggetto che consiste
     * nel solo nome simbolico dell'attributo.
     * @return la stringa modellante lo stato dell'oggetto.
     */
    @Override
    public String toString(){
        return name;	//getName()
    }
}
