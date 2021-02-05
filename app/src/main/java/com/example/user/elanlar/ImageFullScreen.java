package com.example.user.elanlar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.utils.StaticValues;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageFullScreen extends AppCompatActivity
{

    private static  final String GET_ADVER_BY_ID = "http://"+StaticValues.IPV4+":8080/api/adver/advers/";


    ImageView imageView_full_screen;
    SwipeRefreshLayout swipeRefreshLayout_imageFull;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        imageView_full_screen = findViewById(R.id.imageView_full_screen);
        swipeRefreshLayout_imageFull = findViewById(R.id.swipe_up_refresh_image_full);

        Intent intent = getIntent();
        final String adver_id = intent.getExtras().getString("adver_id");

        //for custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        swipeRefreshLayout_imageFull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                getImageFromAd(adver_id);
                swipeRefreshLayout_imageFull.setRefreshing(false);
            }
        });



        getImageFromAd(adver_id);
    }

    private void getImageFromAd(String adver_id)
    {
        swipeRefreshLayout_imageFull.setRefreshing(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_ADVER_BY_ID+adver_id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response)
            {
                try {


                    JSONObject jsonObject = new JSONObject(response.toString());

                    //set image
                    String imageString = response.getString("adver_image");
                    byte[] userImagebyte = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(userImagebyte,0,userImagebyte.length);
                    imageView_full_screen.setImageBitmap(bitmap);

                    swipeRefreshLayout_imageFull.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout_imageFull.setRefreshing(false);
                Toast.makeText(getApplicationContext(), " xeta  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);




    }
}
