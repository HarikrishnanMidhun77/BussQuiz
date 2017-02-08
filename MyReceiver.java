package com.quizapp.hp.quiz;

/**
 * Created by VishnuMidhun on 02-11-2015.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver{
    private NotificationManager mManager;
    @Override
    public void onReceive(Context context, Intent intent) {
       // Toast.makeText(MainActivity2.class, "Correct Answer ", Toast.LENGTH_SHORT).show();
      /*  Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);*/
       NotCls notCls=new NotCls();
        notCls.notific(context,intent);
    }
}
