package com.thinkincode.events_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.thinkincode.events_android.di.DaggerEventsAPIComponent;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.service.EventsAPIService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventsViewModel extends AndroidViewModel {
    private RepositorySingleton repositorySingleton;
    private MutableLiveData<List<Event>> newsResponseObservable  = new MutableLiveData<>();
    private static EventsAPIService apiService = DaggerEventsAPIComponent.builder().build().getApiServices();
    private final CompositeDisposable disposables = new CompositeDisposable();
    public ObservableField<List<Event>> project = new ObservableField<>();


    public EventsViewModel(@NonNull Application application) {
        super(application);
        repositorySingleton = RepositorySingleton.getINSTANCE();
    }




    public void init(String accessToken) {



        repositorySingleton
                .getUserEvents("Bearer " + accessToken)
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
                                           newsResponseObservable.setValue(events);
                                       }

                                       @Override
                                       public void onError(Throwable e) {

                                       }

                                       @Override
                                       public void onComplete() {

                                       }

                                   });

    }





    public LiveData<List<Event>> getObservable() {
        return newsResponseObservable;

    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }
}