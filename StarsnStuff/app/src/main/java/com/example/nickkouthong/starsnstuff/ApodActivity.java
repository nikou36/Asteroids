package com.example.nickkouthong.starsnstuff;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApodActivity extends AppCompatActivity {

    private ImageView apodPic;
    private TextView description;
    private String url;
    private String explanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apod_activity);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        apodPic = findViewById(R.id.apodPic);
        description = findViewById(R.id.description);
        //String picUrl ="";

        String demoUrl = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(demoUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String jsonData = response.body().string();
                    if(response.isSuccessful()) {
                        JSONObject data = new JSONObject(jsonData);
                        url = data.getString("url");
                        explanation = data.getString("explanation");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()  {
                                Glide.with(ApodActivity.this).load(url).into(apodPic);
                                description.setText(explanation);
                            }
                        });


                    }
                }
                catch (IOException e) {

                }
                catch (JSONException e) {

                }
            }
        });







    }


}
