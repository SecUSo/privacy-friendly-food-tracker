package org.secuso.privacyfriendlyfoodtracker.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseEntry;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class OverviewViewModel extends AndroidViewModel {
    private MutableLiveData<List<DatabaseEntry>> list = new MutableLiveData<>();

    DatabaseFacade dbHelper = null;

    public OverviewViewModel(@NonNull Application application) {
        super(application);

        try {
            dbHelper = new DatabaseFacade(application.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(final Date day) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                list.postValue(dbHelper.getEntriesForDay(day));
            }
        });
    }

    public LiveData<List<DatabaseEntry>> getList() {
        return list;
    }


    public void deleteEntryById(int id, Date date) {
        dbHelper.deleteEntryById(id, date, this);
    }

    public void editEntryById(int id, int amount, Date date) {
        dbHelper.editEntryById(id, amount,date,this);

    }
}
