package com.thinkincode.events_android.di;

import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class EventsAPIServiceModule {

  @Provides
  static EventsAPIService provideEventsAPIService(){ return NetworkHelper.create(); }

}