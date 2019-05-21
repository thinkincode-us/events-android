package com.thinkincode.events_android.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.thinkincode.events_android.di.DaggerEventsAPIComponent;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Entity;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.model.PostEventRequest;
import com.thinkincode.events_android.model.User;
import com.thinkincode.events_android.service.EventsAPIService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositorySingleton {
    private ListerAnswer listerAnswer = null;
    private ListerAnswerToken listerAnswerToken = null;
    private ListerAccountEvents listerAccountEvents = null;
    private ListerPdf listerPdf;


    private ListerUserId listerUserId;
    private ListerEntity listerEntity;

    private ListerCatalogEvents listerCatalogEvents;
    private static EventsAPIService apiService = DaggerEventsAPIComponent.builder().build().getApiServices();
    private List<User> listUsers = new ArrayList<>();
    private List<Event> listEvents = new ArrayList<>();


    private static RepositorySingleton INSTANCE = null;
    private String string;

    public interface ListerPdf {
        void Download(Response<ResponseBody> response);


    }

    public interface ListerAnswer {
        void onInputSent(CharSequence input);
    }

    public interface ListerUserId {
        void onInputSentUserId(String userId);
    }

    public interface ListerAnswerToken {
        void onInputSentToken(AuthenticationToken input);
    }

    public interface ListerAccountEvents {
        void onInputSentAccountEvents(List<Event> listEvents);

        void onInputError(String error);
    }

    public interface ListerEntity {
        void onInputSentAccountEntites(List<Entity> listEntity);

        void onInputError(String error);
    }

    public interface ListerCatalogEvents {
        void onInputSentCatalogEvents(List<Event> listEvents);

        void onInputError(String error);
    }

    private RepositorySingleton() {
    }


    public static RepositorySingleton getINSTANCE(ListerAnswer listerAnswer, ListerAnswerToken listerAnswerToken, ListerAccountEvents listerAccountEvents) {
        if (INSTANCE == null)
            INSTANCE = new RepositorySingleton();
        INSTANCE.listerAccountEvents = listerAccountEvents;
        INSTANCE.listerAnswer = listerAnswer;
        INSTANCE.listerAnswerToken = listerAnswerToken;

        return INSTANCE;

    }

    public static RepositorySingleton getINSTANCE(ListerUserId listerUserId, ListerPdf listerPdf) {
        if (INSTANCE == null)
            INSTANCE = new RepositorySingleton();
        INSTANCE.listerPdf = listerPdf;
        INSTANCE.listerUserId = listerUserId;

        return INSTANCE;

    }


    public static RepositorySingleton getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new RepositorySingleton();
        // INSTANCE.listerAccountEvents = listerAccountEvents;


        return INSTANCE;

    }

    public static RepositorySingleton getINSTANCE(ListerEntity listerEntity, ListerAnswer listerAnswer, ListerUserId listerUserId, ListerCatalogEvents listerCatalogEvents) {
        if (INSTANCE == null)
            INSTANCE = new RepositorySingleton();
        INSTANCE.listerEntity = listerEntity;
        INSTANCE.listerAnswer = listerAnswer;
        INSTANCE.listerUserId = listerUserId;
        INSTANCE.listerCatalogEvents = listerCatalogEvents;

        return INSTANCE;

    }


    public void createEntity(Entity entity) {

        Call<Entity> result = apiService.createEntity(entity);

        result.enqueue(new Callback<Entity>() {
            @Override
            public void onResponse(Call<Entity> call, Response<Entity> response) {
                final Entity entityResponse;
                if (response.body() != null) {
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


    public void registerUser(User newUser) {
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

    public void getToken(Map<String, String> userCredentials) {
        Call<AuthenticationToken> result = apiService.getToken(userCredentials);
        result.enqueue(new Callback<AuthenticationToken>() {
            @Override
            public void onResponse(Call<AuthenticationToken> call, Response<AuthenticationToken> response) {
                if (response.body() != null) {
                    AuthenticationToken authenticationToken = response.body();
                    listerAnswerToken.onInputSentToken(authenticationToken);
                } else {
                    if (response.message().contains("Unauthorized")) {
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

    public MutableLiveData getAccountEvents(String token, String id) {
        final MutableLiveData<List<Event>> listEvents = new MutableLiveData<>();
        Call<List<Event>> eventsResult = apiService.getAccountEvents(id, "Bearer " + token);
        eventsResult.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.body() != null) {
                    listEvents.setValue(response.body());
                    //listerAccountEvents.onInputSentAccountEvents(listEvents);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                listerAccountEvents.onInputError(Messages.ERROR_GET_EVENTS.toString());
            }
        });
        return listEvents;
    }


    public MutableLiveData<List<Event>> getUsers(String token) {
        final MutableLiveData<List<Event>> listEvents = new MutableLiveData<>();

        Call<List<User>> call = apiService.getUsers("Bearer " + token);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    listUsers = response.body();
                    String id = listUsers.get(0).getId();
                    listerUserId.onInputSentUserId(id);

                    List<Event> listevents = new ArrayList<>();
                    listevents.add(new Event("id", "name", "entityId", "entityName"));

                    listevents.add(new Event("id2", "name2", "entityId2", "entityName2"));
                    //  listEvents = getAccountEvents(token, id);

                    listEvents.setValue(listevents);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                listerAccountEvents.onInputError(t.getMessage());
            }


        });
        return listEvents;
    }


    public Observable<List<Event>> getUserEvents(String token) {
        return apiService.getUsersRxJava(token).doOnNext(user->listerUserId.onInputSentUserId(user.get(0).getId())).flatMap(user -> apiService.getAccountEventsRxJava(user.get(0).getId(), token));
    }

    // listerUserId.onInputSentUserId(id);



    public void postAccountEvents(String token, String id, PostEventRequest eventRequest) {
        Call<Event> CreateEventResult = apiService.postAccountEvents(id, "Bearer " + token, eventRequest);
        CreateEventResult.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.body() != null) {
                    //response.body();
                    listerAnswer.onInputSent("");
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

            }
        });
    }

    public void getEntities(String token, String id) {
        Call<List<Entity>> EntitiesResult = apiService.getEntities(id, "Bearer " + token);
        EntitiesResult.enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.body() != null) {
                    ArrayList<Entity> ListEntites = (ArrayList<Entity>) response.body();
                    listerEntity.onInputSentAccountEntites(ListEntites);

                    //  ListEntites.get(0);

                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {

            }
        });
    }

    public void getUsersDetails(String token) {
        Call<List<User>> UserResult = apiService.getUsers("Bearer " + token);

        UserResult.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    listUsers = response.body();
                    String id = listUsers.get(0).getId();
                    listerUserId.onInputSentUserId(id);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                listerAccountEvents.onInputError(t.getMessage());
            }


        });
    }

    public void getUsersForEntities(String token) {
        Call<List<User>> UserResult = apiService.getUsers("Bearer " + token);

        UserResult.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    listUsers = response.body();
                    String id = listUsers.get(0).getId();
                    listerUserId.onInputSentUserId(id);

                    getEntities(token, id);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                listerAccountEvents.onInputError(t.getMessage());
            }


        });
    }

    public void getCatalogEvents(String id, String token, String entityId) {
        Call<List<Event>> events = apiService.getCatalogEvents(id, "Bearer " + token, entityId);
        events.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.body() != null) {
                    listerCatalogEvents.onInputSentCatalogEvents(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    public void getPdfEvents(String id, String token) {
        Call<ResponseBody> pdfResult = apiService.getPdfEvents(id, "Bearer " + token);
        pdfResult.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    listerPdf.Download(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


}
