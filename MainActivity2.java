package com.quizapp.hp.quiz;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.navdrawer.SimpleSideDrawer;
import com.quizapp.hp.quiz.SimpleGestureFilter.SimpleGestureListener;
import com.quizapp.hp.quiz.AnalyticsApplication;

import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import com.quizapp.hp.quiz.factory.ManagerTypeface;

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

import random_questions_db.DBclassRandomQuestions;
import random_questions_db.Date_db;

public class MainActivity2 extends AppCompatActivity implements SimpleGestureListener,IabBroadcastReceiver.IabBroadcastListener {
    private TextView red_button,green_button,yellow_button,blue_button,board_note;
    int tap=0,i_code;
    Date sst_end,free_last,aat_end;
    PrefManager pm;
    DBclassRandomQuestions dbQod;
    Date_db date_db;
    long numb;
    ActivityAnimator aa;
    static final String TAG = "Gokera Quiz";
    String err1,err2;
    String qstn, qid,qDate,catst,catend,cd_ver,res=" ",test2;
    Calendar calendar;;
    DBHandler dbRand;
    EditText etQod;
    boolean datain=true,paid=false,recom=false,recom2=false,toastBool=false;
    private SimpleGestureFilter detector;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private PendingIntent pendingIntent;
    SharedPreferences pref;
    IabHelper mHelper;
    String base64EncodedPublicKey;
    IabBroadcastReceiver mBroadcastReceiver;
    static final String PLAY_SST= "sst";
    static final String PLAY_AAT = "baa";
    static final int RC_REQUEST = 10001;
    Tracker mTracker;
    ProgressDialog progressDialog;

    //  TextView txtQodPad;
   /* AnimationDrawable AniFrame;
    AnimationDrawable AniFrame2;
    AnimationDrawable AniFrame3;
    AnimationDrawable AniFrame4;
    AnimationDrawable AniFrame5;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_activity2);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

     /*   getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.gokera_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_main_title);*/
        getSupportActionBar().setTitle(R.string.app_main_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.gokera_icon_tiny);

