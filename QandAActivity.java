package com.quizapp.hp.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.Scroller;
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

import random_questions_db.DBclassRandomQuestions;

public class QandAActivity extends ActionBarActivity implements View.OnClickListener {
    int btnid,count=1;

    long timeRem;
    SharedPreferences pref;
    PrefManager pm;
    int Rflag=0,Qcount=0;
    String review,test,stat,ertest1,ertest2;
    Calendar calendar;
   // ProgressDialog progressDialog;
    int minutes,seconds,Canss=0;
    int qid_int;
    String qid,qtype,qstn,qcat,qsub,qdiff,ans1,ans2,ans3,ans4,rans1,rans2,rans3,rans4,rans_no,strmin,strsec,qidDup,pic;
    ImageButton ibExit;
   DBclassRandomQuestions dbQod;
    DBHandler dbRand;
    ArrayList<String> shown1 = new ArrayList<String>();
    ArrayList<Integer> corr_ansd = new ArrayList<Integer>();
    ArrayList<String> Arr_qid = new ArrayList<String>();
    ArrayList<Integer> wrg_ansd = new ArrayList<Integer>(); //for specifying the question answered right(for ref. table)
    EditText etQstn,etans1,etans2,etans3,etans4,etTime,etQno;
    Button btnA,btnB,btnC,btnD,ibNext;
    FloatingActionButton fabNext,fabRev;
    CountDownTimer Qtimer;
    ImageView imageView;
    Animation a;
    ImageLoader imageLoader;
    String QID_string,btnId2_string,revTest="no review";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qand_a);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.quiz_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.gokera_icon_tiny);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.gokera_icon_tiny);
        initialise();

        pm=new PrefManager(getApplicationContext());


        imageLoader = ImageLoader.getInstance();
        imageView=(ImageView)findViewById(R.id.imageViewQn);
        imageView.setVisibility(View.GONE);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(QandAActivity.this));

        a = AnimationUtils.loadAnimation(this, R.anim.btn_anim);
        a.reset();

        fabNext=(FloatingActionButton)findViewById(R.id.fab);
        fabRev=(FloatingActionButton)findViewById(R.id.fabRev);
        dbRand=new DBHandler(this);
        dbQod=new DBclassRandomQuestions(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        btnId2_string = extras.getString("EXTRA_btnId2");
        btnid=Integer.parseInt(btnId2_string);
        if(btnid==4)
        {
            QID_string= extras.getString("EXTRA_QID");
            qid=QID_string;
        }
        btnA=(Button)findViewById(R.id.btnAns1);
        btnB=(Button)findViewById(R.id.btnAns2);
        btnC=(Button)findViewById(R.id.btnAns3);
        btnD=(Button)findViewById(R.id.btnAns4);

        etQno=(EditText)findViewById(R.id.etQno);
        etQstn=(EditText)findViewById(R.id.txtQuestion);

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

        etans1=(EditText)findViewById(R.id.txtAns1);
        etans2=(EditText)findViewById(R.id.txtAns2);
        etans3=(EditText)findViewById(R.id.txtAns3);
        etans4=(EditText)findViewById(R.id.txtAns4);
        etTime=(EditText)findViewById(R.id.etTimer);
        fabNext.setOnClickListener(this);

        Cursor c1=dbRand.getAllIds();
        c1.moveToFirst();
        while (!c1.isAfterLast()) {
            Arr_qid.add(c1.getString(0));
            c1.moveToNext();
        }
      //  Toast.makeText(getApplicationContext(),"qid array  "+Arr_qid.toString(),Toast.LENGTH_LONG).show();
        switch (btnid) {
            case 1:
                qstnTimer(600000,1000);
               quizRand();
                break;
            case 4:
                qstnTimer(60000,1000);
               quizQod();
        }
     /*   fabRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
                }

    private void initialise()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
           // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qand_a, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QandAActivity.this);
            alertDialog.setTitle("CONFIRMATION");
            alertDialog.setMessage("Do you really want to quit?");
            alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Qtimer.cancel();
                    if (btnid == 4) {
                        goBack();

                    } else {
                        pm.setGifted(false);
                        networkCheck();

                        pm.setNetBool(true);
                        Intent intent = new Intent(QandAActivity.this, MainActivity2.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.disappear_bottom_right_in, R.anim.disappear_bottom_right_out);
                    }
                }
            });
            alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                }
            });
            alertDialog.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void myQsetter(String qs,String a1,String a2,String a3,String a4){
        etQstn.setText(qs);
        /*etQstn.setCompoundDrawablesWithIntrinsicBounds(null,null,null ,null);//getResources().getDrawable(R.drawable.action_next)
        etQstn.setScroller(new Scroller(getApplication()));
        etQstn.setVerticalScrollBarEnabled(true);*/
       // etQstn.setMovementMethod(new ScrollingMovementMethod());
        etans1.setText(a1);
        etans2.setText(a2);
        etans3.setText(a3);
        etans4.setText(a4);
       /* etQstn.setText(qstn);
        etans1.setText(ans1);
        etans2.setText(ans2);
        etans3.setText(ans3);
        etans4.setText(ans4);*/
        etans1.setOnClickListener(this);
        etans2.setOnClickListener(this);
        etans3.setOnClickListener(this);
        etans4.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.txtAns1:

        if ((etans1.getText().toString().trim()).equals(rans1)||(etans1.getText().toString().trim()).equals(rans2)||(etans1.getText().toString().trim()).equals(rans3)||(etans1.getText().toString().trim()).equals(rans4)) {

            qtnRight(btnA);
        } else {
            qtnWrong(btnA);
        }

        break;
        case R.id.txtAns2:
            if ((etans2.getText().toString().trim()).equals(rans1)||(etans2.getText().toString().trim()).equals(rans2)||(etans2.getText().toString().trim()).equals(rans3)||(etans2.getText().toString().trim()).equals(rans4)) {
                qtnRight(btnB);
            } else {
                qtnWrong(btnB);
            }
            break;
        case R.id.txtAns3:
            if ((etans3.getText().toString().trim()).equals(rans1)||(etans3.getText().toString().trim()).equals(rans2)||(etans3.getText().toString().trim()).equals(rans3)||(etans3.getText().toString().trim()).equals(rans4)) {
                qtnRight(btnC);
            } else {
                qtnWrong(btnC);
            }
            break;
        case R.id.txtAns4:
            if ((etans4.getText().toString().trim()).equals(rans1)||(etans4.getText().toString().trim()).equals(rans2)||(etans4.getText().toString().trim()).equals(rans3)||(etans4.getText().toString().trim()).equals(rans4)) {
                qtnRight(btnD);
            } else {
                qtnWrong(btnD);
            }
            break;
            case R.id.fab:
                dbRand.deleteRow(Long.valueOf(qid));
            Qrefresh();
            quizRand();
            break;
            case R.id.fabRev:
                Qtimer.cancel();
                final EditText txtRev = new EditText(getApplicationContext());

                new AlertDialog.Builder(QandAActivity.this)
                        .setTitle("Review")
                        .setMessage("Write your review here:")
                        .setView(txtRev)
                        .setPositiveButton("POST", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                review = txtRev.getText().toString();
                                Rflag=1;
                                networkCheck();
                                qstnTimer(timeRem+3000,1000);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                qstnTimer(timeRem+3000,1000);
                            }
                        })
                        .show();
                break;

       /**/

        }
    }
    public void qtnRight(Button btn)
    {
        btn.setBackgroundColor(Color.GREEN);
        dsble();
        if(btnid==4)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QandAActivity.this);
            alertDialog.setTitle("Question of the day");
            alertDialog.setMessage("Well done!! Your answer is correct!");
            alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    corr_ansd.add(Integer.parseInt(qid));

                    //write the code to update ref. table (inc the correct answered column of this qid)

                  goBack();
                }
            });
            alertDialog.show();
        }
       // Qtimer.cancel();
        else{
        Canss++;
            Qcount++;
            corr_ansd.add(Integer.parseInt(qid));
        Toast.makeText(getApplicationContext(), "Correct Answer ",Toast.LENGTH_SHORT).show();}
    }
    public void qtnWrong(Button btn)
    {
        btn.setBackgroundColor(Color.RED);
        trueFind();
        dsble();
       // Qtimer.cancel();
        if(btnid==4)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QandAActivity.this);
            alertDialog.setTitle("Question of the day");
            alertDialog.setMessage("Oops! Your answer is wrong!");
            alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                   wrg_ansd.add(Integer.parseInt(qid));

                    //write the code to update ref. table

                   goBack();
                }
            });
            alertDialog.show();
        }
       else{
            Qcount++;
            wrg_ansd.add(Integer.parseInt(qid));
        Toast.makeText(getApplicationContext(), "Wrong Answer ",Toast.LENGTH_SHORT).show();}

    }
    public void dsble()
    {
        if(btnid != 4) {
            fabNext.clearAnimation();
           fabNext.startAnimation(a);
            fabNext.setVisibility(View.VISIBLE);
            fabNext.setOnClickListener(this);

            fabRev.clearAnimation();
            fabRev.startAnimation(a);
            fabRev.setVisibility(View.VISIBLE);
            fabRev.setOnClickListener(this);
        }
        imageView.setVisibility(View.INVISIBLE);
        etans1.setEnabled(false);
        etans1.setTextColor(Color.BLACK);
        etans2.setEnabled(false);
        etans2.setTextColor(Color.BLACK);
        etans3.setEnabled(false);
        etans3.setTextColor(Color.BLACK);
        etans4.setEnabled(false);
        etans4.setTextColor(Color.BLACK);
    }
    public void qstnFetch(Cursor c)
    {
       // Toast.makeText(getApplicationContext(),"in fn  "+c.toString(),Toast.LENGTH_LONG).show();
        if (c.moveToFirst()) {

            qid = c.getString(0);
            qcat = c.getString(1);
            qsub=c.getString(2);
            qdiff=c.getString(4);
            qstn = c.getString(5);
            ans1 = c.getString(6);
            ans2 = c.getString(7);
            ans3 = c.getString(8);
            ans4 = c.getString(9);
            rans1 = c.getString(10);
            rans2 = c.getString(11);
            rans3 = c.getString(12);
            rans4 = c.getString(13);
            rans_no=c.getString(14);
            pic=c.getString(15);
          //  Toast.makeText(getApplicationContext(),"  RansNo:"+rans_no+"  pic:"+pic,Toast.LENGTH_LONG).show();

        }
    }
    public void quizRand()
    {
        Cursor c;
        if(count<=10) {

            etQno.setText("Question:" + String.valueOf(count) + "/10");
            try {

                c = dbRand.getColsById(Long.parseLong(Arr_qid.get(Qcount)));
                if (c.moveToFirst())
                    qid = c.getString(0);
               // while(shown1.contains(qid))
                if(shown1.contains(qid))
                {
                    c = dbRand.getColsById(Long.parseLong(Arr_qid.get(Qcount)));
                }
                shown1.add(qid);
                qstnFetch(c);
            }catch (IndexOutOfBoundsException e1)
            {
                e1.getMessage();
            }

           // Toast.makeText(getApplicationContext(),"out fn  "+c.toString(),Toast.LENGTH_LONG).show();

           // Toast.makeText(getApplicationContext(),"  RansNo:"+rans_no+"  pic:"+pic,Toast.LENGTH_LONG).show();
          //  if(pic==null||pic.isEmpty())//(!(pic.equals("nill")))
          /* // if(pic.length()<=4)
            if(pic==null||pic.isEmpty())
            {
                imageView.setVisibility(View.GONE);
            }
           else if(pic.length()<=4)
            {
                imageView.setVisibility(View.GONE);
            }
            else
            {*//*try {
                imageView.setVisibility(View.VISIBLE);
                imageLoader.displayImage("http://gokera.phacsin.com/admins/assets/images/uploads/" + qid + ".jpg", imageView);    //http://gokera.phacsin.com/images*//*
            }catch(Exception e)
            {
                imageView.setVisibility(View.GONE);
            }*/
           // }
            myQsetter(qstn, ans1, ans2, ans3, ans4);
           // dbRand.close();
            fabNext.setVisibility(View.GONE);
            fabRev.setVisibility(View.GONE);
            count++;
        }
        else{
            //Update Question ref. table with correctly/wrongly answered question
            //Update user ref.
            pm.setGifted(false);
            networkCheck();
            dbRand.delete();
            shown1.clear();
            Intent randRsltIntent=new Intent(QandAActivity.this,ResultActivity.class);
            randRsltIntent.putExtra("RandRans",Canss);
            randRsltIntent.putExtra("TOTAL_QS", 10);
            randRsltIntent.putExtra("btn_det", 0);
            startActivity(randRsltIntent);
            overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out);
        }
    }

    public void Qrefresh(){
       /* etans1.setBackgroundDrawable(getDrawable(R.drawable.layout_border));
        etans2.setBackgroundDrawable(getDrawable(R.drawable.layout_border));
        etans3.setBackgroundDrawable(getDrawable(R.drawable.layout_border));
        etans4.setBackgroundDrawable(getDrawable(R.drawable.layout_border));*/
        btnA.setBackgroundColor(Color.parseColor("#0091ea"));
        btnB.setBackgroundColor(Color.parseColor("#0091ea"));
        btnC.setBackgroundColor(Color.parseColor("#0091ea"));
        btnD.setBackgroundColor(Color.parseColor("#0091ea"));

        etans1.setEnabled(true);
        etans2.setEnabled(true);
        etans3.setEnabled(true);
        etans4.setEnabled(true);

    }
   /* public class MyCount extends CountDownTimer{

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv.setText(”done!”);   //TextView object should be defined in onCreate
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv.setText(”Left: ” + millisUntilFinished/1000);// This will be called every Second.

        }
    }
*/
   public void quizQod()
   {
           etQno.setText("Question: " + String.valueOf(count) + "/1");
           Cursor c = dbQod.getColsById(Long.parseLong(qid));
      // qstnFetch(c);
       if (c.moveToFirst()) {

           qid = c.getString(0);
           qcat = c.getString(1);
           qsub=c.getString(2);
           qdiff=c.getString(4);
           qstn = c.getString(5);
           ans1 = c.getString(6);
           ans2 = c.getString(7);
           ans3 = c.getString(8);
           ans4 = c.getString(9);
           rans1 = c.getString(10);
           rans2 = c.getString(11);
           rans3 = c.getString(12);
           rans4 = c.getString(13);
           rans_no=c.getString(14);
           pic=c.getString(15);
         /*  if(pic.length()<=4)
           {
               imageView.setVisibility(View.GONE);
           }
           else
           {
               imageView.setVisibility(View.VISIBLE);
               imageLoader.displayImage("file://"+ Environment.getExternalStorageDirectory().toString()+"/"+"Download/new.jpg",imageView);    //http://gokera.phacsin.com/images*//*
           }*/

       }
           myQsetter(qstn, ans1, ans2, ans3, ans4);
           dbQod.close();
           fabNext.setVisibility(View.GONE);
       fabRev.setVisibility(View.GONE);

       }
    private void goBack()
    {
        pm=new PrefManager(getApplicationContext());
        pm.setQodSet(false);
        dbQod.deleteRow(Long.valueOf(qid));
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        String toDay=df.format(calendar.getTime());
        pref= getSharedPreferences("qodPref",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("qodDate", toDay);
        editor.commit();
        Intent intent = new Intent(QandAActivity.this, MainActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
    public void trueFind()
    {
        if ((etans1.getText().toString().trim()).equals(rans1)||(etans1.getText().toString().trim()).equals(rans2)||(etans1.getText().toString().trim()).equals(rans3)||(etans1.getText().toString().trim()).equals(rans4)) {

            btnA.setBackgroundColor(Color.GREEN);
        }

        else if ((etans2.getText().toString().trim()).equals(rans1)||(etans2.getText().toString().trim()).equals(rans2)||(etans2.getText().toString().trim()).equals(rans3)||(etans2.getText().toString().trim()).equals(rans4)) {
            btnB.setBackgroundColor(Color.GREEN);
        }

        else if ((etans3.getText().toString().trim()).equals(rans1)||(etans3.getText().toString().trim()).equals(rans2)||(etans3.getText().toString().trim()).equals(rans3)||(etans3.getText().toString().trim()).equals(rans4)) {
            btnC.setBackgroundColor(Color.GREEN);
        }
        else if ((etans4.getText().toString().trim()).equals(rans1)||(etans4.getText().toString().trim()).equals(rans2)||(etans4.getText().toString().trim()).equals(rans3)||(etans4.getText().toString().trim()).equals(rans4)) {
            btnD.setBackgroundColor(Color.GREEN);
        }
    }
    @Override
    public void onBackPressed()
    {
    }
    public void qstnTimer(long dur,int interval)
    {
        Qtimer = new CountDownTimer(dur,interval) {

            @Override
            public void onFinish() {
                //timeUp();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(QandAActivity.this);
                alertDialog.setTitle("Time up");
                alertDialog.setMessage("Time is up!!");
                alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        pm.setGifted(false);
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(QandAActivity.this, MainActivity2.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    }
                });
                alertDialog.setPositiveButton("Result", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        networkCheck();
                        dbRand.delete();
                        pm.setGifted(false);
                        shown1.clear();
                        Intent paidRsltIntent = new Intent(QandAActivity.this, ResultActivity.class);
                        paidRsltIntent.putExtra("RandRans", Canss);
                        paidRsltIntent.putExtra("TOTAL_QS",10);
                        paidRsltIntent.putExtra("btn_det", 0);
                        startActivity(paidRsltIntent);
                        overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out);

                    }
                });
                alertDialog.show();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                timeRem=millisUntilFinished;
                minutes = (int) ((millisUntilFinished / 1000) / 60);
                seconds = (int) ((millisUntilFinished / 1000) % 60);
                // int millicn = (int) (millisUntilFinished / 1000);
                if (minutes < 10)
                    strmin = "0" + String.valueOf(minutes);
                else {
                    strmin = String.valueOf(minutes);
                }
                if (seconds < 10)
                    strsec = "0" + String.valueOf(seconds);
                else {
                    strsec = String.valueOf(seconds);
                }
                etTime.setText("Time remaining:" + strmin + ":" + strsec);
            }

        }.start();
    }
    class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String free_result = "";
        String qod_result = "";




        protected void onPreExecute() {
          /*  progressDialog= new ProgressDialog(QandAActivity.this);
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
            String phn=pm.getMobileNumber();
            int wrn_count=wrg_ansd.size();
            int Tno=wrn_count+Canss;
            qid_int=Integer.parseInt(qid);
            StringBuilder stringBuilder=new StringBuilder();
            String paid_rslt = "http://gokera.phacsin.com/result1.php?category=FREE&uid="+uid+"&Tqstn="+Tno+"&curr_ans="+Canss+"&c_ansd=";//+phn;
            stringBuilder.append(paid_rslt);
            if(corr_ansd.isEmpty())
            {
                corr_ansd.add(0);
            }
            for(int cqstn:corr_ansd)
            {
                stringBuilder.append(cqstn+",");// stringBuilder.append("&subs%5B%5D='"+sub+"'") try implode also;
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            stringBuilder.append("&w_ansd=");
            if(wrg_ansd.isEmpty())
            {
                wrg_ansd.add(0);
            }
            for(int wqstn:wrg_ansd)
            {
                stringBuilder.append(wqstn+",");// stringBuilder.append("&subs%5B%5D='"+sub+"'") try implode also;
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            paid_rslt=stringBuilder.toString();
          test= paid_rslt;

            try {
                if(Rflag==1)
                {
                    stat="Review";
                    String urlRev = "http://gokera.phacsin.com/review1.php?uid="+uid+"&qid="+qid_int+"&msg="+review;//+phn;
                    revTest=urlRev;
                    free_result=getJSONfromURL(urlRev);
                    Rflag=0;
                }
                else
                {
                    stat="Question";
                    free_result=getJSONfromURL(paid_rslt);
                }
              /*  if(free_result!=null)
                    progressDialog.setProgress(100);*/



                JSONArray array1    = new JSONArray(free_result);

                for (int  i = 0; i < array1.length() ; i++ ) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));

                }

               // progressDialog.setProgress(1800);

            }
            catch (JSONException e1) {
                Log.d("log_tag", e1.toString());
                ertest1=e1.getMessage();
                e1.printStackTrace();
            }
            catch (Exception e1)
            {
                ertest2=e1.getMessage();
                e1.printStackTrace();
            }

            return null;


        }

        protected void onPostExecute(Void v) {
           /* if(progressDialog!=null)
         /*       progressDialog.dismiss();*//*
            Toast.makeText(getApplicationContext(), test,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),revTest,Toast.LENGTH_LONG).show();
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QandAActivity.this);
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
            nDialog = new ProgressDialog(QandAActivity.this);
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
   /*     if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
    }
   }


