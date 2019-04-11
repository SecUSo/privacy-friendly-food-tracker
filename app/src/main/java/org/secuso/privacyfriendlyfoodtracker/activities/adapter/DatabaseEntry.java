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
package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

/**
 * Models a consumed entry.
 * @author Simon Reinkemeier
 */
public class DatabaseEntry {
    public float energy;
    public int amount;
    public String name;
    public String id;
    public DatabaseEntry(String id, String name, int amount, float energy){
        this.id = id;
        this.amount = amount;
        this.energy = energy;
        this.name = name;
    }
}