package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;
    String phno,password,test;
    String paid_url_select;
    PrefManager pm;
    int flag=0,tap=0;
    @InjectView(R.id.etLphno) EditText _phone;
    @InjectView(R.id.etLpswd) EditText _passwordText;
    @InjectView(R.id.btnLogin) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.link_ForPass) TextView _ForgPassLink;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        pm = new PrefManager(getApplicationContext());
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupTemp.class);//SmsActivity.class);// RegisterActivity.class);
               startActivityForResult(intent, REQUEST_SIGNUP);
                //startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
        _ForgPassLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Forg_passActivity.class);//SmsActivity.class);// RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity

                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }




         phno = _phone.getText().toString();
         password = _passwordText.getText().toString();

        new NetCheck().execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }



    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        finish();
        if(flag==1) {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
        else
            Toast.makeText(getApplicationContext(),"Invalid user name or Password",Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }
    @Override
    public void onBackPressed() {
        tap++;
        if(tap>1)
        {
            moveTaskToBack(true);
    }
        else
            Toast.makeText(getApplicationContext(), "Tap back again to exit",Toast.LENGTH_LONG).show();

    }
    public boolean validate() {
        boolean valid = true;

        String phno = _phone.getText().toString();
        String password = _passwordText.getText().toString();

        if (phno.isEmpty() || !android.util.Patterns.PHONE.matcher(phno).matches()) {
            _phone.setError("enter a valid phone number");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public class task extends AsyncTask<String, String, Boolean>
    {
        InputStream is1 = null;
        String paid_result = "";
        String qod_result = "";




        protected void onPreExecute() {
            progressDialog= new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Checking Server...");
            progressDialog.show();
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            phno=_phone.getText().toString().trim();
            password=_passwordText.getText().toString().trim();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            paid_url_select = "http://gokera.phacsin.com/applogin.php?phno="+phno+"&upswd="+password;
           // Toast.makeText(getApplicationContext(),obj.getInt("user_id")+obj.getString("user_name")+obj.getString("email_id")+obj.getString("phone"),Toast.LENGTH_LONG).show();

            Log.d("log_SSSS",paid_url_select);
            Boolean set=false;
            try {
                paid_result=getJSONfromURL(paid_url_select);
                if(paid_result!=null) {

                    JSONArray array1 = new JSONArray(paid_result);
                   test= array1.toString();

                   for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                   pm.createLogin(Integer.parseInt(obj.getString("user_id")), obj.getString("user_name"), obj.getString("email_id"), obj.getString("phone"));
                      // test= obj.getString("user_name")+"\n"+obj.getInt("user_id")+"\n"+ obj.getString("email_id")+"\n"+obj.getString("phone");
                       // test=String.valueOf(array1.length());

                  //  pm.createLogin(array1.getInt(0), array1.getString(1), array1.getString(2), array1.getString(3));
                    }
                    if(pm.isLoggedIn() ) {
                        flag = 1;
                        set = true;
                    }
                }

            }


            catch (JSONException e1) {
                Log.d("log_tag",e1.toString());

                e1.printStackTrace();
            }
            catch (Exception e1)
            {

                e1.printStackTrace();
            }

            return set;


        }




        protected void onPostExecute(Boolean set) {
            if(progressDialog!=null)
                progressDialog.dismiss();
          //  set=true;//for login without a user
            if(set)
             startActivity(new Intent(getApplicationContext(),IntroActivity.class));
            else
              Toast.makeText(getApplicationContext(),"Invalid Username/password",Toast.LENGTH_LONG).show();
           /* Toast.makeText(getApplicationContext(),paid_result,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),test,Toast.LENGTH_LONG).show();*/
        }

        public String getJSONfromURL(String URL)
        {
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
                is1 =  httpEntity.getContent();
            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                is1.close();
                res=sb.toString();

            }

            catch (IOException e) {
                Log.d("log_tag", "IOException "+e.toString());
            }
            catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result "+e.toString());
            }
            is1=null;
            return res;
        }
    }


    public void networkCheck()
    {
        if(!Network_Available()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
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
            nDialog = new ProgressDialog(LoginActivity.this);
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
               /* Toast toast = Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 700);
                toast.show();*/
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
