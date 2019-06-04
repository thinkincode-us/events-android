package com.thinkincode.events_android.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.PostEventRequest;
import com.thinkincode.events_android.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
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
    Observable<List<User>> getUsersRxJava(@Header("Authorization") String auth);

    @GET("/api/v1/account/users")
    Call<List<User>> getUsers(@Header("Authorization") String auth);

    @GET("api/v1/{accountId}/catalog/entities")
    Observable<List<Entity>> getEntities( @Path("accountId") String accountId, @Header("Authorization") String auth);

    @GET("/api/v1/accounts/{accountId}/events")
   Observable<List<Event>> getAccountEvents(@Path("accountId") String accountId, @Header("Authorization") String auth);

    @POST("/api/v1/accounts/{accountId}/events")
    Observable<Event> postAccountEvents(@Path("accountId") String accountId, @Header("Authorization") String token, @Body PostEventRequest event );

    @GET("/api/v1/{accountId}/catalog/events")
    Observable<List<Event>> getCatalogEvents(@Path("accountId") String accountId,@Header("Authorization") String token,@Query("entityId") String entityId);

    @GET("/api/v1/accounts/{accountId}/events.pdf")

    Call<ResponseBody> getPdfEvents(@Path("accountId") String accountId, @Header("Authorization") String token );


}
