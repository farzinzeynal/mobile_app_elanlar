package com.example.user.elanlar;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.adapters.GridViewAdapter;
import com.example.user.models.AdverModel;
import com.example.user.models.TypeModel;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ByTypeActivity extends AppCompatActivity
{

    GridViewAdapter gridViewAdapterType;
    ArrayList<AdverModel> adverModelArrayList;
    GridView gridView_byType;
    SearchView searchView_byType;
    SwipeRefreshLayout swipeRefreshLayoutByType;
    TextView textView_title;

    String type_name;
    String type_id;

    private static  final String GET_All_ADVER_BY_TYPE = "http://"+StaticValues.IPV4+":8080/api/adver/advers/getAllByType/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_type);


        gridView_byType = findViewById(R.id.gridview_byType);
        searchView_byType = findViewById(R.id.searchView_byType);
        swipeRefreshLayoutByType = findViewById(R.id.swipe_up_type);
        textView_title = findViewById(R.id.textView_title);



        //for custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        Intent intent = getIntent();
        type_id =  intent.getExtras().getString("type_id");
        type_name=  intent.getExtras().getString("type_name");

        getAllByType(type_id);

        textView_title.setText(type_name);

        swipeRefreshLayoutByType.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllByType(type_id);
            }
        });

        searchView_byType.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                if (s != null && s.length() > 0) {
                    gridViewAdapterType.getFilter().filter(s); }
                return false;
            }
        });

    }


    private void getAllByType(String type_id)
    {
      swipeRefreshLayoutByType.setRefreshing(true);

        adverModelArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_ADVER_BY_TYPE+type_id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        AdverModel adverModel = new AdverModel();

                        JSONObject jsonObjectMain = response.getJSONObject(i);

                        JSONObject jsonObject2 = jsonObjectMain.getJSONObject("adver_type");

                        adverModel.setId(jsonObjectMain.getString("id"));
                        adverModel.setAdver_name(jsonObjectMain.getString("adver_name"));
                        adverModel.setAdver_date(jsonObjectMain.getString("adver_date"));
                        adverModel.setAdver_price(jsonObjectMain.getString("adver_price"));
                        adverModel.setAdver_image(jsonObjectMain.getString("adver_image"));
                        adverModel.set_liked(jsonObjectMain.getBoolean("_liked"));

                        type_name = jsonObject2.getString("type_name");

                        adverModelArrayList.add(adverModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                gridViewAdapterType = new GridViewAdapter(getApplicationContext(),adverModelArrayList);
                gridView_byType.setAdapter(gridViewAdapterType);

                swipeRefreshLayoutByType.setRefreshing(false);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                swipeRefreshLayoutByType.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
