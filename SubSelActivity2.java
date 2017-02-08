package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.util.Random;


public class SubSelActivity2 extends ActionBarActivity implements View.OnClickListener{
    Button btnLR,btnQS,btnSP,btnBA;
    String test33;
    boolean[] btnBool;
    TextView tvMenu;
    private int count=0;
    String test,ertest1,ertest2;
    ArrayList<Integer> Dqids;
    ArrayList<Integer> LocQids;
    ProgressDialog progressDialog;
    ArrayList<String> selected = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_sel_activity2);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_sub_sel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.gokera_icon_tiny);
        initialise();
        Dqids=new ArrayList<Integer>();
        LocQids=new ArrayList<Integer>();
        btnBool=new boolean[18];
        for(boolean b:btnBool)
        {
            b=false;
        }
        btnLR=(Button)findViewById(R.id.btnLR);
        btnLR.setOnClickListener(this);
        btnQS=(Button)findViewById(R.id.btnQS);
        btnQS.setOnClickListener(this);
        btnSP=(Button)findViewById(R.id.btnSP);
        btnSP.setOnClickListener(this);
        btnBA=(Button)findViewById(R.id.btnBA);
        btnBA.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.menu_sub_sel_activity2, menu);
        tvMenu = new TextView(this);
        //  tv.setText(getString(R.string.matchmacking)+"  ");
        // tvMenu.setTextColor(getResources().getColor(Color.WHITE));
        // tv.setOnClickListener(this);
        tvMenu.setTextColor((Color.parseColor("#ffffff")));
        tvMenu.setPadding(5, 0, 5, 0);
        tvMenu.setTypeface(null, Typeface.BOLD);
        tvMenu.setTextSize(15);
        menu.add(0, 1, 1, "selected").setActionView(tvMenu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tvMenu.setText("Selected" + "\n      " + String.valueOf(count));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            networkCheck();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnLR:
                if(!btnBool[0]) {
                    btnClick(btnLR, "Logical_Reasoning",0);}
                else {
                    btnReClick(btnLR, "Logical_Reasoning",0);}
                break;
            case R.id.btnQS:
                if(!btnBool[1]) {
                    btnClick(btnQS,"Quantitative_Skills",1);}
                else {
                    btnReClick(btnQS, "Quantitative_Skills", 1);}
                break;
            case R.id.btnSP:
                if(!btnBool[2]) {
                    btnClick(btnSP,"statistics_and_skills",2);}
                else {
                    btnReClick(btnSP, "statistics_and_skills", 2);}
                break;
            case R.id.btnBA:
                if(!btnBool[3]) {
                    btnClick(btnBA, "Business_Acumen",3);}
                else {
                    btnReClick(btnBA, "Business_Acumen", 3);}
                break;

        }


    }
    public void btnClick(Button btn, String str,int i)
    {
        btn.setBackgroundResource(R.color.green);
        selected.add(str);
        btnBool[i]=true;
        count++;
        tvMenu.setText("Selected"+"\n      "+String.valueOf(count));
    }
    public void btnReClick(Button btn,String str,int i)
    {
        btn.setBackgroundResource(R.drawable.bgm_grad);
        selected.remove(str);
        btnBool[i]=false;
        count--;
        tvMenu.setText("Selected"+"\n      "+String.valueOf(count));
    }
    @Override
    public void onBackPressed()
    {

            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);


    }
    public class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String paid_result = "";
        String free_result = "";




        protected void onPreExecute() {
            progressDialog= new ProgressDialog(SubSelActivity2.this);
           /* progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);

        }
        @Override
        protected Void doInBackground(String... params) {
            StringBuilder stringBuilder=new StringBuilder();
            String paid_url_select = "http://gokera.phacsin.com/get_qids_paid.php?table_name=table_questions&category=AAT&subs=";
            stringBuilder.append(paid_url_select);//AAT
            for(String sub:selected)
            {
                stringBuilder.append("'"+sub+"',");// stringBuilder.append("&subs%5B%5D='"+sub+"'") try implode also;
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            paid_url_select=stringBuilder.toString();
            // String paid_url_select = "http://gokera.phacsin.com/select_all.php?table_name=qod_questions";
            // String Qod_url_select = "http://gokera.phacsin.com/select_all.php?table_name=qod_questions";
            test=paid_url_select;

            DBHandler_paid collegeDBHandler = new  DBHandler_paid(getApplicationContext());
           collegeDBHandler.delete();
            try {
                paid_result=getJSONfromURL(paid_url_select);
               // paid_result="["+paid_result.trim()+"]";
                if(paid_result!=null)
                    progressDialog.setProgress(100);
            /*    qod_result=getJSONfromURL(Qod_url_select);
                if(qod_result!=null)
                    progressDialog.setProgress(50);*/



                JSONArray array1    = new JSONArray(paid_result);

                for (int  i = 0; i < array1.length() ; i++ ) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));

                    Dqids.add(Integer.parseInt(obj.getString("qid")));
                   // collegeDBHandler.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                }

                Random random=new Random();
                int n;
                for(int j=0;j<20;j++) {
                    n=random.nextInt(Dqids.size());
                    LocQids.add(Dqids.get(n));
                }


                StringBuilder stringBuilder2=new StringBuilder();
                //String free_url_select = "http://gokera.phacsin.com/select_all.php?table_name=table_questions";
                String free_url_select = "http://gokera.phacsin.com/select_all.php?table_name=table_questions&qids=";

                stringBuilder2.append(free_url_select);
                for(int q:LocQids)
                {
                    stringBuilder2.append(q+",");// stringBuilder.append("&subs%5B%5D='"+sub+"'") try implode also;
                }
                stringBuilder2.deleteCharAt(stringBuilder2.length()-1);
                free_url_select=stringBuilder2.toString();

                free_result=getJSONfromURL(free_url_select);
                test33=free_result;
                if(free_result!=null) {
                    progressDialog.setProgress(1000);
                    //Toast.makeText(getApplicationContext(), "free result is not null"+ertest1,Toast.LENGTH_LONG).show();
                }

                    JSONArray array2    = new JSONArray(free_result);

                    for (int  i = 0; i < array2.length() ; i++ ) {
                        JSONObject obj = array2.getJSONObject(Integer.parseInt(Integer.toString(i)));
                        if(progressDialog!=null)
                            progressDialog.setProgress(100+Integer.parseInt(String.valueOf(collegeDBHandler.getallrowcount())));
                        collegeDBHandler.insertmaster(obj.getString("qid"), obj.getString("category"), obj.getString("subject"), obj.getString("type"), obj.getString("difficulty"), obj.getString("question"), obj.getString("ans1"), obj.getString("ans2"), obj.getString("ans3"), obj.getString("ans4"), obj.getString("c_ans1"), obj.getString("c_ans2"), obj.getString("c_ans3"), obj.getString("c_ans4"), obj.getString("c_ans_no"), obj.getString("picture_name"));
                    }



                progressDialog.setProgress(1800);

            }


            catch (JSONException e1) {
                Log.d("log_tag",e1.toString());
                ertest1=e1.getMessage();
               // collegeDBHandler.resetrows();
                e1.printStackTrace();
            }
            catch (Exception e1)
            {ertest2=e1.getMessage();
               // collegeDBHandler.resetrows();
                e1.printStackTrace();

            }
         //   collegeDBHandler.close();
            // dbQod.close();
            return null;


        }




        protected void onPostExecute(Void v) {
            if(progressDialog!=null)
                progressDialog.dismiss();
          /*  Toast.makeText(getApplicationContext(),test33 ,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Json exception: "+ertest1,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "exception: "+ertest2,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Susel2 paid result:"+paid_result,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),test,Toast.LENGTH_LONG).show();*/
            DBHandler_paid collegeDBHandler = new DBHandler_paid(getApplicationContext());
            if(collegeDBHandler.getallrowcount()>=1) {
                Intent intent = new Intent(SubSelActivity2.this, QandAActivityPaid.class);
                Bundle extras = new Bundle();
                extras.putString("cat", "AAT");
                intent.putExtras(extras);
                startActivity(intent);
                //  DBHandler collegeDBHandler = new DBHandler(getApplicationContext());

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Sorry!Inadequate number of questions",Toast.LENGTH_LONG).show();
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubSelActivity2.this);
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
            nDialog = new ProgressDialog(SubSelActivity2.this);
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
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
