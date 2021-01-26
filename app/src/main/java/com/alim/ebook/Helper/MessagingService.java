package com.alim.ebook.Helper;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alim.ebook.Activities.AboutActivity;
import com.alim.ebook.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), intent);
    }

    public void showNotification(String title, String message, Intent intent){
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 999, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"ebook")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ebook)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
