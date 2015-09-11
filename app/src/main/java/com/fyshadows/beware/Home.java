package com.fyshadows.beware;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    ImageButton btnMenu;
    ImageButton btnCreatePost;
    RelativeLayout MenuLayout;
    private ListView listView;
    ArrayList<Post> PostArray;
    List<Post> list;
    BewareDatabase db;
    PostAdapter adapter;
    TextView txtComment;

    String FromScreen = "No";
    Handler handler = new Handler();
    JSONParser jsonParser;
    TextView txtactionbar;
    ImageButton btnMyPost;
    ImageButton btnCatPlaces;
    ImageButton btnCatFood;
    ImageButton btnCatSafety;
    ImageButton btnCatOthers;
    ImageButton btnCatHealth;
    ImageButton btnCatPeople;
    ImageButton searchbtn;
    Boolean isHandlerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {

            //set action bar
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.actionbar_home);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setLogo(R.drawable.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1a1a1a")));


            txtactionbar = (TextView) findViewById(R.id.txtactionbar);


            Bundle bundle = getIntent().getExtras();
            FromScreen = String.valueOf(bundle.getString("FromScreen"));
            Log.i("FromScreen", FromScreen);

            if (FromScreen.equalsIgnoreCase("MyPost")) {
                txtactionbar.setText("My Post");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                txtactionbar.setText("Home");
            }

            db = new BewareDatabase(this);

            PostArray = new ArrayList<Post>();
            list = new ArrayList<Post>();
            txtComment = (TextView) findViewById(R.id.txtComment);
            listView = (ListView) findViewById(R.id.list);


            list = db.getPostOnCategory(FromScreen);
            if (FromScreen.equalsIgnoreCase("MyPost")) {
                adapter = new PostAdapter(Home.this, list, "MyPost");
            } else {
                adapter = new PostAdapter(this, list, "Home");
            }
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);


            MenuLayout = (RelativeLayout) findViewById(R.id.top_layout);
            MenuLayout.setVisibility(View.INVISIBLE);

            btnMenu = (ImageButton) findViewById(R.id.btnMenu);
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MenuLayout.getVisibility() == View.VISIBLE) {
                        MenuLayout.setVisibility(View.GONE);
                        btnMenu.setImageResource(R.drawable.menu);
                    } else {
                        MenuLayout.setVisibility(View.VISIBLE);
                        btnMenu.setImageResource(R.drawable.menuclose);
                    }
                }
            });

            MenuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuLayout.setVisibility(View.INVISIBLE);
                    btnMenu.setImageResource(R.drawable.menu);
                }
            });

            btnCreatePost = (ImageButton) findViewById(R.id.btnCreatePost);
            btnCreatePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(Home.this, WritePost.class);
                    if (isHandlerRunning) {
                        Log.i("home activity", "handler removed");

                        handler.removeCallbacksAndMessages(null);
                    }
                    startActivity(i);
                }
            });

            //Start Category selection
            btnCatPlaces = (ImageButton) findViewById(R.id.btnCatPlaces);
            btnCatPlaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Places");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("Places");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnCatFood = (ImageButton) findViewById(R.id.btnCatFood);
            btnCatFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Food");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("Food");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnCatSafety = (ImageButton) findViewById(R.id.btnCatSafety);
            btnCatSafety.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Girls Safety");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("Girls Safety");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnCatOthers = (ImageButton) findViewById(R.id.btnCatOthers);
            btnCatOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Others");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("Others");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnCatHealth = (ImageButton) findViewById(R.id.btnCatHealth);
            btnCatHealth.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Health");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("Health");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnCatPeople = (ImageButton) findViewById(R.id.btnCatPeople);
            btnCatPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMenu.setImageResource(R.drawable.menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    txtactionbar.setText("Health");
                    btnMenu.setImageResource(R.drawable.menu);
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("People");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "Home");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });

            btnMyPost = (ImageButton) findViewById(R.id.btnMyPost);
            btnMyPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    btnMenu.setImageResource(R.drawable.menu);
                    txtactionbar.setText("My Posts");
                    MenuLayout.setVisibility(View.INVISIBLE);
                    try {
                        list = db.getPostOnCategory("MyPost");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    adapter = new PostAdapter(Home.this, list, "MyPost");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
//End Category selection


        searchbtn = (ImageButton) findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, SearchActivity.class);
                if (isHandlerRunning) {
                    Log.i("home activity", "handler removed");
                    handler.removeCallbacksAndMessages(null);
                }
                Bundle bundle = new Bundle();
                bundle.putString("FromScreen", "Search");
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isHandlerRunning = true;
                if (MasterDetails.isOnline(Home.this)) {
                    Log.i("Home", "Executing async");
                    new asyncGetLatestPost().execute();
                } else {

                    Toast.makeText(Home.this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
                }


                handler.postDelayed(this, 150 * 50);
            }
        }, 150 * 50);


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
        switch (item.getItemId()) {
            case android.R.id.home:
                txtactionbar.setText("Home");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                btnMenu.setImageResource(R.drawable.menu);
                MenuLayout.setVisibility(View.INVISIBLE);
                try {
                    list = db.getPostOnCategory("No");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                adapter = new PostAdapter(Home.this, list, "Home");
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        return true;


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (MenuLayout.getVisibility() == View.VISIBLE) {
                MenuLayout.setVisibility(View.INVISIBLE);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //to check for updates in questions
    public class asyncGetLatestPost extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                MasterDetails MasterDetails = new MasterDetails();
                BewareDatabase db = new BewareDatabase(Home.this);
                final Post Post = new Post();
                ArrayList<UserDetails> objUserDetails = new ArrayList<UserDetails>();
                objUserDetails = db.getUserDetails();
                JSONObject json;
                int PostId = 0;

                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                PostId = db.getMaxPostId();

                Log.i("HomePostID", String.valueOf(PostId));

                params.clear();
                params.add(new BasicNameValuePair("UserId", String.valueOf(objUserDetails.get(0).getUserId().toString())));
                params.add(new BasicNameValuePair("Location", String.valueOf(objUserDetails.get(0).getLocation().toString())));
                params.add(new BasicNameValuePair("PostId", String.valueOf(PostId)));

                json = jsonParser.makeHttpRequest(MasterDetails.GetPolls, "GET", params);
                Log.i("Home async", "got json");
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        Log.i("Home", "Success");
                        // successfully received product details
                        JSONArray objPostArray = json.getJSONArray("Post"); // JSON
                        // Array
                        for (int i = 0; i < objPostArray.length(); i++) {

                            JSONObject obj = objPostArray.getJSONObject(i);
                            Log.i("SplashScreenSubject", obj.getString("Subject"));
                            Post.setPostId(obj.getInt("PostId"));
                            Post.setHelpFull(obj.getInt("HelpFull"));
                            Post.setNotHelpFull(obj.getInt("NotHelpFull"));
                            Post.setUserId(obj.getString("UserId"));
                            Post.setUserName(obj.getString("UserName"));
                            Post.setCategory(obj.getString("Category"));
                            Post.setSubject(obj.getString("Subject"));
                            Post.setPostText(obj.getString("PostText"));
                            Post.setTopComment(obj.getString("TopComment"));
                            Post.setTopCommentUserName(obj.getString("TopCommentUserName"));
                            Post.setTimeStamp(obj.getString("TimeStamp"));

                            db.InsertPost(Post);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.add(Post);
                                }
                            });


                            //End of getting question details
                        }
                    }
                }
            } catch (JSONException e) {
                Log.i("Home", "Executing Async activity");
            } catch (SQLException e) {
                Log.i("Home", "Failed while retrieving from db");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isHandlerRunning) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
