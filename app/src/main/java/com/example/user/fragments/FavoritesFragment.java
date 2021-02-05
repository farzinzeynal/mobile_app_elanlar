package com.example.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

import java.util.ArrayList;


public class FavoritesFragment extends Fragment
{

    private static  final String GET_All_LIKED_ADVER = "http://"+StaticValues.IPV4+":8080/api/adver/advers/getAllLiked";
    GridView gridView_favorites;
    GridViewAdapter gridViewAdapter_favorites;
    SwipeRefreshLayout swipeRefreshLayout_favorites;
    ArrayList<AdverModel> favoriteArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_favorites, container, false);

        gridView_favorites = view.findViewById(R.id.gridView_favorites);
        swipeRefreshLayout_favorites = view.findViewById(R.id.swipe_up_favorites);


        swipeRefreshLayout_favorites.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllLiked();
            }
        });

        getAllLiked();



        return view;
    }

    private void getAllLiked()
    {
        swipeRefreshLayout_favorites.setRefreshing(true);
        favoriteArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_LIKED_ADVER, null, new Response.Listener<JSONArray>() {
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

                        favoriteArrayList.add(adverModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                gridViewAdapter_favorites = new GridViewAdapter(getContext(),favoriteArrayList);
                gridView_favorites.setAdapter(gridViewAdapter_favorites);

                swipeRefreshLayout_favorites.setRefreshing(false);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                swipeRefreshLayout_favorites.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }




}
