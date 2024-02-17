package com.akp.vtalkanoop.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.BasicUI.SplashScreen;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.vtalkanoop.StukCallActivity;
import com.akp.vtalkanoop.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class Profile extends AppCompatActivity {
    private ImageView  back_img;
    AppCompatButton logout_img;
    AppCompatButton update_btn;
    TextView mobile_tv, name_text, payment_tv;
    TextInputEditText name_et, email_et, mobile_et, about_et,expertise_et,rate_et,address_et,rate_call_et,rate_video_et;
    CircleImageView profile_img;
    private SharedPreferences sharedPreferences;
    String userid,RoleId;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    CardView add_photo_card;
    SwipeRefreshLayout srl_refresh;
    TextInputLayout experties_ll,rate_ll,rate_call_ll,rate_video_ll;
    private String OnlineStatus;
    TextView online_tv;
    AppCompatButton stukcall_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        RoleId = sharedPreferences.getString("Usertype", "");
//        Toast.makeText(getApplicationContext(),""+userid,Toast.LENGTH_LONG).show();

        add_photo_card=findViewById(R.id.add_photo_card);

        logout_img = findViewById(R.id.logout_img);
        back_img = findViewById(R.id.back_img);
        update_btn = findViewById(R.id.update_btn);
        online_tv=findViewById(R.id.online_tv);

        name_text = findViewById(R.id.name_text);
        mobile_tv = findViewById(R.id.mobile_tv);
        payment_tv = findViewById(R.id.payment_tv);
        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        mobile_et = findViewById(R.id.mobile_et);
        about_et = findViewById(R.id.about_et);

        profile_img = findViewById(R.id.profile_img);
        expertise_et=findViewById(R.id.expertise_et);
        srl_refresh=findViewById(R.id.srl_refresh);

        rate_et=findViewById(R.id.rate_et);
        experties_ll=findViewById(R.id.experties_ll);
        rate_ll=findViewById(R.id.rate_ll);
        rate_call_ll=findViewById(R.id.rate_call_ll);
        rate_video_ll=findViewById(R.id.rate_video_ll);

        address_et=findViewById(R.id.address_et);
        rate_call_et=findViewById(R.id.rate_call_et);
        rate_video_et=findViewById(R.id.rate_video_et);

        stukcall_btn=findViewById(R.id.stukcall_btn);

        SettingAPI(userid);
        getAstroDetailsAPI(userid);

        if (RoleId.equalsIgnoreCase("Guest")){
            rate_ll.setVisibility(View.GONE);
            experties_ll.setVisibility(View.GONE);
            rate_call_ll.setVisibility(View.GONE);
            rate_video_ll.setVisibility(View.GONE);
            add_photo_card.setVisibility(View.GONE);
            stukcall_btn.setVisibility(View.GONE);
        }
        else {
            stukcall_btn.setVisibility(View.VISIBLE);
            rate_ll.setVisibility(View.VISIBLE);
            experties_ll.setVisibility(View.VISIBLE);
            rate_video_ll.setVisibility(View.VISIBLE);
            rate_call_ll.setVisibility(View.VISIBLE);
            add_photo_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoliveAPI("ActiveORInActive",userid,"");

                }
            });

        }
