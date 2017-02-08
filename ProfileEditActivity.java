package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ProfileEditActivity extends ActionBarActivity {
    EditText Upswd,Upswd2;
    EditText UCpswd;
   // ProgressDialog progressDialog;
    String OldPaswd;
    EditText Uname,Uemail;
    PrefManager pm;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.gokera_icon_tiny);
        initialise();

        final String UserPassword = "abcd";
        sp=this.getSharedPreferences("AndroidHive",0);

        pm=new PrefManager(getApplicationContext());
        Uname = (EditText) findViewById(R.id.etEprofName);
        // EditText Uphno=(EditText)findViewById(R.id.etEprofPh);
        Button btnEdit=(Button)findViewById(R.id.btnProf_edit);
        Uemail = (EditText) findViewById(R.id.etEprofEmail);
        Upswd = (EditText) findViewById(R.id.etEprofPswd);
        Upswd2 = (EditText) findViewById(R.id.etEprofPswd2);
        Upswd2.setVisibility(View.GONE);
        UCpswd = (EditText) findViewById(R.id.etEprofCpswd2);
        Uname.setText(sp.getString("name", "Name"));
        Uemail.setText(sp.getString("email","email"));
        UCpswd.setVisibility(View.INVISIBLE);
        Upswd.setFocusable(false);
        Upswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtRev = new EditText(ProfileEditActivity.this);
                new AlertDialog.Builder(ProfileEditActivity.this)
                        .setTitle("Change Password?")
                        .setMessage("Enter your current password")
                        .setView(txtRev)
                        .setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                OldPaswd = txtRev.getText().toString();
                                if (OldPaswd.equals(UserPassword)) {
                                    Upswd.setVisibility(View.GONE);
                                    Upswd2.setVisibility(View.VISIBLE);
                                    Upswd2.setHint("Enter new password");
                                    //UCpswd.setVisibility(View.VISIBLE);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Incorrect current password",Toast.LENGTH_LONG).show();
                                    Upswd.setError("Incorrect password");
                                }

                            }
                        })
                        .setNegativeButton("Use Current pswd", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                OldPaswd = txtRev.getText().toString();
                                if (OldPaswd.equals(UserPassword)) {
                                    Upswd.setVisibility(View.GONE);
                                    Upswd2.setVisibility(View.VISIBLE);
                                    Upswd2.setText(OldPaswd);
                                }
                                else
                                {
                                    Upswd.setError("Incorrect password");
                                }
                            }
                        })
                        .show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valOK=validate();
                if(valOK ) {
                    networkCheck();
                    Toast.makeText(getApplicationContext(), "Profile updated Successfully, please login again..", Toast.LENGTH_LONG).show();
                    pm.clearSession();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Profile update failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initialise()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
    public boolean validate()
    {
        boolean valid=true;
        String name=Uname.getText().toString();
        String email=Uemail.getText().toString();
        String pswd=Upswd2.getText().toString();
        if(name.isEmpty()|| name.length() < 3)
        {
            valid=false;
            Uname.setError("at least 3 characters ");
        }
        else {
            Uname.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Uemail.setError("enter a valid email address");
            valid = false;
        } else {
            Uemail.setError(null);
        }
        if (pswd.isEmpty() || pswd.length() < 4 || pswd.length() > 10) {
            Upswd2.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            Upswd2.setError(null);
        }
        if(pswd.trim().equals(UCpswd.getText().toString().trim())){
            UCpswd.setError("password not matching");
            valid = false;
        }

        return valid;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_inv_back) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    public class task extends AsyncTask<String, String, Void> {
        InputStream is1 = null;
        String paid_result = "";
        String qod_result = "";


        protected void onPreExecute() {
          /*  progressDialog = new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);*/

        }

        @Override
        protected Void doInBackground(String... params) {

           String phno= pm.getMobileNumber();
            int uid= pm.getUid();
            String paid_url_select = "http://gokera.phacsin.com/prof_upload.php?phno="+phno+"&uname="+Uname.getText().toString()+"&uemail="+Uemail.getText().toString()+"&upswd="+Upswd2.getText().toString()+"&uid="+uid;//"&phno="+phno

            try {
                paid_result = getJSONfromURL(paid_url_select);
              /*  if (paid_result != null)
                    progressDialog.setProgress(100);*/


                JSONArray array1 = new JSONArray(paid_result);

                for (int i = 0; i < array1.length(); i++) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                }


                //progressDialog.setProgress(1800);

            } catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            // dbQod.close();
            return null;


        }


        protected void onPostExecute(Void v) {
          /*  if (progressDialog != null)
                progressDialog.dismiss();*/

        }


        public String getJSONfromURL(String URL) {
            String res = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
            HttpPost httpPost = new HttpPost(URL);
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();


                //read content
                is1 = httpEntity.getContent();
            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is1.close();
                res = sb.toString();

            } catch (IOException e) {
                Log.d("log_tag", "IOException " + e.toString());
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
            }
            is1 = null;
            return res;
        }
    }



    public void networkCheck()
    {
        if(!Network_Available()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileEditActivity.this);
            alertDialog.setTitle("No Network Connectivity");
            alertDialog.setMessage("Turn ON Wifi or Mobile Data");
            alertDialog.show();
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()

                    {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

            ).show().setCancelable(false);
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()

                    {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    }

            ).show().setCancelable(false);

        }
        else {
            new NetCheck().execute();
        }
    }

    private boolean Network_Available() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            Log.d("log_tag", "Net avail:"
                    + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());
            ConnectivityManager cm = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("log_tag", "Network available:true");
                return true;
            } else {
                Log.d("log_tag", "Network available:false");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public class NetCheck extends AsyncTask<String,String,Boolean>
    {
        ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ProfileEditActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){



            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");//("http://www.entreprenia15.com/quiz/select_paid.php");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new task().execute();
            }
            else{
                nDialog.dismiss();
                new task().execute();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 700);
                toast.show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
    }

}