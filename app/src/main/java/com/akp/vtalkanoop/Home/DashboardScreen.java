package com.akp.vtalkanoop.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.akp.vtalkanoop.BasicUI.LoginScreen;
import com.akp.vtalkanoop.BasicUI.SplashScreen;
import com.akp.vtalkanoop.LiveStreaming;
import com.akp.vtalkanoop.LiveUserList;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.InstallStateUpdatedListener;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.InstallStatus;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.Task;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class DashboardScreen extends AppCompatActivity {
    private ViewPager pager;
    CirclePageIndicator indicator;
    LinearLayout dynamically_astro, dynamically_dating, astro_ll, dating_ll;
    TextView view_all_astro, view_all_dating;
    LinearLayout support_ll;
    private ArrayList<DashbordAdapter> dash_dataArray;
    private ArrayList<DashbordDatingAdapter> dash_date_dataArray;
    private SharedPreferences login_preference;
    String UserType;
    SwipeRefreshLayout srl_refresh;
    String wallet, UserId;
    private SharedPreferences wallet_preference;
    private SharedPreferences.Editor wallet_editor;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    LinearLayout update_ll;
    AlertDialog alertDialog1;


//    private AppUpdateManager appUpdateManager;
//    private InstallStateUpdatedListener installStateUpdatedListener;
//    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

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

        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserType = login_preference.getString("Usertype", "");
        UserId = login_preference.getString("userid", "");
//        Toast.makeText(getApplicationContext(),""+UserId,Toast.LENGTH_LONG).show();
        dynamically_astro = findViewById(R.id.dynamically_astro);
        dynamically_dating = findViewById(R.id.dynamically_dating);
        support_ll = findViewById(R.id.support_ll);
        update_ll = findViewById(R.id.update_ll);
        astro_ll = findViewById(R.id.astro_ll);
        dating_ll = findViewById(R.id.dating_ll);
        view_all_astro = findViewById(R.id.view_all_astro);
        view_all_dating = findViewById(R.id.view_all_dating);
        srl_refresh = findViewById(R.id.srl_refresh);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);
        getBanner();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(1);
        getDashboardAPI();
        getDashboardDatingAPI();
        SettingAPI(UserId);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(DashboardScreen.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDashboardAPI();
                            getDashboardDatingAPI();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(DashboardScreen.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (UserType.equalsIgnoreCase("Astrologer")) {

        }
        if (UserType.equalsIgnoreCase("DGirl")) {

        }
        if (UserType.equalsIgnoreCase("Guest")) {
            astro_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AstroDetails.class);
                    startActivity(intent);
                }
            });
            dating_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DatingDetails.class);
                    startActivity(intent);
                }
            });

            view_all_astro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AstroDetails.class);
                    startActivity(intent);
                }
            });
            view_all_dating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DatingDetails.class);
                    startActivity(intent);
                }
            });
        }


        support_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Support_popup();

              /*  String phone = "+91 9696381023";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);*/
            }
        });

        update_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.akp.vtalkanoop")));

              /*  String phone = "+91 9696381023";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);*/
            }
        });

       /* int images[]={R.drawable.slideone,R.drawable.slidetwo,R.drawable.slidethree,R.drawable.slidefour,R.drawable.slidefive};
        viewFlipper=(ViewFlipper)findViewById(R.id.pager);
        for (int image:images)
        {
            flipperImages(image);
        }*/
       /*  for (int i=0;i<10;i++){
                    GetAstroList(i);
        }
                for (int j=0;j<10;j++){
                    GetDatingList(j);
        }*/
    }

  /*  public void flipperImages(int image)
    {
        ImageView imageView=new ImageView(DashboardScreen.this);
        imageView.setBackground(getResources().getDrawable(image));
        Log.e("images",""+image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(DashboardScreen.this,android.R.anim.slide_in_left);
//       viewFlipper.setInAnimation(DashboardScreen.this,android.R.anim.slide_out_right);
    }*/

    private void GetAstroList(int i) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dashallastrologer, null);
        dynamically_astro.addView(convertView);
        View view = new View(DashboardScreen.this);
        TextView name_et = convertView.findViewById(R.id.name_et);
        TextView status_tv = convertView.findViewById(R.id.status_tv);
        CircleImageView profile_image = convertView.findViewById(R.id.profile_image);

        Glide.with(DashboardScreen.this).load("https://vtalklive.com/" + dash_dataArray.get(i).getProfileImage()).into(profile_image);

//        Picasso.get()
//                .load("https://vtalklive.com/" + dash_dataArray.get(i).getProfileImage())
//                .placeholder(R.drawable.loadinggif)
//                .error(R.drawable.loadinggif)
//                .into(profile_image);


//        Picasso.with(DashboardScreen.this).load("https://vtalklive.com/"+dash_dataArray.get(i).getProfileImage()).placeholder(R.drawable.loadinggif).error(R.drawable.logo).into(profile_image);
        Log.d("img", "imgg" + dash_dataArray.get(i).getProfileImage());
