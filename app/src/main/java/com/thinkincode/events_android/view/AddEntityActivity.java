package com.thinkincode.events_android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEntityActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity_TAG";
    public static final String SAVE_ENTITY_ERROR = "We couldn't save the entity";

    private EditText editText_EntityName;
    private Button button_SaveEntity;
    private EventsAPIService  apiService = NetworkHelper.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_EntityName = findViewById(R.id.editText_entity_name);
        button_SaveEntity = findViewById(R.id.button_save_entity);
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
                Call<Entity> result = apiService.createEntity(entity);

                result.enqueue(new Callback<Entity>() {
                    @Override
                    public void onResponse(Call<Entity> call, Response<Entity> response) {
                        final Entity entityResponse ;
                        if (response.body() != null ) {
                            entityResponse = response.body();
                            entity.setId(entityResponse.getId());
                            Log.d(TAG, "onResponse: " + entity);
                            Toast
                                    .makeText(AddEntityActivity.this,"Saved!",Toast.LENGTH_SHORT)
                                    .show();
                       }
                       finish();
                    }

                    @Override
                    public void onFailure(Call<Entity> call, Throwable t) {
                        Toast
                                .makeText(AddEntityActivity.this,
                                        SAVE_ENTITY_ERROR, Toast.LENGTH_LONG)
                                .show();
                        Log.d(TAG, "onResponse: " + entity);

                    }
                });
            }
        });
    }
}
