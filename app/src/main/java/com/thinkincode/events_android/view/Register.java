package com.thinkincode.events_android.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private TextView firstName, lastName, phone, email, password,passwordCopy;

    private EventsAPIService eventsAPIService = NetworkHelper.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName= findViewById(R.id.firstName);
        lastName= findViewById(R.id.surname);

        phone= findViewById(R.id.phone);
        email= findViewById(R.id.username);
        password=findViewById(R.id.password);

        passwordCopy=findViewById(R.id.confirmpassword);

        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        passwordCopy.addTextChangedListener(textWatcher);


    }
    private boolean flagIsEmpty = true;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() == 0) {
                flagIsEmpty = true;
            }else flagIsEmpty = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                messageUser("You can't leave empty");
                flagIsEmpty = true;
            }else flagIsEmpty = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                messageUser("You can't leave empty");
                flagIsEmpty = true;
            }else flagIsEmpty = false;
        }
    };

    void messageUser(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void registerUser(View view){
        if (!(password.getText().toString()).equals(passwordCopy.getText().toString())){
            messageUser("Password isn't match");
            return;
        }

        if (flagIsEmpty){
            messageUser("Some data is empty");
            return;
        }

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
                if (response.body()!=null) {
                    messageUser("User register");
                }else {
                    messageUser("Error in register");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
