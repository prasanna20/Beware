package com.fyshadows.beware;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import BewareData.CreatePost;
import BewareData.MasterDetails;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class WritePost extends AppCompatActivity {
    Spinner spinnerCategory;
    ArrayList<String> CategoryList;
    EditText  editText_Subject;
    EditText editText_PostText;
    CheckBox chkBox_anonym;
    Button btnWrite;
    MasterDetails MasterDetails;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        //Populate Spinner
        MasterDetails=new MasterDetails();
        spinnerCategory = (Spinner) findViewById(R.id.spinCategory);
        CategoryList = new ArrayList<String>();
        CategoryList= MasterDetails.GetPostCategory();
        populateCategorySpinner();


        editText_Subject=(EditText) findViewById(R.id.editText_Subject);
        editText_PostText=(EditText) findViewById(R.id.editText_PostText);
        chkBox_anonym=(CheckBox) findViewById(R.id.chkBox_anonym);

        btnWrite=(Button) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation

                if(spinnerCategory.getSelectedItem().toString().equals("--Select Category--")  )
                {
                    spinnerCategory.requestFocus();
                    Toast.makeText(WritePost.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(editText_Subject.getText().toString().trim().equals("")  )
                {
                    editText_Subject.requestFocus();
                    Toast.makeText(WritePost.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(editText_PostText.getText().toString().trim().equals("")  )
                {
                    editText_PostText.requestFocus();
                    Toast.makeText(WritePost.this, "Please Enter Post Text", Toast.LENGTH_SHORT).show();
                    return;
                }

                BewareDatabase db=new BewareDatabase(WritePost.this);

                CreatePost CreatePost = new CreatePost();
                CreatePost.setUserId(db.GetUserId().toString());
                CreatePost.setSubject(editText_Subject.getText().toString());
                CreatePost.setCategory(spinnerCategory.getSelectedItem().toString());
                CreatePost.setPostText(editText_PostText.getText().toString());
                if(chkBox_anonym.isChecked())
                {
                    Log.i("WritePost","Checked");
                    CreatePost.setAnonymous(1);
                }
                else
                {
                    Log.i("WritePost","Not Checked");
                    CreatePost.setAnonymous(0);
                }


                if (MasterDetails.isOnline(WritePost.this)) {
                 try{
                    //Store it in server
                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    params.add(new BasicNameValuePair("UserId", CreatePost.getUserId()));
                    params.add(new BasicNameValuePair("Subject", CreatePost.getSubject()));
                    params.add(new BasicNameValuePair("Category", CreatePost.getCategory()));
                    params.add(new BasicNameValuePair("PostText", CreatePost.getPostText()));
                    params.add(new BasicNameValuePair("AnanymousFlag", String.valueOf(CreatePost.getAnonymous())));
                    JSONObject json = jsonParser.makeHttpRequest(
                            MasterDetails.CreatePost, "GET", params);

                    int success = json.getInt("success");

                    if (success == 1) {
                       Toast.makeText(WritePost.this,"Post Created",Toast.LENGTH_SHORT).show();
                        editText_PostText.setText("");
                        editText_Subject.setText("");
                        spinnerCategory.setId(0);


                    } else {
                        Toast.makeText(WritePost.this,"Post Creation failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                     Toast.makeText(WritePost.this,"Post Creation failed",Toast.LENGTH_SHORT).show();
                 }

                } else {
                    Toast.makeText(WritePost.this, "Please connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_post, menu);
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

    /**
     * Adding spinner data
     * */
    private void populateCategorySpinner() {
        List<String> lables = new ArrayList<String>();
        lables.add("--Select Category--");
        for (int i = 0; i < CategoryList.size(); i++) {
            lables.add(CategoryList.get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCategory.setAdapter(spinnerAdapter);
    }
}