//        tv.setText((i + 1) + ". ");
        name_et.setText(dash_dataArray.get(i).getName());

        if (UserType.equalsIgnoreCase("Guest")) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AstroProfile.class);
                    intent.putExtra("id", dash_dataArray.get(i).getAccountId());
                    intent.putExtra("wallet_amount", wallet);
                    startActivity(intent);
                }
            });
        }
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        view.setLayoutParams(params_view);
        dynamically_astro.addView(view);
    }

    private void GetDatingList(int j) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView1 = inflater.inflate(R.layout.dashboarddating, null);
        dynamically_dating.addView(convertView1);
        View view = new View(DashboardScreen.this);
        TextView dname_tv = convertView1.findViewById(R.id.dname_tv);
        TextView dstatus_tv = convertView1.findViewById(R.id.dstatus_tv);
        CircleImageView profile_image = convertView1.findViewById(R.id.dprofile_image);
        dname_tv.setText(dash_date_dataArray.get(j).getName());

        Glide.with(DashboardScreen.this).load("https://vtalklive.com/" + dash_date_dataArray.get(j).getImage()).into(profile_image);

//        Picasso.get()
//                .load("https://vtalklive.com/" + dash_date_dataArray.get(j).getImage())
//                .placeholder(R.drawable.loadinggif)
//                .error(R.drawable.loadinggif)
//                .into(profile_image);


//        Picasso.with(DashboardScreen.this).load("https://vtalklive.com/"+dash_date_dataArray.get(j).getImage()).placeholder(R.drawable.loadinggif).error(R.drawable.logo).into(profile_image);

//        TextView tv=convertView.findViewById(R.id.tv);
//        tv.setText((i + 1) + ". ");

        if (UserType.equalsIgnoreCase("Guest")) {
            convertView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DatingProfileDetails.class);
                    intent.putExtra("id", dash_date_dataArray.get(j).getAccountId());
                    intent.putExtra("wallet_amount", wallet);
                    startActivity(intent);
                }
            });

        }
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        view.setLayoutParams(params_view);
        dynamically_dating.addView(view);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardScreen.this);
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
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

    void getDashboardAPI() {
        dash_dataArray = new ArrayList<DashbordAdapter>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://vtalklive.com/api/Vtalk/GoLive", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String accountId = jsonObject1.getString("AccountId");
                        String name = jsonObject1.getString("Name");
                        String image = jsonObject1.getString("ProfileImage");
                        DashbordAdapter noti_screen_lists = new DashbordAdapter(accountId, name, image);
                        dash_dataArray.add(noti_screen_lists);
                        GetAstroList(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "No Astrologer Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", "LiveList");
                params.put("AccountId", "");
                params.put("Type", "ASTROLOGER");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    void getDashboardDatingAPI() {
        dash_date_dataArray = new ArrayList<DashbordDatingAdapter>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://vtalklive.com/api/Vtalk/GoLive", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                        String accountId = jsonObject1.getString("AccountId");
                        String name = jsonObject1.getString("Name");
                        String image = jsonObject1.getString("ProfileImage");
                        DashbordDatingAdapter noti_screen_lists1 = new DashbordDatingAdapter(accountId, name, image);
                        dash_date_dataArray.add(noti_screen_lists1);
                        GetDatingList(j);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No DatingGirl Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", "LiveList");
                params.put("AccountId", "");
                params.put("Type", "DGirl");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    displayView(1);
                    // hitFilterApi();
                    return true;
                case R.id.chat:
                    displayView(2);
                    return true;
                case R.id.live:
                    displayView(3);
                    return true;
                case R.id.wallet:
                    displayView(4);
                    return true;
                case R.id.profile:
                    displayView(5);
                    return true;
            }
            return false;
        }
    };

    private void displayView(int position) {
        switch (position) {
            case 1:
                break;
            case 2:
                startActivity(new Intent(getApplicationContext(), ChatList.class));
                break;
            case 3:
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), WalletScreen.class));
                break;
            case 5:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                break;
            default:
                break;
        }
    }

    public void SettingAPI(String acountid) {
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
                        wallet = jsonObject1.getString("WalletAmount");
//                        Toast.makeText(getApplicationContext(),""+jsonObject1.getString("WalletAmount"),Toast.LENGTH_LONG).show();
                        wallet_preference = getSharedPreferences("wallet_preference", MODE_PRIVATE);
                        wallet_editor = wallet_preference.edit();
                        wallet_editor.putString("Wallet_Amount", jsonObject1.getString("WalletAmount"));
                        wallet_editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DashboardScreen.this, call1, "", true);
    }


    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardScreen.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://vtalklive.com/api/Vtalk/Banner", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bandata",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                  JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(DashboardScreen.this, bannerData));
                    indicator.setViewPager(pager);
                    final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                    indicator.setRadius(2 * density);
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            pager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }

                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardScreen.this, "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardScreen.this);
        requestQueue.add(stringRequest);
    }


  /*  private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, DashboardScreen.FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)

                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.purple_500))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }*/


    private void Support_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.support, viewGroup, false);
        TextView email_tv = (TextView) dialogView.findViewById(R.id.email_tv);
        TextView number_tv = (TextView) dialogView.findViewById(R.id.number_tv);
        Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
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
}