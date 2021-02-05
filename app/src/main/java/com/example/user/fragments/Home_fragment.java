package com.example.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.adapters.GridViewAdapter;
import com.example.user.adapters.RecyclerAdapterType;
import com.example.user.elanlar.AdverAllDetails;
import com.example.user.elanlar.R;
import com.example.user.models.AdverModel;
import com.example.user.models.TypeModel;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;


public class Home_fragment extends Fragment
{
    RecyclerView recyclerView_type;
    RecyclerAdapterType recyclerAdapterType;
    GridViewAdapter gridViewAdapter;
    ArrayList<TypeModel> typeModelArrayList;
    ArrayList<AdverModel> adverModelArrayList;
    GridView gridView_adver;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView_home;


    //urls
    private static  final String GET_All_TYPE = "http://"+StaticValues.IPV4+":8080/api/adver/type";
    private static  final String GET_All_ADVER = "http://"+StaticValues.IPV4+":8080/api/adver/advers";




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {

        View view =inflater.inflate(R.layout.fragment_home_fragment, viewGroup, false);


        //init
        recyclerView_type = view.findViewById(R.id.recyclerType);
        gridView_adver = view.findViewById(R.id.gridView_Alladver);
        swipeRefreshLayout = view.findViewById(R.id.swipe_up_home);
        searchView_home = view.findViewById(R.id.searchView_home);



       getAllType();

        showAllwithGridView();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                getAllType();
                showAllwithGridView();
            }
        });

        //searvh view implementing
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                if (s != null && s.length() > 0) {
                    gridViewAdapter.getFilter().filter(s); }
                return false;
            }
        });

        return view;

    }




    private void showAllwithGridView()
    {
        swipeRefreshLayout.setRefreshing(true);

        adverModelArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_ADVER, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        AdverModel adverModel = new AdverModel();

                        JSONObject jsonObjectMain = response.getJSONObject(i);

                        adverModel.setId(jsonObjectMain.getString("id"));
                        adverModel.setAdver_name(jsonObjectMain.getString("adver_name"));
                        adverModel.setAdver_date(jsonObjectMain.getString("adver_date"));
                        adverModel.setAdver_price(jsonObjectMain.getString("adver_price"));
                        adverModel.setAdver_image(jsonObjectMain.getString("adver_image"));
                        adverModel.set_liked(jsonObjectMain.getBoolean("_liked"));
                        adverModel.setAdver_number(jsonObjectMain.getString("adver_number"));

                        adverModelArrayList.add(adverModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                gridViewAdapter = new GridViewAdapter(getContext(),adverModelArrayList);
                gridView_adver.setAdapter(gridViewAdapter);

                swipeRefreshLayout.setRefreshing(false);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }


    private void getAllType()
    {
        typeModelArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_TYPE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {

                for(int i = 0; i < response.length(); i++)
                {
                    try {
                        TypeModel typeModel = new TypeModel();

                        JSONObject jsonObjectMain = response.getJSONObject(i);

                        typeModel.setId(jsonObjectMain.getString("id"));
                        typeModel.setType_name(jsonObjectMain.getString("type_name"));

                        typeModelArrayList.add(typeModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerAdapterType =  new RecyclerAdapterType(getContext(),typeModelArrayList);
                recyclerView_type.setAdapter(recyclerAdapterType);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }



}


