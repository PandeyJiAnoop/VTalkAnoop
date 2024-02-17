package com.akp.vtalkanoop;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.akp.vtalkanoop.Home.ChatHistory;
import com.akp.vtalkanoop.Home.ChatList;
public class LiveUserList extends AppCompatActivity {
    ImageView back_img;
    LinearLayout dynamically_chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_user_list);
        back_img = findViewById(R.id.back_img);
        dynamically_chatList = findViewById(R.id.dynamically_chatList);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < 2; i++) {
            GetChatList();
        }
    }

    private void GetChatList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dynamic_liveuser, null);
        dynamically_chatList.addView(convertView);
        View view = new View(LiveUserList.this);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(), AgoraActivity.class);
//                startActivity(intent);
            }
        });
//        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
//        view.setLayoutParams(params_view);
//        dynamically_chatList.addView(view);
    }
}