package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class SignupTemp extends ActionBarActivity {

    EditText Upswd;
    EditText UCpswd,Uphno;
    EditText Uname,Uemail;
    Button btnTempSignUp;
    Spinner spinner;
    //BetterSpinner textView;
    String phno,name,mailid,pswd,Cpswd,country;
    boolean result=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_temp);

        spinner=(Spinner)findViewById(R.id.spinner);

      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
         textView = (BetterSpinner)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
*/


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(
                this,R.array.countries, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);
       /* ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arSpinner);
        spinner.setAdapter(adapter);*/

        spinner.setSelection(93);


       /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Hexadecimal"))
                {
                    operand.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else{
                    operand.setInputType(InputType.TYPE_CLASS_NUMBER);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        Upswd = (EditText) findViewById(R.id.etEsignPswd2);
        UCpswd = (EditText) findViewById(R.id.etEsignCpswd2);
        Uphno = (EditText) findViewById(R.id.etEsignPhno);
        //Uphno.setText(GetCountryZipCode());
        Uname = (EditText) findViewById(R.id.etEsignName);
        Uemail = (EditText) findViewById(R.id.etEsignEmail);
        btnTempSignUp = (Button) findViewById(R.id.btnTemp_signup);


        btnTempSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country=spinner.getSelectedItem().toString();
                phno = Uphno.getText().toString();
                name = Uname.getText().toString();
                mailid = Uemail.getText().toString();
                pswd = Upswd.getText().toString();
                Cpswd = UCpswd.getText().toString();

                if (validate()) {
                    networkCheck();
                }
            }
        });




        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup_temp, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
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
    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }
    public boolean validate() {
        boolean valid = true;



        if (phno.isEmpty() || !android.util.Patterns.PHONE.matcher(phno).matches() || phno.length()<10) {
            Uphno.setError("enter a valid phone number");
            valid = false;
        }
        else
        {
            Uphno.setError(null);
        }
        if (name.isEmpty() ||  containsWhiteSpace(name)) {

            valid=false;
            Uname.setError("enter a name (Name should not contain white spaces)");
        }
        else
        {
            Uname.setError(null);
        }
        if (mailid.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mailid).matches() ) {
            Uemail.setError("enter a valid email id");
            valid = false;
        }
        else
        {
            Uemail.setError(null);
        }
        if(pswd.isEmpty() || !pswd.equals(Cpswd))
        {
            UCpswd.setError("Password Mismatch");
            valid = false;
        }
        else
        {
            UCpswd.setError(null);
        }
        if(pswd.length()<4 ||  containsWhiteSpace(pswd))
        {
            Upswd.setError("Password must have minimum 4 characters and should not contain white spaces.");
            valid = false;
        }
        else
        {
            Upswd.setError(null);
        }
        if(spinner.getSelectedItemId()==0)
        {
            Toast.makeText(getApplicationContext(),"Select a valid country..",Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }
    public static boolean containsWhiteSpace(final String testCode){
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){
                    return true;
                }
            }
        }
        return false;
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

            String paid_url_select = "http://gokera.phacsin.com/signup_temp.php?phno="+phno+"&uname="+name+"&uemail="+mailid+"&upswd="+pswd+"&country="+country;//"&phno="+phno

            try {
                paid_result = getJSONfromURL(paid_url_select);
              /*  if (paid_result != null)
                    progressDialog.setProgress(100);*/
                if (paid_result != null)
                    result=true;

               /* JSONArray array1 = new JSONArray(paid_result);

                for (int i = 0; i < array1.length(); i++) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                   // result=obj.toString();
                }

*/
                //progressDialog.setProgress(1800);

          /*  } catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();*/
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            // dbQod.close();
            return null;


        }


        protected void onPostExecute(Void v) {
          /*  if (progressDialog != null)
                progressDialog.dismiss();*/
          //  Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
            if(result)
            {
                Toast.makeText(getApplicationContext(),"Signed up successfully\n Welcome to gokera..!",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Please login..",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }

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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupTemp.this);
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
            nDialog = new ProgressDialog(SignupTemp.this);
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
              /*  Toast toast = Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 700);
                toast.show();*/
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
