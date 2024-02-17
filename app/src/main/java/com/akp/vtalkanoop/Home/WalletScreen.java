package com.akp.vtalkanoop.Home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.CashfreePaymentGateway;
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
import java.util.List;
import java.util.Random;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class WalletScreen extends AppCompatActivity {
    private ImageView back_img;
    RecyclerView wallet_rec;
    AppCompatButton add_money_btn;
    private Dialog alertDialog;
    private SharedPreferences login_preference;
    String UserId;
    TextView wallet_amount;
    String AmountData;
    private SharedPreferences wallet_preference;
    private SharedPreferences.Editor wallet_editor;
    String WalletMoney;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private EditText rupee_et;
    AppCompatButton btnAdvanced;
    private AlertDialog alertDialog1;
    EditText rupee_et1,name_et,account_no_et,ifsc_et,branch_name_et;
    String sno;
    String Accountname,Accountno,Accountifsc,Accountbranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);
        back_img=findViewById(R.id.back_img);
        wallet_rec=findViewById(R.id.wallet_rec);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("userid", "");

        wallet_preference = getSharedPreferences("wallet_preference", MODE_PRIVATE);
        WalletMoney = wallet_preference.getString("WalletAmount", "");

//        Toast.makeText(WalletScreen.this, sno, Toast.LENGTH_SHORT).show();

        add_money_btn=findViewById(R.id.add_money_btn);
        wallet_amount=findViewById(R.id.wallet_amount);

        btnAdvanced=findViewById(R.id.btnAdvanced);

        Random rand = new Random();
        sno = String.format("%04d", rand.nextInt(10000));

        add_money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw_popup();
            }
        });
        SettingAPI(UserId);
        GetAccountDetails(UserId);
         srl_refresh = findViewById(R.id.srl_refresh);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(WalletScreen.this)) {
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
                    Toast.makeText(WalletScreen.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GetWalletHistory(UserId);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DashboardScreen.class);
                startActivity(intent);
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debit_popup();
            }
        });


    }


    private void withdraw_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.withdrawpopup, viewGroup, false);
        rupee_et = (EditText) dialogView.findViewById(R.id.rupee_et);

        Button ok = (Button) dialogView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rupee_et.getText().toString().equalsIgnoreCase("")){
                    rupee_et.setError("Fields can't be empty");
                    rupee_et.requestFocus();
                }
                else {
                    GenrateTokenAPI("https://api.cashfree.com/api/v2/cftoken/order","160408bcc0a3a50db8a76071b9804061","INR",
                            rupee_et.getText().toString(),sno,"30199ae880b9743d3258e5b2b4aa50a7d137fd42");
//                    AddWalletMoneyAPI(UserId,rupee_et.getText().toString());
                }
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }








    public  void  GetWalletHistory(String accountid){
        String otp1 = new GlobalAppApis().WalletHistoryList(accountid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getWalletHistory(otp1);
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
                        hm.put("Message", jsonObject1.getString("Message"));
                        hm.put("DebitAmount", jsonObject1.getString("DebitAmount"));
                        hm.put("CreditAmount", jsonObject1.getString("CreditAmount"));
                        hm.put("WalletStatus", jsonObject1.getString("WalletStatus"));
                        hm.put("Entrydate", jsonObject1.getString("Entrydate"));
                        arrayList.add(hm);


                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(WalletScreen.this, 1);
                    WalletAdapter walletHistoryAdapter = new WalletAdapter(WalletScreen.this, arrayList);
                    wallet_rec.setLayoutManager(gridLayoutManager);
                    wallet_rec.setAdapter(walletHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WalletScreen.this, call1, "", true);
    }



    public  void  SettingAPI(String acountid){
        String otp1 = new GlobalAppApis().SettingApi(acountid);
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
                        String walletamount= jsonObject1.getString("WalletAmount");
                        String type= jsonObject1.getString("UserType");
                        wallet_amount.setText(walletamount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WalletScreen.this, call1, "", true);
    }



    public  void  GenrateTokenAPI(String appurl,String appid,String currency,String orderamt,String orderid,String secretkey){
        String otp1 = new GlobalAppApis().TokenAPI(appurl,appid,currency,orderamt,orderid,secretkey);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getToken(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonArrayr = jsonObject.getJSONObject("Response");
                        Intent intent=new Intent(getApplicationContext(), CashfreePaymentGateway.class);
                        intent.putExtra("token",jsonArrayr.getString("cftoken"));
                        intent.putExtra("amount",rupee_et.getText().toString());
                    intent.putExtra("s_no",sno);
                        startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WalletScreen.this, call1, "", true);
    }

    private void Debit_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.debitpopup, viewGroup, false);
        rupee_et1 = (EditText) dialogView.findViewById(R.id.rupee_et1);
        name_et = (EditText) dialogView.findViewById(R.id.name_et);
        account_no_et = (EditText) dialogView.findViewById(R.id.account_no_et);
        ifsc_et = (EditText) dialogView.findViewById(R.id.ifsc_et);
        branch_name_et = (EditText) dialogView.findViewById(R.id.branch_name_et);
        Button ok1 = (Button) dialogView.findViewById(R.id.buttonOk1);
