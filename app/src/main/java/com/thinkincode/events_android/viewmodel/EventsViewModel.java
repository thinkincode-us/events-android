package com.thinkincode.events_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.thinkincode.events_android.model.Event;

import java.util.List;

public class EventsViewModel extends AndroidViewModel {
    private RepositorySingleton repositorySingleton;
    private LiveData<List<Event>> newsResponseObservable;


    public ObservableField<List<Event>> project = new ObservableField<>();


    public EventsViewModel(@NonNull Application application) {
        super(application);
        repositorySingleton = RepositorySingleton.getINSTANCE();
    }


    public void init( String accessToken) {
        newsResponseObservable = repositorySingleton.getUsers(accessToken);
    }


    public LiveData<List<Event>> getObservable() {
        return newsResponseObservable;
    }
}