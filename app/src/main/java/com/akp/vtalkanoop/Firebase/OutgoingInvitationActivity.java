package com.akp.vtalkanoop.Firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.log.JitsiMeetLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OutgoingInvitationActivity extends AppCompatActivity  {
    private PreferenceManager preferenceManager;
    private String inviterToken = null;
    private String meetingRoom  = null;
    private String meetingType  = null;
    private int rejectionCount = 0;
    private int totalReceivers = 0;
    String token,name,profileimage;
    CircleImageView textFirstChar;
    TextView textUserName;
    String UserId,RecieverId,ChatRate;
    SharedPreferences caller_preference;
    SharedPreferences.Editor caller_editor;
    SharedPreferences data_preference;
    SharedPreferences.Editor data_editor;
    MediaPlayer ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);
        textFirstChar=findViewById(R.id.textFirstChar);
        textUserName=findViewById(R.id.textUserName);
        preferenceManager = new PreferenceManager(getApplicationContext());
        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        meetingType = getIntent().getStringExtra("type");
        token = getIntent().getStringExtra("device_id");
        name = getIntent().getStringExtra("Astroname");
        profileimage = getIntent().getStringExtra("Astroimage");
        UserId = getIntent().getStringExtra("callerid");
        RecieverId = getIntent().getStringExtra("recieverid");
        ChatRate = getIntent().getStringExtra("chat_rate");
