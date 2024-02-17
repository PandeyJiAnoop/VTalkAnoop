package com.akp.vtalkanoop.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.vtalkanoop.BasicUI.DatingVerify;
import com.akp.vtalkanoop.Firebase.Constanta;
import com.akp.vtalkanoop.Firebase.OutgoingInvitationActivity;
import com.akp.vtalkanoop.Firebase.PreferenceManager;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.vtalkanoop.videocall.Dialogs.LoaderDialog;
import com.akp.vtalkanoop.videocall.Helpers.Utils;
import com.akp.vtalkanoop.videocall.Services.DisableTouch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.MIME_WHATSAPP_BUSINESS_VIDEO_CALL;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.MIME_WHATSAPP_BUSINESS_VOIP_CALL;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.MIME_WHATSAPP_VIDEO_CALL;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.MIME_WHATSAPP_VOIP_CALL;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.PACKAGE_WHATSAPP;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.PACKAGE_WHATSAPP_BUSINESS;
import static com.akp.vtalkanoop.videocall.Helpers.Constants.URL_WHATSAPP;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.contactIdByPhoneNumber;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.getAppIconFromPackage;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.getAppNameFromPackage;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.hasActiveInternetConnection;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.isAppInstalled;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.isStringValid;
import static com.akp.vtalkanoop.videocall.Helpers.Utils.saveNewContact;

public class DatingProfileDetails extends AppCompatActivity {
    ImageView back_img;
    LinearLayout dynamic_profile,dynamic_exp;
    TextView text_view_show_more;
    LinearLayout chat_online_ll,video_call_online,call_online_ll;

    private ArrayList<AstroProfileAdapter> astroimage_dataArray;
    private ArrayList<DatingExpertiesAdapter> datingexp_dataArray;
    ImageView img_showProfile;
    String ID;
    TextView name_tv,txt_chat,txt_call,txt_video;
    String Profileimages,perMinRate,voiceCallRate,videoCallRate;

    private LoaderDialog loaderDialog;
    private Utils utils;
    String mobileNo,phoneNumber;
    String deviceid;
    String walletamount,userid;
     SharedPreferences sharedPreferences;
     SharedPreferences caller_preference;
     SharedPreferences.Editor caller_editor;
    String wallet_amount;
     SharedPreferences wallet_preference;
     SharedPreferences.Editor wallet_editor;
     SharedPreferences data_preference;
     SharedPreferences.Editor data_editor;
    private PreferenceManager preferenceManager;
    private AlertDialog alertDialog;
    String call_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_profile_details);
        preferenceManager = new PreferenceManager(getApplicationContext());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ID=getIntent().getStringExtra("id");
//        walletamount=getIntent().getStringExtra("wallet_amount");
        wallet_preference = getSharedPreferences("wallet_preference", MODE_PRIVATE);
        walletamount = wallet_preference.getString("Wallet_Amount", "");

        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        back_img=findViewById(R.id.back_img);
        dynamic_exp=findViewById(R.id.dynamic_exp);
        dynamic_profile=findViewById(R.id.dynamic_profile);
        chat_online_ll=findViewById(R.id.chat_online_ll);
        video_call_online=findViewById(R.id.video_call_online);
        call_online_ll=findViewById(R.id.call_online_ll);
        text_view_show_more=findViewById(R.id.text_view_show_more);
        name_tv=findViewById(R.id.name_tv);
        img_showProfile=findViewById(R.id.img_showProfile);

        txt_chat=findViewById(R.id.txt_chat);
        txt_call=findViewById(R.id.txt_call);
        txt_video=findViewById(R.id.txt_video);

//        Toast.makeText(getApplicationContext(),""+walletamount,Toast.LENGTH_LONG).show();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAstroDetailsAPI();
        SettingAPI(userid);



        chat_online_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletamount.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a chat", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }
                else if (Double.parseDouble(walletamount) >= Double.parseDouble(perMinRate)){
                    Intent intent1 = new Intent(getApplicationContext(), ChatHistory.class);
                    intent1.putExtra("AstroId", ID);
                    intent1.putExtra("Astrotype", "DATING");
                    intent1.putExtra("Astroname", name_tv.getText().toString());
                    intent1.putExtra("wallet_amount",walletamount);
                    intent1.putExtra("chat_rate", perMinRate);
                    intent1.putExtra("callerid", userid);
                    intent1.putExtra("recieverid", ID);
                    intent1.putExtra("Astroimage", Profileimages);
                    startActivity(intent1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a chat", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }
            }
        });
        video_call_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_type="video";

                if (walletamount.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a Video call", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }
                else if (Double.parseDouble(walletamount) >= Double.parseDouble(videoCallRate)){
                    if (deviceid == null || deviceid.trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), " User is not available for meeting", Toast.LENGTH_SHORT).show();
                    } else {
                        CallingServiceAPI(userid,ID,"Calling","VideoCall","0");
                    }
