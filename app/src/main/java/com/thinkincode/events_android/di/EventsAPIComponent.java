package com.thinkincode.events_android.di;

import com.thinkincode.events_android.service.EventsAPIService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = EventsAPIServiceModule.class)
public interface EventsAPIComponent {

    EventsAPIService getApiServices();

}
