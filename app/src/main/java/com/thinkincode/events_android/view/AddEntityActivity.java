package com.thinkincode.events_android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.viewmodel.AddEventViewModel;
import com.thinkincode.events_android.viewmodel.EventsViewModel;
import com.thinkincode.events_android.viewmodel.RepositorySingleton;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.PostEventRequest;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEntityActivity extends AppCompatActivity  {

    private EditText editText_EventName;
    private Button button_SaveEvent;
    private RepositorySingleton repositorySingleton;

    private AuthenticationToken authenticationToken;
    private List<Entity> entities;
    private List<Event> events;
    private String userId;
    private Event event;
    private String entityId = "";
    private String entityname = "";
    private String eventId = "";
    private RecyclerView recyclerViewEntities;
    private RecyclerView recyclerViewEvents;
    private AddEventViewModel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        editText_EventName = findViewById(R.id.editText_event_name);
        button_SaveEvent = findViewById(R.id.button_save_event);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEntities = findViewById(R.id.recyclerViewEntities);
        recyclerViewEntities.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(layoutManager2);
        //  repositorySingleton = RepositorySingleton.getINSTANCE(this, this, this, this);

        Intent intent = getIntent();
        authenticationToken = (AuthenticationToken) intent.getSerializableExtra("authenticationToken");
        viewmodel = ViewModelProviders.of(this).get(AddEventViewModel.class);
        viewmodel.init2(authenticationToken.getAccessToken());
        LiveDataObserveViewModel4Entites();

        button_SaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = editText_EventName.getText().toString();
                PostEventRequest eventRequest = new PostEventRequest(eventId, eventName, entityId, entityname);
                viewmodel.init(authenticationToken.getAccessToken(), eventRequest);
                LiveDataObserveViewModel();
            }
        });
    }

    public void LiveDataObserveViewModel4Entites() {

        viewmodel.getEntitesObservable().observe(this, new Observer<List<Entity>>() {
                    @Override
                    public void onChanged(@Nullable List<Entity> entitieslist) {
                        entities = entitieslist;
                        List<String> entityName = new ArrayList<>();
                        for (Entity entity : entitieslist) {
                            entityName.add(entity.getName());
                        }


                        CarouselLikeAdpater adapter = new CarouselLikeAdpater(AddEntityActivity.this, entityName) {
                            @Override
                            public void theonclick(ViewHolder viewHolder) {

                                entityname = viewHolder.name.getText().toString();
                                for (Entity entitiy : entities) {

                                    if (entitiy.getName().equals(entityname)) {
                                        entityId = entitiy.getId();
                                    }
                                }
                                //repositorySingleton.getCatalogEvents(userId, authenticationToken.getAccessToken(), entityId);
                                viewmodel.init3(authenticationToken.getAccessToken(), entityId);
                                LiveDataObserveViewModel4Events();
                            }
                        };

                        recyclerViewEntities.setAdapter(adapter);
                    }
                }


        );
    }


    public void LiveDataObserveViewModel() {

        viewmodel.getUserObservable().observe(this,


                new Observer<Event>() {
                    @Override
                    public void onChanged(@Nullable Event event) {
                        //   List<Event> listEvents = evm2.getObservable().getValue();
                        finish();
                    }
                });
    }


    public void LiveDataObserveViewModel4Events() {
        viewmodel.getEventsObservable().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> listEvents) {

                events = listEvents;
                List<String> eventsName = new ArrayList<>();
                for (Event event : listEvents) {
                    eventsName.add(event.getName());
                }

                CarouselLikeAdpater adapter = new CarouselLikeAdpater(AddEntityActivity.this, eventsName) {
                    @Override
                    public void theonclick(ViewHolder viewHolder) {
                        String eventname = viewHolder.name.getText().toString();
                        editText_EventName.setText(eventname);


                        for (Event event : events) {

                            if (event.getName().equals(eventname)) {
                                eventId = event.getId();
                            }
                        }
                    }

                };
                recyclerViewEvents.setAdapter(adapter);
            }
        });


    }




    @Override
    protected void onResume() {
        super.onResume();
        // repositorySingleton = RepositorySingleton.getINSTANCE(this, this, this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repositorySingleton = null;
    }

    void messageUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





    public class CarouselLikeAdpater extends RecyclerView.Adapter<CarouselLikeAdpater.ViewHolder> {

        private static final String TAG = "CarouselLikeAdpater";
        private List<String> mNames = new ArrayList<>();
        private List<String> mImagesUrls = new ArrayList<>();
        private Context mcontext;

        public CarouselLikeAdpater(Context mcontext, List<String> mNames, List<String> mImagesUrls) {
            this.mNames = mNames;
            this.mImagesUrls = mImagesUrls;
            this.mcontext = mcontext;
        }

        public CarouselLikeAdpater(Context mcontext, List<String> mNames) {
            this.mNames = mNames;
            this.mcontext = mcontext;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            Log.d(TAG, "onCreateViewHolder: ");
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            Log.d(TAG, "onBindViewHolder: ");
            viewHolder.name.setText(mNames.get(position));
         /*   Glide.with(mcontext)
                    .asBitmap()
                    .load(mImagesUrls.get(position))
                    .into (viewHolder.image);

*/
            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    theonclick(viewHolder);

                }
            });


        }

        public void theonclick(ViewHolder viewHolder) {

        }


        @Override
        public int getItemCount() {
            return mNames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView image;
            TextView name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
            }
        }

    }

}