//                checkPermissionsWithDexter();
//                Intent intent1=new Intent(getApplicationContext(), AgoraActivity.class);
//                startActivity(intent1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a Video call", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }
            }
        });
        call_online_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_type="audio";
                if (walletamount.equalsIgnoreCase("null")) {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a Voice call", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }
                else if (Double.parseDouble(walletamount) >= Double.parseDouble(voiceCallRate)){
                    if (deviceid == null || deviceid.trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "User is not available for meeting", Toast.LENGTH_SHORT).show();
                    } else {
                        CallingServiceAPI(userid,ID,"Calling","VoiceCall","0");

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "You Don't have sufficient Balance to make a Voice call", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),WalletScreen.class));
                }


              /*  utils = new Utils(DatingProfileDetails.this);
                loaderDialog = new LoaderDialog(DatingProfileDetails.this);
                //Initializing Dexter
                Dexter.withActivity(DatingProfileDetails.this)
                        .withPermissions(
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS,
                                Manifest.permission.CALL_PHONE
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                                    //Redirecting user to settings if permission is permanently denied
                                    utils.toast(getString(R.string.permissions_grant));
                                    utils.openAppSettings();
                                }else if (multiplePermissionsReport.areAllPermissionsGranted()){
                                    //Disabling the restricted touch are and making the call
                                    makeWhatsAppoiceCall(DatingProfileDetails.this);
//                                    startDisableTouchService();
                                }else {
                                    //Notifying user that permission has been denied
                                    utils.toast(getString(R.string.permissions_denied));
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
*/
//
//                Intent intent1=new Intent(getApplicationContext(), AgoraVoiceCall.class);
//                startActivity(intent1);
            }
        });
    }

    private void GetExpertiesList(int j) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView1 = inflater.inflate(R.layout.dynamic_experties, null);
        dynamic_exp.addView(convertView1);
        View view = new View(DatingProfileDetails.this);
        TextView tvexp=(TextView)convertView1.findViewById(R.id.tvexp);
        if (datingexp_dataArray.get(j).getResExpertise().equalsIgnoreCase("null")){
            tvexp.setText("Good communication");
        }
        tvexp.setText(datingexp_dataArray.get(j).getResExpertise());
        Log.d("resd","jhkhjk"+datingexp_dataArray.get(j).getResExpertise());
//        TextView tv=convertView.findViewById(R.id.tv);
//        tv.setText((i + 1) + ". ");
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        view.setLayoutParams(params_view);
        dynamic_exp.addView(view);
    }
    private void GetImageList(int j) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView1 = inflater.inflate(R.layout.dynamic_photo, null);
        dynamic_profile.addView(convertView1);
        View view = new View(DatingProfileDetails.this);
        ImageView img=(ImageView)convertView1.findViewById(R.id.img);
//        Glide.with(getApplicationContext())
//                .load("http://vtalk.signaturesoftware.org/"+astroimage_dataArray.get(j).getExpertise())
//                .error(R.drawable.girl).fitCenter().placeholder(R.drawable.loadinggif)
//                .into(img);
//        Picasso.with(DatingProfileDetails.this).load("https://vtalklive.com/"+astroimage_dataArray.get(j).getExpertise()).placeholder(R.drawable.loadinggif).error(R.drawable.girl).fit().into(img);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(astroimage_dataArray.get(j).getExpertise())
                .into(new BitmapImageViewTarget(img) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        img.setImageBitmap(resource);
                    }
                });

