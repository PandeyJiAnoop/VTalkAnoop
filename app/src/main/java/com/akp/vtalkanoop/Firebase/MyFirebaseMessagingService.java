package com.akp.vtalkanoop.Firebase;



import android.content.Intent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.akp.vtalkanoop.Home.WalletScreen;
import com.akp.vtalkanoop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.akp.vtalkanoop.Firebase.IncomingInvitationActivity;
import com.akp.vtalkanoop.Firebase.Constanta;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constanta.REMOTE_MSG_TYPE);

        if (type != null) {
            if (type.equals(Constanta.REMOTE_MSG_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomingInvitationActivity.class);
                intent.putExtra(
                        Constanta.REMOTE_MSG_MEETING_TYPE,
                        remoteMessage.getData().get(Constanta.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(
                        Constanta.KEY_FIRST_NAME,
                        remoteMessage.getData().get(Constanta.KEY_FIRST_NAME)
                );
                intent.putExtra(
                        Constanta.KEY_LAST_NAME,
                        remoteMessage.getData().get(Constanta.KEY_LAST_NAME)
                );

                intent.putExtra(
                        Constanta.KEY_Caller_ID,
                        remoteMessage.getData().get(Constanta.KEY_Caller_ID)
                );
                intent.putExtra(
                        Constanta.KEY_Reciever_ID,
                        remoteMessage.getData().get(Constanta.KEY_Reciever_ID)
                );
                intent.putExtra(
                        Constanta.KEY_Rate,
                        remoteMessage.getData().get(Constanta.KEY_Rate)
                );

                intent.putExtra(
                        Constanta.REMOTE_MSG_INVITER_TOKEN,
                        remoteMessage.getData().get(Constanta.REMOTE_MSG_INVITER_TOKEN)
                );
                intent.putExtra(
                        Constanta.REMOTE_MSG_MEETING_ROOM,
                        remoteMessage.getData().get(Constanta.REMOTE_MSG_MEETING_ROOM)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (type.equals(Constanta.REMOTE_MSG_INVITATION_RESPONSE)){
                Intent intent = new Intent(Constanta.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(
                        Constanta.REMOTE_MSG_INVITATION_RESPONSE,
                        remoteMessage.getData().get(Constanta.REMOTE_MSG_INVITATION_RESPONSE)
                );
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
    }
}

