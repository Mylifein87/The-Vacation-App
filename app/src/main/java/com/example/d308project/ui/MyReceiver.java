package com.example.d308project.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.d308project.R;

public class MyReceiver extends BroadcastReceiver {

    String channel_id = "test";
    static int notificationID;

    @SuppressLint("NotificationPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String title = null;
        String contentText = null;

        if (action != null) {

            switch (action) {

                case "exc action": // Excursion alert
                    Toast.makeText(context,
                            intent.getStringExtra("key"),
                            Toast.LENGTH_LONG).show();

                    title = "Excursion Alert";
                    contentText = intent.getStringExtra("key");
                    break;

                case "start action": // Vacation start
                    Toast.makeText(context,
                            intent.getStringExtra("start key"),
                            Toast.LENGTH_LONG).show();

                    title = "Vacation Start Alert";
                    contentText = intent.getStringExtra("start key");
                    break;

                case "end action": // Vacation end
                    Toast.makeText(context,
                            intent.getStringExtra("end key"),
                            Toast.LENGTH_LONG).show();

                    title = "Vacation End Alert";
                    contentText = intent.getStringExtra("end key");
                    break;
            }
        }

        createNotificationChannel(context, channel_id);

        Notification notification =
                new NotificationCompat.Builder(context, channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)   // REQUIRED
                        .setContentTitle(title)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationID++, notification);
    }

    private void createNotificationChannel(Context context, String channel_id) {

        CharSequence name = "Vacation Alerts";
        String description = "Vacation and Excursion Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(channel_id, name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }
}
