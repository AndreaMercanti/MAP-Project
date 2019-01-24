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
 * <p>Modella un item specifico che consiste in una coppia attributo-valore dove 
 * l'attributo è di tipo discreto e quindi anche il valore è di tipo discreto.
 * @author Andrea Mercanti
 */
public class DiscreteItem extends Item {
    /**
     * <p>Inizializza i valori descriventi l'item con i valori {@code attribute}
     * e {@code value} passati come parametri.
     * @param attribute attributo di tipo discreto.
     * @param value valore che assume l'item tra quelli nel dominio dell'attributo.
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * <p>Sovraccarica il metodo della classe superiore {@code Item}, quindi 
     * verifica se il valore dell'item puntato da {@code this} è uguale al 
     * valore dell'item discreto {@code a} passato in input, restituendo:</p>
     * <ul>
     * <li>0 se {@code equals()} sui due valori discreti risulta vero;</li>
     * <li>1 altrimenti.</li>
     * </ul>
     * @param a item di tipo discreto da confrontare.
     * @return 0 se i due valori sono uguali, 1 altrimenti.
     */
    @Override
    double distance(Object a) {
        DiscreteItem item = (DiscreteItem) a;
        if (((String) getValue()).equals((String) item.getValue()))
            return 0.0;
        else
            return 1.0;
    }
}