//        Toast.makeText(getApplicationContext(),astroimage_dataArray.get(j).getExpertise(),Toast.LENGTH_LONG).show();
//        Log.d("res","dd"+astroimage_dataArray.get(j).getExpertise());

        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        view.setLayoutParams(params_view);
        dynamic_profile.addView(view);
    }
    public  void  getAstroDetailsAPI(){
        astroimage_dataArray = new ArrayList<AstroProfileAdapter>();
        datingexp_dataArray = new ArrayList<DatingExpertiesAdapter>();
        String otp1 = new GlobalAppApis().ProfileDetails(ID);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getdetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
//                    Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                    Log.d("res","ress"+result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject getDetails = jsonArrayr.getJSONObject(i);
                        String experiance = getDetails.getString("Experiance");
                        Profileimages = getDetails.getString("UserImg");
                        String language = getDetails.getString("Language");
                        mobileNo = getDetails.getString("MobileNo");
                        String name = getDetails.getString("FirstName");
                        perMinRate = getDetails.getString("PerMinRate");
                        String rating = getDetails.getString("Rating");
                        String ProfileDescription = getDetails.getString("ProfileDescription");
                        String lastName = getDetails.getString("LastName");
                        String emailId = getDetails.getString("EmailId");
                        deviceid = getDetails.getString("DeviceId");
                        videoCallRate = getDetails.getString("VideoCallRate");
                        voiceCallRate = getDetails.getString("VoiceCallRate");
                        String address = getDetails.getString("Address");
                        name_tv.setText(name);
                        text_view_show_more.setText(ProfileDescription+",i'm from "+address);
                        txt_call.setText(perMinRate);
                        txt_video.setText(videoCallRate);
                        txt_chat.setText(voiceCallRate);

//                        Picasso.get()
//                                .load(Profileimages)
//                                .placeholder(R.drawable.loadinggif)
//                                .error(R.drawable.loadinggif)
//                                .into(img_showProfile);
                        Glide.with(DatingProfileDetails.this).load("url").into(img_showProfile);


//                        Picasso.with(DatingProfileDetails.this).load("https://vtalklive.com/"+Profileimages).placeholder(R.drawable.loadinggif).error(R.drawable.girl).fit().into(img_showProfile);
                        if (perMinRate.equalsIgnoreCase("null") || videoCallRate.equalsIgnoreCase("null") || voiceCallRate.equalsIgnoreCase("null")){
                            Toast.makeText(getApplicationContext(),"Price not available",Toast.LENGTH_LONG).show();
                            chat_online_ll.setClickable(false);
                            call_online_ll.setClickable(false);
                            video_call_online.setClickable(false);
                            chat_online_ll.setEnabled(false);
                            call_online_ll.setEnabled(false);
                            video_call_online.setEnabled(false);
                        }
                        else {
                            chat_online_ll.setClickable(true);
                            call_online_ll.setClickable(true);
                            video_call_online.setClickable(true);
                            chat_online_ll.setEnabled(true);
                            call_online_ll.setEnabled(true);
                            video_call_online.setEnabled(true);
                        }
                    }

                    JSONArray jsonArrayrimg = jsonObject.getJSONArray("Photo");
                    for (int k = 0; k < jsonArrayrimg.length(); k++) {
                        JSONObject getDetailsimg = jsonArrayrimg.getJSONObject(k);
                        String imageName = getDetailsimg.getString("UserImage");
                        AstroProfileAdapter astrolistimg = new AstroProfileAdapter(imageName);
                        astroimage_dataArray.add(astrolistimg);
                        GetImageList(k);
                    }

                    JSONArray jsonArrayrex = jsonObject.getJSONArray("Expertise");
                    Log.d("skdjskd","sdsdhs"+jsonArrayrex);
                    for (int j = 0; j < jsonArrayrex.length(); j++) {
                        JSONObject getDetailsex = jsonArrayrex.getJSONObject(j);
                        String resExpertise = getDetailsex.getString("ResExpertise");

                        DatingExpertiesAdapter astrolist1 = new DatingExpertiesAdapter(resExpertise);
                        datingexp_dataArray.add(astrolist1);
                        GetExpertiesList(j);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DatingProfileDetails.this, call1, "", true);
    }








    //    Video call integration
    private class SaveContactAndCall extends AsyncTask<Void, Void, Void> {
        String contactID, packageName, whatsAppMime;
        boolean noInternet = false;

        public SaveContactAndCall(String packageName, String whatsAppMime) {
            this.packageName = packageName;
            this.whatsAppMime = whatsAppMime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing loader dialog
            loaderDialog.show();
            //Getting the typed phone number
            phoneNumber = "+91"+mobileNo;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (hasActiveInternetConnection(DatingProfileDetails.this, URL_WHATSAPP)){
                //Getting contact ID from phone number
                contactID = contactIdByPhoneNumber(DatingProfileDetails.this, phoneNumber, whatsAppMime);

                //Lets proceed if the phone number is not saved
                if (!isStringValid(contactID)){
                    //Saving the number as contact & getting the ID
                    saveNewContact(phoneNumber, phoneNumber, getContentResolver());
                    /*Adding a delay to let WhatsApp update MIME details
                     * of the contact*/
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contactID = contactIdByPhoneNumber(DatingProfileDetails.this, phoneNumber, whatsAppMime);
                        }
                    }, 5000);
                }
            }else {
                noInternet = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Dismissing the progress bar
            loaderDialog.dismiss();


            /*Finally making the video call after checking if the device has active internet
             * connection or not*/
            if (noInternet){
                utils.toast(getString(R.string.no_internet));
            }else if (isStringValid(contactID)){
                utils.launchWhatsAppCall(contactID, packageName, whatsAppMime, DatingProfileDetails.this);
            }else {
                utils.toast(getString(R.string.not_whatsapp_number));
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                if (ActivityCompat.checkSelfPermission(DatingProfileDetails.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        }
    }

    private void checkPermissionsWithDexter(){
        //Initializing Dexter
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.CALL_PHONE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                            //Redirecting user to settings if permission is permanently denied
                            utils.toast(getString(R.string.permissions_grant));
                            utils.openAppSettings();
                        }else if (multiplePermissionsReport.areAllPermissionsGranted()){
                            //Disabling the restricted touch are and making the call
                            makeWhatsAppVideoCall(DatingProfileDetails.this);
//                            startDisableTouchService();
                        }else {
                            //Notifying user that permission has been denied
                            utils.toast(getString(R.string.permissions_denied));
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

  /*  private void restrictAreaAndCall(){
        //Before Android M we didn't need any permission to draw over apps
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startDisableTouchService();
            makeWhatsAppVideoCall(ChatActivity.this);
        }

        //Checking if permission has been granted to draw over apps
        else if (Settings.canDrawOverlays(this)) {
            startDisableTouchService();
            makeWhatsAppVideoCall(ChatActivity.this);
        }

        //Launching overlay permission settings
        else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 10);
            utils.toast(getString(R.string.allow_overlay));
        }
    }*/

    private void startDisableTouchService(){
        Intent intent = new Intent(this, DisableTouch.class);
        ContextCompat.startForegroundService(this, intent);

    }

    private void makeWhatsAppVideoCall(Context context){
        if (isAppInstalled(context, PACKAGE_WHATSAPP) && isAppInstalled(context, PACKAGE_WHATSAPP_BUSINESS)){
            showAppOptionsToCall(context);
        }else if (isAppInstalled(context, PACKAGE_WHATSAPP)){
            new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP, MIME_WHATSAPP_VIDEO_CALL).execute();
        }else if (isAppInstalled(context, PACKAGE_WHATSAPP_BUSINESS)){
            new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP_BUSINESS, MIME_WHATSAPP_BUSINESS_VIDEO_CALL).execute();
        }else {
            utils.toast(getString(R.string.whatsapp_not_found));
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
            if (ActivityCompat.checkSelfPermission(DatingProfileDetails.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }

    private void showAppOptionsToCall(final Context context){
        BottomSheetBuilder builder = new BottomSheetBuilder(context);
        builder.setMode(BottomSheetBuilder.MODE_LIST);
        builder.expandOnStart(true);
        builder.addTitleItem(getString(R.string.call_via));

        //Adding WhatsApps to the list
        builder.addItem(0, getAppNameFromPackage(context, PACKAGE_WHATSAPP), getAppIconFromPackage(context, PACKAGE_WHATSAPP));
        builder.addItem(1, getAppNameFromPackage(context, PACKAGE_WHATSAPP_BUSINESS), getAppIconFromPackage(context, PACKAGE_WHATSAPP_BUSINESS));


        builder.setItemClickListener(new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP, MIME_WHATSAPP_VIDEO_CALL).execute();
                        break;
                    case 1:
                        new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP_BUSINESS, MIME_WHATSAPP_BUSINESS_VIDEO_CALL).execute();
                        break;
                }
            }
        });
        builder.createDialog().show();
    }


    private void makeWhatsAppoiceCall(Context context){
        if (isAppInstalled(context, PACKAGE_WHATSAPP) && isAppInstalled(context, PACKAGE_WHATSAPP_BUSINESS)){
            showAppOptionsToVoiceCall(context);
        }else if (isAppInstalled(context, PACKAGE_WHATSAPP)){
            new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP, MIME_WHATSAPP_VOIP_CALL).execute();
        }else if (isAppInstalled(context, PACKAGE_WHATSAPP_BUSINESS)){
            new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP_BUSINESS, MIME_WHATSAPP_BUSINESS_VOIP_CALL).execute();
        }else {
            utils.toast(getString(R.string.whatsapp_not_found));
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
            if (ActivityCompat.checkSelfPermission(DatingProfileDetails.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }

    private void showAppOptionsToVoiceCall(final Context context){
        BottomSheetBuilder builder1 = new BottomSheetBuilder(context);
        builder1.setMode(BottomSheetBuilder.MODE_LIST);
        builder1.expandOnStart(true);
        builder1.addTitleItem(getString(R.string.call_via));

        //Adding WhatsApps to the list
        builder1.addItem(0, getAppNameFromPackage(context, PACKAGE_WHATSAPP), getAppIconFromPackage(context, PACKAGE_WHATSAPP));
        builder1.addItem(1, getAppNameFromPackage(context, PACKAGE_WHATSAPP_BUSINESS), getAppIconFromPackage(context, PACKAGE_WHATSAPP_BUSINESS));


        builder1.setItemClickListener(new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item1) {
                switch (item1.getItemId()){
                    case 0:
                        new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP, MIME_WHATSAPP_VOIP_CALL).execute();
                        break;
                    case 1:
                        new DatingProfileDetails.SaveContactAndCall(PACKAGE_WHATSAPP_BUSINESS, MIME_WHATSAPP_BUSINESS_VOIP_CALL).execute();
                        break; } } });

        builder1.createDialog().show();
    }


    public void CallingServiceAPI(String caller_acountid,String reciever_accountid,String status,
                                  String calling_type,String calling_time){
        String otp1 = new GlobalAppApis().CallingApi(caller_acountid,reciever_accountid,
                status,calling_type,calling_time);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCalling(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {

//                Log.d("dffdfd","dd"+result);

//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);

                        data_preference = getSharedPreferences("data_preference", MODE_PRIVATE);
                        data_editor = data_preference.edit();
                        data_editor.putString("data_caller",jsonObject1.getString("CallerAccountId"));
                        data_editor.putString("data_reciever",jsonObject1.getString("ReceiverAccountId"));
                        data_editor.putString("data_callrate",jsonObject1.getString("CallCharge"));
                        data_editor.commit();

                        preferenceManager.putString(Constanta.KEY_Caller_ID, jsonObject1.getString("CallerAccountId"));
                        preferenceManager.putString(Constanta.KEY_Reciever_ID, jsonObject1.getString("ReceiverAccountId"));
                        preferenceManager.putString(Constanta.KEY_Rate, jsonObject1.getString("CallCharge"));


//                        payment_tv.setText(jsonObject1.getString("WalletAmount"));
                        if (jsonObject1.getString("Status").equalsIgnoreCase("true")) {
                            caller_preference = getSharedPreferences("callerpreference", MODE_PRIVATE);
                            caller_editor = caller_preference.edit();
                            caller_editor.putString("callerid",userid);
                            caller_editor.putString("recieverid",ID);
                            caller_editor.putString("chat_rate",voiceCallRate);
                            caller_editor.apply();//I added this line and started to work...
                            caller_editor.commit();
                            Intent intent = new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
                            intent.putExtra("type", call_type);
                            intent.putExtra("device_id", deviceid);
                            intent.putExtra("Astroname", name_tv.getText().toString());
                            intent.putExtra("callerid", userid);
                            intent.putExtra("recieverid", ID);
                            intent.putExtra("Astroimage", Profileimages);
                            intent.putExtra("chat_rate",voiceCallRate);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Calling.."+jsonObject1.getString("Msg"),Toast.LENGTH_LONG).show();
                        }
                        if (jsonObject1.getString("Status").equalsIgnoreCase("false")) {
                            Toast.makeText(getApplicationContext(),jsonObject1.getString("Msg"),Toast.LENGTH_LONG).show();
                            AnotherCallPopup();
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DatingProfileDetails.this, call1, "", true);
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
                        wallet_amount= jsonObject1.getString("WalletAmount");
                        wallet_preference = getSharedPreferences("wallet_preference", MODE_PRIVATE);
                        wallet_editor = wallet_preference.edit();
                        wallet_editor.putString("WalletAmount",jsonObject1.getString("WalletAmount"));
                        wallet_editor.commit();
                        String type= jsonObject1.getString("UserType");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DatingProfileDetails.this, call1, "", true);
    }

    private void AnotherCallPopup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.successfully_bid_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

}