//handle button click
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(Profile.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAstroDetailsAPI(userid);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(Profile.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        stukcall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), StukCallActivity.class);
                startActivity(intent);
            }
        });



        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this); //Home is name of the activity
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        startActivity(intent);
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

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoleId.equalsIgnoreCase("Guest")) {
                    rate_ll.setVisibility(View.GONE);
                    experties_ll.setVisibility(View.GONE);
                    EditAstroDetailsAPI(name_et.getText().toString(),
                            email_et.getText().toString(), about_et.getText().toString(), userid, temp, "0", "0","0","0",address_et.getText().toString());
                    getAstroDetailsAPI(userid);


                    //                    if (temp == null) {
//                        EditAstroDetailsAPI(name_et.getText().toString(),
//                                email_et.getText().toString(), about_et.getText().toString(), userid, "", "", "0");
//                    } else {
//                        EditAstroDetailsAPI(name_et.getText().toString(),
//                                email_et.getText().toString(), about_et.getText().toString(), userid, temp, "", "0");
//                    }
                }
                else {
                    rate_ll.setVisibility(View.VISIBLE);
                    experties_ll.setVisibility(View.VISIBLE);
                    Log.d("gchbfgh","fgh"+name_et.getText().toString()+
                            email_et.getText().toString()+about_et.getText().toString()+userid+temp+expertise_et.getText().toString()+rate_et.getText().toString()+rate_video_et.getText().toString()+rate_call_et.getText().toString()+address_et.getText().toString());

                    EditAstroDetailsAPI(name_et.getText().toString(),
                            email_et.getText().toString(), about_et.getText().toString(), userid,temp,expertise_et.getText().toString(),rate_et.getText().toString(),rate_video_et.getText().toString(),rate_call_et.getText().toString(),address_et.getText().toString());
                    getAstroDetailsAPI(userid);


                }
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DashboardScreen.class);
                startActivity(intent);
            }
        });
    }


    public void getAstroDetailsAPI(String userid) {
        String otp1 = new GlobalAppApis().ProfileDetails(userid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getdetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String experiance = getDetails.getString("Experiance");
                        String userImg = getDetails.getString("UserImg");
                        String language = getDetails.getString("Language");
                        String mobileNo = getDetails.getString("MobileNo");
                        String name = getDetails.getString("FirstName");
                        String perMinRate = getDetails.getString("PerMinRate");
                        String rating = getDetails.getString("Rating");
                        String email = getDetails.getString("EmailId1");
                        String expertise1 = getDetails.getString("Expertise1");
                        String ProfileDescription = getDetails.getString("ProfileDescription");
                        String lastName = getDetails.getString("LastName");
                        String videoCallRate = getDetails.getString("VideoCallRate");
                        String voiceCallRate = getDetails.getString("VoiceCallRate");
                        String address = getDetails.getString("Address");
                        address_et.setText(address);
                        rate_call_et.setText(voiceCallRate);
                        rate_video_et.setText(videoCallRate);

                        name_text.setText(name);
                        mobile_tv.setText(mobileNo);
                        name_et.setText(name);
                        email_et.setText(email);
                        mobile_et.setText(mobileNo);

                        about_et.setText(ProfileDescription);
                        rate_et.setText(perMinRate);
                        expertise_et.setText(expertise1);
                        Log.d("rs","sdsd"+userImg);
//                        Glide.with(getApplicationContext()).asBitmap().load(profile).into(profile_img);
                        Glide.with(Profile.this).load(userImg).into(profile_img);

//                        Picasso.get()
//                                .load(userImg)
//                                .placeholder(R.drawable.loadinggif)
//                                .error(R.drawable.loadinggif)
//                                .into(profile_img);

//                        Toast.makeText(getApplicationContext(),userImg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Profile.this, call1, "", true);
    }


    public void EditAstroDetailsAPI(String name,String email,String about,String userid,String filename,String expertise,String rate,
                                    String videocall,String voicecall,String addrss) {
        String otp1 = new GlobalAppApis().EditProfileDetails(name,email,about,userid,filename,expertise,rate,videocall,voicecall,addrss);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getEditProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
//                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String lastName = getDetails.getString("id");
                        Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();
                        getAstroDetailsAPI(userid);
                    }
//                    Toast.makeText(getApplicationContext(),""+jsonObject.getJSONArray("Message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Profile.this, call1, "", true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Profile.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,
                Base64.DEFAULT);
        Log.d("dd",";dd"+temp);
        profile_img.setImageBitmap(bm);
//        if (RoleId.equalsIgnoreCase("Guest")){
//            rate_ll.setVisibility(View.GONE);
//            experties_ll.setVisibility(View.GONE);
//            if (temp == null) {
//                EditAstroDetailsAPI(name_et.getText().toString(),
//                        email_et.getText().toString(), about_et.getText().toString(), userid, "", "0", "0","0","0",address_et.getText().toString());
//            } else {
//                EditAstroDetailsAPI(name_et.getText().toString(),
//                        email_et.getText().toString(), about_et.getText().toString(), userid, temp, "0", "0","0","0",address_et.getText().toString());
//            }
//        }
//        else {
//            rate_ll.setVisibility(View.VISIBLE);
//            experties_ll.setVisibility(View.VISIBLE);
//            EditAstroDetailsAPI(name_et.getText().toString(),
//                    email_et.getText().toString(), about_et.getText().toString(), userid,temp,expertise_et.getText().toString(),rate_et.getText().toString(),rate_video_et.getText().toString(),rate_call_et.getText().toString(),address_et.getText().toString());
//        }
    }






    public  void  GoliveAPI(String action,String acountid,String type){
        String otp1 = new GlobalAppApis().GoLiveAPI(action,acountid,type);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getGoLive(otp1);
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
                        hm.put("Sttaus", jsonObject1.getString("Sttaus"));
                        OnlineStatus= jsonObject1.getString("Sttaus");
                        online_tv.setText(OnlineStatus);
                      Toast.makeText(getApplicationContext(),jsonObject1.getString("Sttaus"),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Profile.this, call1, "", true);
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
                        payment_tv.setText(jsonObject1.getString("WalletAmount"));

                        if (jsonObject1.getString("IsLive").equalsIgnoreCase("false")){
                            online_tv.setText("Offline");
                        }
                        else {
                            online_tv.setText("Online");
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Profile.this, call1, "", true);
    }













}