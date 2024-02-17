package com.akp.vtalkanoop.BasicUI;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akp.vtalkanoop.Firebase.Constanta;
import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.R;
import com.akp.vtalkanoop.RetrofitAPI.ApiService;
import com.akp.vtalkanoop.RetrofitAPI.ConnectToRetrofit;
import com.akp.vtalkanoop.RetrofitAPI.GlobalAppApis;
import com.akp.vtalkanoop.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import retrofit2.Call;

import static com.akp.vtalkanoop.RetrofitAPI.API_Config.getApiClient_ByPost;
public class DatingVerify extends AppCompatActivity {
    ImageButton back_btn;
    VideoView video_upload;
    private static final String VIDEO_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private ByteArrayOutputStream byteBuffer;
    TextView clieck;
    Button buttonOk;
    String GetMobile,sinSaltoFinal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_verify);
        GetMobile=getIntent().getStringExtra("mob");
//        Toast.makeText(getApplicationContext(),GetMobile,Toast.LENGTH_LONG).show();
        video_upload=findViewById(R.id.video_upload);
        back_btn=findViewById(R.id.back_btn);
        clieck=findViewById(R.id.clieck);
        buttonOk=findViewById(R.id.buttonOk);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clieck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatingVerifyAPI(GetMobile,sinSaltoFinal2);
            }
        });
    }


    private void showPictureDialog () {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseVideoFromGallary();
                                break;
                        } }});
        pictureDialog.show();
    }

    public void chooseVideoFromGallary () {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what", "Cancel");
            return;
        }
        if (requestCode == GALLERY) {
            Log.d("what", "gale");
            if (data != null) {
                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                Log.d("path", selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                video_upload.setVideoURI(contentURI);
                video_upload.requestFocus();
                video_upload.start();
                String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                Cursor cursor = managedQuery(contentURI, projection, null, null, null);
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Log.d("File Name:",filePath);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                // Setting the thumbnail of the video in to the image view
                InputStream inputStream = null;
// Converting the video in to the bytes
                try
                {
                    inputStream = getContentResolver().openInputStream(contentURI);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                byteBuffer = new ByteArrayOutputStream();
                int len = 0;
                try
                {
                    while ((len = inputStream.read(buffer)) != -1)
                    {
                        byteBuffer.write(buffer, 0, len);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                System.out.println("converted!");
                String videoData="";
                //Converting bytes into base64
                videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                Log.d("rfghthghg " , videoData);
                sinSaltoFinal2 = videoData.trim();
                String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
                Log.d("fgdgd" , sinsinSalto2);
            }}
    }

    private void saveVideoToInternalStorage (String filePath){
        File newfile;
        try {
            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
            if (currentFile.exists()) {
                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath (Uri uri){
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void  getDatingVerifyAPI(String mobile,String videourl){
        String otp1 = new GlobalAppApis().DatingVerifyAPI(mobile,videourl);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getDatingVerifyVideo(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        Toast.makeText(getApplicationContext(),"Verify Successfully!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, DatingVerify.this, call1, "", true);
    }

}