package com.example.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.elanlar.AdverAllDetails;
import com.example.user.elanlar.R;
import com.example.user.models.AdverModel;
import com.example.user.utils.StaticValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter  extends BaseAdapter implements Filterable
{

    private Context mcontext;
    ArrayList<AdverModel> mList;
    ArrayList<AdverModel> filterList;
    CustomFilter customFilter;

    TextView textView_price,textView_name,textView_date;
    ImageView imageView_adver_list;
    CheckBox checkBox_liked;
    CardView cardView;
    boolean liked;


    private static  final String UNLIKE_ADVER = "http://"+StaticValues.IPV4+":8080/api/adver/advers/unLikeAdver/";
    private static  final String LIKE_ADVER = "http://"+StaticValues.IPV4+":8080/api/adver/advers/addToLiked/";

    public GridViewAdapter(@Nullable Context mcontext, @Nullable ArrayList<AdverModel> mList)
    {
        this.mcontext = mcontext;
        this.mList = mList;
        this.filterList=mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView ==null)
        {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.gridview_model,parent,false);
        }

        final AdverModel adverModel = (AdverModel) this.getItem(position);

        textView_price =convertView.findViewById(R.id.textView_card_price);
        textView_name =convertView.findViewById(R.id.textViev_card_name);
        textView_date =convertView.findViewById(R.id.textView_card_date);
        imageView_adver_list =convertView.findViewById(R.id.imageView_list);
        checkBox_liked =convertView.findViewById(R.id.checkBox_liked);
        cardView =convertView.findViewById(R.id.cadrView);

        textView_name.setText(adverModel.getAdver_name());
        textView_price.setText(adverModel.getAdver_price()+" AZN");
        textView_date.setText(adverModel.getAdver_date().substring(0,10));


        final String liked_id = adverModel.getId();



        // set image in list
        String imageString = adverModel.getAdver_image();
        byte[] userImagebyte = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(userImagebyte,0,userImagebyte.length);
        imageView_adver_list.setImageBitmap(bitmap);


    /*   //check liked or unliked
        liked = adverModel.is_liked();
        if(liked) { checkBox_liked.setButtonDrawable(R.drawable.liked_asset); }
        else { checkBox_liked.setButtonDrawable(R.drawable.unlike_asset); }*/


        //click an adver
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {   Intent intent  = new Intent(mcontext,AdverAllDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID", adverModel.getId());
                mcontext.startActivity(intent); }});


        // like or unlike adver
        checkBox_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cheked = ((CheckBox)view).isChecked();

                if(cheked)
                { likeAdver(liked_id);}
                else
                { unlikeAdver(liked_id);}
            }
        });

        return convertView;
    }




    private void unlikeAdver(String id)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, UNLIKE_ADVER+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if(response.getString("id")!=null)
                    { Toast.makeText(mcontext,"Seçilmiş elanlardan çıxarıldı",Toast.LENGTH_LONG).show(); }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mcontext, " xeta  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(jsonObjectRequest);


    }


    private void likeAdver(String id)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, LIKE_ADVER+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if(response.getString("id")!=null)
                    { Toast.makeText(mcontext,"Seçilmiş elanlara əlavə olundu",Toast.LENGTH_LONG).show(); }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mcontext, " xeta  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(jsonObjectRequest);



    }


    @Override
    public Filter getFilter()
    {
        if(customFilter == null)
        {
            customFilter = new CustomFilter();
        }

        return customFilter;
    }


    //inner class
    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results =  new FilterResults();

            if(constraint!=null && constraint.length()>0)
            {
                constraint = constraint.toString().toUpperCase();

                ArrayList<AdverModel> filters = new ArrayList<>();

                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getAdver_name().toUpperCase().contains(constraint))
                    {
                        //return list
                        AdverModel a = new AdverModel(filterList.get(i).getId(),
                                                      filterList.get(i).getAdver_name(),
                                                      filterList.get(i).getAdver_price(),
                                                      filterList.get(i).getAdver_date(),
                                                      filterList.get(i).getAdver_number(),
                                                      filterList.get(i).getAdver_city(),
                                                      filterList.get(i).getAdver_detail(),
                                                      filterList.get(i).is_liked(),
                                                      filterList.get(i).getAdver_image(),
                                                      filterList.get(i).getOwner_name(),
                                                      filterList.get(i).getOwner_mail(),
                                                      filterList.get(i).getOwner_phone(),
                                                      filterList.get(i).getUpdated_at(),
                                                      filterList.get(i).getAdver_type(),
                                                      filterList.get(i).getType_id());
                        filters.add(a);
                    }
                }

                results.count=filters.size();
                results.values = filters;
            }
            else
            {
                results.count=filterList.size();
                results.values = filterList;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constrait, FilterResults results)
        {
            mList = (ArrayList<AdverModel>) results.values;
            notifyDataSetChanged();

        }
    }
}
