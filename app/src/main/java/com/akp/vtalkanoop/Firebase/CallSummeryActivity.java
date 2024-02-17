package com.akp.vtalkanoop.Firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CallSummeryActivity extends AppCompatActivity {
    AppCompatButton exit_btn;
    TextView call_duration_tv,diamond_tv;
    private SharedPreferences login_preference;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_summery);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
//        Toast.makeText(getApplicationContext(),""+UserId,Toast.LENGTH_LONG).show();
        call_duration_tv=findViewById(R.id.call_duration_tv);
        diamond_tv=findViewById(R.id.diamond_tv);
        exit_btn=findViewById(R.id.exit_btn);

        CallSummeryAPI(UserId);

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),DashboardScreen.class);
                startActivity(in);
//                new AlertDialog.Builder(CallSummeryActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
//                        .setMessage("Are you sure?")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(Intent.ACTION_MAIN);
//                                intent.addCategory(Intent.CATEGORY_HOME);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }).setNegativeButton("no", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back Pressed not allowed!",Toast.LENGTH_LONG).show();
    }


    public void CallSummeryAPI(String acountid){
        String otp1 = new GlobalAppApis().CallSummery(acountid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCallSummery(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Log.d("sdgshdgsdhsdg","djhsgdjgh"+result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        if (getDetails.getString("CallTime").equalsIgnoreCase("null")){
                            call_duration_tv.setText("Total Call Duration:- "+"1"+" min.");
                            diamond_tv.setText("Diamond:- "+getDetails.getString("Amount"));
                        }
                        else {
                            call_duration_tv.setText("Total Call Duration:- "+getDetails.getString("CallTime")+" min.");
                            diamond_tv.setText("Diamond:- "+getDetails.getString("Amount"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CallSummeryActivity.this, call1, "", true);
    }

}