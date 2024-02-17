package com.akp.vtalkanoop.Firebase;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.akp.vtalkanoop.Home.WalletScreen;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class IncomingInvitationActivity extends AppCompatActivity {
    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;
    String time;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };

    String meetingType;
    //    private String meetingType = null;
    Ringtone currentRingtone;
    SharedPreferences sharedPreferences, wallet_Preferences;
    String UserId, RecieverId, Rate;
    String walletamount, call_rate, loginusertype;
    SharedPreferences login_preference;
    private double total_minut;
    SharedPreferences wallet_preference;
    ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        loginusertype = login_preference.getString("Usertype", "");
//        Toast.makeText(getApplicationContext(),loginusertype,Toast.LENGTH_LONG).show();
        sharedPreferences = getSharedPreferences("data_preference", MODE_PRIVATE);
//        UserId= sharedPreferences.getString("data_caller", "");
//        RecieverId= sharedPreferences.getString("data_reciever", "");
        call_rate = sharedPreferences.getString("data_callrate", "");
//        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
//            hangUp();
//        }
        requestPermission();
        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        meetingType = getIntent().getStringExtra(Constanta.REMOTE_MSG_MEETING_TYPE);
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_video);
            } else {
                imageMeetingType.setImageResource(R.drawable.ic_audio);
            }
        }
        //fetch default Ringtone
        Ringtone defaultRingtone = RingtoneManager.getRingtone(IncomingInvitationActivity.this,
                Settings.System.DEFAULT_RINGTONE_URI);
        //fetch current Ringtone
        Uri currentRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(IncomingInvitationActivity.this
                .getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        currentRingtone = RingtoneManager.getRingtone(IncomingInvitationActivity.this, currentRintoneUri);
        //display Ringtone title
//        output.setText(defaultRingtone.getTitle(IncomingInvitationActivity.this)+" and " +
//                "Current Ringtone:"+currentRingtone.getTitle(IncomingInvitationActivity.this));
        //play current Ringtone
        currentRingtone.play();


        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
        running = true;

        TextView textFirstChar = findViewById(R.id.textFirstChar);
        TextView textUserName = findViewById(R.id.textUserName);
        TextView textEmail = findViewById(R.id.textEmail);

        String firstName = getIntent().getStringExtra(Constanta.KEY_FIRST_NAME);
        if (firstName != null) {
            textFirstChar.setText(firstName.substring(0, 1));
        }

        textUserName.setText(String.format(
                "%s",
                firstName
        ));
//        textUserName.setText(String.format(
//                "%s %s",
//                firstName,
//                getIntent().getStringExtra(Constanta.KEY_LAST_NAME)
//        ));
        UserId = getIntent().getStringExtra(Constanta.KEY_Caller_ID);
        RecieverId = getIntent().getStringExtra(Constanta.KEY_Reciever_ID);
        Rate = getIntent().getStringExtra(Constanta.KEY_Rate);
//        Toast.makeText(getApplicationContext(),""+UserId+RecieverId,Toast.LENGTH_LONG).show();

        String otp1 = new GlobalAppApis().SettingApi(UserId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getSetting(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        walletamount = jsonObject1.getString("WalletAmount");
                        total_minut = Double.parseDouble(walletamount) / Double.parseDouble(Rate);
//                        Toast.makeText(getApplicationContext(),"yy"+total_minut+Rate,Toast.LENGTH_LONG).show();
                        double DELAY = total_minut * 60000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                running = false;
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                                LocalBroadcastManager.getInstance(IncomingInvitationActivity.this).registerReceiver(broadcastReceiver, intentFilter);
//                    Toast.makeText(getApplicationContext(),"Total Call Duration"+time,Toast.LENGTH_LONG).show();
                                if (meetingType.equals("audio")) {
                                    CallingServiceAPI(UserId, RecieverId, "Done", "VoiceCall", time);
//                        Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VoiceCall"+time);
                                } else {
                                    CallingServiceAPI(UserId, RecieverId, "Done", "VideoCall", time);
//                        Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VideoCall"+time);
                                }
                                Toast.makeText(getApplicationContext(), "You have insufficient balance", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), WalletScreen.class);
                                startActivity(intent);
                            }
                        }, (long) DELAY);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, IncomingInvitationActivity.this, call1, "", true);


