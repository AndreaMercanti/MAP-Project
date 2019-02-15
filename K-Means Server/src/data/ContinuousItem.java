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

/**
 * <p>Modella un item specifico che consiste in una coppia attributo-valore dove 
 * l'attributo è di tipo continuo e quindi il valore è uno scalare.
 * @author Andrea Mercanti 
 */
public class ContinuousItem extends Item{
    /**
     * <p>Costruisce l'item di tipo ocontinuo, iniziallizzando i valori dei 
     * membri attributi con quelli passati in input.
     * @param attribute tipo di attributo (continuo).
     * @param value valore che assume l'item tra quelli nel dominio dell'attributo.
     */
    public ContinuousItem(Attribute attribute, Object value) {
        super(attribute, value);
    }

    /**
     * <p>Sovraccarica il metodo della classe superiore {@code Item}, quindi 
     * determina il valore assoulto della differenza tra il valore scalare 
     * dell'item corrente e il valore scalare associato all'item {@code a}.
     * @param a item di tipo continuo da confrontare.
     * @return il valore assoulto della differenza tra i valori scalari di
     * {@code this} e {@code a}.
     */
    @Override
    double distance(Object a) {
        double result;
        ContinuousItem item = (ContinuousItem) a;
        double val_1 = ((ContinuousAttribute) getAttribute()).getScaledValue((double) getValue());
        double val_2 = ((ContinuousAttribute) item.getAttribute()).getScaledValue((double) item.getValue());
        result = Math.abs(val_1 - val_2);
        return result;
    }

}