//        Toast.makeText(getApplicationContext(),UserId+RecieverId,Toast.LENGTH_LONG).show();

        textUserName.setText(name);
        Glide.with(getApplicationContext())
                .load(profileimage)
                .error(R.drawable.person).placeholder(R.drawable.loadinggif)
                .into(textFirstChar);

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_video);
            } else {
                imageMeetingType.setImageResource(R.drawable.ic_audio);
            }
        }
        requestPermission();

        ImageView imageStopInvitation = findViewById(R.id.imageStopInvitation);
        imageStopInvitation.setOnClickListener(view -> {
            if (getIntent().getBooleanExtra("isMultiple", false)) {
                Type type = new TypeToken<ArrayList<User>>(){}.getType();
                ArrayList<User> receivers = new Gson().fromJson(getIntent().getStringExtra("selectedUsers"), type);
                cancelInvitation( null, receivers);
            } else {
                if (token != null) {
                    cancelInvitation(token, null);
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                inviterToken = task.getResult().getToken();
                if (meetingType != null) {
                    if (getIntent().getBooleanExtra("isMultiple", false)) {
                        Type type = new TypeToken<ArrayList<User>>(){}.getType();
                        ArrayList<User> receivers = new Gson().fromJson(getIntent().getStringExtra("selectedUsers"), type);
                        if (receivers != null) {
                            totalReceivers = receivers.size();
                        }
                        initiateMeeting(meetingType, null, receivers);
                    } else {
                        if (token != null) {
                            totalReceivers = 1;
                            initiateMeeting(meetingType, token, null);
                        }
                    }
                }
            }
        });
    }

    private void initiateMeeting(String meetingType, String receiverToken, ArrayList<User> receivers) {
        try {
            JSONArray tokens = new JSONArray();
            if (receiverToken != null) {
                tokens.put(receiverToken);
            }
            if (receivers != null && receivers.size() > 0) {
                StringBuilder userNames = new StringBuilder();
                for (int i=0; i < receivers.size(); i++) {
                    tokens.put(receivers.get(i).token);
                    userNames.append(receivers.get(i).firstName).append(" ").append(receivers.get(i).lastName).append("\n");
                }
            }
            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();
            data.put(Constanta.REMOTE_MSG_TYPE, Constanta.REMOTE_MSG_INVITATION);
            data.put(Constanta.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constanta.KEY_FIRST_NAME, preferenceManager.getString(Constanta.KEY_FIRST_NAME));
            data.put(Constanta.KEY_LAST_NAME, preferenceManager.getString(Constanta.KEY_LAST_NAME));
            data.put(Constanta.KEY_Caller_ID, preferenceManager.getString(Constanta.KEY_Caller_ID));
            data.put(Constanta.KEY_Reciever_ID, preferenceManager.getString(Constanta.KEY_Reciever_ID));
            data.put(Constanta.KEY_Rate, preferenceManager.getString(Constanta.KEY_Rate));

            data.put(Constanta.REMOTE_MSG_INVITER_TOKEN, inviterToken);
            meetingRoom = preferenceManager.getString(Constanta.KEY_USER_ID) + "_" +
                    UUID.randomUUID().toString().substring(0, 5);
            data.put(Constanta.REMOTE_MSG_MEETING_ROOM, meetingRoom);
            body.put(Constanta.REMOTE_MSG_DATA, data);
            body.put(Constanta.REMOTE_MSG_REGISTRATION_IDS, tokens);
            sendRemoteMessage(body.toString(), Constanta.REMOTE_MSG_INVITATION);

        } catch (Exception e) {
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
                    if (type.equals(Constanta.REMOTE_MSG_INVITATION)) {
                        ring= MediaPlayer.create(OutgoingInvitationActivity.this,R.raw.outcall);
                        ring.setLooping(true);
                        ring.start();
                        Toast.makeText(OutgoingInvitationActivity.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                    } else if (type.equals(Constanta.REMOTE_MSG_INVITATION_RESPONSE)) {
                        ring.stop();
                        Toast.makeText(OutgoingInvitationActivity.this, "Invitation cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                        if (meetingType.equals("audio")) {
                            CallingServiceAPI(UserId,RecieverId,"Rejected","VoiceCall","0");
                        }
                        else {
                            CallingServiceAPI(UserId,RecieverId,"Rejected","VideoCall","0");            }
                    }
                    else {
                    }
                } else {
                    Toast.makeText(OutgoingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(OutgoingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } });
    }
    private void cancelInvitation(String receiverToken, ArrayList<User> receivers) {
        try {
            JSONArray tokens = new JSONArray();
            if (receiverToken != null) {
                tokens.put(receiverToken); }
            if (receivers != null && receivers.size() > 0) {
                for (User user : receivers) {
                    tokens.put(user.token);
                }}
            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();
            data.put(Constanta.REMOTE_MSG_TYPE, Constanta.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constanta.REMOTE_MSG_INVITATION_RESPONSE, Constanta.REMOTE_MSG_INVITATION_CANCELLED);
            body.put(Constanta.REMOTE_MSG_DATA, data);
            body.put(Constanta.REMOTE_MSG_REGISTRATION_IDS, tokens);
            sendRemoteMessage(body.toString(), Constanta.REMOTE_MSG_INVITATION_RESPONSE);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constanta.REMOTE_MSG_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constanta.REMOTE_MSG_INVITATION_ACCEPTED)) {
                    try {
                       /* URL serverURL = new URL("https://meet.jit.si");
                        JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder();
                        builder.setServerURL(serverURL);
                        builder.setWelcomePageEnabled(true);*/
//                        builder.setFeatureFlag("meeting-password.enabled",  false);
//                        builder.setFeatureFlag("live-streaming.enabled",  false);
//                        builder.setFeatureFlag("raise-hand.enabled",  false);
//                        builder.setFeatureFlag("chat.enabled",false);
//                        builder.setFeatureFlag("prejoinpage.enabled", false);
                        //builder.setRoom(meetingRoom);

  //                            2nd module add code only dono page par incoming outgoing
//                                builder.setFeatureFlag("add-people.enabled", false)
//                                .setFeatureFlag("calendar.enabled", false)
//                                .setFeatureFlag("close-captions.enabled", false)
//                                .setFeatureFlag("chat.enabled", false)
//                                .setFeatureFlag("invite. enabled",false)
//                                .setFeatureFlag("resolution", 480)
//                                .setFeatureFlag("live-streaming.enabled", false)
//                                .setFeatureFlag("meeting-name.enabled", false)
//                                .setFeatureFlag("call-integration.enabled", false)
//                                .setFeatureFlag("meeting-password.enabled", false)
//                                .setFeatureFlag("recording.enabled", false)
//                                .setFeatureFlag("pip.enabled", false)
//                                .setFeatureFlag("filmstrip.enabled", false)
//                                .setFeatureFlag("video-mute.enabled", false)
//                                .setFeatureFlag("audio-mute.enabled", false)
//                                .setFeatureFlag("video-share.enabled", false)
//                                .setFeatureFlag("tile-view.enabled", false)
//                                        .setFeatureFlag("security-options.enabled", false);

 //                            2nd module add code only dono page par incoming outgoing

                        ring.stop();
//                        builder.setVideoMuted(false);
                        if (meetingType.equals("audio")) {
                            Intent intent1 = new Intent(OutgoingInvitationActivity.this, CallActivity.class);
                            intent1.putExtra("callID", UserId);
                            intent1.putExtra("userName", UserId);
                            startActivity(intent1);
                            finish();
                           /* requestPermissionIfNeeded(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), new RequestCallback() {
                                @Override
                                public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                    if (allGranted) {
                                        //builder.setVideoMuted(true);
                                        Intent intent1 = new Intent(OutgoingInvitationActivity.this, CallActivity.class);
                                        intent1.putExtra("callID", UserId);
                                        intent1.putExtra("userName", UserId);
                                        startActivity(intent1);
                                        finish();
                                    } else {
                                        Toast.makeText(OutgoingInvitationActivity.this, "Some permissions have not been granted.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/



                        } else {
                            Intent intent1 = new Intent(OutgoingInvitationActivity.this, AVideoCallActivity.class);
                            intent1.putExtra("callID", UserId);
                            intent1.putExtra("userName", UserId);
                            startActivity(intent1);
                            finish();
                            /*requestPermissionIfNeeded(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), new RequestCallback() {
                                @Override
                                public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                    if (allGranted) {
                                        Intent intent1 = new Intent(OutgoingInvitationActivity.this, AVideoCallActivity.class);
                                        intent1.putExtra("callID", UserId);
                                        intent1.putExtra("userName", UserId);
                                        startActivity(intent1);
                                        finish();
                                    } else {
                                        Toast.makeText(OutgoingInvitationActivity.this, "Some permissions have not been granted.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
*/

                        }
                        /*JitsiMeetActivity.launch(OutgoingInvitationActivity.this, builder.build());
                        finish();*/

                    } catch (Exception e) {
                        Toast.makeText(OutgoingInvitationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (type.equals(Constanta.REMOTE_MSG_INVITATION_REJECTED)) {
                    rejectionCount += 1;
                    if (rejectionCount == totalReceivers) {
                        ring.stop();
                        Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constanta.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver);

    }

    public  void  CallingServiceAPI(String caller_acountid,String reciever_accountid,String status,
                                    String calling_type,String calling_time){
        String otp1 = new GlobalAppApis().CallingApi(caller_acountid,reciever_accountid,
                status,calling_type,calling_time);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCalling(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);

                        if (jsonObject1.getString("Msg").equalsIgnoreCase("Notification Sended successfully")) {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OutgoingInvitationActivity.this, call1, "", true);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Click on cancel button to exit",Toast.LENGTH_LONG).show();
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


