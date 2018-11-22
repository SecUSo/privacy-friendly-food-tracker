package org.secuso.privacyfriendlyfoodtracker.activities.adapter;


public class DatabaseEntry {
    public int energy;
    public int amount;
    public String name;
    public String id;
    public DatabaseEntry(String id, String name, int amount, int energy){
        this.id = id;
        this.amount = amount;
        this.energy = energy;
        this.name = name;
    }
}