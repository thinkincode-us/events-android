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
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerEvents extends AppCompatActivity implements UserHistoryAdapter.ItemClickLister {
    public static final String TAG = "RecyclerUsers_TAG";

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private EventsAPIService apiEntityService = NetworkHelper.create();
    private FloatingActionButton floatingActionButton;
    private AuthenticationToken authenticationToken;
    List<Event> listEvents = new ArrayList<>();

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
                Intent intent = new Intent(RecyclerEvents.this, AddEntityActivity.class);
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
            Call<List<Event>> result = apiEntityService.getEvents( "0dd5bdbf-40b7-48d2-aa64-8fe6f970b491","Bearer " + authenticationToken.getAccessToken());
            result.enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    if (response.body() != null) {
                        listEvents = response.body();
                        adapter = new UserHistoryAdapter(RecyclerEvents.this, listEvents);
                        recycler.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    Log.d(TAG, "onCreate: " + t.getMessage());

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }
}
