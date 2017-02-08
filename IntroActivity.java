package com.quizapp.hp.quiz;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
//import android.database.sqlite.SqliteWrapper;


import random_questions_db.DBclassRandomQuestions;


public class IntroActivity extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms
String test,test2,test3;
    PrefManager pm;
    ImageView myImage2;
     ImageView myImage;
    boolean boolQOD;
     PendingIntent pendingIntent;
    AlarmManager alarmMgr;
    Calendar calendar;
    String toDate;
    String ertest1,ertest2;
    ArrayList<Integer> Dqids;
    ArrayList<Integer> LocQids;
    private ProgressDialog progressDialog;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        myImage2 = (ImageView)findViewById(R.id.imgLogo1);

        Intent myIntent = new Intent(IntroActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(IntroActivity.this, 0, myIntent, 0);
        alarmMgr  = (AlarmManager)getSystemService(ALARM_SERVICE);

        pm=new PrefManager(getApplicationContext());

       Dqids=new ArrayList<Integer>();
        LocQids=new ArrayList<Integer>(20);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE,45);

        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        toDate=df.format(calendar.getTime());

        if(toDate.equals(pm.getKeyQodDate())) {
            boolQOD = false;
        }
        else {
            boolQOD = true;
        }

/*

         final String[] APN_PROJECTION = {
                Telephony.Carriers.TYPE,            // 0
                Telephony.Carriers.MMSC,            // 1
                Telephony.Carriers.MMSPROXY,        // 2
                Telephony.Carriers.MMSPORT          // 3
        };
        final Cursor apnCursor =SqliteWrapper.query(getApplicationContext(), this.getApplicationContext().getContentResolver(), Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current"), APN_PROJECTION, null, null, null);
*/


        final DBHandler dbHandler = new DBHandler(getApplicationContext());
     //   final DBHandler dbQod = new DBHandler(getApplicationContext());
        myImage = (ImageView)findViewById(R.id.imgLogo);
        final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_rot);
        myImage.startAnimation(myRotation);
        myRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                myImage2.setVisibility(View.VISIBLE);
                myImage.setVisibility(View.GONE);

               if (dbHandler.getallrowcount()>=0)//!=
                  networkCheck();
                else
               {
                   if( pm.isLoggedIn())
                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    else
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }

            dbHandler.close();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        if(!pm.isNotifActivated())
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            pm.notifActivate();
        }
       // myImage2.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {

    }

