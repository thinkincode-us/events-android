package com.thinkincode.events_android.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewModelSingleton;
import com.thinkincode.events_android.viewmodel.Messages;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.PostEventRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddEntityActivity extends AppCompatActivity implements EventsAPIServiceViewModelSingleton.ListerEntity,
        EventsAPIServiceViewModelSingleton.ListerAnswer,EventsAPIServiceViewModelSingleton.ListerUserId, EventsAPIServiceViewModelSingleton.ListerCatalogEvents, AdapterView.OnItemSelectedListener {

    private EditText editText_EntityName;
    private Button button_SaveEntity;
    private EventsAPIServiceViewModelSingleton eventsAPIServiceViewModelSingleton;

    private AuthenticationToken authenticationToken;
    private Spinner spinnerEntities, spinnerEvents;
    private List<Entity> entities;
    private List<Event> events;
    private String userId;
    private Event event;
    private String entityId="";
    private String entityname="";
    private String eventId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        editText_EntityName = findViewById(R.id.editText_event_name);
        button_SaveEntity = findViewById(R.id.button_save_event);
        spinnerEntities = findViewById(R.id.spinnerEntities);
        spinnerEvents = findViewById(R.id.spinnerEvents);
        spinnerEntities.setOnItemSelectedListener(this);
        spinnerEvents.setOnItemSelectedListener(this);
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this,this, this, this);
        Intent intent = getIntent();
        authenticationToken = (AuthenticationToken) intent.getSerializableExtra("authenticationToken");
        eventsAPIServiceViewModelSingleton.getUsersForEntities(authenticationToken.getAccessToken());

        button_SaveEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = editText_EntityName.getText().toString();
                PostEventRequest eventRequest = new PostEventRequest(eventId, eventName, entityId, entityname);
                eventsAPIServiceViewModelSingleton.postAccountEvents(authenticationToken.getAccessToken(), userId, eventRequest);
            }
        });
    }



    /*
    @Override
    public void onInputSent(CharSequence input) {
        messageUser(input.toString());
        if (Messages.SAVE_ENTITY_SUCCESSFUL.toString().equals(input.toString())) {
            finish();
        }
    }*/

    // public void setSpinner (List<Entity> list) {
    @Override
    public void onInputSentAccountEntites(List<Entity> list) {
        entities = list;
        List<String> entityName = new ArrayList<>();
        for (Entity entity : list) {
            entityName.add(entity.getName());
        }
        ArrayAdapter<String> adp = new ArrayAdapter<>(AddEntityActivity.this, android.R.layout.simple_spinner_item, entityName);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEntities.setAdapter(adp);
    }

    @Override
    public void onInputSentCatalogEvents(List<Event> listEvents) {
        events = listEvents;
        List<String> eventsName = new ArrayList<>();
        for (Event event : listEvents) {
            eventsName.add(event.getName());
        }
        ArrayAdapter<String> adp = new ArrayAdapter<>(AddEntityActivity.this, android.R.layout.simple_spinner_item, eventsName);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvents.setAdapter(adp);
    }

    @Override
    public void onInputError(String error) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this,this, this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            eventsAPIServiceViewModelSingleton = null;
    }

    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerEntities){

        entityname = parent.getItemAtPosition(position).toString();
        for (Entity entitiy:entities) {

            if (entitiy.getName().equals(entityname)){
                entityId = entitiy.getId();
            }
        }
            eventsAPIServiceViewModelSingleton.getCatalogEvents(userId, authenticationToken.getAccessToken(),entityId);

        }
        else if(parent.getId() == R.id.spinnerEvents)
        {
            String eventname = parent.getItemAtPosition(position).toString();
            editText_EntityName.setText(eventname);


            for (Event event:events) {

                if (event.getName().equals(eventname)){
                    eventId = event.getId();
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onInputSentUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onInputSent(CharSequence input) {
        finish();
    }
}
