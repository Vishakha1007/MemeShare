package com.application.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    ImageView imageView;
    ProgressBar progressBar;

    String currentImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progress);
        loadmeme();


    }
private void loadmeme()
{
    progressBar.setVisibility(View.VISIBLE);
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    final String url ="https://meme-api.herokuapp.com/gimme";



// Request a string response from the provided URL.
    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
            new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    try {
                         currentImage=response.getString("url");
                        Glide.with(MainActivity.this).load(currentImage).listener(new RequestListener<Drawable>() {
                            @Override
                               public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   progressBar.setVisibility(View.GONE);
                                   return false;
                               }
                           @Override
                               public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                               }
                        }
                        ).into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG,"That didn't work!");

        }
    });

// Add the request to the RequestQueue.
    queue.add(jsonObjectRequest);
}
    public void sharememe(View view) {
        Intent intent= new Intent(Intent.ACTION_SEND);
intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme from my new MemeShare application "+currentImage+" ");
    Intent chooser= Intent.createChooser(intent, "This meme send by my new MemeShare application");
startActivity(chooser);
    }

    public void nextmeme(View view) {
        loadmeme();
    }
}
