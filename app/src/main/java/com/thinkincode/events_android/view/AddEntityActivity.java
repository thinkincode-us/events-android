package com.thinkincode.events_android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewModelSingleton;
import com.thinkincode.events_android.viewmodel.Messages;

public class AddEntityActivity extends AppCompatActivity implements EventsAPIServiceViewModelSingleton.ListerAnswer {

    private EditText editText_EntityName;
    private Button button_SaveEntity;
    private EventsAPIServiceViewModelSingleton eventsAPIServiceViewModelSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_EntityName = findViewById(R.id.editText_entity_name);
        button_SaveEntity = findViewById(R.id.button_save_entity);
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this,null,null);
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
                eventsAPIServiceViewModelSingleton.createEntity(entity);
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

    @Override
    protected void onResume() {
        super.onResume();
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this,null,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            eventsAPIServiceViewModelSingleton = null;
    }

    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
