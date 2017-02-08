package com.quizapp.hp.quiz;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.security.Provider;

/**
 * Created by VishnuMidhun on 03-11-2015.
 */
public class NotCls {
    private NotificationManager mManager;

    public void notific(Context context, Intent intent) {
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,IntroActivity.class);

        // Notification notification = new Notification(R.drawable.gokera_icon,"Face gokera challenge of the day!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.gokera_icon)
                .setContentTitle("Gokera daily challenge ")
                .setContentText("Face gokera challenge of the day!")
                .setContentIntent(pendingNotificationIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);
        //notification.flag |= Notification.FLAG_AUTO_CANCEL;
      //  notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL
        //  notification.setLatestEventInfo(this.getApplicationContext(), "Gokera Challenge", "Face gokera challenge of the day!", pendingNotificationIntent);
        mManager.notify(0, mBuilder.build());

    }

}