package com.fyshadows.beware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Home extends AppCompatActivity {

    Button btnMenu;
    Button btnCreatePost;
    RelativeLayout MenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
}
