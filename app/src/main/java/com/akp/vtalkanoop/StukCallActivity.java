package com.akp.vtalkanoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.Home.ChatList;
import com.akp.vtalkanoop.Home.CustomerAdapter;
import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.Home.NetworkConnectionHelper;
import com.akp.vtalkanoop.Home.Profile;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class StukCallActivity extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private SharedPreferences login_preference;
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageButton back_btn;
    TextView count_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuk_call);


        back_btn = findViewById(R.id.back_btn);
        srl_refresh = findViewById(R.id.srl_refresh);
        count_tv = findViewById(R.id.count_tv);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(StukCallActivity.this)) {
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
                    Toast.makeText(StukCallActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        StukCallAPI(UserId);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardScreen.class);
                startActivity(intent);
            }
        });

    }


    public void StukCallAPI(String acountid) {
        String otp1 = new GlobalAppApis().StukCall(acountid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getStukcall(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("fdhjfd",result);

//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("DataStatus", jsonObject1.getString("DataStatus"));
                        hm.put("ReceiverID", jsonObject1.getString("ReceiverID"));
                        hm.put("ID", jsonObject1.getString("ID"));
                        hm.put("Receiver", jsonObject1.getString("Receiver"));
                        hm.put("CallerID", jsonObject1.getString("CallerID"));
                        hm.put("Caller", jsonObject1.getString("Caller"));
                        hm.put("Status", jsonObject1.getString("Status"));
                        hm.put("StartTime", jsonObject1.getString("StartTime"));
                        hm.put("Disconnectddate", jsonObject1.getString("Disconnectddate"));
                        count_tv.setText("("+ jsonArrayr.length()+")");
arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(StukCallActivity.this, 1);
                    StukcallAdapter walletHistoryAdapter = new StukcallAdapter(StukCallActivity.this, arrayList);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(walletHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, StukCallActivity.this, call1, "", true);
    }


    public class StukcallAdapter extends RecyclerView.Adapter<StukcallAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;
        public StukcallAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public StukcallAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_stuk, viewGroup, false);
            StukcallAdapter.VH viewHolder = new StukcallAdapter.VH(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StukcallAdapter.VH vh, int i) {
            vh.reciever_tv.setText("Receiver:- "+arrayList.get(i).get("Receiver") + "(" + arrayList.get(i).get("ReceiverID") + ")");
            vh.caller_tv.setText("Caller:- "+arrayList.get(i).get("Caller") + "(" + arrayList.get(i).get("CallerID") + ")");
            vh.msgTime.setText("START DATETIME:- "+arrayList.get(i).get("StartTime"));
            vh.status_tv.setText(arrayList.get(i).get("Status"));
            vh.ststustv.setText("Status:- "+arrayList.get(i).get("Status"));

            if (arrayList.get(i).get("Status").equalsIgnoreCase("Accepted")) {
                vh.status_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context); //Home is name of the activity
                        builder.setMessage("Do you want to Terminate Call?");
                        builder.setPositiveButton("Rejected", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String otp1 = new GlobalAppApis().RemoveStukCall(arrayList.get(i).get("ID"));
                                ApiService client1 = getApiClient_ByPost();
                                Call<String> call1 = client1.getRemovecall(otp1);
                                new ConnectToRetrofit(new RetrofitCallBackListenar() {
                                    @Override
                                    public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                                            for (int i = 0; i < jsonArrayr.length(); i++) {
                                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                                Toast.makeText(context, jsonObject1.getString("Msg"), Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(getApplicationContext(),DashboardScreen.class);
                                                startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, StukCallActivity.this, call1, "", true);
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView reciever_tv, caller_tv, msgTime, status_tv,ststustv;

            public VH(@NonNull View itemView) {
                super(itemView);
                reciever_tv = itemView.findViewById(R.id.reciever_tv);
                caller_tv = itemView.findViewById(R.id.caller_tv);
                msgTime = itemView.findViewById(R.id.msgTime);
                status_tv = itemView.findViewById(R.id.status_tv);
                ststustv = itemView.findViewById(R.id.ststustv);
            }
        }


    }
}