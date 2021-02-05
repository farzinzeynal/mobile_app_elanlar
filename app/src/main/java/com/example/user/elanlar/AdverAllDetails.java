package com.example.user.elanlar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.utils.StaticValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdverAllDetails extends AppCompatActivity {


    TextView textView_adver_name,
            textView_adver_price,
            textView_adver_city,
            textView_adver_detail,
            textView_adver_date,
            textView_adver_number,
            textView_adver_update,
            textView_owner_name,
            textView_owner_mail,
            textView_owner_phone,
            textView_adver_type;

    String adver_id = null;

    ImageView imageView_adver_detail;
    FloatingActionButton fab_call;
    Button button_all_adver_by_user;

    private static  final String GET_ADVER_BY_ID = "http://"+StaticValues.IPV4+":8080/api/adver/advers/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adver_all_details);

        //for custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("ID");

        //init
        textView_adver_name = findViewById(R.id.textViev_detail_adver_name);
        textView_adver_price = findViewById(R.id.textView_detail_adver_price);
        textView_adver_city = findViewById(R.id.textView_detail_adver_city);
        textView_adver_detail = findViewById(R.id.textView_detail_adver_detail);
        textView_adver_number = findViewById(R.id.textView_detail_adver_num);
        textView_adver_date = findViewById(R.id.textView_detail_adver_date);
        textView_adver_update = findViewById(R.id.textView_detail_adver_update);
        textView_adver_type = findViewById(R.id.textView_detail_adver_type);
        textView_owner_name = findViewById(R.id.textView_detail_owner_name);
        textView_owner_mail = findViewById(R.id.textView_detail_owner_mail);
        textView_owner_phone = findViewById(R.id.textView_detail_owner_phone);
        button_all_adver_by_user = findViewById(R.id.button_all_adver_by_user);
        imageView_adver_detail = findViewById(R.id.imageView_detail_adver);
        fab_call = findViewById(R.id.fab_call);



        getDetails(id);

        //click fab and make call
        fab_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(AdverAllDetails.this,new String[]{Manifest.permission.CALL_PHONE},2);
                        return;
                    }
                }

                Intent makeCall = new Intent(Intent.ACTION_CALL);
                makeCall.setData(Uri.parse("tel:" + StaticValues.phone_number));
                startActivity(makeCall);
            }
        });

        // view full image
        imageView_adver_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdverAllDetails.this,ImageFullScreen.class);
                intent.putExtra("adver_id",adver_id);
                startActivity(intent);
            }
        });

    }



    private void getDetails(String id)
    {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_ADVER_BY_ID+id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response)
            {
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());

                    JSONObject jsonObject2= response.getJSONObject("adver_type");

                    adver_id = response.getString("id");

                    textView_adver_name.setText(response.getString("adver_name"));
                    textView_adver_price.setText(response.getString("adver_price")+" AZN");
                    textView_adver_number.setText(response.getString("adver_number"));
                    textView_adver_city.setText(response.getString("adver_city"));
                    textView_adver_detail.setText(response.getString("adver_detail"));
                    textView_adver_date.setText(response.getString("adver_date").substring(0,10));
                    textView_adver_update.setText(response.getString("updated_at").substring(0,10));
                    textView_adver_type.setText(jsonObject2.getString("type_name"));
                    textView_owner_name.setText(response.getString("adver_owner_name"));
                    textView_owner_mail.setText(response.getString("adver_owner_mail"));
                    textView_owner_phone.setText(response.getString("adver_owner_phone"));


                    //set image
                    String imageString = response.getString("adver_image");
                    byte[] userImagebyte = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(userImagebyte,0,userImagebyte.length);
                    imageView_adver_detail.setImageBitmap(bitmap);

                    StaticValues.phone_number= response.getString("adver_owner_phone");
                    StaticValues.imageEncoded= response.getString("adver_image");

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), " xeta  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);



    }
}
