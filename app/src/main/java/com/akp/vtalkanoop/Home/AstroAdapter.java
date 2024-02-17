package com.akp.vtalkanoop.Home;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.siyamed.shapeimageview.CircularImageView;
//import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AstroAdapter extends RecyclerView.Adapter<AstroAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public AstroAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public AstroAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_astrolist, viewGroup, false);
        AstroAdapter.VH viewHolder = new AstroAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull AstroAdapter.VH vh, int i) {
        vh.astroname.setText(arrayList.get(i).get("Name"));
        if (arrayList.get(i).get("IsLive").equalsIgnoreCase("true")){
            vh.status_tv.setText("Live");
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,AstroProfile.class);
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
        vh.price.setText(arrayList.get(i).get("PerMinRate"));
        vh.experience.setText(arrayList.get(i).get("Experiance")+"  Years");
        vh.language.setText(arrayList.get(i).get("Languages"));
        Glide.with(context).load(arrayList.get(i).get("ProfileImage")).into(vh.astroimage);

//        Picasso.get()
//                .load(arrayList.get(i).get("ProfileImage"))
//                .placeholder(R.drawable.loadinggif)
//                .error(R.drawable.loadinggif)
//                .into( vh.astroimage);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView astroname, price,experience,language,status_tv;
        CircularImageView astroimage;
        public VH(@NonNull View itemView) {
            super(itemView);
            astroname=itemView.findViewById(R.id.astroname);
            price=itemView.findViewById(R.id.price);
            experience=itemView.findViewById(R.id.experience);
            language=itemView.findViewById(R.id.language);
            status_tv=itemView.findViewById(R.id.status_tv);
            astroimage=itemView.findViewById(R.id.astroimage);
        }
    }
}