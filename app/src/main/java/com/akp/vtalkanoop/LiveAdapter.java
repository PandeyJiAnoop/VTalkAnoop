package com.akp.vtalkanoop;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.Home.AstroProfile;
import com.akp.vtalkanoop.Home.DatingProfileDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
//import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public LiveAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public LiveAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_astrodating, viewGroup, false);
        LiveAdapter.VH viewHolder = new LiveAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull LiveAdapter.VH vh, int i) {
        vh.name_et.setText(arrayList.get(i).get("FirstName"));
        Glide.with(context)
                .asBitmap()
                .load(arrayList.get(i).get("ProfileImage"))
                .into(new BitmapImageViewTarget(vh.img) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        vh.img.setImageBitmap(resource);
                    }
                });

//        Picasso.with(context).load(arrayList.get(i).get("ProfileImage")).placeholder(R.drawable.loadinggif).error(R.drawable.loadinggif).into(vh.img);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, MainActivity.class);
////                intent.putExtra("id",arrayList.get(i).get("AccountId"));
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView name_et;
        ImageView img;
        public VH(@NonNull View itemView) {
            super(itemView);
            name_et=itemView.findViewById(R.id.name_et);
            img=itemView.findViewById(R.id.img);
        }
    }
}