//     check if urserid doesnot exit then cancel invitataion
        if (UserId.equalsIgnoreCase("null")) {
        } else {
            ImageView imageAcceptInvitation = findViewById(R.id.imageAcceptInvitation);
            imageAcceptInvitation.setOnClickListener(view -> sendInvitationResponse(
                    Constanta.REMOTE_MSG_INVITATION_ACCEPTED,
                    getIntent().getStringExtra(Constanta.REMOTE_MSG_INVITER_TOKEN)));

            ImageView imageRejectInvitation = findViewById(R.id.imageRejectInvitation);
            imageRejectInvitation.setOnClickListener(view -> sendInvitationResponse(
                    Constanta.REMOTE_MSG_INVITATION_REJECTED,
                    getIntent().getStringExtra(Constanta.REMOTE_MSG_INVITER_TOKEN)
            ));
        }

    }

    private void sendInvitationResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);
            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();
            data.put(Constanta.REMOTE_MSG_TYPE, Constanta.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constanta.REMOTE_MSG_INVITATION_RESPONSE, type);
            body.put(Constanta.REMOTE_MSG_DATA, data);
            body.put(Constanta.REMOTE_MSG_REGISTRATION_IDS, tokens);
            sendRemoteMessage(body.toString(), type);
//            for ringtone
            currentRingtone.stop();

            //    condition auto cut when 60min done
            double DELAY = 60 * 60000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    running = false;
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                    LocalBroadcastManager.getInstance(IncomingInvitationActivity.this).registerReceiver(broadcastReceiver, intentFilter);
//                    Toast.makeText(getApplicationContext(),"Total Call Duration"+time,Toast.LENGTH_LONG).show();
                    if (meetingType.equals("audio")) {
                        CallingServiceAPI(UserId, RecieverId, "Done", "VoiceCall", time);
//                        Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VoiceCall"+time);
                    } else {
                        CallingServiceAPI(UserId, RecieverId, "Done", "VideoCall", time);
//                        Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VideoCall"+time);
                    }
                    Toast.makeText(getApplicationContext(), "You have insufficient balance", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), WalletScreen.class);
                    startActivity(intent);
                }
            }, (long) DELAY);

//    condition auto cut when 60min done


