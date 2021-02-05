package com.example.user.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.elanlar.AdverAllDetails;
import com.example.user.elanlar.ByTypeActivity;
import com.example.user.elanlar.R;
import com.example.user.models.TypeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapterType extends  RecyclerView.Adapter<RecyclerAdapterType.CostomViewHolder> {


    private Context mcontext;
    private ArrayList<TypeModel> myList;

    public RecyclerAdapterType(@Nullable  Context mcontext,@Nullable ArrayList<TypeModel> myList) {
        this.mcontext = mcontext;
        this.myList = myList;
    }


    @NonNull
    @Override
    public CostomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.costom_type_view, null);
        CostomViewHolder holder = new CostomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CostomViewHolder holder, int position)
    {
        final TypeModel typeModel = myList.get(position);
        holder.button_type_view.setText(typeModel.getType_name());

        holder.button_type_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type_id = typeModel.getId();
                String type_name = typeModel.getType_name();
                Intent intent = new Intent(mcontext,ByTypeActivity.class);
                intent.putExtra("type_id",type_id);
                intent.putExtra("type_name",type_name);
                mcontext.startActivity(intent);
             }
        });
    }


    @Override
    public int getItemCount()
    { return myList.size(); }

    /********************************************* CostomViewHolder CLASS************************************************************************/

    class CostomViewHolder extends RecyclerView.ViewHolder
    {

        Button button_type_view;


        public CostomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            button_type_view = itemView.findViewById(R.id.buttonType);
        }

    }
}



