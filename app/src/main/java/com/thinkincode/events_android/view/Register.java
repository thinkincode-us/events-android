package com.thinkincode.events_android.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private TextView firstName, lastName, phone, email, password;

    private EventsAPIService eventsAPIService = NetworkHelper.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName= findViewById(R.id.firstName);
        lastName= findViewById(R.id.surname);

        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        password=findViewById(R.id.phone);


    }


    public void registerUser(){

        User newUser = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                phone.getText().toString(),
                email.getText().toString(),
                password.getText().toString());

        Call<User> registerCallback = eventsAPIService.registerUser(newUser);

        registerCallback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // @TODO: do something with the user, perhaps store it?
                User retrievedUser = response.body();

                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
