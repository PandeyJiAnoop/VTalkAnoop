package com.akp.vtalkanoop.BasicUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.InstallStateUpdatedListener;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.InstallStatus;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class SplashScreen extends AppCompatActivity {
    ImageView ivImage,ivImage1;
    long Delay = 5000;
    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 2000;
    private SharedPreferences sharedPreferences;
    String code;
    String versionName = "", versionCode = "";
//    private AppUpdateManager appUpdateManager;
//    private InstallStateUpdatedListener installStateUpdatedListener;
//    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String token = instanceIdResult.getToken();
////                Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
//                // send it to server
//            }
//        });

        if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }


        UpdateVersion();

//        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
//        installStateUpdatedListener = state -> {
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                popupSnackBarForCompleteUpdate();
//            } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                removeInstallStateUpdateListener();
//            } else {
//                Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
//            }
//        };
//        appUpdateManager.registerListener(installStateUpdatedListener);
//        checkUpdate();


        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        code= sharedPreferences.getString("userid", "");
        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        ivImage = findViewById(R.id.ivImage);
        ivImage1 = findViewById(R.id.ivImage1);
        ivImage.setAnimation(downtoup);
        ivImage1.setAnimation(uptodown);

    }
    private void checkLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (code.equalsIgnoreCase("")){
                    Intent myIntent = new Intent(SplashScreen.this,LoginScreen.class);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(myIntent);
                }


            }
        },SPLASH_TIME_OUT);

    }





//    private void checkUpdate() {
//
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                startUpdateFlow(appUpdateInfo);
//            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                popupSnackBarForCompleteUpdate();








//            }
//        });
//    }
//
//    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
//        try {
//            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, SplashScreen.FLEXIBLE_APP_UPDATE_REQ_CODE);
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
//            if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//            } else if (resultCode == RESULT_OK) {
//                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//                checkUpdate();
//            }
//        }
//    }
//
//    private void popupSnackBarForCompleteUpdate() {
//        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
//
//                .setAction("Install", view -> {
//                    if (appUpdateManager != null) {
//                        appUpdateManager.completeUpdate();
//                    }
//                })
//                .setActionTextColor(getResources().getColor(R.color.purple_500))
//                .show();
//    }
//
//    private void removeInstallStateUpdateListener() {
//        if (appUpdateManager != null) {
//            appUpdateManager.unregisterListener(installStateUpdatedListener);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        removeInstallStateUpdateListener();
//    }


    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
      /*  Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

      */
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // or finish();
              /*  if (code.equalsIgnoreCase("")){
                    Intent myIntent = new Intent(SplashScreen.this,WelcomeSlider.class);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(myIntent);
                }*/
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));

            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void UpdateVersion() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://vtalklive.com/api/Vtalk/GetVendorVersion", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        String UpdateVersion = jsonObject1.getString("UpdateVersion");
//                        Toast.makeText(getApplicationContext(),""+UpdateVersion,Toast.LENGTH_LONG).show();
                        String status = jsonObject1.getString("Status");
                        if (status.equalsIgnoreCase("True"))
                            getVersionInfo();
                        {
                            if (versionName.equalsIgnoreCase(UpdateVersion)) {
                                checkLogin();
                            } else {
                                AlertVersion();
                                //checkLogin();
                            } } }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
