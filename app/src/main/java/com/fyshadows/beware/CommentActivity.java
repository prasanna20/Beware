package com.fyshadows.beware;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import BewareAdapter.CommentsAdapter;
import BewareAdapter.PostAdapter;
import BewareData.Comment;
import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class CommentActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Comment> list;
    final Handler handler = new Handler();
    JSONParser jsonParser;

    int scrolly = 0;
    int first = 0;
    CommentsAdapter adapter;
    TextView txtEmpty;
    ImageButton img_postComment;
    EditText edittext_Comment;

    String PostID;
    String UserId;
    String CommentText;
    String UserName;


    ArrayList<UserDetails> objUserDetails;

    BewareDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        jsonParser = new JSONParser();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle bundle = getIntent().getExtras();
        PostID = String.valueOf(bundle.getInt("PostId"));
        Log.i("CommentActivity", PostID);

        db = new BewareDatabase(this);


        list = new ArrayList<Comment>();
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        listView = (ListView) this.findViewById(android.R.id.list);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        if (MasterDetails.isOnline(this)) {
            txtEmpty.setText("Loading Comments....");
            new AsyncGetComments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("No internet Connection.Please connect to internet");
        }

       /* handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (MasterDetails.isOnline(CommentActivity.this)) {
                    getComments();
                } else {
                    TextView txt = (TextView) findViewById(R.id.txtEmpty);
                    txt.setText("No internet Connection.Please connect to internet");
                    Toast.makeText(CommentActivity.this, "No internet Connection.Please connect to internet..", Toast.LENGTH_LONG).show();
                }


                handler.postDelayed(this, 300 * 500);
            }
        }, 300 * 500);*/
        edittext_Comment = (EditText) findViewById(R.id.edittext_Comment);
        img_postComment = (ImageButton) findViewById(R.id.img_postComment);
        img_postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation
                if (edittext_Comment.getText().toString().trim().equals("")) {
                    edittext_Comment.requestFocus();
                    Toast.makeText(CommentActivity.this, "Please Enter Comments", Toast.LENGTH_SHORT).show();
                    return;
                }

                CommentText = edittext_Comment.getText().toString();
                CommentText = CommentText.replace("\"", "");
                CommentText = CommentText.replace("\'", "");

                objUserDetails = new ArrayList<BewareData.UserDetails>();
                objUserDetails = db.getUserDetails();
                UserId = objUserDetails.get(0).getUserId();
                UserName = objUserDetails.get(0).getUserName();
                if (MasterDetails.isOnline(CommentActivity.this)) {
                    new PostCommentTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(CommentActivity.this, "Please connect to internet..", Toast.LENGTH_SHORT).show();
                }

                edittext_Comment.setText("");

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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
             asyncGetLatestCount async=new asyncGetLatestCount();
             async.cancel(true);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public class AsyncGetComments extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            if (MasterDetails.isOnline(CommentActivity.this)) {

                list = getComments();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (list == null) {
                txtEmpty.setVisibility(View.VISIBLE);
                txtEmpty.setText("Be the first to comment.");
            } else {
                adapter = new CommentsAdapter(CommentActivity.this, list);
                listView.setAdapter(adapter);
                listView.setSelection(adapter.getCount() - 1);
                txtEmpty.setVisibility(View.INVISIBLE);
            }

        }
    }


    public class PostCommentTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            if (MasterDetails.isOnline(CommentActivity.this)) {
                try {
                    //Store it in server
                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    params.add(new BasicNameValuePair("UserId", UserId));
                    params.add(new BasicNameValuePair("PostId", PostID));
                    params.add(new BasicNameValuePair("Comment", CommentText));

                    db.UpdateCommentCount(Integer.valueOf(PostID), 1, 1);

                    JSONObject json = jsonParser.makeHttpRequest(
                            MasterDetails.PostComments, "GET", params);

                    int success = json.getInt("success");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());

                    if (success == 1) {

                        int CommentCount = json.getInt("CommentCount");

                        if (CommentCount != 0) {
                            db.UpdateCommentCount(Integer.valueOf(PostID), CommentCount, 2);
                        }

                        Comment objComment = new Comment();
                        objComment.setTimeStamp(String.valueOf(date));
                        objComment.setCommentText(CommentText);
                        objComment.setUserName(UserName);

                        if (list == null) {
                            list = new ArrayList<Comment>();
                            list.add(objComment);

                        } else {
                            list.add(objComment);
                        }




                    }
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (list != null) {
                adapter = new CommentsAdapter(CommentActivity.this, list);
                listView.setAdapter(adapter);
                listView.setSelection(adapter.getCount() - 1);
                txtEmpty.setVisibility(View.INVISIBLE);
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                txtEmpty.setText("Be the first to comment.");
            }
            new asyncGetLatestCount().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    public ArrayList<Comment> getComments() {
        try {
            int index = 0;
            int top = 0;
            int success;

            JSONObject json;
              /*  if (first > 0) {
                    index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    top = (v == null) ? 0 : v.getTop();
                    list.clear();

                }*/

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("PostId", PostID));
            Log.i("CommentActivityParam", "ParamAdded");
            json = jsonParser.makeHttpRequest(
                    MasterDetails.GetComments, "GET", params);
            if (json != null) {
                // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray CommentObj = json.getJSONArray("Comment"); // JSON
                    // Array
                    for (int i = 0; i < CommentObj.length(); i++) {

                        Comment objComment = new Comment();
                        JSONObject obj = CommentObj.getJSONObject(i);
                        objComment.setCommentId(obj.getInt("CommentId"));
                        objComment.setUserName(obj.getString("UserName").toString());
                        objComment.setCommentText(obj.getString("Comment").toString());
                        objComment.setTimeStamp(obj.getString("TimeStamp").toString());
                        list.add(objComment);
                    }


                    return list;


                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return null;
    }


    public class asyncGetLatestCount extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();

        public asyncGetLatestCount() {
            super();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.i("Comment async start", "got json");

                        MasterDetails MasterDetails = new MasterDetails();
                        BewareDatabase db = new BewareDatabase(CommentActivity.this);

                        int success;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.clear();

                        params.add(new BasicNameValuePair("PostId", String.valueOf(PostID)));

                        JSONObject json = jsonParser.makeHttpRequest(MasterDetails.GetLatestCount, "GET", params);
                        Log.i("Comment async  Post", "got json");
                        Log.i("Comment async  post id", String.valueOf(PostID));
                        if(json != null) {
                            if (json.length() > 0) {
                                // json success tag
                                success = json.getInt("success");
                                if (success == 1) {
                                    Log.i("Comment async Lat Post", "got json sucesss");
                                    JSONArray objPostArray = json.getJSONArray("LatestCount"); // JSON

                                    Log.i("Comment async length", String.valueOf(objPostArray.length()));
                                    if (objPostArray.length() > 0) {
                                        // Array
                                        JSONObject obj = objPostArray.getJSONObject(0);
                                        db.UpdateLatestCount(Integer.parseInt(PostID), obj.getInt("HelpFull"), obj.getInt("NotHelpFull"), obj.getInt("CommentCount"));

                                    }
                                }
                            }
                        }

            } catch (JSONException e) {
                Log.i("Home", "Executing Async activity" + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}
