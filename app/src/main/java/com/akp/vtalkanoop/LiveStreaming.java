package com.akp.vtalkanoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.Home.ChatList;
import com.akp.vtalkanoop.Home.CustomerAdapter;
import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.Home.NetworkConnectionHelper;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class LiveStreaming extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private SharedPreferences login_preference;
    String UserId,Usertype;
    SwipeRefreshLayout srl_refresh;
    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);
        srl_refresh = findViewById(R.id.srl_refresh);

        back_btn=findViewById(R.id.back_btn);

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(LiveStreaming.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(LiveStreaming.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
        Usertype = login_preference.getString("Usertype", "");
        cust_recyclerView= findViewById(R.id.cust_recyclerView);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardScreen.class);
                startActivity(intent);
            }
        });
        getLiveList(UserId,"");
    }

    public  void  getLiveList(String acountid,String type ){
        String otp1 = new GlobalAppApis().LiveStreamAPI(acountid,type);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getLiveStream(otp1);
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
                        hm.put("FirstName", jsonObject1.getString("FirstName"));
                        hm.put("ProfileImage", jsonObject1.getString("ProfileImage"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveStreaming.this, 2);
                    LiveAdapter walletHistoryAdapter = new LiveAdapter(LiveStreaming.this, arrayList);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(walletHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, LiveStreaming.this, call1, "", true); }
}