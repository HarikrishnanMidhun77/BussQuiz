package com.quizapp.hp.quiz;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

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


public class QaReview extends ActionBarActivity {
    DBHandler_paid dbpaid;
    Cursor cur_qa;
    String qid,qtype,qstn,ans1=" ",ans2,ans3,ans4,rans1="",rans2="",rans3="",rans4="",rans_no,strmin,strsec,qidDup,pic,review;
    FloatingActionButton fabNext,fabPrev;
    Button btnA,btnB,btnC,btnD;
    EditText etQstn,etans1,etans2,etans3,etans4;
    TextView tvMenu;
    static int qno=0;
    PrefManager pm;
    ArrayList<Integer> ansd = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_review);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.qs_rev);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.gokera_icon_tiny);
        initialise();

        final Bundle bundle=getIntent().getExtras();
        Intent myIntent=getIntent();
        myIntent.getIntegerArrayListExtra("answered");
        ansd=bundle.getIntegerArrayList("answered");
       // Toast.makeText(getApplicationContext(),ansd.toString(),Toast.LENGTH_LONG).show();

        pm=new PrefManager(getApplicationContext());
        fabNext = (FloatingActionButton) findViewById(R.id.Reviewfab);
        fabPrev=(FloatingActionButton)findViewById(R.id.fabPrev);

        btnA = (Button) findViewById(R.id.btnAns1R);
        btnB = (Button) findViewById(R.id.btnAns2R);
        btnC = (Button) findViewById(R.id.btnAns3R);
        btnD = (Button) findViewById(R.id.btnAns4R);

        etQstn=(EditText) findViewById(R.id.txtQuestionR);
        etQstn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view.getId()==R.id.txtQuestion) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
      /*  etQstn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);//getResources().getDrawable(R.drawable.action_next)
        etQstn.setScroller(new Scroller(getApplication()));
        etQstn.setVerticalScrollBarEnabled(true);*/
        etans1 = (EditText) findViewById(R.id.txtAns1R);
        etans2 = (EditText) findViewById(R.id.txtAns2R);
        etans3 = (EditText) findViewById(R.id.txtAns3R);
        etans4 = (EditText) findViewById(R.id.txtAns4R);
        dbpaid = new DBHandler_paid(this);
       // fabNext.setOnClickListener(QaReview.this);

       fetQs(qno);

        /*dbpaid = new DBHandler_paid(this);


        if(cur_qa.moveToFirst())
        {
            do{

    Toast.makeText(this,cur_qa.getString(0),Toast.LENGTH_LONG).show();
            }while(cur_qa.moveToNext());
        }
        cur_qa.moveToFirst();*/
     //   fetQs(qno);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

               if(qno<20) {
                   fetQs(qno++);
                   tvMenu.setText("Qstn" + "\n      " + String.valueOf(qno + 1));
               }
                else {
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(QaReview.this);
                   alertDialog.setTitle("Confirmation");
                   alertDialog.setMessage("Review Completed.Do you want to exit?");
                   alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface arg0, int arg1) {
                           // TODO Auto-generated method stub

                       }
                   });
                   alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface arg0, int arg1) {
                           // TODO Auto-generated method stub
                           Intent intent = new Intent(QaReview.this, MainActivity2.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

                       }
                   });
                   alertDialog.show();
               }
            }
        });
