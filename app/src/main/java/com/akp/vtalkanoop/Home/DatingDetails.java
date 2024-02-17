package com.akp.vtalkanoop.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akp.vtalkanoop.LiveStreaming;
import com.akp.vtalkanoop.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatingDetails extends AppCompatActivity {
ImageView back_img;
RecyclerView data_recyclerView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        back_img=findViewById(R.id.back_img);
        data_recyclerView=findViewById(R.id.data_recyclerView);
        fab=findViewById(R.id.fab);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDashboardAPI("2");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LiveStreaming.class);
                intent.putExtra("type","DGirl");
                startActivity(intent);
            }
        });

    }


    void getDashboardAPI(String userType) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://vtalklive.com/api/Vtalk/UserList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("AccountId", jsonObject1.getString("AccountId"));
                        hm.put("Name", jsonObject1.getString("Name"));
                        hm.put("IsLive", jsonObject1.getString("IsLive"));
                        hm.put("PerMinRate", jsonObject1.getString("PerMinRate"));
                        hm.put("Experiance", jsonObject1.getString("Experiance"));
                        hm.put("Languages", jsonObject1.getString("Languages"));
                        hm.put("ProfileImage", jsonObject1.getString("ProfileImage"));
                        arrayList.add(hm);

                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DatingDetails.this, 2);
                    DatingAdapter walletHistoryAdapter = new DatingAdapter(DatingDetails.this, arrayList);
                    data_recyclerView.setLayoutManager(gridLayoutManager);
                    data_recyclerView.setAdapter(walletHistoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserType", userType);
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

}