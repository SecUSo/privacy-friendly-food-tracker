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