boolean boolToggle(boolean bool)
{
    if(bool)
        return false;
    else return true;
}
    class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String free_result = "";
        String qod_result = "";



        protected void onPreExecute() {

            progressDialog= new ProgressDialog(IntroActivity.this);
          /*  progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.getWindow().setGravity(Gravity.BOTTOM);
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.gokera_icon_tiny);
            progressDialog.show();


        }
        @Override
        protected Void doInBackground(String... params) {

            String Qod_url_select ="http://gokera.phacsin.com/get_qids.php";//"http://gokera.phacsin.com/select_all.php?table_name=table_questions";




            DBclassRandomQuestions dbQod = new DBclassRandomQuestions(getApplicationContext());
            DBHandler collegeDBHandler = new DBHandler(getApplicationContext());
            collegeDBHandler.delete();
           // dbQod.delete();
            try {

                qod_result=getJSONfromURL(Qod_url_select);
                if(qod_result!=null)
                    progressDialog.setProgress(50);


               //if(dbQod.getallrowcount()<=1)
              // {

            /*   // for (int  i = 0; i < array2.length() ; i++ ) {
                    JSONObject  obj = array2.getJSONObject(Integer.parseInt(Integer.toString(1)));
                    if(progressDialog!=null)
                        progressDialog.setProgress(100+Integer.parseInt(String.valueOf(dbQod.getallrowcount())));
                    dbQod.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                  pm.setQodId(obj.getString("qid"));
                test2 = array2.toString();
                test3=obj.toString();
                   // test2=obj.getString("question");
               // }*/
               // }
               JSONArray array2    = new JSONArray(qod_result);
            if(progressDialog!=null)
                progressDialog.setProgress(1+Integer.parseInt(String.valueOf(dbQod.getallrowcount())));


                for (int  i = 0; i < array2.length() ; i++ ) {
                    JSONObject  obj = array2.getJSONObject(Integer.parseInt(Integer.toString(i)));
                    Dqids.add(Integer.parseInt(obj.getString("qid")));
                     }
               // test2 = array2.toString();
                Random random=new Random();
                int n;
                for(int j=0;j<20;j++) {
                    n=random.nextInt(Dqids.size());
                    LocQids.add(Dqids.get(n));
                }




                StringBuilder stringBuilder=new StringBuilder();
                //String free_url_select = "http://gokera.phacsin.com/select_all.php?table_name=table_questions";
                String free_url_select = "http://gokera.phacsin.com/select_all.php?table_name=table_questions&qids=";

                stringBuilder.append(free_url_select);
                for(int q:LocQids)
                {
                    stringBuilder.append(q+",");// stringBuilder.append("&subs%5B%5D='"+sub+"'") try implode also;
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                free_url_select=stringBuilder.toString();
                test2=free_url_select;
                free_result=getJSONfromURL(free_url_select);
                if(free_result!=null)
                    progressDialog.setProgress(1000);

                JSONArray array1    = new JSONArray(free_result);
                Log.d("log",free_result);
                for (int  i = 0; i < array1.length() ; i++ ) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                    // test=obj.toString();
                  if(progressDialog!=null)
                        progressDialog.setProgress(100+Integer.parseInt(String.valueOf(collegeDBHandler.getallrowcount())));

                    if(i==15 && boolQOD)
                    {
                        dbQod.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                        pm.setQodId(obj.getString("qid"));
                        pm.setQodDate(toDate);
                    }
                    collegeDBHandler.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                    //test=obj.getString("qid");
                }
              progressDialog.setProgress(100);

            }


            catch (JSONException e1) {
                Log.d("log_tag", e1.toString());
              //  collegeDBHandler.resetrows();
                ertest1=e1.getMessage();
                e1.printStackTrace();
            }
            catch (Exception e1)
            {
               // collegeDBHandler.resetrows();
                e1.printStackTrace();
                ertest2=e1.getMessage();
            }
          //  collegeDBHandler.close();
            dbQod.close();
            return null;


        }




        protected void onPostExecute(Void v) {
            if(progressDialog!=null)
                progressDialog.dismiss();
            DBHandler collegeDBHandler = new DBHandler(getApplicationContext());
            DBclassRandomQuestions dbQod = new DBclassRandomQuestions(getApplicationContext());
          /*  Toast.makeText(getApplicationContext(),test,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"numbers:"+qod_result,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Query:"+test2,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Questions: "+free_result,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Json exception: "+ertest1,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "exception: "+ertest2,Toast.LENGTH_LONG).show();*/
          //  Toast.makeText(getApplicationContext(), "from db single row : "+test3+"        Array2: "+test2,Toast.LENGTH_LONG).show();
          /*  if(dbQod.getallrowcount()>=1)
            {
               // Toast.makeText(getApplicationContext(),"There is something in table QOD"+test2,Toast.LENGTH_LONG).show();
            }
            else
            {
              //  Toast.makeText(getApplicationContext(),"table QOD is EMPTY"+test2,Toast.LENGTH_LONG).show();
            }*/
          //  Toast.makeText(getApplicationContext(),free_result,Toast.LENGTH_LONG).show();
          //  Toast.makeText(getApplicationContext(),qod_result,Toast.LENGTH_LONG).show();
           if(pm.isLoggedIn()) {
                Intent intent = new Intent(IntroActivity.this, MainActivity2.class);
                startActivityForResult(intent, 0);
            }
            else {
               Intent intent = new Intent(IntroActivity.this, LoginActivity.class);//Login
               startActivityForResult(intent, 0);
           }

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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntroActivity.this);
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

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(IntroActivity.this);
           // nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
           // nDialog.setIndeterminate(false);
            nDialog.getWindow().setGravity(Gravity.BOTTOM);
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
                    URL url = new URL("http://www.google.com");//("http://www.entreprenia15.com/quiz/select_all.php");
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
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}