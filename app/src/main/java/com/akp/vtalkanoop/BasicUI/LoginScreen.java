package com.akp.vtalkanoop.BasicUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.Firebase.Constanta;
import com.akp.vtalkanoop.Firebase.PreferenceManager;
import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.vtalkanoop.Util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.akp.vtalkanoop.Util.Constants.FCM_KEY;

public class LoginScreen extends AppCompatActivity {
    TextInputEditText email_et,pass_et;
    AppCompatButton btn_login;
    TextView link_signup;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String token,mobile;
    private PreferenceManager preferenceManager;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        //enable Gps method
        enableLoc();
        preferenceManager = new PreferenceManager(getApplicationContext());
        btn_login=findViewById(R.id.btn_login);
        email_et=findViewById(R.id.email_et);
        pass_et=findViewById(R.id.pass_et);
        link_signup=findViewById(R.id.link_signup);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            token = instanceIdResult.getToken();
            Log.e("newToken", token);
        });
        mobile=email_et.getText().toString();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (pass_et.getText().toString().equalsIgnoreCase("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else {
                    getLoginAPI(email_et.getText().toString(),pass_et.getText().toString(),token);
                }
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegistrationScreen.class);
                startActivity(intent);
            }
        });
    }

    public void  getLoginAPI(String mobile,String deviceid,String token){
        String otp1 = new GlobalAppApis().Login(mobile,deviceid,token);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getLogin(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        String Id = jsonObject1.getString("AccountId");
                        String Otp = jsonObject1.getString("UserName");
                        String name = jsonObject1.getString("Name");
                        String UserType = jsonObject1.getString("UserType");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                        if (msg.equalsIgnoreCase("Login Suuccessfully.")){
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("userid",Id);
                            login_editor.putString("Usertype",UserType);
                            login_editor.putString("username",name);
                            login_editor.putString("mobile",mobile);
                            login_editor.commit();

                            preferenceManager.putString(Constanta.KEY_USER_ID, Id);
                            preferenceManager.putString(Constanta.KEY_FIRST_NAME, name);
                            preferenceManager.putString(Constanta.KEY_LAST_NAME, Otp);
                            preferenceManager.putString(Constanta.KEY_FCM_TOKEN,token);

//                        Toast.makeText(getApplicationContext(),"Login Successfully!",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), DashboardScreen.class);
                            startActivity(intent);

                        }

                        /*if (UserType.equalsIgnoreCase("2")){
                            Toast.makeText(getApplicationContext(),"Please upload a Sort Video of 10sec.",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), DatingVerify.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Login Successfully!",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), DashboardScreen.class);
                            startActivity(intent);
                        }*/
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"UserId and password not matched!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, LoginScreen.this, call1, "", true);
    }


    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(LoginScreen.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(LoginScreen.this, REQUEST_LOCATION);
//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity(); // or finish();
    }
    }