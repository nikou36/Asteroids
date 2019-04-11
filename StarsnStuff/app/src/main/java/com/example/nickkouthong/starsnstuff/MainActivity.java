package com.example.nickkouthong.starsnstuff;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateTimePatternGenerator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    //Globals for storing asteroid data.
    public static final String TAG = MainActivity.class.getSimpleName();
    public static ArrayList<String> mNames = new ArrayList<>();
    public static ArrayList<String> mSizes = new ArrayList<>();
    public static ArrayList<String> mDangers = new ArrayList<>();
    JSONArray mdata = new JSONArray();
    String dateFacDB;

    private TextView title;




    //Initialize main method by displaying list of Asteroids gathered from NASA's API.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();
        bar.hide();


        title = findViewById(R.id.titleText);
        Date currentTime = Calendar.getInstance().getTime();

        //Get and set current date.
        dateFacDB = DateFormat.format("yyyy-MM-dd", currentTime).toString();
        title.setText("Report for " + dateFacDB);

        String neoUrl = "https://api.nasa.gov/neo/rest/v1/feed?start_date="+ dateFacDB + "&end_date=" + dateFacDB + "&api_key=DEMO_KEY";

        //Request to NASA API
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(neoUrl).build();
        Call call = client.newCall(req);
        final ArrayList<String> tempNames = new ArrayList<>();
        final ArrayList<String> tempSize = new ArrayList<>();
        final ArrayList<String> tempDangers = new ArrayList<>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Network Failed");

            }

            @Override
            public void onResponse(Call call, Response response) {

                if(response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject entireData = new JSONObject(jsonData);
                        JSONObject neededData = entireData.getJSONObject("near_earth_objects");
                        mdata = neededData.getJSONArray(dateFacDB);

                        /*
                         Run on UI thread to prevent asynchronous tasks.
                         NOTE: Implement worker threads in the future to improve
                         performance.
                        */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i < mdata.length(); i++) {
                                    try{
                                        JSONObject aster = mdata.getJSONObject(i);
                                        mNames.add(aster.getString("name"));
                                        Log.e(TAG, "" + aster.getString("name"));
                                        mSizes.add(aster.getJSONObject("estimated_diameter")
                                                .getJSONObject("feet").getDouble("estimated_diameter_max") + "");
                                        //Log.e(TAG, "up size: " + mNames.size());
                                        if(aster.getBoolean("is_potentially_hazardous_asteroid")) {
                                            mDangers.add("Is potentially dangerous");
                                        }else {
                                            mDangers.add("Not dangerous");
                                        }
                                    }catch(JSONException e) {

                                    }
                                }
                                Log.e(TAG, "Size of global names: " + mNames.size() );
                                Log.e(TAG, "Size of global size: " + mSizes.size() );
                                Log.e(TAG, "Size of global dangers: " + mDangers.size() );
                                initRecyclerView();
                            }
                        });


                    }catch(IOException e) {

                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);

                    }
                }

            }
        });



        //Initialize and set button to picture of the day activity
        Button apod = findViewById(R.id.apodButton);
         apod.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                openApod();
             }
         });
    }

    //Prepare recyclerview by calling RecyclerView adapter
    private void initRecyclerView() {
        Log.e(TAG,"Size of lists entering RV init: " + this.mNames.size());
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mSizes, mDangers);
        Log.d(adapter.TAG, " adapter size: " + adapter.neoNames.size());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    //Opens picture of the day activity
    public void openApod() {
        Intent intent = new Intent(this,ApodActivity.class);
        startActivity(intent);
    }

}
