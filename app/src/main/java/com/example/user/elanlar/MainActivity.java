package com.example.user.elanlar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.fragments.FavoritesFragment;
import com.example.user.fragments.Home_fragment;
import com.example.user.utils.StaticValues;

public class MainActivity extends AppCompatActivity
{

    AlertDialog.Builder builder;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new Home_fragment()).commit();
                    return true;
                case R.id.navigation_add_adver:
                    startActivity(new Intent(MainActivity.this,AddAnAdver.class));
                    return true;
                case R.id.navigation_liked_adver:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new FavoritesFragment()).commit();
                    return true;
                case R.id.navigation_account:
                    showInfoDialog();
                    return true;
            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //for custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new Home_fragment()).commit();

    }

    private void showInfoDialog()
    {

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.about);
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.about_icon);
        builder.setTitle("Məlumat");
        builder.setPositiveButton(
                "Ok", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        builder.show();


    }

}

