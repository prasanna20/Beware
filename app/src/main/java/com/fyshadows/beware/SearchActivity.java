package com.fyshadows.beware;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BewareAdapter.PostAdapter;
import BewareData.Comment;
import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class SearchActivity extends AppCompatActivity {

    EditText editText_Search;
    ImageButton searchbtn;
    private ListView listView;
    ArrayList<Post> list;
    BewareDatabase db;
    PostAdapter adapter;
    String FromScreen;
    String SearchValue;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editText_Search = (EditText) findViewById(R.id.editText_Search);
        searchbtn = (ImageButton) findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_Search.getText().toString().length() <= 0) {
                    Toast.makeText(SearchActivity.this, "Please Enter Search Text", Toast.LENGTH_SHORT).show();
                    editText_Search.requestFocus();
                } else {
                    SearchValue = editText_Search.getText().toString();
                    if (MasterDetails.isOnline(SearchActivity.this)) {
                        Log.i("Home", "Executing async");
                        new asyncGetLatestPost().execute();
                    } else {

                        Toast.makeText(SearchActivity.this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        Bundle bundle = getIntent().getExtras();
        FromScreen = String.valueOf(bundle.getString("FromScreen"));
        Log.i("FromScreen", FromScreen);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
              finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //to check for updates in questions
    public class asyncGetLatestPost extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();
        JSONObject json;

        @Override
        protected void onPreExecute() {
            list=null;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                MasterDetails MasterDetails = new MasterDetails();
                BewareDatabase db = new BewareDatabase(SearchActivity.this);

                ArrayList<UserDetails> objUserDetails = new ArrayList<UserDetails>();
                objUserDetails = db.getUserDetails();

                int PostId = 0;

                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.clear();
                params.add(new BasicNameValuePair("UserId", String.valueOf(objUserDetails.get(0).getUserId().toString())));
                params.add(new BasicNameValuePair("Location", String.valueOf(objUserDetails.get(0).getLocation().toString())));
                params.add(new BasicNameValuePair("SearchValue", String.valueOf(SearchValue)));

                json = jsonParser.makeHttpRequest(MasterDetails.GetPolls, "GET", params);
                Log.i("Home", "got json");
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        Log.i("Home", "Success");
                        // successfully received product details
                        JSONArray objPostArray = json.getJSONArray("Post"); // JSON
                        // Array
                        for (int i = 0; i < objPostArray.length(); i++) {
                            Post Post = new Post();
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

                            if (list == null) {
                                list = new ArrayList<Post>();
                                list.add(Post);

                            } else {
                                list.add(Post);
                            }

                            //End of getting question details
                        }
                    }
                }
            } catch (JSONException e) {
                Log.i("Home", "Executing Async activity");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(list != null) {
                listView = (ListView) findViewById(R.id.list);
                adapter = new PostAdapter(SearchActivity.this, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(0);
                adapter.notifyDataSetChanged();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (MasterDetails.isOnline(SearchActivity.this)) {
                            Log.i("SearchActivity", "Executing async");
                            adapter.notifyDataSetChanged();
                        } else {

                            Toast.makeText(SearchActivity.this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
                        }


                        handler.postDelayed(this, 50 * 50);
                    }
                }, 150 * 50);
            }

        }
    }


}
