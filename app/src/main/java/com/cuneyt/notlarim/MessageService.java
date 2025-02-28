package com.cuneyt.notlarim;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

public class MessageService extends FirebaseMessagingService {
    private NotificationCompat.Builder builder;

    private Context mContext;
    public MessageService() {
        
    }

    public void setContext(Context context){
        mContext = context.getApplicationContext();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String baslik = message.getNotification() != null ? message.getNotification().getTitle() : "Yeni Bildirim";
        String icerik = message.getNotification() != null ? message.getNotification().getBody() : "Mesaj yok";

        sendMessage(baslik, icerik);

    }

    public void sendMessage(String baslik, String icerik){
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String kanalId = "123";
            String kanalAd = "kanalAd";
            String kanalTanim = "kanalTanim";
            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel kanal = notificationManager.getNotificationChannel(kanalId);

            if (kanal == null){
                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanim);
                notificationManager.createNotificationChannel(kanal);
            }

            builder = new NotificationCompat.Builder(mContext, kanalId);
            builder.setContentTitle(baslik);
            builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

        } else {
            builder = new NotificationCompat.Builder(mContext);
            builder.setContentTitle(baslik);
            builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentIntent(pendingIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        notificationManager.notify(1, builder.build());
    }
}
