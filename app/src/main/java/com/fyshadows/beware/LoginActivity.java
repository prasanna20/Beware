package com.fyshadows.beware;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import BewareData.District;
import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.State;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

public class LoginActivity extends AppCompatActivity {

    Spinner spinnerCity;
    ArrayList<String> CityList;
    MasterDetails MasterDetails=new MasterDetails();
    Button btnRegistration;
    EditText editText_name;
    EditText editText_email;
    String UserId="";
    String emailRegistered;
    final Random myRandom = new Random(6);
    String TimeValue;
    Calendar c;
    JSONParser jsonParser = new JSONParser();
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    String regId;
    BewareDatabase db;
    SharedPreferences prefs;
    ProgressDialog pDialog;
    private ArrayList<State> stateList;
    Spinner spinState;

    private ArrayList<District> districtList;
    Spinner spinDistrict;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(LoginActivity.this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Check for first time activity
        if (prefs.getBoolean("Firsttimeactivity", false)) {

                // This block is used for the already installed activity
                SplashScreenActivity();

        }
        else
        {

            new GetStateDetails().execute();
        }

        //GET EditText value
        spinState = (Spinner) findViewById(R.id.spinState);
        editText_name= (EditText) findViewById(R.id.editText_name);
        editText_email=(EditText) findViewById(R.id.editText_email);

        //State Spinner


        //City Spinner
        spinDistrict = (Spinner) findViewById(R.id.spinDistrict);
        districtList = new ArrayList<District>();
        populateDistrictSpinner();


        spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                String selectedState = spinState.getSelectedItem().toString();
                Log.i("college selected", selectedState);
                if (!selectedState.equalsIgnoreCase("--Select State--")) {
                    new GetDistrictDetails(selectedState).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Email
        emailRegistered = getEmail(this);
        editText_email.setText(emailRegistered);

        c = Calendar.getInstance();
        TimeValue = String.valueOf(c.get(Calendar.MINUTE)) + String.valueOf(c.get(Calendar.SECOND));

        new asyncGcmRegistration().execute();

        db=new BewareDatabase(this);

        //Registration

        btnRegistration=(Button)findViewById( R.id.btnRegistration);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation
                if(editText_name.getText().toString().trim().equals("")  )
                {
                    editText_name.requestFocus();
                    Toast.makeText(LoginActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(editText_email.getText().toString().trim().equals("")  )
                {
                    editText_email.requestFocus();
                    Toast.makeText(LoginActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spinState.getSelectedItem().toString().equals("--Select State--")  )
                {
                    spinState.requestFocus();
                    Toast.makeText(LoginActivity.this, "Please Select State", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spinDistrict.getSelectedItem().toString().equals("--Select District--")  )
                {
                    spinDistrict.requestFocus();
                    Toast.makeText(LoginActivity.this, "Please Select District", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDetails UserDetails = new UserDetails();
                UserDetails.setUserName(editText_name.getText().toString());
                UserDetails.setEmailId(editText_email.getText().toString());
                UserDetails.setState(spinState.getSelectedItem().toString());
                UserDetails.setDistrict(spinDistrict.getSelectedItem().toString());
                UserId = emailRegistered.substring(0, 5) + String.valueOf(myRandom.nextInt()).substring(0, 4) + String.valueOf(TimeValue);
                UserDetails.setUserId(UserId);
                UserDetails.setGcmId(regId);
                if (MasterDetails.isOnline(LoginActivity.this)) {

                    if (db.InsertUserDetails(UserDetails)) {
                        Log.i("LoginActivity", "Registered Successfully");
                        Toast.makeText(LoginActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Firsttimeactivity", true);
                        editor.commit();
                        SplashScreenActivity();
                    } else {
                        Log.i("LoginActivity", "Not Registered Successfully");
                        Toast.makeText(LoginActivity.this, "Registration Unsuccesfull,Please Try Again.", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Firsttimeactivity", false);
                        editor.commit();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private  Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    /**
     * Adding spinner data
     * */
    private void populateCitySpinner() {
        List<String> lables = new ArrayList<String>();
        lables.add("--Select City--");
        for (int i = 0; i < CityList.size(); i++) {
            lables.add(CityList.get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCity.setAdapter(spinnerAdapter);
    }

    /*
    GCM Registration
     */

    public void GcmRegistration() {
        // Make sure the device has the proper dependencies.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
            regId = getRegistrationId(this);

            if (regId.isEmpty()) {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(this);
                }
                try{
                    regId = gcm.register(MasterDetails.sender_id);
                }
                catch (IOException ex) {
                    String msg = "Error :" + ex.getMessage();
                    Log.i("LoginActivity",msg);

                }

            }
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LoginActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, LoginActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            //Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            //Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(SplashScreen.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private  int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //Async GCM Registration

    public class asyncGcmRegistration extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            Log.i("LoginActivity", "into async task");
            GcmRegistration();
            return null;
        }
    }

    public void SplashScreenActivity()
    {
        Intent i = new Intent(LoginActivity.this, SplashScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    //Start :  Load spinner data for State

    private class GetStateDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Fetching State..");
            // pDialog.setCancelable(false);
           pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int success;
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            stateList = new ArrayList<State>();
            params.clear();
            try {
                JSONObject json = jsonParser.makeHttpRequest(MasterDetails.GetStateDetails, "GET", params );

                    if (json != null) {
                        if (json.length() > 0) {
                            // json success tag
                            success = json.getInt("success");
                            if (success == 1) {

                                JSONArray stateDetails = json
                                        .getJSONArray("StateDetails");


                                for (int i = 0; i < stateDetails.length(); i++) {
                                    JSONObject objState = (JSONObject) stateDetails.get(i);
                                    State state = new State(
                                            objState.getString("Name"),
                                            objState.getString("Value")
                                    );
                                    stateList.add(state);

                                }

                            }
                        }

                    } else {
                        Log.e("JSON Data", "Didn't receive any data from server!");
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateStateSpinner();
            pDialog.dismiss();
        }

    }


    private void populateStateSpinner() {
        List<String> lables = new ArrayList<String>();
        lables.add("--Select State--");
        for (int i = 0; i < stateList.size(); i++) {
            lables.add(stateList.get(i).getLabel());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinState.setAdapter(spinnerAdapter);
    }

    //End :  Load spinner data for State


//Start :  Load spinner data for District

    private class GetDistrictDetails extends AsyncTask<Void, Void, Void> {

        String selectedState;


        public GetDistrictDetails(String selectedStateParam) {
            super();
            selectedState = selectedStateParam ;
            selectedState= '"' + selectedState + '"';
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Fetching District..");
            // pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int success;
            districtList = new ArrayList<District>();
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.clear();
            Log.i("selectedState",selectedState);
            params.add(new BasicNameValuePair("State", String.valueOf(selectedState)));

            JSONObject json = jsonParser.makeHttpRequest(MasterDetails.GetDistrictDetails, "GET", params);
            try {
            if(json != null) {
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        JSONArray districtDetails = json
                                .getJSONArray("DistrictDetails");

                        for (int i = 0; i < districtDetails.length(); i++) {
                            JSONObject objDistrict = (JSONObject) districtDetails.get(i);
                            District district = new District(
                                    objDistrict.getString("Name"),
                                    objDistrict.getString("Value")
                            );
                            districtList.add(district);
                        }
                    }
                }
            else {
                        Log.e("JSON Data", "Didn't receive any data from server!");
                    }

            }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateDistrictSpinner();
            pDialog.dismiss();
        }

    }


    private void populateDistrictSpinner() {
        List<String> lables = new ArrayList<String>();
        lables.add("--Select District--");
        if(districtList != null) {
            for (int i = 0; i < districtList.size(); i++) {
                lables.add(districtList.get(i).getLabel());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinDistrict.setAdapter(spinnerAdapter);
    }

    //End :  Load spinner data for District
}
