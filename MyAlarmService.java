package com.quizapp.hp.quiz;

/**
 * Created by VishnuMidhun on 02-11-2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyAlarmService extends Service{
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        Toast.makeText(getApplicationContext(), "Correct Answer ",Toast.LENGTH_SHORT).show();
      /*  mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity2.class);

       // Notification notification = new Notification(R.drawable.gokera_icon,"Face gokera challenge of the day!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.gokera_icon)
                .setContentTitle("Gokera daily challenge ")
                .setContentText("Face gokera challenge of the day!")
                .setContentIntent(pendingNotificationIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
       //notification.flag |= Notification.FLAG_AUTO_CANCEL;
      //  notification.setLatestEventInfo(this.getApplicationContext(), "Gokera Challenge", "Face gokera challenge of the day!", pendingNotificationIntent);
        mManager.notify(0, mBuilder.build());*/
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
