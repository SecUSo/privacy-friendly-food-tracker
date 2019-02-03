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

import java.util.Date;
import java.util.Locale;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseEntry;

public class DatabaseAdapter {

    public boolean insertEntry(String name, int amount, int energy, Date date) {
        return true;
    }

    public boolean removeEntry(String id) {
        return true;
    }

    public boolean editEntry(String id, String name, int amount, int energy) {
        return true;
    }

    public DatabaseEntry[] getEntriesForDay(Date date) {
        DatabaseEntry firstEntry = new DatabaseEntry("1234", "Apple", 100, 222);
        DatabaseEntry secondEntry = new DatabaseEntry("1234", "Pear", 100, 222);

        DatabaseEntry[] list = {firstEntry, secondEntry};
        return list;
    }

}

