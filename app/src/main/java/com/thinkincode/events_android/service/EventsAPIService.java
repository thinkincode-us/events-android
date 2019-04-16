package com.thinkincode.events_android.service;

import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsAPIService {

    @POST("/events")
    Call<Entity> createEntity (@Body Entity  entity);

    @GET("/events")
    Call<List<Entity>> getAllEntities();

    @POST("/login")
    Call<AuthenticationToken> getToken(@Body Map<String, String> userCredentials);

    @GET("/api/v1/catalog/events")
    Call<List<Event>> getEvents(@Query("entityId") String entityId , @Header("Authorization") String auth);

    @POST("/api/v1/account/register")
    Call<User> registerUser (@Body User user);

    @GET("/api/v1/account/users")
    Call<List<User>> getUsers( @Header("Authorization") String auth);


    @GET("api/v1/{accountId}/catalog/entities")
    Call<List<Entity>> getEntities( @Path("accountId") String accountId, @Header("Authorization") String auth);

    @GET("/api/v1/accounts/{accountId}/events")
    Call<List<Event>> getAccountEvents( @Path("accountId") String accountId, @Header("Authorization") String auth);

    @POST("/api/v1/accounts/{accountId}/events")
    Call<Event>  postAccountEvents(@Path("accountId") String accountId, @Header("Authorization") String token, @ Body Event event );


}
