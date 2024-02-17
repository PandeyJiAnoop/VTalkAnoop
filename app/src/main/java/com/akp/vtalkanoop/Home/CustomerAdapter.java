package com.akp.vtalkanoop.Home;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;


import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    String Type;
    public CustomerAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public CustomerAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_chat, viewGroup, false);
        CustomerAdapter.VH viewHolder = new CustomerAdapter.VH(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context,AstroChatScreen.class);
//                intent.getStringExtra(arrayList.get(i).get("AccountId"));
//                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
        vh.username.setText(arrayList.get(i).get("Name"));
        vh.lastMsg.setText(arrayList.get(i).get("LastMessage"));
        vh.msgTime.setText(arrayList.get(i).get("LastMesDate"));

        Glide.with(context)
                .asBitmap()
                .load("https://vtalklive.com/"+arrayList.get(i).get("ProfileImage"))
                .into(new BitmapImageViewTarget(vh.profile) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        vh.profile.setImageBitmap(resource);
                    }
                });

//        Picasso.with(context).load("https://vtalklive.com/"+arrayList.get(i).get("ProfileImage")).placeholder(R.drawable.loadinggif).error(R.drawable.logo).into(vh.profile);
//        if (AstroId.equalsIgnoreCase("ASTRO")){
//            if ((arrayList.get(i).get("Text").equalsIgnoreCase(""))){
//                vh.thermsg.setVisibility(View.GONE);
//                vh.mymsg_tv.setVisibility(View.GONE);
//            }
//            else {
//                vh.mymsg_tv.setText(arrayList.get(i).get("Text"));
//            }
//        }
//        else {
//            if ((arrayList.get(i).get("Text").equalsIgnoreCase(""))){
//                vh.thermsg.setVisibility(View.GONE);
//                vh.mymsg_tv.setVisibility(View.GONE);
//            }
//            else {
//                vh.thermsg.setText(arrayList.get(i).get("Text"));
//            }
//        }


        if (arrayList.get(i).get("UserType").equalsIgnoreCase("Astrologer")){
            Type="ASTRO";
        }
        else if (arrayList.get(i).get("UserType").equalsIgnoreCase("DGirl")){
            Type="DATING";
        }
        else if (arrayList.get(i).get("UserType").equalsIgnoreCase("Guest")){
            Type="Guest";
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatHistory.class);
                intent.putExtra("AstroId",arrayList.get(i).get("AccountId"));
                intent.putExtra("Astrotype",Type);
                intent.putExtra("Astroname",arrayList.get(i).get("Name"));
                intent.putExtra("Astroimage","https://vtalklive.com/"+arrayList.get(i).get("ProfileImage"));
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView username, lastMsg,msgTime;
                CircleImageView profile;
        public VH(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            msgTime = itemView.findViewById(R.id.msgTime);
//            txt_rs = itemView.findViewById(R.id.txt_rs);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}

