package com.thinkincode.events_android.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerEntities extends AppCompatActivity implements UserHistoryAdapter.ItemClickLister {
    public static final String TAG = "RecyclerUsers_TAG";

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private EventsAPIService apiEntityService = NetworkHelper.create();
    private FloatingActionButton floatingActionButton;
    private AuthenticationToken authenticationToken;
    List<Entity> listEntity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_users);
        recycler = findViewById(R.id.recycler_view_users);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        authenticationToken = (AuthenticationToken) intent.getSerializableExtra("authenticationToken");

        updateData();

        floatingActionButton = findViewById(R.id.fab_add_entity);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerEntities.this, AddEntityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        try {
            Call<List<Entity>> result = apiEntityService.getAllEntities();
            result.enqueue(new Callback<List<Entity>>() {
                @Override
                public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                    listEntity = response.body();
                    adapter = new UserHistoryAdapter(RecyclerEntities.this, listEntity);
                    recycler.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Entity>> call, Throwable t) {
                    Log.d(TAG, "onCreate: " + t.getMessage());

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }
}
