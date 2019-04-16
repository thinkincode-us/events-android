package com.thinkincode.events_android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewMode;
import com.thinkincode.events_android.viewmodel.Messages;

public class AddEntityActivity extends AppCompatActivity implements EventsAPIServiceViewMode.ListerAnswer {

    private EditText editText_EntityName;
    private Button button_SaveEntity;
    private EventsAPIServiceViewMode eventsAPIServiceViewMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_EntityName = findViewById(R.id.editText_entity_name);
        button_SaveEntity = findViewById(R.id.button_save_entity);
        eventsAPIServiceViewMode = EventsAPIServiceViewMode.getINSTANCE(this,null,null);
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

    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