fabPrev.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (qno > 0) {
            fetQs(qno--);
            tvMenu.setText("Qstn" + "\n      " + String.valueOf(qno+1));
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QaReview.this);
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                }
            });
            alertDialog.setPositiveButton("Result", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(QaReview.this, MainActivity2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

                }
            });
            alertDialog.show();
        }
    }
});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qa_review, menu);
        tvMenu = new TextView(this);
        //  tv.setText(getString(R.string.matchmacking)+"  ");
        // tvMenu.setTextColor(getResources().getColor(Color.WHITE));
        // tv.setOnClickListener(this);
        tvMenu.setTextColor((Color.parseColor("#ffffff")));
        tvMenu.setPadding(5, 0, 5, 0);
        tvMenu.setTypeface(null, Typeface.BOLD);
        tvMenu.setTextSize(15);
        menu.add(0, 1, 1, "Qstn").setActionView(tvMenu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tvMenu.setText("Qstn" + "\n      " + String.valueOf(qno+1));
        return true;
    }
    private void initialise()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rev) {
            final EditText txtRev = new EditText(getApplicationContext());

            new AlertDialog.Builder(QaReview.this)
                    .setTitle("Review")
                    .setMessage("Write your review here:")
                    .setView(txtRev)
                    .setPositiveButton("POST", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            review = txtRev.getText().toString();
                            networkCheck();


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void fetQs(int lqno)
    {
        cur_qa=dbpaid.getColsById(Long.valueOf(ansd.get(lqno)));
        // cur_qa=dbpaid.getColsById(Long.valueOf(1));
        if(cur_qa.moveToFirst())
        {
            qid = cur_qa.getString(0);
            qtype = cur_qa.getString(1);
            qstn = cur_qa.getString(5);
            ans1 = cur_qa.getString(6);
            ans2 = cur_qa.getString(7);
            ans3 = cur_qa.getString(8);
            ans4 = cur_qa.getString(9);
            rans1 = cur_qa.getString(10);
            rans2 = cur_qa.getString(11);
            rans3 = cur_qa.getString(12);
            rans4 = cur_qa.getString(13);
            rans_no = cur_qa.getString(14);
            pic = cur_qa.getString(15);
        }
     //   Toast.makeText(getApplicationContext(),String.valueOf(lqno)+qid+qtype+qstn,Toast.LENGTH_LONG).show();
        Qrefresh();
        setQ();
    }
    public void setQ() {
        etQstn.setText(qstn);
        etans1.setText(ans1);
        etans2.setText(ans2);
        etans3.setText(ans3);
        etans4.setText(ans4);

       /* if(ans1.equals(rans1) || ans1.equals(rans2) || ans1.equals(rans3) || ans1.equals(rans4) )
            btnA.setBackgroundColor(Color.GREEN);
        if(ans2.equals(rans1) || ans2.equals(rans2) || ans2.equals(rans3) || ans2.equals(rans4))
                btnB.setBackgroundColor(Color.GREEN);
        if(ans3.equals(rans1) || ans3.equals(rans2) || ans3.equals(rans3) || ans3.equals(rans4))
            btnB.setBackgroundColor(Color.GREEN);
       if(ans4.equals(rans1) || ans4.equals(rans2) || ans4.equals(rans3) || ans4.equals(rans4))
            btnB.setBackgroundColor(Color.GREEN);*/

        }

    public void Qrefresh() {

        btnA.setBackgroundColor(Color.parseColor("#0091ea"));
        btnB.setBackgroundColor(Color.parseColor("#0091ea"));
        btnC.setBackgroundColor(Color.parseColor("#0091ea"));
        btnD.setBackgroundColor(Color.parseColor("#0091ea"));
    }
    class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String free_result = "";
        String qod_result = "";




        protected void onPreExecute() {
         /*   progressDialog= new ProgressDialog(QandAActivityPaid.this);
            progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);*/

        }
        @Override
        protected Void doInBackground(String... params) {
            // String phn=pm.getMobileNumber();

            int uid=pm.getUid();

            try {

                    String urlRev = "http://gokera.phacsin.com/review1.php?uid="+uid+"&qid="+qid+"&msg="+review;//+phn;
                    free_result=getJSONfromURL(urlRev);



                JSONArray array1    = new JSONArray(free_result);

                for (int  i = 0; i < array1.length() ; i++ ) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));

                }

                //progressDialog.setProgress(1800);

            }
            catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();
            }
            catch (Exception e1)
            {

                e1.printStackTrace();
            }

            return null;


        }

        protected void onPostExecute(Void v) {
         /*   if(progressDialog!=null)
                progressDialog.dismiss();*/
         /*   Toast.makeText(getApplicationContext(), test,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Json exception: "+ertest1,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "exception: "+ertest2,Toast.LENGTH_LONG).show();*/
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QaReview.this);
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
            nDialog = new ProgressDialog(QaReview.this);
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
                    URL url = new URL("http://www.google.com");//("http://www.entreprenia15.com/quiz/result1.php");
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
       /* if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
    }

    }
