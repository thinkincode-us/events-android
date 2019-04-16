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
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewMode;

import java.util.ArrayList;
import java.util.List;

public class RecyclerEvents extends AppCompatActivity implements UserHistoryAdapter.ItemClickLister, EventsAPIServiceViewMode.ListerAccountEvents {
    public static final String TAG = "RecyclerUsers_TAG";

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private EventsAPIServiceViewMode eventsAPIServiceViewMode;
    private FloatingActionButton floatingActionButton;
    private AuthenticationToken authenticationToken;
    List<User> listUsers = new ArrayList<>();
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
                intent.putExtra("authenticationToken",authenticationToken) ;

                startActivity(intent);
            }
        });
        eventsAPIServiceViewMode = new EventsAPIServiceViewMode(this);
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
            eventsAPIServiceViewMode.getUsersForEvents(authenticationToken.getAccessToken());

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }

    @Override
    public void onInputSentAccountEvents(List<Event> listEvents) {
        adapter = new UserHistoryAdapter(RecyclerEvents.this, listEvents);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onInputError(String error) {

    }
}
