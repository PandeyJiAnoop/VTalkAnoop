package com.akp.vtalkanoop.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class ChatHistory extends AppCompatActivity {
    EditText messageBox;
    ImageButton sendBtn;
    RecyclerView recyclerView;
    private SharedPreferences login_preference;
    String AstroId, UserId;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView chat_recyclerView;
    String otp1, AstroImage, Wallet_balance, Charge;
    String UserType, AstroType, AstroName, login_user_type;
    SwipeRefreshLayout srl_refresh;
    TextView tv;
    CircularImageView back_img;
    private SharedPreferences wallet_preference;
    ImageView refresh;
//    int count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");
        login_user_type = login_preference.getString("Usertype", "");
        wallet_preference = getSharedPreferences("wallet_preference", MODE_PRIVATE);
        Wallet_balance = wallet_preference.getString("WalletAmount", "");
        AstroId = getIntent().getStringExtra("AstroId");
        AstroType = getIntent().getStringExtra("Astrotype");
        Charge = getIntent().getStringExtra("chat_rate");
        refresh=findViewById(R.id.refresh);
//        Toast.makeText(getApplicationContext(),""+Wallet_balance,Toast.LENGTH_LONG).show();
    /*    if (login_user_type.equalsIgnoreCase("Guest")) {
            if (Wallet_balance.equalsIgnoreCase("null")) {
                Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a chat", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), WalletScreen.class));
            }
           *//* else {
                total_minut = Double.parseDouble(Wallet_balance) / Double.parseDouble(Charge);
                double DELAY = total_minut * 60000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You have insufficient balance", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), WalletScreen.class);
                        startActivity(intent);
                    }
                }, (long) DELAY);
            }*//*
        }*/


//        getAutoRefresh();


        AstroName = getIntent().getStringExtra("Astroname");
        AstroImage = getIntent().getStringExtra("Astroimage");

        srl_refresh = findViewById(R.id.srl_refresh);
        tv = findViewById(R.id.tv);
        back_img = findViewById(R.id.back_img);
//        Charge = getIntent().getStringExtra("charge");
        messageBox = findViewById(R.id.messageBox);
//        Log.d("resyuytuu", "ree" + UserId + AstroId+AstroType);
        sendBtn = findViewById(R.id.sendBtn);
        chat_recyclerView = findViewById(R.id.chat_recyclerView);

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(AstroImage)
                .into(new BitmapImageViewTarget(back_img) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        back_img.setImageBitmap(resource);
                    }
                });

