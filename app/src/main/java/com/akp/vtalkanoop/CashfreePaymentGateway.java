package com.akp.vtalkanoop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.Home.WalletScreen;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_BANK_CODE;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;

public class CashfreePaymentGateway extends AppCompatActivity {
    private String token;
    private String get_token;
    private ImageView ivMenu;
    private SharedPreferences login_preference;
    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }
    SeamlessMode currentMode = SeamlessMode.CARD;
    String UserName,Usermobile,get_amount;
    String AmountData,UserId,s_no;
    private static final String TAG = "CashfreePaymentGateway";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashfree_payment_gateway);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserName = login_preference.getString("username", "");
        Usermobile = login_preference.getString("mobile", "");
        UserId = login_preference.getString("userid", "");
        get_token  = getIntent().getStringExtra("token");
        get_amount = getIntent().getStringExtra("amount");
        s_no = getIntent().getStringExtra("s_no");
//        Toast.makeText(CashfreePaymentGateway.this, s_no, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),s_no,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),UserName,Toast.LENGTH_LONG).show();
        ivMenu=findViewById(R.id.back_img);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(), WalletScreen.class);
               startActivity(intent);
            }
        });
//        Toast.makeText(getApplicationContext(),""+get_token,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        if (bundle.getString(key).equalsIgnoreCase("CANCELLED")){
                            Toast.makeText(getApplicationContext(),"Transaction CANCELLED",Toast.LENGTH_LONG).show();
                        }
                        else if (bundle.getString(key).equalsIgnoreCase("FAILED")){
                            Toast.makeText(getApplicationContext(),"Transaction FAILED",Toast.LENGTH_LONG).show();
                        }
                        else if (bundle.getString(key).equalsIgnoreCase("SUCCESS")){
                            Toast.makeText(getApplicationContext(),"Transaction SUCCESS!",Toast.LENGTH_LONG).show();
                            AddWalletMoneyAPI(UserId,get_amount);
                        } } } }
    }

    public void onClick(View view) {
        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "PROD";
        //Show the UI for doGPayPayment and phonePePayment only after checking if the apps are ready for payment

        if (view.getId() == R.id.phonePe_exists) {
            Toast.makeText(
                    CashfreePaymentGateway.this,
                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(CashfreePaymentGateway.this, stage)+"",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (view.getId() == R.id.gpay_ready) {
            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(CashfreePaymentGateway.this, new GooglePayStatusListener() {
                @Override
                public void isReady() {
                    Toast.makeText(CashfreePaymentGateway.this, "Ready", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void isNotReady() {
                    Toast.makeText(CashfreePaymentGateway.this, "Not Ready", Toast.LENGTH_SHORT).show();
                }
            });
            return; }

        token = get_token;
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);


        switch (view.getId()) {
            /***
             * This method handles the payment gateway invocation (web flow).
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             * @param color1 Background color of the toolbar
             * @param color2 text color and icon color of toolbar
             * @param hideOrderId If true hides order Id from the toolbar
             */
            case R.id.web: {
                cfPaymentService.doPayment(CashfreePaymentGateway.this, getInputParams(), token, stage, "#784BD2", "#FFFFFF", false);
//                 cfPaymentService.doPayment(CashfreePaymentGateway.this, params, token, stage);
                break;
            }
            /***
             * Same for all payment modes below.
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             */
            case R.id.upi: {
//      cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
                cfPaymentService.upiPayment(CashfreePaymentGateway.this, getInputParams(), token, stage);
                break;
            }
            case R.id.amazon: {
                cfPaymentService.doAmazonPayment(CashfreePaymentGateway.this, getInputParams(), token, stage);
                break;
            }
            case R.id.gpay: {
                cfPaymentService.gPayPayment(CashfreePaymentGateway.this, getInputParams(), token, stage);
                break;
            }
            case R.id.phonePe: {
                cfPaymentService.phonePePayment(CashfreePaymentGateway.this, getInputParams(), token, stage);
                break;
            }
            case R.id.web_seamless: {
                cfPaymentService.doPayment(CashfreePaymentGateway.this, getSeamlessCheckoutParams(), token, stage);
                break;
            }} }



    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */

        String appId = "160408bcc0a3a50db8a76071b9804061";
        String orderId = s_no;
        String orderAmount = get_amount;
        String orderNote = "Viksol Grocery Payment";
        String customerName = UserName;
        String customerPhone = Usermobile;
        String customerEmail = "admin@viksol.in";
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }
    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "VALID_CARD_NUMBER");
                params.put(PARAM_CARD_YYYY, "YYYY");
                params.put(PARAM_CARD_MM, "MM");
                params.put(PARAM_CARD_HOLDER, "CARD_HOLDER_NAME");
                params.put(PARAM_CARD_CVV, "CVV");
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(CashfreePaymentGateway.this);
        builder.setMessage("Are you sure want to Back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(),WalletScreen.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public  void  AddWalletMoneyAPI(String accountid,String amount){
        String otp1 = new GlobalAppApis().WalletAPI(accountid,amount);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getaddwalletList(otp1);
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
                        hm.put("WalletAmount", jsonObject1.getString("WalletAmount"));
                        AmountData=jsonObject1.getString("WalletAmount");
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
        }, CashfreePaymentGateway.this, call1, "", true);
    }



}