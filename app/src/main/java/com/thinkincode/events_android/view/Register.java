package com.thinkincode.events_android.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.viewmodel.RepositorySingleton;
import com.thinkincode.events_android.viewmodel.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements RepositorySingleton.ListerAnswer {

    private static final String TAG = "Register";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private TextView firstName, state, city, lastName, phone, email, password, passwordCopy;
    private TextView policy, match;
    private FusedLocationProviderClient mFusedLocationClient;

    private RepositorySingleton repositorySingleton;
    private boolean mLocationPermissionGranted = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.surname);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkMapServices();

        repositorySingleton = RepositorySingleton.getINSTANCE(this, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        repositorySingleton = RepositorySingleton.getINSTANCE(this, null, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repositorySingleton = null;
    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isGpsEnabled()) {

                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isGpsEnabled() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        } else {
            if (mLocationPermissionGranted) {
                populateCityState();
            } else {
                getLocationPermission();
            }
        }
        return true;
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            populateCityState();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {

        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Register.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Register.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    populateCityState();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    populateCityState();
                } else {
                    getLocationPermission();
                }
            }
        }

    }

    private void populateCityState() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();

                    List<Address> addresses = new ArrayList<>();
                    Geocoder geocoder = new Geocoder(Register.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        state.setText(addresses.get(0).getAdminArea());
                        city.setText(addresses.get(0).getLocality());
                    } catch (IOException e) {
                        e.printStackTrace();

                    }


                }
            }
        });

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
        repositorySingleton.registerUser(newUser);
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
