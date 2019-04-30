package com.thinkincode.events_android.view;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.AuthenticationToken;
import com.thinkincode.events_android.model.Event;
import com.thinkincode.events_android.viewmodel.EventsAPIServiceViewModelSingleton;
import com.thinkincode.events_android.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.support.constraint.Constraints.TAG;

public class RecyclerEvents extends AppCompatActivity implements UserHistoryAdapter.ItemClickLister, EventsAPIServiceViewModelSingleton.ListerAccountEvents, EventsAPIServiceViewModelSingleton.ListerUserId, EventsAPIServiceViewModelSingleton.ListerPdf {
    public static final String TAG = "RecyclerUsers_TAG";

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private EventsAPIServiceViewModelSingleton eventsAPIServiceViewModelSingleton;
    private FloatingActionButton floatingActionButton;
    private AuthenticationToken authenticationToken;
    // final String filename = "Events.pdf";
    //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filename);

    private String userId;
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
                intent.putExtra("authenticationToken", authenticationToken);
                startActivity(intent);

            }
        });
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this, this, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:

                eventsAPIServiceViewModelSingleton.getPdfEvents(userId, authenticationToken.getAccessToken());
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
        eventsAPIServiceViewModelSingleton = EventsAPIServiceViewModelSingleton.getINSTANCE(this, this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsAPIServiceViewModelSingleton = null;
    }


    private void updateData() {
        try {
            eventsAPIServiceViewModelSingleton.getUsers(authenticationToken.getAccessToken());
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

    @Override
    public void onInputSentUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void Download(retrofit2.Response<ResponseBody> response) {
        final String filename = "Events.pdf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filename);
        DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask(this, this, file);
        downloadFileAsyncTask.execute(response.body().byteStream());
    }


    public class DownloadFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {

        final String appDirectoryName = "Events";

        private final Context ctx;
        private final Activity activity;
        File file;

        public DownloadFileAsyncTask(Context ctx, Activity activity, File file) {
            this.file = file;
            this.ctx = ctx;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(InputStream... params) {
            InputStream inputStream = params[0];
            OutputStream output = null;

            int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;


            if (ContextCompat.checkSelfPermission(ctx.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
            }


            try {
                output = new FileOutputStream(file);
                //output = new FileOutputStream( filename,true);
                byte[] buffer = new byte[1024]; // or other buffer size
                int read;

                Log.d(TAG, "Attempting to write to: " + file);
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    Log.v(TAG, "Writing to buffer to output stream.");
                }
                Log.d(TAG, "Flushing output stream.");
                output.flush();
            } catch (IOException e) {
                Log.e(TAG, "IO Exception: " + e.getMessage());
                Log.d(TAG, "Output flushed.");
                e.printStackTrace();
                return false;
            } finally {


                try {
                    if (output != null) {
                        output.close();

                        Log.d(TAG, "Output stream closed sucessfully.");
                    } else {
                        Log.d(TAG, "Output stream is null");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Log.d(TAG, "Download success: " + result);
            Toast.makeText(ctx.getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri URI = FileProvider.getUriForFile(ctx, getPackageName() + ".provider", file);
            intent.setDataAndType(URI, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        }
    }


}