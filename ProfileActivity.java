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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.r0adkll.slidr.Slidr;

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
import java.sql.Date;
import java.util.ArrayList;

import random_questions_db.Bar_db;
import random_questions_db.Pie_db;


public class ProfileActivity extends ActionBarActivity implements SimpleGestureFilter.SimpleGestureListener {
    private SimpleGestureFilter detector;
    int Rans=100,Tans=150;
    BarEntry v[];
    ArrayList<BarEntry>[] valsChart;
    ArrayList<String> Mdate;
    ArrayList<String> Dper;
    ProgressDialog progressDialog;
    TextView txtNo;
    ImageButton profEd;

    Pie_db pie_db;
    Bar_db bar_db;
    String phn,test,test2,test3;
    String DperTest,MdatTest;
    PieChart Pchart;
    BarChart chart;
    BarDataSet[] bd;
    PrefManager pm;
    Button more;
    int tBAA,tSST,tFREE;
    TextView txtname,txtphno,txtemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int primary = getResources().getColor(R.color.matBlack);
        int secondary = getResources().getColor(R.color.matBrown);
        Slidr.attach(this, primary, secondary);

        pm=new PrefManager(ProfileActivity.this);
        pie_db=new Pie_db(getApplicationContext());
        bar_db=new Bar_db(getApplicationContext());

        txtname=(TextView)findViewById(R.id.txtProf_name);
        txtphno=(TextView)findViewById(R.id.txtProf_ph);
        txtemail=(TextView)findViewById(R.id.txtProfListEmail);
        txtname.setText(pm.getName());
        txtphno.setText(pm.getMobileNumber());
      //  Toast.makeText(getApplicationContext(),"mobile: "+pm.getMobileNumber(),Toast.LENGTH_LONG).show();
        txtemail.setText(pm.getEmail());

