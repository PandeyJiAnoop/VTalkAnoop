package com.akp.vtalkanoop.BasicUI;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akp.vtalkanoop.GetAddressIntentService;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.vtalkanoop.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;

public class RegistrationScreen extends AppCompatActivity {
    TextView login;
    Button btn_register;
    TextInputEditText input_email,input_address, input_mobile, input_password, input_name,input_exp,input_language,input_price,input_price_voice,input_price_video;
    Spinner type;
    String[] spinnerValueHoldValue = {"Customer", "Astrology", "Friendship"};
    String[] spinnerValueHoldValue1 = {"Both", "Astrology", "Friendship"};
    String selected_type,Intrest_type;
    CircleImageView img_showProfile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    private TextInputLayout input_ll,lang_ll,price_ll,price_ll_voice,price_ll_video;
    RelativeLayout intrest_rl;
    Spinner intrest;
    private AlertDialog alertDialog;
    TextView selectintrest_tv;
    String token;
    TextView view_details_tv;
    private ProgressDialog dialog;

//    enable location
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

//    getcurrentlocation
   private FusedLocationProviderClient fusedLocationClient;
   private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
   private LocationAddressResultReceiver addressResultReceiver;
   private Location currentLocation;
   private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        //enable Gps method
        enableLoc();
//       getCurrentlocation
        addressResultReceiver = new LocationAddressResultReceiver(new Handler());
        input_address = findViewById(R.id.input_address);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            };
        };
        startLocationUpdates();


        login = findViewById(R.id.login);
        btn_register = findViewById(R.id.btn_register);
        input_name = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_mobile = findViewById(R.id.input_mobile);
        input_password = findViewById(R.id.input_password);
        input_exp = findViewById(R.id.input_exp);
        input_language = findViewById(R.id.input_language);
        type = findViewById(R.id.type);
        img_showProfile=findViewById(R.id.img_showProfile);
        input_ll=findViewById(R.id.input_ll);
        lang_ll=findViewById(R.id.lang_ll);
        intrest_rl=findViewById(R.id.intrest_rl);
        intrest=findViewById(R.id.intrest);
        price_ll=findViewById(R.id.price_ll);
        input_price=findViewById(R.id.input_price);

        price_ll_voice=findViewById(R.id.price_ll_voice);
        input_price_voice=findViewById(R.id.input_price_voice);

        price_ll_video=findViewById(R.id.price_ll_video);
        input_price_video=findViewById(R.id.input_price_video);
        view_details_tv=findViewById(R.id.view_details_tv);

        selectintrest_tv=findViewById(R.id.selectintrest_tv);
        img_showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            token = instanceIdResult.getToken();
            Log.e("newToken", token);
        });

        view_details_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsconditionpopup();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationScreen.this,
                android.R.layout.simple_list_item_1, spinnerValueHoldValue);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                           @Override
                                           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                               if (type.getSelectedItem().toString().equalsIgnoreCase("Customer")) {
                                                   selected_type = "1";
                                                   input_ll.setVisibility(View.GONE);
                                                   lang_ll.setVisibility(View.GONE);
                                                   intrest_rl.setVisibility(View.VISIBLE);
                                                   selectintrest_tv.setVisibility(View.VISIBLE);
                                                   price_ll.setVisibility(View.GONE);
                                                   input_price.setVisibility(View.GONE);

                                                   price_ll_voice.setVisibility(View.GONE);
                                                   input_price_voice.setVisibility(View.GONE);

                                                   price_ll_video.setVisibility(View.GONE);
                                                   input_price_video.setVisibility(View.GONE);


                                               } else if (type.getSelectedItem().toString().equalsIgnoreCase("Astrology")) {
                                                   selected_type = "3";
                                                   input_ll.setVisibility(View.VISIBLE);
                                                   lang_ll.setVisibility(View.VISIBLE);
                                                   intrest_rl.setVisibility(View.GONE);
                                                   selectintrest_tv.setVisibility(View.GONE);
                                                   price_ll.setVisibility(View.VISIBLE);
                                                   input_price.setVisibility(View.VISIBLE);
                                                   price_ll_voice.setVisibility(View.VISIBLE);
                                                   input_price_voice.setVisibility(View.VISIBLE);

                                                   price_ll_video.setVisibility(View.VISIBLE);
                                                   input_price_video.setVisibility(View.VISIBLE);

                                               } else if (type.getSelectedItem().toString().equalsIgnoreCase("Friendship")) {
                                                   selected_type = "2";
                                                   input_ll.setVisibility(View.VISIBLE);
                                                   lang_ll.setVisibility(View.VISIBLE);
                                                   intrest_rl.setVisibility(View.GONE);
                                                   selectintrest_tv.setVisibility(View.GONE);
                                                   price_ll.setVisibility(View.VISIBLE);
                                                   input_price.setVisibility(View.VISIBLE);

                                                   price_ll_voice.setVisibility(View.VISIBLE);
                                                   input_price_voice.setVisibility(View.VISIBLE);

                                                   price_ll_video.setVisibility(View.VISIBLE);
                                                   input_price_video.setVisibility(View.VISIBLE);

//                                                   withdraw_popup();
                                               }
