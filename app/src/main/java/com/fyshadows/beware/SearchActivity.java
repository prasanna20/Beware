package com.fyshadows.beware;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    Boolean HandleRunning = false;
    TextView txtNoSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtNoSearch = (TextView) findViewById(R.id.txtNoSearch);
        editText_Search = (EditText) findViewById(R.id.editText_Search);
        editText_Search.setOnKeyListener(new View.OnKeyListener()
        {
           @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("Search", "Clicked");
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (editText_Search.getText().toString().length() <= 0) {
                                Toast.makeText(SearchActivity.this, "Please Enter Search Text", Toast.LENGTH_SHORT).show();
                                editText_Search.requestFocus();
                            } else {
                                SearchValue = editText_Search.getText().toString();
                                if (MasterDetails.isOnline(SearchActivity.this)) {
                                    Log.i("Home", "Executing async");
                                    new asyncGetLatestPost().execute();
                                }


                                Boolean searchFocus = editText_Search.isFocused();
                                if (searchFocus) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editText_Search.getWindowToken(), 0);
                                }
                            }

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        searchbtn = (ImageButton) findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_Search.getText().toString().length() <= 0) {
                    Toast.makeText(SearchActivity.this, "Please Enter Search Text", Toast.LENGTH_SHORT).show();
                    editText_Search.requestFocus();
                } else {
                    SearchValue = editText_Search.getText().toString();
                    SearchValue= SearchValue.replace("\"", "");
                    SearchValue= SearchValue.replace("\'", "");

                    if (MasterDetails.isOnline(SearchActivity.this)) {
                        Log.i("Home", "Executing async");
                        new asyncGetLatestPost().execute();
                    } else {

                        Toast.makeText(SearchActivity.this, "Please connect to internet..", Toast.LENGTH_SHORT).show();
                    }

                    Boolean searchFocus = editText_Search.isFocused();
                    if (searchFocus) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText_Search.getWindowToken(), 0);
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
                if (HandleRunning) {
                    handler.removeCallbacksAndMessages(null);
                }
                Intent i = new Intent(SearchActivity.this, Home.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("FromScreen", "No");
                i.putExtras(bundle);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (HandleRunning) {
                handler.removeCallbacksAndMessages(null);
            }
            Intent i = new Intent(SearchActivity.this, Home.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("FromScreen", "No");
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //to check for updates in questions
    public class asyncGetLatestPost extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();
        JSONObject json;
        ProgressDialog searchingProgress = null;
        ListView listView = (ListView) findViewById(R.id.list);

        @Override
        protected void onPreExecute() {
            searchingProgress = new ProgressDialog(SearchActivity.this);
            searchingProgress.setMessage("Searching...");
            searchingProgress.show();
            list = null;
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
            super.onPostExecute(result);
            Log.i("Searchactivity", "Inpostexecute");
            if (list != null) {


                txtNoSearch.setVisibility(View.INVISIBLE);

                adapter = new PostAdapter(SearchActivity.this, list, "Search");
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(0);
                Log.i("Searchactivity", "Adapter notified");


            } else {
                listView.setAdapter(null);
                txtNoSearch.setVisibility(View.VISIBLE);
                txtNoSearch.setText("Sorry ! No Results found..");
            }

            searchingProgress.dismiss();
        }
    }


}