//        Picasso.with(ChatHistory.this).load(AstroImage).placeholder(R.drawable.loadinggif).error(R.drawable.person).into(back_img);

        if (AstroType.equalsIgnoreCase("Guest")) {
            if (Wallet_balance.equalsIgnoreCase("null")) {
                Toast.makeText(getApplicationContext(), "You have insufficient balance", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), WalletScreen.class);
                startActivity(intent);
            } else {
                getChatDetails(AstroId, UserId, "", "", "", "0", "2");
            }
        } else if (AstroType.equalsIgnoreCase("DATING")) {
            getChatDetails(UserId, AstroId, "", "", "", "0", "2");
        } else if (AstroType.equalsIgnoreCase("ASTRO")) {
            getChatDetails(UserId, AstroId, "", "", "", "0", "2");
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


//        refresh = new Runnable() {
//            public void run() {
//                // Do something
//                handler.postDelayed(refresh, 5000);
//            }
//        };
//        handler.post(refresh);
//        getChatDetails(UserId, AstroId, "","","","0","2");

        tv.setText(AstroName);


       /* if (Wallet_balance.equalsIgnoreCase("null")){
            Toast.makeText(getApplicationContext(),"You have insufficient balance",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),WalletScreen.class);
            startActivity(intent);
        }*/
     /*   double d1 = Double.parseDouble(Wallet_balance), d2 = Double.parseDouble(Charge);
        double division = d1 / d2;
        double DELAY= division*60000;
//                        Toast.makeText(getApplicationContext(),""+DELAY,Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"You have insufficient balance",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),WalletScreen.class);
                startActivity(intent);
            }
        }, (long) DELAY);
*/


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(ChatHistory.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (AstroType.equalsIgnoreCase("Guest")) {
                                getChatDetails(AstroId, UserId, "", "", "", "0", "2");
                            } else if (AstroType.equalsIgnoreCase("DATING")) {
                                getChatDetails(UserId, AstroId, "", "", "", "0", "2");
                            } else if (AstroType.equalsIgnoreCase("ASTRO")) {
                                getChatDetails(UserId, AstroId, "", "", "", "0", "2");
                            }
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(ChatHistory.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBox.getText().toString().equalsIgnoreCase("")) {
                    messageBox.setError("Please write your message");
                    messageBox.requestFocus();
                } else {
                    if (AstroType.equalsIgnoreCase("ASTRO")) {
                        getChatDetails(UserId, AstroId, AstroType, messageBox.getText().toString(), "", "0", "1");
                        messageBox.setText("");
//                        getChatDetails(AstroId, UserId, "","","","0","2");
                    } else if (AstroType.equalsIgnoreCase("DATING")) {
                        getChatDetails(UserId, AstroId, "ASTRO", messageBox.getText().toString(), "", "0", "1");
                        messageBox.setText("");
//                        getChatDetails(AstroId, UserId, "","","","0","2");
                    } else if (AstroType.equalsIgnoreCase("Guest")) {
                        getChatDetails(AstroId, UserId, "Guest", messageBox.getText().toString(), "", "0", "1");
                        messageBox.setText("");
//                        getChatDetails(AstroId, UserId, "","","","0","2");
                    } else {
                        getChatDetails(UserId, AstroId, AstroType, messageBox.getText().toString(), "", "0", "1");
                        messageBox.setText("");
//                        doTheAutoRefresh();
//                        finish();
//                        overridePendingTransition( 0, 0);
//                        startActivity(getIntent());
//                        overridePendingTransition( 0, 0);
//                        getChatDetails(AstroId, UserId, "","","","0","2");
                    }
                }
            }
        });

/*//wallet api
        String otp1 = new GlobalAppApis().ProfileDetials(UserId);ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getprofile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try { JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("SettingResp");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String accountId = getDetails.getString("AccountId");
                        Wallet_balance = getDetails.getString("Balance");
                        if (Wallet_balance.equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(),"You have insufficient balance",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),WalletScreen.class);
                            startActivity(intent);
                        }
                        double d1 = Double.parseDouble(Wallet_balance), d2 = Double.parseDouble(Charge);
                        double division = d1 / d2;
                        double DELAY= division*60000;
//                        Toast.makeText(getApplicationContext(),""+DELAY,Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"You have insufficient balance",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),WalletScreen.class);
                                startActivity(intent);
                            }
                        }, (long) DELAY);

                    } } catch (JSONException e) {
                    e.printStackTrace();
                } }}, ChatHistory.this, call1, "", true);
//end wallet api


//    }*/
    }



    public void getChatDetails(String custid, String astroid, String rtype, String text, String status, String time, String action) {
        otp1 = new GlobalAppApis().ChatDetails(custid, astroid, rtype, text, status, time, action);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getChatService(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res", "response" + result);
//               Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Receiver", jsonObject1.getString("Receiver"));
                        hm.put("Text", jsonObject1.getString("Text"));

//                        hm.put("show_amount", jsonObject1.getString("show_amount"));
//                        hm.put("updated_at", jsonObject1.getString("updated_at"));
                        arrayList.add(hm);
//                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
//                        String experiance = getDetails.getString("AstroAccountId");
//                        String images = getDetails.getString("ChatId");
//                        String language = getDetails.getString("CustomerAccountId");
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ChatHistory.this, 1);
                    ChatAdapter walletHistoryAdapter = new ChatAdapter(ChatHistory.this, arrayList);
                    chat_recyclerView.setLayoutManager(gridLayoutManager);
                    chat_recyclerView.setAdapter(walletHistoryAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ChatHistory.this, call1, "", true);
    }
    @Override
    public void onBackPressed() {
       Intent intent=new Intent(getApplicationContext(),DashboardScreen.class);
       startActivity(intent);
    }


  /*  private void getAutoRefresh() {
       count++;
refresha(1000);

    }
    private void refresha(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getAutoRefresh();
            }
        };
    }*/
}