package com.thinkincode.events_android.service;

import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventsAPIService {

    @POST("/events")
    Call<Entity> createEntity (@Body Entity  entity);

    @GET("/events")
    Call<List<Entity>> getAllEntities();


    //curl -v -XPOST 172.17.0.2:8080/login --data '{"username": "user", "password": "password"}' -H "Content-Type: application/json"

    /*
    {"username":"user",
    "roles":["Authority1"],
    "access_token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwibmJmIjoxNTUzODA2ODE0LCJyb2xlcyI6WyJBdXRob3JpdHkxIl0sImlzcyI6ImV2ZW50cy1hcGkiLCJleHAiOjE1NTM4MTA0MTQsImlhdCI6MTU1MzgwNjgxNH0.LR-DE7kG854_XflUuU8z2IJztRoyA1mVtE8NoKgU4Mc",
    "refresh_token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwibmJmIjoxNTUzODA2ODE0LCJyb2xlcyI6WyJBdXRob3JpdHkxIl0sImlzcyI6ImV2ZW50cy1hcGkiLCJpYXQiOjE1NTM4MDY4MTR9.j8L02zlMu-jZF0HhGE0RtWjHuMrYU41IydXKdH50yKg",
    "expires_in":3600}
     */
    @POST("/login")
    Call<AuthenticationToken> getToken(@Body Map<String, String> userCredentials);




}