//                Toast.makeText(RegistrationScreen.this, selected_type, Toast.LENGTH_LONG).show();
                                           }
                                           @Override
                                           public void onNothingSelected(AdapterView<?> parent) { // TODO Auto-generated method stub } }); } }
                                           }
                                       });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(RegistrationScreen.this,
                android.R.layout.simple_list_item_1, spinnerValueHoldValue1);
        intrest.setAdapter(adapter1);
        intrest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (intrest.getSelectedItem().toString().equalsIgnoreCase("Both")) {
                    Intrest_type = "Both";
                } else if (intrest.getSelectedItem().toString().equalsIgnoreCase("Astrology")) {
                    Intrest_type = "Astrology";
                } else if (intrest.getSelectedItem().toString().equalsIgnoreCase("Friendship")) {
                    Intrest_type = "Friendship";
                }
//                Toast.makeText(RegistrationScreen.this, selected_type, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { // TODO Auto-generated method stub } }); } }
            }
        });
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(intent);
                    }
                });

                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (input_name.getText().toString().equalsIgnoreCase("")) {
                            input_name.setError("Fields can't be blank!");
                            input_name.requestFocus();
                        } else if (input_email.getText().toString().equalsIgnoreCase("")) {
                            input_email.setError("Fields can't be blank!");
                            input_email.requestFocus();
                        } else if (input_mobile.getText().toString().equalsIgnoreCase("")) {
                            input_mobile.setError("Fields can't be blank!");
                            input_mobile.requestFocus();
                        } else if (input_password.getText().toString().equalsIgnoreCase("")) {
                            input_password.setError("Fields can't be blank!");
                            input_password.requestFocus();
                        }
                        else {
//                            Log.d("ewewrwerwewerrwe","df"+temp);
                                if (type.getSelectedItem().toString().equalsIgnoreCase("Customer")) {
                                    getRegisterAPI(input_name.getText().toString(), input_email.getText().toString(), input_mobile.getText().toString(), input_password.getText().toString(), selected_type,"","",temp,Intrest_type,token,"0","0","0",input_address.getText().toString());
                                } else if (type.getSelectedItem().toString().equalsIgnoreCase("Astrology")) {
                                    if (img_showProfile.getDrawable()==null){
                                        Toast.makeText(getApplicationContext(),"Profile Not Uploaded!",Toast.LENGTH_LONG).show();
//The imageView is empty
                                    }
                                    else {
                                        getRegisterAPI(input_name.getText().toString(), input_email.getText().toString(), input_mobile.getText().toString(), input_password.getText().toString(), selected_type,input_exp.getText().toString(),input_language.getText().toString(),temp,"",token,
                                                input_price.getText().toString(),input_price_video.getText().toString(),input_price_voice.getText().toString(),input_address.getText().toString());
                                    }

                                } else if (type.getSelectedItem().toString().equalsIgnoreCase("Friendship")) {
                                    if (img_showProfile.getDrawable() == null) {
                                        Toast.makeText(getApplicationContext(), "Profile Not Uploaded!", Toast.LENGTH_LONG).show();
//The imageView is empty
                                    } else {
                                        getRegisterAPI(input_name.getText().toString(), input_email.getText().toString(), input_mobile.getText().toString(), input_password.getText().toString(), selected_type, input_exp.getText().toString(), input_language.getText().toString(), temp, "", token,
                                                input_price.getText().toString(), input_price_video.getText().toString(), input_price_voice.getText().toString(),input_address.getText().toString());
                                    }
                                }
// The imageView is occupied.
                            }
                        }
                });
            }



    public void getRegisterAPI(String name, String email, String mobile, String pass, String usertype,String experience,String language,String file,String intrest,String token,String voice,String video,String chat,String add) {
                String otp1 = new GlobalAppApis().Register(name, email, mobile, pass, usertype,experience,language,file,intrest,token,voice,video,chat,add);
                ApiService client1 = getApiClient_ByPost();
                Call<String> call1 = client1.getRegister(otp1);
                new ConnectToRetrofit(new RetrofitCallBackListenar() {
                    @Override
                    public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                      Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                            for (int i = 0; i < jsonArrayr.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                if (jsonObject1.getString("mes").equalsIgnoreCase("Dating Succesfully Registered")){
                                    Toast.makeText(getApplicationContext(), "Register Successfully, please verify!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), DatingVerify.class);
                                    intent.putExtra("mob",input_mobile.getText().toString());
                                    startActivity(intent);

                                }
                                else if (jsonObject1.getString("mes").equalsIgnoreCase("Successfully Inserted")){
                                    Toast.makeText(getApplicationContext(), "Register Successfully!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                                    startActivity(intent);
                                }
                                else if (jsonObject1.getString("mes").equalsIgnoreCase("Mobile Number Already Exists")){
                                    Toast.makeText(getApplicationContext(), "Mobile Number Already Exists!", Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Please check your details!" + e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, RegistrationScreen.this, call1, "", true);
            }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission not granted, " +
                                    "restart the app if you want the feature",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }


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

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(RegistrationScreen.this);
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

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        img_showProfile.setImageBitmap(bm);
            Bitmap immagex=bm;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b,Base64.DEFAULT);

    }


  /*  @Override
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
        final CharSequence[] items = {
                "Choose from Library",
                "Cancel"
        };
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(RegistrationScreen.this);
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
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,
                Base64.DEFAULT);
        Log.d("dd",";dd"+temp);
        temp1 = temp.trim();
        sinsinSalto2 = temp1.replaceAll("\n", "");
        Log.d("fgdgd" , sinsinSalto2);

        img_showProfile.setImageBitmap(bm);
    }
*/
    private void termsconditionpopup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.terms_conditionpopup, viewGroup, false);
        WebView webview = (WebView) dialogView.findViewById(R.id.webview);
        ImageView cancel_img = (ImageView) dialogView.findViewById(R.id.cancel_img);

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            // This method will be triggered when the Page Started Loading

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog = ProgressDialog.show(RegistrationScreen.this, null,
                        "Please Wait...");
                dialog.setCancelable(true);
                super.onPageStarted(view, url, favicon);
            }

            // This method will be triggered when the Page loading is completed
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }
            // This method will be triggered when error page appear
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                dialog.dismiss();
                // You can redirect to your own page instead getting the default
                // error page
                Toast.makeText(RegistrationScreen.this,
                        "The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
                webview.loadUrl("https://vtalklive.com/term-condition.aspx");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        WebSettings webSettings = webview.getSettings();
        webview.loadUrl("https://vtalklive.com/term-condition.aspx");
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);



        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }



    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(RegistrationScreen.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(RegistrationScreen.this, REQUEST_LOCATION);
//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }



//    getcurrentlocation
@SuppressWarnings("MissingPermission")
private void startLocationUpdates() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    } else {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null);
    }
}

    @SuppressWarnings("MissingPermission")
    private void getAddress() {

        if (!Geocoder.isPresent()) {
            Toast.makeText(RegistrationScreen.this,
                    "Can't find current address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);

    }
    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(RegistrationScreen.this,
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");

            showResults(currentAdd);
        }
    }
    private void showResults(String currentAdd){
        input_address.setText(currentAdd);
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}