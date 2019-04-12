package com.thinkincode.events_android.viewmodel;

import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.service.EventsAPIService;
import com.thinkincode.events_android.service.NetworkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsAPIServiceViewMode {
    private ListerAnswer listerAnswer;
    private ListerAnswerToken listerAnswerToken;
    private ListerAccountEvents listerAccountEvents;
    private static EventsAPIService apiService;
    private List<User> listUsers = new ArrayList<>();
    private List<Event> listEvents = new ArrayList<>();

    public interface ListerAnswer {
        void onInputSent(CharSequence  input);
    }

    public interface ListerAnswerToken {
        void onInputSentToken(AuthenticationToken input);
    }

    public interface ListerAccountEvents {
        void onInputSentAccountEvents(List<Event> listEvents);
        void onInputError(String error);
    }


    public EventsAPIServiceViewMode(ListerAnswer listerAnswer) {
        this.listerAnswer = listerAnswer;
        if (apiService==null)
            apiService= NetworkHelper.create();
    }

    public EventsAPIServiceViewMode(ListerAnswerToken listerAnswerToken,ListerAnswer listerAnswer) {
        this.listerAnswerToken = listerAnswerToken;
        this.listerAnswer = listerAnswer;
        if (apiService==null)
            apiService= NetworkHelper.create();
    }

    public EventsAPIServiceViewMode(ListerAccountEvents listerAnswer) {
        this.listerAccountEvents = listerAnswer;
        if (apiService==null)
            apiService= NetworkHelper.create();
    }

    public void createEntity(Entity entity){

        Call<Entity> result = apiService.createEntity(entity);

        result.enqueue(new Callback<Entity>() {
            @Override
            public void onResponse(Call<Entity> call, Response<Entity> response) {
                final Entity entityResponse ;
                if (response.body() != null ) {
                    entityResponse = response.body();
                    entity.setId(entityResponse.getId());
                    listerAnswer.onInputSent(Messages.SAVE_ENTITY_SUCCESSFUL.toString());
                }
            }

            @Override
            public void onFailure(Call<Entity> call, Throwable t) {
                listerAnswer.onInputSent(Messages.SAVE_ENTITY_ERROR.toString());
            }
        });
    }

    public void registerUser(User newUser){
        Call<User> registerCallback = apiService.registerUser(newUser);

        registerCallback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    listerAnswer.onInputSent(Messages.SAVE_USER_SUCCESSFUL.toString());
                } else {
                    listerAnswer.onInputSent(Messages.SAVE_USER_ERROR.toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void getToken(Map<String,String> userCredentials){
        Call<AuthenticationToken> result =  apiService.getToken(userCredentials);
        result.enqueue(new Callback<AuthenticationToken>() {
            @Override
            public void onResponse(Call<AuthenticationToken> call, Response<AuthenticationToken> response) {
                if (response.body() != null)
                {
                   AuthenticationToken authenticationToken = response.body();
                    listerAnswerToken.onInputSentToken(authenticationToken);
                }else{
                    if (response.message().contains("Unauthorized")){
                        listerAnswer.onInputSent(Messages.TOKEN_ERROR.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationToken> call, Throwable t) {
               listerAnswer.onInputSent(Messages.LOGIN_USER_ERROR.toString());
            }
        });
    }

    public void getAccountEvents(String token,String id ){

        Call<List<Event>> eventsResult = apiService.getAccountEvents( id,"Bearer " + token);
        eventsResult.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.body() != null) {
                    listEvents = response.body();
                    listerAccountEvents.onInputSentAccountEvents(listEvents);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                listerAccountEvents.onInputError(Messages.ERROR_GET_EVENTS.toString());
            }
        });
    }

    public void getUsers(String token){
        Call<List<User>> UserResult = apiService.getUsers( "Bearer " + token);

        UserResult.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    listUsers = response.body();
                    String id = listUsers.get(0).getId();
                    getAccountEvents(id,token);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                listerAccountEvents.onInputError(t.getMessage());
            }


        });
    }
}