        getSupportActionBar().setLogo(R.drawable.gokera_icon_tiny);

       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.gokera_icon);
        getSupportActionBar().setTitle(R.string.app_main_title);*/
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      // getSupportActionBar().setHomeActionContentDescription("Home",R.string.app_main_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pencil);
        getSupportActionBar().setHomeButtonEnabled(false);
       // getSupportActionBar().setIcon(R.drawable.gokera_icon);
        getSupportActionBar().setTitle(R.string.app_main_title);*/
       // getSupportActionBar().setDisplayUseLogoEnabled(true);
               // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initialise();
        date_db=new Date_db(getApplicationContext());
        dbQod = new DBclassRandomQuestions(getApplicationContext());
        AnalyticsApplication Aapplication1 = (AnalyticsApplication) getApplication();
        mTracker = Aapplication1.getDefaultTracker();

      //  Drawable draw=res.getDrawable(R.drawable.custom_progressbar);

     /*   Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        msgStr = extras.getString("qod_message");*/


      /*  Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 3);

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE,45);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.AM);
  alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);*/


        String code1="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkDSPS4bDj7JA1OS9NI";
        String code2="zaT7hSTmYvjPTZcELsgT75LYFrTPHWtbDSmyRwC7";
        String code3= "suZ0jDj4K/dD2RxP/w05SiIihICm6ayGuhY0CerX8Ilh";
        String code4="nnXPwR9A/RZ+wJACm+83zAhobAa4Id2CFcsZkQUfaHuSnQIraCz6vSq4+xtTEm5KmjlZUvukdFvaQdNukflI/";
         base64EncodedPublicKey =code1+code2+code3+code4+"UzCjlmElloSvy+Ev3j2whWK4/6qnykp+UuH2LTMGjN7qBNy9GtYy9y7Cq4N7T7mehApiqlDtLqN5Xn8A9mgaIEyqKqqdZ76zeJWQct2SVOplQokzVjr4D4LAZ/rEra6H+vNKDDIhQDRurwC4Isncj6qA9EQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);


        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE,45);

        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        qDate=df.format(calendar.getTime());

        pref= getSharedPreferences("qodPref",MODE_PRIVATE);

        String prefDay=pref.getString("qodDate", "2015-10-8");
        Date prefDate=Date.valueOf(prefDay);
        Date DqDate=Date.valueOf(qDate);
      //  Toast.makeText(getApplicationContext(), qDate+" == "+ prefDay, Toast.LENGTH_LONG).show();

        pm=new PrefManager(this);
       // pm.setQodSet(false);
        //pm.setQodId("8");
        if(!pm.getQodSet())
        {
            getQid();

        }
        else
        {
            try {
                qid = pm.getQodid();
                //  Toast.makeText(getApplicationContext(),"Qid fro pm: "+qid ,Toast.LENGTH_LONG).show();
                Cursor c2 = dbQod.getColsById(Long.parseLong(qid));
                if (c2.moveToFirst()) {
                    qstn = c2.getString(5);
                }
            }catch(Exception e1){
                Toast.makeText(getApplicationContext(),"Qid exception "+e1.getMessage() ,Toast.LENGTH_LONG).show();
            }
        }






                etQod = (EditText) findViewById(R.id.etQodPad);

        detector=new SimpleGestureFilter(this, this);
       //getSupportActionBar().set//HomeButtonEnabled(true);
        aa = new ActivityAnimator();

        //txtQodPad=(TextView)findViewById(R.id.txtQodPad);



        //Toast.makeText(getApplicationContext(), "DB QOD DONE ",Toast.LENGTH_LONG).show();
        // dbQod.insertmaster("SAS","qstn","35%","30%","90%","40%","40%");
      //  dbQod.close();

        Button btnInst= (Button) findViewById(R.id.btn_inst2);
        btnInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity2.this, Instructions.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        Button ansNow = (Button) findViewById(R.id.btnQodPad);
        ansNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAnow = new Intent(MainActivity2.this, QandAActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_btnId2", "4");
                extras.putString("EXTRA_QID", qid);
                intentAnow.putExtras(extras);
            /*    intentAnow.putExtra("btnId",4);
               intentAnow.putExtra("qId",qid);*/

                MainActivity2.this.startActivity(intentAnow);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        });



        if(prefDate.equals(DqDate)){

            etQod.setText("Wait till tomorrow for the next challenge!\n Be Prepared!!  ");
            ansNow.setEnabled(false);
            ansNow.setVisibility(View.GONE);
        }
        else{
            ansNow.setVisibility(View.VISIBLE);
            ansNow.setEnabled(true);
            etQod.setText(qstn);
        }

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity2.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

       /* String qstn2=" Income level of the customers acquired and % of customers in each of these levels are as follow.\n" +
                "Income Level\t% Customers\n" +
                "Low\t10%\n" +
                "Medium\t50%\n" +
                "High\t35%\n" +
                "Very High\t5%\n" +
                "A customer is selected randomly. What is probability that the customer will be from “High” and Very High” income group.";*/
      /*  String qstn=" We want to read a CSV (comma separated file). Following SAS program is submitted.\n" +
                "proc import datafile=\"C:\\temp\\test.csv\"\n" +
                "out=shoes\n" +
                "dbms=csv\n" +
                "replace;\n" +
                "getnames=no;\n" +
                "run;\n" +
                "proc print;\n" +
                "run";
        String ans1="SAS gives error as there is no input dataset name is given in print statement";
        String ans2="SAS prints the input file without any error";
        String ans3="Since “getname=no” is used without listing name of variables, SAS gives an error “No SAS variable/column names provided”";
        String ans4="None of these";*/
       /* dbRand = new DBHandler(this);
        dbRand.open();
        // dbRand.insertmaster("SAS", qstn, ans1, ans2, ans3, ans4, ans4);
        Toast.makeText(getApplicationContext(), "DB random DONE ",Toast.LENGTH_LONG).show();
        dbRand.close();
*/
      /*  ImageView IVbrainAnim = (ImageView) findViewById(R.id.ivBrainAnim1);
        ImageView IVbrainAnim2 = (ImageView) findViewById(R.id.ivBrainAnim2);
        ImageView IVbrainAnim3 = (ImageView) findViewById(R.id.ivBrainAnim3);
        ImageView IVbrainAnim4 = (ImageView) findViewById(R.id.ivBrainAnim4);
     *//*   ImageView IVbrainAnim5 = (ImageView)findViewById(R.id.ivBrainAnim5);*//*

        IVbrainAnim.setBackgroundResource(R.drawable.brain_anim);
        IVbrainAnim2.setBackgroundResource(R.drawable.brain_anim);
        IVbrainAnim3.setBackgroundResource(R.drawable.brain_anim);
        IVbrainAnim4.setBackgroundResource(R.drawable.brain_anim);
       *//* IVbrainAnim5.setBackgroundResource(R.drawable.brain_anim);*//*

        AniFrame = (AnimationDrawable) IVbrainAnim.getBackground();
        AniFrame2 = (AnimationDrawable) IVbrainAnim2.getBackground();
        AniFrame3 = (AnimationDrawable) IVbrainAnim3.getBackground();
        AniFrame4 = (AnimationDrawable) IVbrainAnim4.getBackground();
     *//*   AniFrame5 = (AnimationDrawable) IVbrainAnim5.getBackground();*//*

        new CountDownTimer(300, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                AniFrame.start();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                AniFrame2.start();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        new CountDownTimer(500, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                AniFrame3.start();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        new CountDownTimer(1600, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here

                AniFrame4.start();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();*/


      /*  AniFrame5.start();*/

       final Date toDay=Date.valueOf(df.format(calendar.getTime()));
        Button btnDailyTest = (Button) findViewById(R.id.btn_daily_test);
        btnDailyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(free_last.before(toDay) || pm.getGifted()) {
                    pm.setGifted(false);
                    Intent intentRandom = new Intent(MainActivity2.this, QandAActivity.class);
                /*intentRandom.putExtra("btnId",1);*/
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_btnId2", "1");
                    intentRandom.putExtras(extras);
                    startActivity(intentRandom);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
                else
                {
                    final EditText txtRev = new EditText(getApplicationContext());
                    txtRev.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new AlertDialog.Builder(MainActivity2.this)
                            .setTitle("Do you have an Invitation code?")
                            .setMessage("Enter code here")
                            .setView(txtRev)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if(txtRev.getText().toString().length()<7)
                                    {
                                        Toast.makeText(getApplicationContext(), "Invalid code format!!",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        i_code = Integer.valueOf(txtRev.getText().toString());
                                        datain = false;
                                        networkCheck();//for checking entered code
                                        //if yes, intent to introactivity
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                }
            }
        });
        Button btn_sst = (Button) findViewById(R.id.btn_ss_test);
        btn_sst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (false){//sst_end.before(toDay)) {
                    String payload = "";

                    mHelper.launchPurchaseFlow(MainActivity2.this,PLAY_SST, RC_REQUEST,
                            mPurchaseFinishedListener, payload);

                }
                else
                {
                    Intent intentBAA = new Intent(MainActivity2.this, SubSelActivity.class);
                    startActivity(intentBAA);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);


                    //goto play payment
                }
                    }
                });



                Button btn_baa = (Button) findViewById(R.id.btn_baa2);
                btn_baa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                     if (false){//(aat_end.before(toDay)) {
                         String payload = "";

                         mHelper.launchPurchaseFlow(MainActivity2.this,PLAY_AAT, RC_REQUEST,
                                 mPurchaseFinishedListener, payload);

                     }
                        else
                        {
                            Intent intentBAA = new Intent(MainActivity2.this, SubSelActivity2.class);
                            startActivity(intentBAA);
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);


                            //goto play payment
                        }
                    }
                });
        if(pm.getNetBool()) {
            networkCheck();
            pm.setNetBool(false);
        }
        else
        {
            date_db.open();
            Cursor cDates=date_db.getColsById(1);
            if(cDates.moveToFirst()) {
                sst_end = Date.valueOf(cDates.getString(0));
                aat_end = Date.valueOf(cDates.getString(1));
                free_last = Date.valueOf(cDates.getString(2));
            }
            date_db.close();

        }

}
  @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "Setting screen name: " + "MainActivity");
        mTracker.setScreenName("Image~" + "MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }
    public void getQid()
    {
        Cursor c;
        try{
           // c= dbQod.getRandom();
            qid=pm.getQodid();
            c=dbQod.getColsById(Long.valueOf(qid));
            if (c.moveToFirst()) {
               // qid = c.getString(0);
                qstn = c.getString(5);
                // qDate=c.getString(17);
           }
        }
       catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Get qid error:  "+e.getMessage(),Toast.LENGTH_LONG).show();
            qid ="8";
            qstn ="No questions available, please restart the app ";
        }
       // pm.setQodId(qid);
        pm.setQodSet(true);
    }
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */


            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing


          //  updateUi();
           // setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
               // setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                //setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(PLAY_SST)) {
                catst="sst_st_d";
                catend="sst_end_d";
                paid=true;
                networkCheck();
            }
            if (purchase.getSku().equals(PLAY_AAT)) {
                catst="baa_st_d";
                catend="baa_end_d";
                paid=true;
                networkCheck();
            }
          /*  else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }*/

        }
    };
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    private void initialise()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        red_button = (TextView) findViewById(R.id.btn_daily_test);
        red_button.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        green_button = (TextView) findViewById(R.id.btn_ss_test);
        green_button.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        yellow_button = (TextView) findViewById(R.id.btn_baa2);
        yellow_button.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        blue_button = (TextView) findViewById(R.id.btn_inst2);
        blue_button.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_medium));
        board_note = (TextView)findViewById(R.id.etQodPad);
        board_note.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_arial));





    }
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }
   /* @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            pm.clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.disappear_bottom_right_in, R.anim.disappear_bottom_right_out);

            return true;
        }

        if (id == R.id.action_notifications) {
          //  mSlidingMenu.toggleLeftDrawer();
            Intent intent=new Intent(MainActivity2.this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.layout.activity_main_activity2);
            return true;
        }
        if (id == R.id.action_recommend) {

            long x = 1000000L;
            long y = 9999999L;
            Random r = new Random();
             numb = x+((long)(r.nextDouble()*(y-x)));
            recom=true;
            networkCheck();
            try
            { Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "BuzzQuiz");
                String sAux = "\nTry out BussQuiz, keep improving yourself!\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.quizapp.hp.quiz \n\n"+"Your gift code for attempting an extra daily special test is : "+numb+"( Applicable when attempting special daily quiz second time in a day.) . Gift code is valid only for 7 days, Don't miss it!! ";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            }
            catch(Exception e)
            { //e.toString();
            }
            recom2=true;
            networkCheck();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        tap++;
        if(tap>1)
        {
        /* android.os.Process.killProcess(android.os.Process.myPid());
           // System.exit(1);
            super.onDestroy();*/
            moveTaskToBack(true);
        }
        else
        Toast.makeText(getApplicationContext(), "Tap back again to exit",Toast.LENGTH_LONG).show();

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
/*
            Intent intent=new Intent(MainActivity2.this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.layout.activity_main_activity2);*/
        }
        if(direction==SimpleGestureFilter.SWIPE_RIGHT)
        {

            Intent intent=new Intent(MainActivity2.this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        }
    }
