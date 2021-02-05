package com.example.user.elanlar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.adapters.CustomSpinnerAdapter;
import com.example.user.fragments.Home_fragment;
import com.example.user.models.AdverModel;
import com.example.user.models.AdverRequest;
import com.example.user.models.TypeModel;
import com.example.user.retrtofit_api.AdverApi;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAnAdver extends AppCompatActivity
{



    EditText editText_adver_name,
            editText_adver_price,
            editText_adver_city,
            editText_adver_detail,
            editText_owner_name,
            editText_owner_mail,
            editText_owner_phone;

    Spinner spinner_type;
    ImageView imageView_add_adver;
    Button button_add_adver;

    ArrayList<TypeModel> typeNameList;
    CustomSpinnerAdapter customSpinnerAdapter;

    ProgressBar progressBar_add;

    String type_id;
    Uri uri;


    //retrofit Api
    AdverApi adverApi;

    private static  final String GET_All_TYPE = "http://"+StaticValues.IPV4+":8080/api/adver/type";
    private static  final String ADD_ADVER = "http://"+StaticValues.IPV4+":8080/api/adver/advers";

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final static  int REQUEST_CODE_GALLERY = 999;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_an_adver);


        //init
        editText_adver_name = findViewById(R.id.editText_adver_name);
        editText_adver_price = findViewById(R.id.editText_adver_price);
        editText_adver_city = findViewById(R.id.editText_adver_city);
        editText_adver_detail = findViewById(R.id.editText_adver_detail);
        editText_owner_name = findViewById(R.id.editText_owner_name);
        editText_owner_mail = findViewById(R.id.editText_owner_mail);
        editText_owner_phone = findViewById(R.id.editText_owner_phone);
        spinner_type = findViewById(R.id.spinner_type);
        imageView_add_adver = findViewById(R.id.imageView_add_adver);
        button_add_adver = findViewById(R.id.button_add_adver);
        progressBar_add = findViewById(R.id.progressBar_add_adver);

        //for custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        progressBar_add.setVisibility(View.GONE);

        fillSpinner();


        //select type of adver
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                TypeModel typeModel = customSpinnerAdapter.getItem(i);
                type_id = typeModel.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        imageView_add_adver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getDialog();
            }
        });




        button_add_adver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (editText_adver_name.length() == 0) { editText_adver_name.setError("Boş buraxıla bilməz"); return;}

                if (editText_adver_price.length() == 0) { editText_adver_price.setError("Boş buraxıla bilməz"); return;}

                if (editText_adver_city.length() == 0) { editText_adver_city.setError("Boş buraxıla bilməz"); return;}

                if (editText_adver_detail.length() == 0) { editText_adver_detail.setError("Boş buraxıla bilməz"); return;}

                if (editText_owner_name.length() == 0) { editText_owner_name.setError("Boş buraxıla bilməz"); return;}

                if (editText_owner_mail.length() == 0) { editText_owner_mail.setError("Boş buraxıla bilməz"); return;}

                if (editText_owner_phone.length() == 0) { editText_owner_phone.setError("Boş buraxıla bilməz"); return;}

                addAdverRetrofit(type_id,imageViewToString(imageView_add_adver));


            }
        });

    }



    private void addAdverRetrofit(String type_id, String imageString)
    {
        progressBar_add.setVisibility(View.VISIBLE);

        //Rertofit config
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticValues.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        adverApi = retrofit.create(AdverApi.class);


        //create new requset model
        final AdverRequest request = new AdverRequest(editText_adver_name.getText().toString().trim(),
                imageString,
                editText_adver_price.getText().toString().trim(),
                editText_adver_city.getText().toString().trim(),
                editText_adver_detail.getText().toString().trim(),
                editText_owner_name.getText().toString().trim(),
                editText_owner_mail.getText().toString().trim(),
                editText_owner_phone.getText().toString().trim(),
                type_id);


        Call<AdverModel> call = adverApi.createPost(request);

        call.enqueue(new Callback<AdverModel>() {
            @Override
            public void onResponse(Call<AdverModel> call, retrofit2.Response<AdverModel> response)
            {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show(); return;
                }

                AdverModel adverModel = response.body();
            }

            @Override
            public void onFailure(Call<AdverModel> call, Throwable t) {
                // Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        Toast.makeText(getApplicationContext(),"Elan əlavə olundu",Toast.LENGTH_LONG).show();
        progressBar_add.setVisibility(View.GONE);
        finish();


    }


    private void getDialog()
    {

        LinearLayout linearLayout_photo;
        LinearLayout linearLayout_galery;

        final AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alert = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert = new AlertDialog.Builder(this);
        }


        LayoutInflater layoutInflater = getLayoutInflater();

        View view1 = layoutInflater.inflate(R.layout.dialog_layout,null);

        linearLayout_photo = view1.findViewById(R.id.linear_takePhoto);
        linearLayout_galery = view1.findViewById(R.id.linear_takeGalery);


        alert.setView(view1);

        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setCancelable(true);


        linearLayout_photo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                getFromCamera();
                dialog.cancel();
            }
        });

        linearLayout_galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromGalery();
                dialog.cancel();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getFromCamera()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }



    private void getFromGalery()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY);
    }



    private void fillSpinner()
    {
        typeNameList = new ArrayList<>();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_TYPE,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                for(int i = 0; i < response.length(); i++)
                {

                    try {

                        JSONObject jsonObjectMain = response.getJSONObject(i);

                        TypeModel typeModel= new TypeModel();

                        typeModel.setId(jsonObjectMain.getString("id"));
                        typeModel.setType_name(jsonObjectMain.getString("type_name"));

                        typeNameList.add(typeModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, typeNameList);
                customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_type.setAdapter(customSpinnerAdapter);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

    }


    //convert Image to String
    private String imageViewToString(ImageView imageView_add_adver)
    {
        Bitmap bitmap = ((BitmapDrawable)imageView_add_adver.getDrawable()).getBitmap();
        ByteArrayOutputStream stream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }

    //convert Image to byte[]
    private byte[] imageViewTobyte(ImageView imageView_add_adver)
    {
        Bitmap bitmap = ((BitmapDrawable)imageView_add_adver.getDrawable()).getBitmap();
        ByteArrayOutputStream stream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    //result and open activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        //result for galery
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null)
        {
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView_add_adver.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //result for camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView_add_adver.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    //request permisson
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        //request for galery
        if (requestCode == REQUEST_CODE_GALLERY)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Galareya'ya girməyə icazəniz yoxdur",Toast.LENGTH_LONG).show();
            }
            return;
        }

        //request for camera
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Kamera icazəsi verildi", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Kameranın açılmasına icazə verilmədi", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}

