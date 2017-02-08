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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;

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
import java.util.Random;
import com.github.mikephil.charting.charts.HorizontalBarChart;

public class ResultActivity extends ActionBarActivity {
    int Rans;
    int Per;
    double pointper=0;
    String pointperstr="",test1,test2;
    int Tans=10,btn_det;
//HorizontalBarChart chart;
    BarDataSet[] bd;
    DBHandler_paid dbpaid;
    ProgressDialog progressDialog;
    Button det_res;
    PrefManager pm;
    BarEntry v[];
    ArrayList<BarEntry>[] valsChart;
    ArrayList<Integer> ansd = new ArrayList<Integer>();
    TextView resRang;
    String ran;
   // TextView txtTans,txtCans,txtPer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        det_res=(Button)findViewById(R.id.btnDetRev);
        resRang=(TextView)findViewById(R.id.txtResRange);
       // resRang.setVisibility(View.GONE);
        det_res.setVisibility(View.GONE);
        dbpaid = new DBHandler_paid(this);
        //chart=(HorizontalBarChart)findViewById(R.id.Rangechart);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.gokera_icon_tiny);
        initialise();

        pm=new PrefManager(getApplicationContext());
        pm.setNetBool(true);
        pm.setNetBool2(true);
        pm.setNetBool3(true);


        ArrayList<String> listStr = new ArrayList<String>();
        listStr.add("Correct");
        listStr.add("wrong");
       // ArrayList<PieDataSet> listVal = new ArrayList<PieDataSet>();

        ArrayList<Entry> valsChart = new ArrayList<Entry>();
        ArrayList<PieDataSet> dataset = new ArrayList<PieDataSet>();

        PieChart chart = (PieChart) findViewById(R.id.chart);

        final Bundle bundle=getIntent().getExtras();
        Intent myIntent=getIntent();
        myIntent.getIntExtra("btn_det",0);
        btn_det=bundle.getInt("btn_det");
        myIntent.getIntExtra("RandRans",0);
        Rans=bundle.getInt("RandRans");
        myIntent.getIntExtra("TOTAL_QS",0);
        Tans= bundle.getInt("TOTAL_QS");

        /*txtTans.setText("10");
        txtCans.setText(String.valueOf(Rans));
        //Per=((Rans/10)*100);
        Per=Rans*10;
        txtPer.setText(String.valueOf(Per + "%"));*/
        if(btn_det==1) {
            det_res.setVisibility(View.VISIBLE);
            myIntent.getIntegerArrayListExtra("answered");
            ansd=bundle.getIntegerArrayList("answered");
         //   Toast.makeText(getApplicationContext(),ansd.toString(),Toast.LENGTH_LONG).show();
         //   resRang.setVisibility(View.VISIBLE);
        }

        else
        { det_res.setVisibility(View.GONE);
         //   resRang.setVisibility(View.GONE);
        }

