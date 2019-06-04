package com.thinkincode.events_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.PostEventRequest;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddEventViewModel extends AndroidViewModel {

    private RepositorySingleton repositorySingleton;
    private MutableLiveData<Event> EventObservable = new MutableLiveData<>();
    private MutableLiveData<List<Entity>> EntitiesObservable = new MutableLiveData<>();

    private MutableLiveData<List<Event>> EventsObservable = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AddEventViewModel(@NonNull Application application) {
        super(application);

        repositorySingleton = RepositorySingleton.getINSTANCE();
    }

    public void init(String accessToken, PostEventRequest eventRequest) {



        repositorySingleton
                .postAccountEvent("Bearer " + accessToken, eventRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new
                                   io.reactivex.Observer<Event>() {


                                       @Override
                                       public void onSubscribe(Disposable d) {
                                           disposables.add(d);

                                       }

                                       @Override
                                       public void onNext(Event event) { EventObservable.setValue(event);
                                       }

                                       @Override
                                       public void onError(Throwable e) {

                                       }

                                       @Override
                                       public void onComplete() {

                                       }

                                   });

    }


    public void init2(String accessToken) {



        repositorySingleton.getEntities("Bearer " + accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new
                                   io.reactivex.Observer<List<Entity>>() {


                                       @Override
                                       public void onSubscribe(Disposable d) {
                                           disposables.add(d);

                                       }

                                       @Override
                                       public void onNext(List<Entity> entities) {
                                           EntitiesObservable.setValue(entities);
                                       }

                                       @Override
                                       public void onError(Throwable e) {

                                       }

                                       @Override
                                       public void onComplete() {

                                       }

                                   });

    }

    public void init3(String token, String entityId) {
        repositorySingleton.getCatalogEvents("Bearer " + token, entityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new
                                   io.reactivex.Observer<List<Event>>() {


                                       @Override
                                       public void onSubscribe(Disposable d) {
                                           disposables.add(d);

                                       }

                                       @Override
                                       public void onNext(List<Event> events) {
                                           EventsObservable.setValue(events);
                                       }

                                       @Override
                                       public void onError(Throwable e) {

                                       }

                                       @Override
                                       public void onComplete() {

                                       }

                                   });
    }




    public LiveData<Event> getUserObservable() {
        return EventObservable;

    }

    public LiveData<List<Entity>> getEntitesObservable() {
        return EntitiesObservable;

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public LiveData<List<Event>>  getEventsObservable() {
        return EventsObservable;
    }


}
