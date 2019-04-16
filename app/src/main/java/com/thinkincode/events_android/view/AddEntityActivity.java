package com.thinkincode.events_android.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewMode;
import com.thinkincode.events_android.viewmodel.Messages;

import java.util.List;

public class AddEntityActivity extends AppCompatActivity implements EventsAPIServiceViewMode.ListerAnswer, EventsAPIServiceViewMode.ListerEntity {

    private EditText editText_EntityName;
    private Button button_SaveEntity;
    private EventsAPIServiceViewMode eventsAPIServiceViewMode;
    private AuthenticationToken authenticationToken;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        editText_EntityName = findViewById(R.id.editText_event_name);
        button_SaveEntity = findViewById(R.id.button_save_event);
        spinner =  findViewById(R.id.spinner);

        eventsAPIServiceViewMode = new EventsAPIServiceViewMode((EventsAPIServiceViewMode.ListerAccountEvents) this, this);
        Intent intent = getIntent();
        authenticationToken = (AuthenticationToken) intent.getSerializableExtra("authenticationToken");
        eventsAPIServiceViewMode.getUsersForEntities(authenticationToken.getAccessToken());

        button_SaveEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entity entity = new Entity();
                String entityName = editText_EntityName.getText().toString();
                if (entityName.isEmpty()){
                    Toast
                            .makeText(AddEntityActivity.this,"Please enter a name", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                entity.setName(entityName);
                eventsAPIServiceViewMode.createEntity(entity);
            }
        });
    }

    @Override
    public void onInputSent(CharSequence input) {
        messageUser(input.toString());
        if (Messages.SAVE_ENTITY_SUCCESSFUL.toString().equals(input.toString())){
            finish();
        }
    }

   // public void setSpinner (List<Entity> list) {
   @Override
   public void onInputSentAccountEntites(List<Entity> list){
        ArrayAdapter<Entity> adp = new ArrayAdapter<>(AddEntityActivity.this, android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp);
    }

    @Override
    public void onInputError(String error) {

    }


    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