//            if (meetingType.equals("audio")) {
//                CallingServiceAPI(UserId,RecieverId,"Accepted","VoiceCall","0");
//            }
//            CallingServiceAPI(UserId,RecieverId,"Accepted","VideoCall","0");

        } catch (Exception e) {
            CallingServiceAPI(UserId, RecieverId, "Rejected", "VoiceCall", "0");
            CallingServiceAPI(UserId, RecieverId, "Rejected", "VideoCall", "0");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constanta.getRemoteMessageHeaders(), remoteMessageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constanta.REMOTE_MSG_INVITATION_ACCEPTED)) {
                        try {
                            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setMode(AudioManager.MODE_IN_CALL);
                            audioManager.setSpeakerphoneOn(true);
                            registerForBroadcastMessages();
                            if (meetingType.equalsIgnoreCase("audio")) {
                                Intent intent = new Intent(IncomingInvitationActivity.this, CallActivity.class);
                                intent.putExtra("callID", UserId);
                                intent.putExtra("userName", UserId);
                                intent.putExtra("RecieverId", RecieverId);
                                intent.putExtra("rate", call_rate);
                                intent.putExtra("call_time", time);
                                startActivity(intent);
                                finish();
                                CallingServiceAPI(UserId, RecieverId, "Accepted", "VoiceCall", "0");

                               /* requestPermissionIfNeeded(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), new RequestCallback() {
                                    @Override
                                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                        if (allGranted) {
                                            Intent intent = new Intent(IncomingInvitationActivity.this, CallActivity.class);
                                            intent.putExtra("callID", UserId);
                                            intent.putExtra("userName", UserId);
                                            intent.putExtra("RecieverId", RecieverId);
                                            intent.putExtra("rate", call_rate);
                                            intent.putExtra("call_time", time);
                                            startActivity(intent);
                                            finish();
                                            CallingServiceAPI(UserId, RecieverId, "Accepted", "VoiceCall", "0");
                                        } else {
                                            Toast.makeText(IncomingInvitationActivity.this, "Some permissions have not been granted.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/




                            } else {
                                Intent intent = new Intent(IncomingInvitationActivity.this, AVideoCallActivity.class);
                                intent.putExtra("callID", UserId);
                                intent.putExtra("userName", UserId);
                                intent.putExtra("RecieverId", RecieverId);
                                intent.putExtra("rate", call_rate);
                                intent.putExtra("call_time", time);
                                startActivity(intent);
                                finish();
                                CallingServiceAPI(UserId, RecieverId, "Accepted", "VideoCall", "0");
                                finish();
                               /* requestPermissionIfNeeded(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), new RequestCallback() {
                                    @Override
                                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                        if (allGranted) {
                                            Intent intent = new Intent(IncomingInvitationActivity.this, AVideoCallActivity.class);
                                            intent.putExtra("callID", UserId);
                                            intent.putExtra("userName", UserId);
                                            intent.putExtra("RecieverId", RecieverId);
                                            intent.putExtra("rate", call_rate);
                                            intent.putExtra("call_time", time);
                                            startActivity(intent);
                                            finish();
                                            CallingServiceAPI(UserId, RecieverId, "Accepted", "VideoCall", "0");
                                            finish();
                                        } else {
                                            Toast.makeText(IncomingInvitationActivity.this, "Some permissions have not been granted.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                            }

//                            Intent intent = new Intent(IncomingInvitationActivity.this, AVideoCallActivity.class);
//                            intent.putExtra("callID", UserId);
//                            intent.putExtra("userName", UserId); //demo
//                            intent.putExtra("RecieverId", RecieverId);
//                            intent.putExtra("rate", call_rate);
//                            intent.putExtra("call_time", time);
//                            startActivity(intent);
//                            finish();
//                            CallingServiceAPI(UserId, RecieverId, "Accepted", "VideoCall", "0");
//                            finish();
                           /* requestPermissionIfNeeded(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), new RequestCallback() {
                                @Override
                                public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                    if (allGranted) {
                                        Intent intent = new Intent(IncomingInvitationActivity.this, AVideoCallActivity.class);
                                        intent.putExtra("callID", UserId);
                                        intent.putExtra("userName", UserId); //demo
                                        intent.putExtra("RecieverId", RecieverId);
                                        intent.putExtra("rate", call_rate);
                                        intent.putExtra("call_time", time);
                                        startActivity(intent);
                                        finish();
                                        CallingServiceAPI(UserId, RecieverId, "Accepted", "VideoCall", "0");
                                        finish();
                                    } else {
                                        Toast.makeText(IncomingInvitationActivity.this, "Some permissions have not been granted.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/

                            /*if (meetingType.equals("audio")) {
                                Intent intent = new Intent(IncomingInvitationActivity.this, CallActivity.class);
                                intent.putExtra("callID", UserId);
                                intent.putExtra("username", "Demo");
                                intent.putExtra("RecieverId", RecieverId);
                                intent.putExtra("rate", call_rate);
                                startActivity(intent);
                                finish();
                                //builder.setVideoMuted(true);
                                CallingServiceAPI(UserId,RecieverId,"Accepted","VoiceCall","0");
                            }*/
                            //CallingServiceAPI(UserId,RecieverId,"Accepted","VideoCall","0");
                            //JitsiMeetActivity.launch(IncomingInvitationActivity.this, builder.build());
                            //finish();

                            /*URL serverURL = new URL("https://meet.jit.si");
                            JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder();
                            builder.setServerURL(serverURL);

                            builder.setWelcomePageEnabled(false);*/
//                            builder.setFeatureFlag("meeting-password.enabled",  false);
//                            builder.setFeatureFlag("live-streaming.enabled",  false);
//                            builder.setFeatureFlag("raise-hand.enabled",  false);
//                            builder.setFeatureFlag("chat.enabled",false);
//                            builder.setFeatureFlag("prejoinpage.enabled", false);
//                            2nd module add code only dono page par incoming outgoing

//                                    builder.setFeatureFlag("add-people.enabled", false)
//                                    .setFeatureFlag("calendar.enabled", false)
//                                    .setFeatureFlag("close-captions.enabled", false)
//                                    .setFeatureFlag("chat.enabled", false)
//                                    .setFeatureFlag("invite.enabled", false)
//                                    .setFeatureFlag("resolution", 480)
//                                    .setFeatureFlag("live-streaming.enabled", false)
//                                    .setFeatureFlag("meeting-name.enabled", false)
//                                    .setFeatureFlag("call-integration.enabled", false)
//                                    .setFeatureFlag("meeting-password.enabled", false)
//                                    .setFeatureFlag("recording.enabled", false)
//                                    .setFeatureFlag("pip.enabled", false)
//                                    .setFeatureFlag("filmstrip.enabled", false)
//                                    .setFeatureFlag("video-mute.enabled", false)
//                                    .setFeatureFlag("audio-mute.enabled", false)
//                                    .setFeatureFlag("video-share.enabled", false)
//                                    .setFeatureFlag("tile-view.enabled", false) .setFeatureFlag("raise-hand.enabled", false)
//                                    .setFeatureFlag("security-options.enabled", false);

//                            2nd module add code only dono page par incoming outgoing


//                            AudioManager audioManager =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//                            audioManager.setMode(AudioManager.MODE_IN_CALL);
//                            audioManager.setSpeakerphoneOn(true);

                           /* registerForBroadcastMessages();
                            builder.setRoom(getIntent().getStringExtra(Constanta.REMOTE_MSG_MEETING_ROOM));
                            if (meetingType.equals("audio")) {
                                builder.setVideoMuted(true);
                                CallingServiceAPI(UserId,RecieverId,"Accepted","VoiceCall","0");
                            }
                            CallingServiceAPI(UserId,RecieverId,"Accepted","VideoCall","0");
                            JitsiMeetActivity.launch(IncomingInvitationActivity.this, builder.build());
                            finish();*/
                        } catch (Exception e) {
                            CallingServiceAPI(UserId, RecieverId, "Rejected", "VoiceCall", "0");
                            CallingServiceAPI(UserId, RecieverId, "Rejected", "VideoCall", "0");
                            Toast.makeText(IncomingInvitationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else if (type.equals(Constanta.REMOTE_MSG_INVITATION_REJECTED)) {
                        if (meetingType.equals("audio")) {
                            CallingServiceAPI(UserId, RecieverId, "Rejected", "VoiceCall", "0");
                        }
                        CallingServiceAPI(UserId, RecieverId, "Rejected", "VideoCall", "0");
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (meetingType.equals("audio")) {
                            CallingServiceAPI(UserId, RecieverId, "Rejected", "VoiceCall", "0");
                        }
                        CallingServiceAPI(UserId, RecieverId, "Rejected", "VideoCall", "0");
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }


            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(IncomingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constanta.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constanta.REMOTE_MSG_INVITATION_CANCELLED)) {
                    if (meetingType.equals("audio")) {
                        CallingServiceAPI(UserId, RecieverId, "Rejected", "VoiceCall", "0");
                    }
                    CallingServiceAPI(UserId, RecieverId, "Rejected", "VideoCall", "0");
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }

 /*  if (type.equals(Constanta.REMOTE_MSG_INVITATION_CANCELLED)) {
//                    if (meetingType.equals("audio")) {
//                        CallingServiceAPI(UserId,RecieverId,"Done","VoiceCall","01:00");
//                    }
//                    CallingServiceAPI(UserId,RecieverId,"Done","VideoCall","01:00");
                }*/
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(invitationResponseReceiver, new IntentFilter(Constanta.REMOTE_MSG_INVITATION_RESPONSE));
        Toast.makeText(getApplicationContext(), "calling....", Toast.LENGTH_LONG).show();
//call aane par open ho rha
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

  /*  @Override
    public void onDestroy() {
        super.onDestroy();
        JitsiMeetActivityDelegate.onHostDestroy(this);
    }*/


//    @Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
//                invitationResponseReceiver,new IntentFilter(Constanta.REMOTE_MSG_INVITATION_CANCELLED));
//        if (meetingType.equals("audio")) {
//            CallingServiceAPI(UserId,RecieverId,"Done","VoiceCall","00:59");
//        }
//        CallingServiceAPI(UserId,RecieverId,"Done","VideoCall","00:59");
//
//        Intent intent=new Intent(getApplicationContext(),CallSummeryActivity.class);
//        startActivity(intent);
//    }


//    @Override
//    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
//
//        super.onDestroy();
//    }


    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();
        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
         */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    // Example for handling different JitsiMeetSDK events
    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);
            switch (event.getType()) {
                case CONFERENCE_JOINED:
//                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
                    break;
                case PARTICIPANT_JOINED:
//                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;
                case CONFERENCE_TERMINATED:
//                    Toast.makeText(getApplicationContext(),"Call Ended!",Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(),"Call Ended!"+event.getData(),Toast.LENGTH_LONG).show();
                    break;
                case PARTICIPANT_LEFT:
                    Toast.makeText(getApplicationContext(), "Call Ended!", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(),"Call Ended"+event.getData(),Toast.LENGTH_LONG).show();
                    hangUp();
                    break;
            }
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private void hangUp() {
//        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//        Toast.makeText(getApplicationContext(),"Anoop Pandey",Toast.LENGTH_LONG).show();
//        Log.d("desffg","wewe"+UserId+RecieverId+"Done"+"VoiceCall"+"01:00");
//        Log.d("desffg23424","wewe"+time);
        running = false;
//        Toast.makeText(getApplicationContext(),"Total Call Duration"+time,Toast.LENGTH_LONG).show();
        if (meetingType.equals("audio")) {
            CallingServiceAPI(UserId, RecieverId, "Done", "VoiceCall", time);
//            Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VoiceCall"+time);
        } else {
            CallingServiceAPI(UserId, RecieverId, "Done", "VideoCall", time);
//            Log.d("sdgshdgsdhsdg111343453","djhsgdjgh"+UserId+RecieverId+"Done"+"VideoCall"+time);
        }

    }


    public void CallingServiceAPI(String caller_acountid, String reciever_accountid, String status,
                                  String calling_type, String calling_time) {
        String otp1 = new GlobalAppApis().CallingApi(caller_acountid, reciever_accountid,
                status, calling_type, calling_time);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCalling(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                Log.d("sghghsgd", "sdjskdh" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String status = getDetails.getString("Status");
                        String msg = getDetails.getString("Msg");
                        if (msg.equalsIgnoreCase("Call Succesfully Done")) {
                            Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
                            Intent intent = new Intent(getApplicationContext(), CallSummeryActivity.class);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    JSONArray jsonArrayr1 = jsonObject1.getJSONArray("Response");
                    Toast.makeText(getApplicationContext(),"fgdfgfdgdfghdf"+jsonArrayr1,Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArrayr1.length(); i++) {
                        JSONObject jsonObject11 = jsonArrayr1.getJSONObject(i);
                        Toast.makeText(getApplicationContext(),"fgdfgfdgdfghdf"+jsonObject11.getString("Msg"),Toast.LENGTH_LONG).show();
                        if (jsonObject11.getString("Msg").equalsIgnoreCase("Call Succesfully done")){
                          Intent intent=new Intent(getApplicationContext(),CallSummeryActivity.class);
                          startActivity(intent);
                      }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, IncomingInvitationActivity.this, call1, "", true);
    }


    // Save the state of the stopwatch
    // if it's about to be destroyed.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }


    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                time = String.format(Locale.getDefault(),
                        "%02d:%02d", minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void requestPermission() {
        String[] permissionNeeded = {
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO"};
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionNeeded, 101);
        }
    }


/*
    private void requestPermissionIfNeeded(List<String> permissions, RequestCallback requestCallback) {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }
        if (allGranted) {
            requestCallback.onResult(true, permissions, new ArrayList<>());
            return;
        }

        PermissionX.init(this).permissions(permissions).onExplainRequestReason((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.permission_explain_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.permission_explain_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.permission_explain_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.permission_explain_mic);
                    }
                } else {
                    message = this.getString(R.string.permission_explain_camera_mic);
                }
            }
            scope.showRequestReasonDialog(deniedList, message, getString(R.string.ok));
        }).onForwardToSettings((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.settings_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.settings_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.settings_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.settings_mic);
                    }
                } else {
                    message = this.getString(R.string.settings_camera_mic);
                }
            }
            scope.showForwardToSettingsDialog(deniedList, message, getString(R.string.settings),
                    getString(R.string.cancel));
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                 @NonNull List<String> deniedList) {
                if (requestCallback != null) {
                    requestCallback.onResult(allGranted, grantedList, deniedList);
                }
            }
        });
    }*/
}