        det_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResultActivity.this,QaReview.class);
                intent.putExtra("answered",ansd);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_out_right);
            }
        });

        Entry v1 = new Entry(Rans,0);
        valsChart.add(v1);
        Entry v2 = new Entry((Tans-Rans),1);
        valsChart.add(v2);

        //PieDataSet setComp1 = new PieDataSet(valsChart, "Company 1");
        networkCheck();

       float fRans=(float)Rans;
        PieDataSet pd1=new PieDataSet(valsChart," ") ;
    pd1.setColors(new int[]{ getResources().getColor(R.color.metGreen) , getResources().getColor( R.color.metRed)});
            dataset.add(pd1);
        PieData pdata=new PieData(listStr,pd1);
        //chart.setData();

        chart.setData(pdata);
        chart.setDescriptionPosition(250.0f, 50.0f);
        chart.setDescriptionTextSize(100.00f);
        chart.setCenterText("Total Questions\n" + String.valueOf(Tans));
        chart.setCenterTextSize(21);
        chart.setCenterTextColor(getResources().getColor(R.color.white));
        chart.setHoleColor(getResources().getColor(R.color.matBrown));
        chart.setDescriptionColor(getResources().getColor(R.color.matBrown));
        chart.setDescription("Performance Analysis:");
        chart.animateX(6000, Easing.EasingOption.EaseInOutBack);
        chart.invalidate();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            dbpaid.delete();
            Intent intent=new Intent(this,MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.disappear_bottom_right_in, R.anim.disappear_bottom_right_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        dbpaid.delete();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }

      /*  public Entry(float val, int xIndex){

        }*/
      public class task extends AsyncTask<String, String, Void>
      {
          InputStream is1 = null;
          String paid_result = "";
          String qod_result = "";
          String free_result = "";



          protected void onPreExecute() {
              try{
                  DBHandler_paid dbHandler_paid=new DBHandler_paid(getApplicationContext());
                 // dbHandler_paid.delete();
              }catch(NullPointerException e)
              {
                  Toast.makeText(getApplicationContext(), "Table not Cleared", Toast.LENGTH_LONG).show();
              }
              progressDialog= new ProgressDialog(ResultActivity.this);
           /* progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");*/
              progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
              progressDialog.setMax(1800);
              progressDialog.show();
              progressDialog.setCancelable(false);

          }
          @Override
          protected Void doInBackground(String... params) {
                int user_id=pm.getUid();
              String paid_url_select = "http://gokera.phacsin.com/result_percent.php?uid="+user_id;

              try {
                  paid_result=getJSONfromURL(paid_url_select);
                  test1=paid_result;
                   paid_result="["+paid_result.trim()+"]";
                  if(paid_result!=null)
                      progressDialog.setProgress(100);
            /*    qod_result=getJSONfromURL(Qod_url_select);
                if(qod_result!=null)
                    progressDialog.setProgress(50);*/
                  JSONArray array1    = new JSONArray(paid_result);
                  test2=array1.toString();
                  for (int  i = 0; i < array1.length() ; i++ ) {
                      JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                        pointperstr+=obj.getString("pointper"); //Integer.parseInt(obj.getString("pointper"));

                      // collegeDBHandler.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                 }

                  progressDialog.setProgress(1800);

                /*  ArrayList<String> listStr = new ArrayList<String>();
        *//*for(int i=1;i<=30;i+=1){
            listStr.add(String.valueOf(i));
        }*//* ArrayList<BarDataSet> dataset = new ArrayList<BarDataSet>();

                  v=new BarEntry[2];
                  valsChart = new ArrayList[50];
                  bd=new BarDataSet[50];

                  v[1]= new BarEntry(pointper,1-1);
                  valsChart[1]=new ArrayList<BarEntry>();
                  valsChart[1].add(v[1]);
                  bd[1]=new BarDataSet(valsChart[1]," ");
                  bd[1].setColor(getResources().getColor(R.color.metGreen));
                  dataset.add(bd[1]);

                  BarData pdata=new BarData(listStr,dataset);
                  chart.setData(pdata);
                  chart.setDescriptionPosition(250.0f, 50.0f);
                  chart.setDescriptionTextSize(100.00f);
                  chart.setDescriptionColor(getResources().getColor(R.color.matBrown));
                  chart.setDescription("Performance Analysis:");
                  chart.animateX(6000, Easing.EasingOption.EaseInOutBack);
                  chart.invalidate();*/

              }


              catch (JSONException e1) {


                  Log.d("log_tag", e1.toString());
//                collegeDBHandler.resetrows();
                  e1.printStackTrace();
              }
              catch (Exception e1)
              {
                 // ertest2=e1.getMessage();

                  //collegeDBHandler.resetrows();
                  e1.printStackTrace();
              }
              // collegeDBHandler.close();
              // dbQod.close();
              return null;


          }




          protected void onPostExecute(Void v) {
              if(progressDialog!=null)
                  progressDialog.dismiss();
              resRang.setText(ran);
            /* Toast.makeText(getApplicationContext(),test1,Toast.LENGTH_LONG).show();
              Toast.makeText(getApplicationContext(),test2,Toast.LENGTH_LONG).show();*/
            //  Toast.makeText(getApplicationContext(),pointperstr+pointper,Toast.LENGTH_LONG).show();
              try{
                  pointper=Double.valueOf(pointperstr);
              }
              catch(Exception e)
              {
                  pointper=0;
              }
              if(pointper<10)
              {
                  ran="You belong to the lower 10 % performer category.";
              }
              else if(pointper<20 && pointper>10){
                  ran="You belong to the lower 20 % performer category.";
              }
              else if(pointper<30 && pointper>20){
                  ran="You belong to the lower 30 % performer category.";
              }
              else if(pointper<40 && pointper>30){
                  ran="You belong to the lower  40 % performer category.";
              }
              else if(pointper<50 && pointper>40){
                  ran="You belong to the lower 50 % performer category.";
              }
              else if(pointper<60 && pointper>50){
                  ran="Congratulations...You belong to the top 50 % performer category.";
              }
              else if(pointper<70 && pointper>60){
                  ran="Congratulations...You belong to the top 40 % performer category.";
              }
              else if(pointper<80 && pointper>70){
                  ran="Congratulations...You belong to the top 30% performer category.";
              }
              else if(pointper<90 && pointper>80){
                  ran="Congratulations...You belong to the top 20% performer category.";
              }
              else if(pointper<100 && pointper>90){
                  ran="Congratulations...You belong to the top 10% performer category.";
              }
              resRang.setText(ran);
          /*   Toast.makeText(getApplicationContext(),paid_result,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Json exception: "+ertest1,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"exception: "+ertest2,Toast.LENGTH_LONG).show();*/
            /*  DBHandler_paid collegeDBHandler = new DBHandler_paid(getApplicationContext());
              if(collegeDBHandler.getallrowcount()>=1) {
                  Intent intent=new Intent(ResultActivity.this,QandAActivityPaid.class);
                  Bundle extras = new Bundle();
                  extras.putString("cat", "SST");
                  intent.putExtras(extras);
                  startActivity(intent);
              }*/

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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResultActivity.this);
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
            nDialog = new ProgressDialog(ResultActivity.this);
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
              /*  Toast toast = Toast.makeText(getApplicationContext(), "Error in Network Connection", Toast.LENGTH_SHORT);

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