public void gotoIntro()
{
    Intent intentRandom = new Intent(MainActivity2.this,IntroActivity.class);
    startActivity(intentRandom);
    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
}
    @Override
    public void onDoubleTap() {

    }

    @Override
    public void receivedBroadcast() {

    }

    public class task extends AsyncTask<String, String, Void>
    {
        InputStream is1 = null;
        String paid_result = "";
        String qod_result = "";




        protected void onPreExecute() {
            progressDialog= new ProgressDialog(MainActivity2.this);
           /* progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(true);

        }
        @Override
        protected Void doInBackground(String... params) {
            pm=new PrefManager(getApplicationContext());
           int uid= pm.getUid();
            String phno=pm.getMobileNumber();
            String paid_url_select = "http://gokera.phacsin.com/q_date_check.php?table_name=user_table&uid="+uid;
            String inv_chk= "http://gokera.phacsin.com/inv_check.php?code="+numb;
            String payComplete="http://gokera.phacsin.com/pay_ins.php?uid="+uid+"&catst="+catst+"&catend="+catend;
            String recomReq="http://gokera.phacsin.com/gift_code.php?gcode="+numb;
            String recomCheck="http://gokera.phacsin.com/inv_verify.php?gcode="+i_code;

            try {
                if(recom){
                    paid_result= getJSONfromURL(inv_chk);
                    recom=false;
                    JSONArray array1 = new JSONArray(paid_result);

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                        numb =Long.valueOf( obj.getString("newCode"));
                    }
                }
                if(recom2){
                    paid_result= getJSONfromURL(recomReq);
                    recom2=false;
                }
                if(paid)
                {
                    paid_result = getJSONfromURL(payComplete);
                    paid=false;
                }
                if(datain) {
                    paid_result = getJSONfromURL(paid_url_select);
                   if (paid_result != null)
                        progressDialog.setProgress(100);

                    date_db.open();
                    JSONArray array1 = new JSONArray(paid_result);

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                        sst_end = Date.valueOf(obj.getString("sst_end_d"));
                        aat_end = Date.valueOf(obj.getString("baa_end_d"));
                        free_last = Date.valueOf(obj.getString("ldfq"));
                        date_db.updateRow(1,obj.getString("sst_end_d"),obj.getString("baa_end_d"),obj.getString("ldfq"));
                    }
                    date_db.close();
                    datain=true;
                }
                else
                {
                    paid_result = getJSONfromURL(recomCheck);

                   paid_result="["+paid_result.trim()+"]";
                   // test2=paid_result;
                    if (paid_result != null) {
                       /* progressDialog.setTitle("Checking the code..");
                       progressDialog.setProgress(100);*/

                        JSONArray array1 = new JSONArray(paid_result);
                        test2=array1.toString();
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(0)));
                           cd_ver= obj.getString("verified");
                           if(cd_ver.equals("true"))
                            {
                            pm.setGifted(true);
                                gotoIntro();

                            }
                            else
                            {
                                toastBool=true;
                                res="Code not found or Code expired!!";//cannot use, toast will always come
                            }
                        }

                       /* Intent intentRandom = new Intent(MainActivity2.this, QandAActivity.class);
                *//*intentRandom.putExtra("btnId",1);*//*
                        Bundle extras = new Bundle();
                        extras.putString("EXTRA_btnId2", "1");
                        intentRandom.putExtras(extras);
                        startActivity(intentRandom);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/
                    }
                    else
                    {
                        progressDialog.setTitle("Checking the code..");
                        progressDialog.setProgress(100);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity2.this);
                        alertDialog.setTitle("Oops!");
                        alertDialog.setMessage("Code entered is invalid!");
                        alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub


                            }
                        });

                        alertDialog.show();
                    }

                }


                progressDialog.setProgress(1800);

            }


            catch (JSONException e1) {
                Log.d("log_tag", e1.toString());
                err1=e1.toString();
                e1.printStackTrace();
            }
            catch (Exception e1)
            {
                err2=e1.toString();
                e1.printStackTrace();
            }

            // dbQod.close();
            return null;


        }




        protected void onPostExecute(Void v) {
            if(progressDialog!=null)
               progressDialog.dismiss();
            if(toastBool){
                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
            toastBool=false;
            }

        /*
            Toast.makeText(getApplicationContext(),String.valueOf(sst_end)+String.valueOf(aat_end)+String.valueOf(free_last),Toast.LENGTH_LONG).show();
           */
          //  Toast.makeText(getApplicationContext(),paid_result,Toast.LENGTH_LONG).show();
    // Toast.makeText(getApplicationContext(),test2+cd_ver,Toast.LENGTH_LONG).show();
         //   Toast.makeText(getApplicationContext(),"Json exception: "+err1+ "verum Exception: "+err2,Toast.LENGTH_LONG).show();
            DBHandler collegeDBHandler = new DBHandler(getApplicationContext());
            if(collegeDBHandler.getallrowcount()==1598) {
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity2.this);
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


                nDialog = new ProgressDialog(MainActivity2.this);
                nDialog.setTitle("Checking Network");
                nDialog.setMessage("Loading..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
            if(!recom || !recom2 ) {
                nDialog.show();
            }
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
