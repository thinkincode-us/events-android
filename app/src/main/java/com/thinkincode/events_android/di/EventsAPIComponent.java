package com.thinkincode.events_android.di;

import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.view.LoginActivity;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewMode;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = EventsAPIServiceModule.class)
public interface EventsAPIComponent {

    EventsAPIService getApiServices();

}
