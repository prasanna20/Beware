package com.fyshadows.beware;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class SplashScreen extends AppCompatActivity {

    Thread SplashScreenTimer;
    BewareDatabase db;
    ArrayList<UserDetails> UserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = new BewareDatabase(this);
        SplashScreenTimer = new Thread() {
            public void run() {
                int logoTimer = 0;
                while (logoTimer < 15000) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logoTimer = logoTimer + 300;

                }
                ;
                HomeActivity();
            }
        };

        //Get Post
        if (MasterDetails.isOnline(this)) {
            new asyncGetLatestPost().execute();
        } else {
            Toast.makeText(this, "Please connect to internet. You Are on offline mode", Toast.LENGTH_LONG).show();
        }

        SplashScreenTimer.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void HomeActivity() {
        Intent i = new Intent(SplashScreen.this, Home.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("FromScreen", "No");
        i.putExtras(bundle);
        startActivity(i);
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
                BewareDatabase db = new BewareDatabase(SplashScreen.this);
                Post Post = new Post();
                ArrayList<UserDetails> objUserDetails = new ArrayList<UserDetails>();
                objUserDetails = db.getUserDetails();
                JSONObject json;
                int PostId = 0;

                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                PostId = db.getMaxPostId();

                Log.i("SplashScreenPostID", String.valueOf(PostId));

                params.clear();
                params.add(new BasicNameValuePair("UserId", String.valueOf(objUserDetails.get(0).getUserId().toString())));
                params.add(new BasicNameValuePair("Location", String.valueOf(objUserDetails.get(0).getState().toString())));
                params.add(new BasicNameValuePair("PostId", String.valueOf(PostId)));

                json = jsonParser.makeHttpRequest(MasterDetails.GetPolls, "GET", params);
                Log.i("SplashScreen", "got json");
                if(json != null) {
                    if (json.length() > 0) {
                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            Log.i("SplashScreen", "Success");
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

                                //End of getting question details
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                Log.i("SplashScreen", "Executing Async activity");
            } catch (SQLException e) {
                Log.i("SplashScreen", "Failed while retrieving from db");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }
}
