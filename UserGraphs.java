package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
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

import random_questions_db.Pie_data2;


public class UserGraphs extends ActionBarActivity {
    int TansDay,TansTotal,TansMonth;
    int RansDay,RansTotal,RansMonth;
  //  ProgressDialog progressDialog;
    PrefManager pm;
    PieChart chart1,chart2,chart3;
    String test,test2,test3;
    JSONArray array1;
    Pie_data2 pie_data2;

  public UserGraphs(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_graphs);

       pm=new PrefManager(getApplicationContext());
        pie_data2=new Pie_data2(getApplicationContext());
         chart1 = (PieChart) findViewById(R.id.chart1);
        chart2 = (PieChart) findViewById(R.id.chart2);
         chart3 = (PieChart) findViewById(R.id.chart3);

        TansDay=0;
        RansDay=0;
        TansTotal=0;
        RansTotal=0;
        TansMonth=0;
        RansMonth=0;
        if(pm.getNetBool3())
        {
            pm.setNetBool3(false);
            networkCheck();

        }
       else{
            pie_data2.open();
            Cursor cDates=pie_data2.getColsById(1);
            if(cDates.moveToFirst()) {
                TansDay=cDates.getInt(1);
                RansDay=cDates.getInt(2);
                TansMonth=cDates.getInt(3);
                RansMonth=cDates.getInt(4);
                TansTotal=cDates.getInt(5);
                RansTotal=cDates.getInt(6);
            }
            pie_data2.close();

            ArrayList<Entry> valsChart1 = new ArrayList<Entry>();
            ArrayList<Entry> valsChart2 = new ArrayList<Entry>();
            ArrayList<Entry> valsChart3 = new ArrayList<Entry>();

            ArrayList<String> listStr = new ArrayList<String>();
            listStr.add("Correct");
            listStr.add("wrong");

            Entry c1v1 = new Entry(RansDay,0);
            valsChart1.add(c1v1);
            Entry c1v2 = new Entry((TansDay-RansDay),1);
            valsChart1.add(c1v2);
            PieDataSet pd1=new PieDataSet(valsChart1," ") ;
            pd1.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
            PieData pdata1=new PieData(listStr,pd1);



            Entry c2v1 = new Entry(RansTotal,0);
            valsChart2.add(c2v1);
            Entry c2v2 = new Entry((TansTotal-RansTotal),1);
            valsChart2.add(c2v2);
            PieDataSet pd2=new PieDataSet(valsChart2," ") ;
            pd2.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
            PieData pdata2=new PieData(listStr,pd2);



            Entry c3v1 = new Entry(RansMonth, 0);
            valsChart3.add(c3v1);
            Entry c3v2 = new Entry((TansMonth - RansMonth), 1);
            valsChart3.add(c3v2);
            PieDataSet pd3=new PieDataSet(valsChart3, " ");
            pd3.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
            PieData pdata3=new PieData(listStr,pd3);

            //chartMkFn(chart1, pdata1, TansDay,chart2,pdata3,TansMonth,chart3,pdata2,TansTotal);

            chartMake(chart1, pdata1, TansDay,"Today's Performance");
            chartMake(chart2,pdata3,TansMonth,"Monthly Performance");
            chartMake(chart3,pdata2,TansTotal,"Overall Performance");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_graphs, menu);
        return true;
    }
    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this,ProfileActivity.class);
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

   void chartMake(PieChart chart,PieData pdata,int Tans,String des) {
        chart.setData(pdata);
     //  chart.setDescriptionPosition(430.0f, 357.0f);
       // chart.setDescriptionTextSize(100.00f);
        chart.setCenterText("Total Questions\n" + String.valueOf(Tans));
        chart.setCenterTextSize(12);
        chart.setCenterTextColor(getResources().getColor(R.color.white));
        chart.setHoleColor(getResources().getColor(R.color.matBrown));
        chart.setDescriptionColor(getResources().getColor(R.color.matBrown));
        //chart.setDescription(des);
     //   chart.animateX(6000, Easing.EasingOption.EaseInElastic);
      //  chart.invalidate();

    }
    class task extends AsyncTask<String, String, Void> {
        InputStream is1 = null;
        String free_result = "";
        String free_result2 = "";
        String qod_result = "";


        protected void onPreExecute() {
           /* progressDialog = new ProgressDialog(UserGraphs.this);
            progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);*/

        }

        @Override
        protected Void doInBackground(String... params) {
            pm=new PrefManager(getApplicationContext());
            int uid= pm.getUid();
            String free_url_select = "http://gokera.phacsin.com/graph_input1.php?table_name=user_ref&table_name2=user_table&uid="+uid;//+phn;
            StringBuilder strB=new StringBuilder();


            try {
                free_result = getJSONfromURL(free_url_select);
                free_result2="["+free_result.trim()+"]";
                test2=free_result2;

                 array1 = new JSONArray(free_result2);
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                    if(obj.isNull("Day_T_Q") || obj.isNull("Day_C_A") )
                    {
                        TansDay=0;
                        RansDay=0;
                    }
                    else {
                        TansDay = Integer.parseInt(obj.getString("Day_T_Q"));
                        RansDay = Integer.parseInt(obj.getString("Day_C_A"));
                    }
                    if(obj.isNull("Total_T_Q") || obj.isNull("Total_C_A") )
                    {
                        TansTotal=0;
                        RansTotal=0;
                    }
                    else
                    {
                        TansTotal = Integer.parseInt(obj.getString("Total_T_Q"));
                        RansTotal = Integer.parseInt(obj.getString("Total_C_A"));
                    }
                  if(obj.isNull("Month_T_Q") || obj.isNull("Month_C_A"))
                  {
                      TansMonth=0;
                      RansMonth=0;
                  }
                    else
                  {
                      TansMonth = Integer.parseInt(obj.getString("Month_T_Q"));
                      RansMonth = Integer.parseInt(obj.getString("Month_C_A"));
                  }

                    pie_data2.updateRow(1,String.valueOf(TansDay),String.valueOf(RansDay),String.valueOf(TansMonth),String.valueOf(RansMonth),String.valueOf(TansTotal),String.valueOf(RansTotal));
                }
                test3=String.valueOf(TansDay)+"\n"+String.valueOf(RansDay)+"\n"+String.valueOf(TansTotal)+"\n"+String.valueOf(RansTotal);

                ArrayList<Entry> valsChart1 = new ArrayList<Entry>();
                ArrayList<Entry> valsChart2 = new ArrayList<Entry>();
                ArrayList<Entry> valsChart3 = new ArrayList<Entry>();

                ArrayList<String> listStr = new ArrayList<String>();
                listStr.add("Correct");
                listStr.add("wrong");

                Entry c1v1 = new Entry(RansDay,0);
                valsChart1.add(c1v1);
                Entry c1v2 = new Entry((TansDay-RansDay),1);
                valsChart1.add(c1v2);
                PieDataSet pd1=new PieDataSet(valsChart1," ") ;
                pd1.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
                PieData pdata1=new PieData(listStr,pd1);



                Entry c2v1 = new Entry(RansTotal,0);
                valsChart2.add(c2v1);
                Entry c2v2 = new Entry((TansTotal-RansTotal),1);
                valsChart2.add(c2v2);
                PieDataSet pd2=new PieDataSet(valsChart2," ") ;
                pd2.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
                PieData pdata2=new PieData(listStr,pd2);



                Entry c3v1 = new Entry(RansMonth, 0);
                valsChart3.add(c3v1);
                Entry c3v2 = new Entry((TansMonth - RansMonth), 1);
                valsChart3.add(c3v2);
                PieDataSet pd3=new PieDataSet(valsChart3, " ");
                pd3.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.metRed)});
                PieData pdata3=new PieData(listStr,pd3);

                //chartMkFn(chart1, pdata1, TansDay,chart2,pdata3,TansMonth,chart3,pdata2,TansTotal);

                chartMake(chart1, pdata1, TansDay,"Today's Performance");
                chartMake(chart2,pdata3,TansMonth,"Monthly Performance");
                chartMake(chart3,pdata2,TansTotal,"Overall Performance");
           // test2=String.valueOf(TansDay)+"\n"+String.valueOf(RansDay)+"\n"+String.valueOf(TansTotal);


                test=array1.toString();
                //progressDialog.setProgress(1800);

            } catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();
            } catch (Exception e1) {

                e1.printStackTrace();
            }



            return null;


        }


        protected void onPostExecute(Void v) {
           /* if (progressDialog != null)
                progressDialog.dismiss();*/
            //Toast.makeText(getApplicationContext(),"pie values:"+test2,Toast.LENGTH_LONG).show();
          //  Toast.makeText(getApplicationContext(),"Free resullt  "+test2+"\n"+"array: "+test+"\n"+test3,Toast.LENGTH_LONG).show();
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
                BufferedReader br = new BufferedReader(new InputStreamReader(is1, HTTP.UTF_8));
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserGraphs.this);
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
            nDialog = new ProgressDialog(UserGraphs.this);
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
                    URL url = new URL("http://www.google.com");//("http://www.entreprenia15.com/quiz/graph_input1.php");
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
      /*  if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
    }
}

