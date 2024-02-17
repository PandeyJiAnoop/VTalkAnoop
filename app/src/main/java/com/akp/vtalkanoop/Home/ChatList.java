package com.akp.vtalkanoop.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.R;
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

public class ChatList extends AppCompatActivity {
    ImageButton back_btn;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private SharedPreferences login_preference;
    String UserId;
    SwipeRefreshLayout srl_refresh;
    TextView count_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        back_btn= findViewById(R.id.back_btn);
        srl_refresh = findViewById(R.id.srl_refresh);
        count_tv=findViewById(R.id.count_tv);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(ChatList.this)) {
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
                    Toast.makeText(ChatList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
        cust_recyclerView= findViewById(R.id.cust_recyclerView);
        getCustomer(UserId);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DashboardScreen.class);
                startActivity(intent);
            }
        });
    }

    public  void  getCustomer(String accountid){
        String otp1 = new GlobalAppApis().CustomerList(accountid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCustomerList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                Log.d("res","res"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("AccountId", jsonObject1.getString("AccountId"));
                        hm.put("LastMesDate", jsonObject1.getString("LastMesDate"));
                        hm.put("LastMessage", jsonObject1.getString("LastMessage"));
                        hm.put("Name", jsonObject1.getString("Name"));
                        hm.put("ProfileImage", jsonObject1.getString("ProfileImage"));
                        hm.put("UserType", jsonObject1.getString("UserType"));
//                        hm.put("show_amount", jsonObject1.getString("show_amount"));
//                        hm.put("updated_at", jsonObject1.getString("updated_at"));
                        count_tv.setText("Total:- "+jsonArrayr.length());

                        arrayList.add(hm);
//                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
//                        String experiance = getDetails.getString("AstroAccountId");
//                        String images = getDetails.getString("ChatId");
//                        String language = getDetails.getString("CustomerAccountId");
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ChatList.this, 1);
                    CustomerAdapter walletHistoryAdapter = new CustomerAdapter(ChatList.this, arrayList);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(walletHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ChatList.this, call1, "", true);
    }

}