//        GetAccountDetails(UserId);


        if (Accountname == null){
            name_et.setClickable(true);
            name_et.setFocusable(true);
        }
        else {
            name_et.setText(Accountname);
            name_et.setClickable(false);
            name_et.setFocusable(false);
        }
        if (Accountno == null){
            account_no_et.setClickable(true);
            account_no_et.setFocusable(true);
        }
        else {
            account_no_et.setText(Accountno);
            account_no_et.setClickable(false);
            account_no_et.setFocusable(false);
        }
        if (Accountifsc == null){
            ifsc_et.setClickable(true);
            ifsc_et.setFocusable(true);
        }
        else {
            ifsc_et.setText(Accountifsc);
            ifsc_et.setClickable(false);
            ifsc_et.setFocusable(false);
        }
        if (Accountbranch == null){
            branch_name_et.setClickable(true);
            branch_name_et.setFocusable(true);
        }
        else {
            branch_name_et.setText(Accountbranch);
            branch_name_et.setClickable(false);
            branch_name_et.setFocusable(false);
        }



        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rupee_et1.getText().toString().equalsIgnoreCase("")){
                    rupee_et1.setError("Fields can't be empty");
                    rupee_et1.requestFocus();
                }
                else if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be empty");
                    name_et.requestFocus();
                }
                else if (account_no_et.getText().toString().equalsIgnoreCase("")){
                    account_no_et.setError("Fields can't be empty");
                    account_no_et.requestFocus();
                }
               else if (ifsc_et.getText().toString().equalsIgnoreCase("")){
                    ifsc_et.setError("Fields can't be empty");
                    ifsc_et.requestFocus();
                }
                else if (branch_name_et.getText().toString().equalsIgnoreCase("")){
                    branch_name_et.setError("Fields can't be empty");
                    branch_name_et.requestFocus();
                }
                else {
                    DebitWalletMoneyAPI(UserId,rupee_et1.getText().toString(),name_et.getText().toString(),account_no_et.getText().toString(),
                            ifsc_et.getText().toString(),branch_name_et.getText().toString());
                }
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    public  void  DebitWalletMoneyAPI(String accountid,String amount,String ac_name,String acc_no,String ifsc,String branch ){
        String otp1 = new GlobalAppApis().DebitWalletAPI(accountid,amount,ac_name,acc_no,ifsc,branch);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getdebitwalletList(otp1);
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
                        hm.put("Message", jsonObject1.getString("Message"));
                        Toast.makeText(getApplicationContext(),jsonObject1.getString("Message"),Toast.LENGTH_LONG).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WalletScreen.this, call1, "", true);
    }




    public  void  GetAccountDetails(String accountid){
        String otp1 = new GlobalAppApis().AccountDetailsAPI(accountid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getAccountdetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        Accountname=jsonObject1.getString("Accholdername");
                        Accountno=jsonObject1.getString("AccNo");
                        Accountifsc=jsonObject1.getString("Ifsc");
                        Accountbranch=jsonObject1.getString("BranchName");
                        //                        HashMap<String, String> hm = new HashMap<>();
//                        hm.put("Message", jsonObject1.getString("Message"));
//                        Toast.makeText(getApplicationContext(),jsonObject1.getString("Message"),Toast.LENGTH_LONG).show();
//                        finish();
//                        overridePendingTransition(0, 0);
//                        startActivity(getIntent());
//                        overridePendingTransition(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WalletScreen.this, call1, "", true);
    }


}