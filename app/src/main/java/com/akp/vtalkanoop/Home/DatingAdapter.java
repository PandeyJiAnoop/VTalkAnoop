package com.akp.vtalkanoop.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
//import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DatingAdapter extends RecyclerView.Adapter<DatingAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public DatingAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public DatingAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_dating_list, viewGroup, false);
        DatingAdapter.VH viewHolder = new DatingAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull DatingAdapter.VH vh, int i) {
        if (arrayList.get(i).get("IsLive").equalsIgnoreCase("true")){
            vh.status_tv.setText("Live");
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,DatingProfileDetails.class);
                    intent.putExtra("id",arrayList.get(i).get("AccountId"));
                    context.startActivity(intent);
                }
            });
        }
        else {
            vh.status_tv.setText("Offline");
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Currently not available",Toast.LENGTH_LONG).show();
                }
            });
        }
        vh.name_tv.setText(arrayList.get(i).get("Name"));
        vh.price.setText(arrayList.get(i).get("PerMinRate"));
        vh.language.setText(arrayList.get(i).get("Languages"));

        Glide.with(context).load(arrayList.get(i).get("ProfileImage")).into(vh.profile_img);


//        Picasso.get()
//                .load(arrayList.get(i).get("ProfileImage"))
//                .placeholder(R.drawable.loadinggif)
//                .error(R.drawable.loadinggif)
//                .into( vh.profile_img);


//        Picasso.with(context).load(arrayList.get(i).get("ProfileImage")).placeholder(R.drawable.loadinggif).error(R.drawable.girlb).into(vh.profile_img);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView name_tv,status_tv,price,language;
        CircleImageView profile_img;
        public VH(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.astroname);
            status_tv=itemView.findViewById(R.id.status_tv);
            profile_img=itemView.findViewById(R.id.astroimage);
            price=itemView.findViewById(R.id.price);
            language=itemView.findViewById(R.id.language);
        }
    }
}
