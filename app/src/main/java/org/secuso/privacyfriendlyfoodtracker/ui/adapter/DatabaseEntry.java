/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.ui.adapter;

/**
 * Models a consumed entry.
 * @author Simon Reinkemeier
 */
public class DatabaseEntry {
    public float energy;
    public float carbs;
    public float sugar;
    public float protein;
    public float fat;
    public float satFat;
    public int amount;
    public String name;
    public int id;
    public DatabaseEntry(int id, String name, int amount, float energy, float carbs, float sugar, float protein, float fat, float satFat){
        this.id = id;
        this.amount = amount;
        this.energy = energy;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.fat = fat;
        this.satFat = satFat;
        this.name = name;
    }
}