        profEd=(ImageButton)findViewById(R.id.btnprofedit);
        profEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,ProfileEditActivity.class);
                startActivity(intent);
            }
        });
        more=(Button)findViewById(R.id.btnMcharts);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,UserGraphs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
        txtNo=(TextView)findViewById(R.id.txtNoCon);
        txtNo.setVisibility(View.GONE);

        detector=new SimpleGestureFilter(this, this);
        Mdate = new ArrayList<String>();
        Dper = new ArrayList<String>();

        Pchart=(PieChart)findViewById(R.id.pie_chart);
        chart=(BarChart)findViewById(R.id.Barchart);

        if(pm.getNetBool2()) {
            pm.setNetBool2(false);
            networkCheck();

        }
        else {
            pie_db.open();
            Cursor cDates=pie_db.getColsById(1);
            if(cDates.moveToFirst()) {
                tSST=cDates.getInt(1);
                tBAA=cDates.getInt(2);
                tFREE=cDates.getInt(3);
            }
            pie_db.close();

            ArrayList<Entry> PvalsChart = new ArrayList<Entry>();

            ArrayList<String> PlistStr = new ArrayList<String>();
            PlistStr.add("SST");
            PlistStr.add("BAA");
            PlistStr.add("FREE");

            Entry c1v1 = new Entry(tSST,0);
            PvalsChart.add(c1v1);
            Entry c1v2 = new Entry(tBAA,1);
            PvalsChart.add(c1v2);
            Entry c1v3 = new Entry(tFREE,2);
            PvalsChart.add(c1v3);
            PieDataSet pd1=new PieDataSet(PvalsChart," ");
            pd1.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.matYellow), getResources().getColor(R.color.blue)});
            PieData pdata1=new PieData(PlistStr,pd1);

            chartMake(Pchart, pdata1,(tBAA+tSST+tFREE));

            bar_db.open();
            Cursor cBar=bar_db.getAllTitles();

            if(cBar.moveToFirst()) {
               do{
                   MdatTest=cBar.getString(1)!=null?cBar.getString(1):"0";
                  Mdate.add( MdatTest);
                  // DperTest=cBar.getDouble(2)!=null?String.valueOf(cBar.getDouble(2)):"0";
                   Dper.add(String.valueOf(cBar.getDouble(2)));
                } while(cBar.moveToNext());

            }
            bar_db.close();

            ArrayList<String> listStr = new ArrayList<String>();
        /*for(int i=1;i<=30;i+=1){
            listStr.add(String.valueOf(i));
        }*/
            listStr.addAll(Mdate);
            v=new BarEntry[50];
            valsChart = new ArrayList[50];
            bd=new BarDataSet[50];

            ArrayList<BarDataSet> dataset = new ArrayList<BarDataSet>();


            for(int i=1;i<=Dper.size();i+=3){
                v[i]= new BarEntry(Float.parseFloat(Dper.get(i - 1)),(i-1));
                valsChart[i]=new ArrayList<BarEntry>();
                valsChart[i].add(v[i]);
                bd[i]=new BarDataSet(valsChart[i]," ");
                bd[i].setColor(getResources().getColor(R.color.metGreen));
                dataset.add(bd[i]);
            }
            BarData bdata=new BarData(listStr,dataset);

            chart.setData(bdata);
            // chart.animateX(6000, Easing.EasingOption.Linear);
            chart.setDescription("Monthly Performance");
            // chart.invalidate();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
            Intent intent=new Intent(this,MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        if(direction==SimpleGestureFilter.SWIPE_LEFT)
        {

            Intent intent=new Intent(ProfileActivity.this,MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
    }

    @Override
    public void onDoubleTap() {
        Intent intent=new Intent(ProfileActivity.this,UserGraphs.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
    void chartMake(PieChart chart,PieData pdata,int Tans) {
        chart.setData(pdata);
      // chart.setDescriptionPosition(430.0f, 357.0f);
        //chart.setDescriptionTextSize(100.00f);
        chart.setCenterText("Total Attempts\n" + String.valueOf(Tans));
        chart.setCenterTextSize(12);
        chart.setCenterTextColor(getResources().getColor(R.color.white));
        chart.setHoleColor(getResources().getColor(R.color.matBrown));
        chart.setDescriptionColor(getResources().getColor(R.color.matBrown));
        //chart.setDescription("Attempts/Category");
      //  chart.animateX(6000, Easing.EasingOption.EaseOutBack);

       // chart.invalidate();
    }
    public class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String paid_result = "";
        String pie_result = "";




        protected void onPreExecute() {
            progressDialog= new ProgressDialog(ProfileActivity.this);
          /*  progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);

        }
        @Override
        protected Void doInBackground(String... params) {
            pm=new PrefManager(getApplicationContext());
            int uid=pm.getUid();

            String paid_url_select = "http://gokera.phacsin.com/prof_input.php?uid="+uid;//+phn
            String Pdat= "http://gokera.phacsin.com/prof_pie_input.php?uid="+uid;

            try {
                paid_result=getJSONfromURL(paid_url_select);


              if(paid_result!=null)
                    progressDialog.setProgress(50);


                JSONArray array2    = new JSONArray(paid_result);

                for (int  i = 0; i < array2.length() ; i++ ) {
                    JSONObject obj = array2.getJSONObject(Integer.parseInt(Integer.toString(i)));
                    if(obj.isNull("sst_quiz_att"))
                    {
                        tSST=0;
                    }
                    else {
                        tSST=Integer.parseInt(obj.getString("sst_quiz_att"));
                    }
                    tBAA=obj.isNull("baa_quiz_att")?0:Integer.parseInt(obj.getString("baa_quiz_att"));
                    tFREE=obj.isNull("free_quiz_att")?0:Integer.parseInt(obj.getString("free_quiz_att"));
                   // tFREE=Integer.parseInt(obj.getString("free_quiz_att"));
                    pie_db.updateRow(1,String.valueOf(tSST),String.valueOf(tBAA),String.valueOf(tFREE));

                }

                ArrayList<Entry> PvalsChart = new ArrayList<Entry>();

                ArrayList<String> PlistStr = new ArrayList<String>();
                PlistStr.add("SST");
                PlistStr.add("BAA");
                PlistStr.add("FREE");

                Entry c1v1 = new Entry(tSST,0);
                PvalsChart.add(c1v1);
                Entry c1v2 = new Entry(tBAA,1);
                PvalsChart.add(c1v2);
                Entry c1v3 = new Entry(tFREE,2);
                PvalsChart.add(c1v3);
                PieDataSet pd1=new PieDataSet(PvalsChart," ");
                pd1.setColors(new int[]{getResources().getColor(R.color.metGreen), getResources().getColor(R.color.matYellow), getResources().getColor(R.color.blue)});
                PieData pdata1=new PieData(PlistStr,pd1);

                chartMake(Pchart, pdata1,(tBAA+tSST+tFREE));
             //   test2=String.valueOf(tSST)+"\n"+String.valueOf(tBAA)+"\n"+String.valueOf(tFREE);
              //  Toast.makeText(getApplicationContext(),"pie values:"+test2,Toast.LENGTH_LONG).show();



                pie_result=getJSONfromURL(Pdat);
                test=pie_result;
                JSONArray array1    = new JSONArray(pie_result);

                if(pie_result!=null)
                {
                    chart.setVisibility(View.VISIBLE);
                    progressDialog.setProgress(50);
                }
                else
                chart.setVisibility(View.GONE);



                bar_db.open();
                bar_db.delete();
                for (int  i = 0; i < array1.length() ; i++ ) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                    if(!(obj.isNull("at_date")) && !(obj.isNull("AVER")) )
                    {
                        Mdate.add(obj.getString("at_date"));
                        Dper.add(obj.getString("AVER"));
                        bar_db.insertmaster(obj.getString("at_date"),obj.getString("AVER"));
                    }


                 // obj.getString("Q_ref_ID"), obj.getString("Q_DATE"), obj.getString("CATEGORY"), obj.getString("TOPICS"), obj.getString("DIFFICULTY"), obj.getString("QUESTION"), obj.getString("ANS1"), obj.getString("ANS2"), obj.getString("ANS3"), obj.getString("ANS4"), obj.getString("C_ANS1"), obj.getString("C_ANS2"), obj.getString("C_ANS3"), obj.getString("C_ANS4"), obj.getString("C_ANS_NO"), obj.getString("PICTURE_NAME"));
                }
                bar_db.close();
                test2=Mdate.toString()+"\n"+Dper.toString();
                test2=array1.toString();
                ArrayList<String> listStr = new ArrayList<String>();
        /*for(int i=1;i<=30;i+=1){
            listStr.add(String.valueOf(i));
        }*/
                listStr.addAll(Mdate);
                v=new BarEntry[50];
                valsChart = new ArrayList[50];
                bd=new BarDataSet[50];

                ArrayList<BarDataSet> dataset = new ArrayList<BarDataSet>();


                for(int i=1;i<=Dper.size();i+=3){
                    v[i]= new BarEntry(Float.parseFloat(Dper.get(i - 1)),(i-1));
                    valsChart[i]=new ArrayList<BarEntry>();
                    valsChart[i].add(v[i]);
                    bd[i]=new BarDataSet(valsChart[i]," ");
                    bd[i].setColor(getResources().getColor(R.color.metGreen));
                    dataset.add(bd[i]);
                }
                BarData bdata=new BarData(listStr,dataset);

                chart.setData(bdata);
               // chart.animateX(6000, Easing.EasingOption.Linear);
                chart.setDescription("Monthly Performance");
               // chart.invalidate();

              // test2=String.valueOf(tSST)+"\n"+String.valueOf(tBAA)+"\n"+String.valueOf(tFREE);
               test=Mdate.toString()+"\n"+Dper;
                test3="Array2: "+array2.toString();
                progressDialog.setProgress(1800);

            }


            catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();
            }
            catch (Exception e1)
            {

                e1.printStackTrace();
            }

            // dbQod.close();
            return null;


        }




        protected void onPostExecute(Void v) {
           if(progressDialog!=null)
                progressDialog.dismiss();

         //   Toast.makeText(getApplicationContext(),paid_result+"\n"+test3+"\n"+test2,Toast.LENGTH_LONG).show();
          //  Toast.makeText(getApplicationContext(),test2,Toast.LENGTH_LONG).show();
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
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
            Pchart.setVisibility(View.VISIBLE);
            chart.setVisibility(View.VISIBLE);
            txtNo.setVisibility(View.GONE);
            more.setVisibility(View.VISIBLE);
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
            nDialog = new ProgressDialog(ProfileActivity.this);
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
               /* chart.setVisibility(View.GONE);
                Pchart.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                txtNo.setVisibility(View.VISIBLE);*/
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
       if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
