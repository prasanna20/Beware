package com.fyshadows.beware;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appdatasearch.GetRecentContextCall;


import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BewareAdapter.PostAdapter;
import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class Home extends AppCompatActivity {

    Button btnMenu;
    Button btnCreatePost;
    RelativeLayout MenuLayout;
    private ListView listView;
    ArrayList<Post> PostArray;
    List<Post> list ;
    BewareDatabase db;
    PostAdapter adapter;
    TextView txtComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40E0D0")));

        db=new BewareDatabase(this);

        PostArray=new  ArrayList<Post>();
        list = new ArrayList<Post>();
        txtComment=(TextView)  findViewById(R.id.txtComment);
        listView = (ListView) findViewById(R.id.list);

        try {
            list=db.getPostOnCategory("No");

            adapter = new PostAdapter(this, list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        MenuLayout = (RelativeLayout) findViewById(R.id.top_layout);
        MenuLayout.setVisibility(View.INVISIBLE);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuLayout.setVisibility(View.VISIBLE);
            }
        });

        MenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuLayout.setVisibility(View.INVISIBLE);
            }
        });

        btnCreatePost = (Button) findViewById(R.id.btnCreatePost);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuLayout.setVisibility(View.INVISIBLE);
                Intent i = new Intent(Home.this, WritePost.class);
                startActivity(i);
            }
        });



    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if( MenuLayout.getVisibility()==View.VISIBLE)
            {
                MenuLayout.setVisibility(View.INVISIBLE);
                return true;
            }
            else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
