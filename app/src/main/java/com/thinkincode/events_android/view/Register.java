package com.thinkincode.events_android.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewModelSingleton;
import com.thinkincode.events_android.viewmodel.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements EventsAPIServiceViewModelSingleton.ListerAnswer {

    private TextView firstName, lastName, phone, email, password, passwordCopy;
    private TextView policy, match;

    private EventsAPIServiceViewModelSingleton eventsAPIServiceViewModelSingleton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.surname);

        phone = findViewById(R.id.phone);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        passwordCopy = findViewById(R.id.confirmpassword);

        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher2);
        passwordCopy.addTextChangedListener(textWatcher2);
        policy = findViewById(R.id.textViewPolicy);
        match = findViewById(R.id.textViewMatch);
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this, null, null);
    }

    private boolean flagIsEmpty = true;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() == 0) {
                flagIsEmpty = true;
            } else flagIsEmpty = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                messageUser("You can't leave empty");
                flagIsEmpty = true;
            } else flagIsEmpty = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                messageUser("You can't leave empty");
                flagIsEmpty = true;
            } else flagIsEmpty = false;

        }
    };
    private final TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (!PasswordValidator.validate(s.toString())) {
                    policy.setText(getApplicationContext().getString(R.string.policy));
                    policy.setVisibility(View.VISIBLE);
                    return;
                } else {
                    policy.setVisibility(View.GONE);
                }
            }
            String pass1 = password.getText().toString(), pass2 = passwordCopy.getText().toString();
            if (!pass1.equals(pass2)) {
                match.setVisibility(View.VISIBLE);
                match.setText(getApplicationContext().getString(R.string.password_is_not_match));
            } else {
                match.setVisibility(View.GONE);
            }
        }
    };


    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void registerUser(View view) {


        if (flagIsEmpty) {
            messageUser("Required data is empty");
            return;
        }
        if (!(password.getText().toString()).equals(passwordCopy.getText().toString())) {
            messageUser("Password isn't match");
            return;
        }

        if (!(password.getText().toString()).equals(passwordCopy.getText().toString())) {
            messageUser(getApplicationContext().getString(R.string.password_is_not_match));
            return;
        }

        if (!PasswordValidator.validate(password.getText().toString())) {
            messageUser("Password isn't well structured");
            return;
        }


        if (!isValidEmail(email.getText().toString())) {

            messageUser("Invalid email address");
            return;
        }

        User newUser = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                phone.getText().toString(),
                email.getText().toString(),
                password.getText().toString());
        eventsAPIServiceViewModelSingleton.registerUser(newUser);
    }

    @Override
    public void onInputSent(CharSequence input) {
        if (Messages.SAVE_USER_SUCCESSFUL.toString().equals(input.toString())) {
            messageUser(input.toString());
            try {
                Thread.sleep(500);
                finish();
            } catch (Exception ex) {
            }
        }
    }


    public static class PasswordValidator {

        private static Pattern pattern;
        private static Matcher matcher;

        private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        public PasswordValidator() {

        }

        public static boolean validate(final String password) {
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            boolean io = matcher.matches();
            return io;
        }

        public boolean validate2(final String password) {
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            boolean io = matcher.matches();
            return io;
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
