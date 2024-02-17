package com.akp.vtalkanoop.Firebase;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;

public class CallActivity extends AppCompatActivity {
    //private SharedPreferences sharedPreferences;
     String callID,CallTime;

    String RecieverId,Rate;
    private SharedPreferences login_preference;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call2);
        // Adding this line will prevent taking screenshot in your app
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
        /*sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        loginUserName = sharedPreferences.getString("uName", "");*/
        addFragment();
    }

    public void addFragment() {
        long appID = 1387707727;
        String appSign = "5c46de05ce52771760518133b1f6ececb56e29f387b68ac1bcf124f1e3d051f7";
        callID = getIntent().getStringExtra("callID");
        RecieverId = getIntent().getStringExtra("RecieverId");
        Rate = getIntent().getStringExtra("rate");
        CallTime = getIntent().getStringExtra("call_time");
        String name = getIntent().getStringExtra("userName");
//        String userID = Build.MANUFACTURER + "_" + generateUserID();
        Log.d("resp_data",appID+"\nappsignin:- "+appSign+"\nCallId:- "+callID+"\nUserId:- "+UserId+"\nName:- "+name+"\nCallID:- "+callID);


        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();
        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(appID, appSign, UserId,
                name, callID, config);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitNow();



//        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();
//        config.turnOnMicrophoneWhenJoining = true;
//        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(appID, appSign, UserId, name, callID, config);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitNow();
//        Log.d("resp_data",appID+"\nappsignin:- "+appSign+"\nCallId:- "+callID+"\nUserId:- "+UserId+"\nName:- "+name+"\nCallID:- "+callID+"\nConfig:- "+config);

    }

    private String generateUserID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 5) {
            int nextInt = random.nextInt(10);
            if (builder.length() == 0 && nextInt == 0) {
                continue;
            }
            builder.append(nextInt);
        }
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        ZegoUIKitPrebuiltCallInvitationService.unInit();
        CallingServiceAPI(callID,RecieverId,"Done","VoiceCall",CallTime);
        super.onDestroy();
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
                //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                //Log.d("sghghsgd","sdjskdh"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String status = getDetails.getString("Status");
                        String msg = getDetails.getString("Msg");
                        if (msg.equalsIgnoreCase("Call Succesfully Done")){
                            Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
                            Intent intent=new Intent(getApplicationContext(),CallSummeryActivity.class);
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
        }, CallActivity.this, call1, "", true);
    }

}