package data;

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
 * <p>Modella un attributo di tipo numerico quindi continuo in un qualsiasi intervallo,
 * per tanto include anche metodi di normalizzazione del dominio nel caso di confronti
 * con attributi i cui valori fanno parte di domini diversi.
 * @author Andrea Mercanti
 */
public class ContinuousAttribute extends Attribute {
    /**Limite superiore dell'intervallo*/
    private double min;
    /**Limite inferiore dell'intervallo*/
    private double max;

    /**
     * <p>Inizializza i valori descriventi l'attributo con i valori {@code name}, 
     * {@code index}, {@code min} e {@code max} rappresentanti il dominio, 
     * passati come parametri.
     * @param name nome simbolico dell'attributo.
     * @param index identificativo numerico indicante la posizione ordinale dell'attributo in tabella.
     * @param min limite inferiore del dominio.
     * @param max limite superiore del dominio.
     */
    ContinuousAttribute(String name, int index, double min, double max){
            super(name, index);
            this.min = min;
            this.max = max;
    }

    /**
     * <p>Calcola e restituisce il valore normalizzato nell'intervallo [0,1] 
     * del parametro {@code v} passato in input.
     * @param v valore da normalizzare.
     * @return valore normalizzato.
     */
    double getScaledValue(double v){
        return (v-min)/(max-min);
    }
}
