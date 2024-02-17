package com.akp.vtalkanoop.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.R;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public ChatAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public ChatAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_chathistory, viewGroup, false);
        ChatAdapter.VH viewHolder = new ChatAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
        String AstroId=(arrayList.get(i).get("Receiver"));
        if (AstroId.equalsIgnoreCase("ASTRO")){
            vh.thermsg.setText(arrayList.get(i).get("Text"));
            vh.thermsg.setVisibility(View.VISIBLE);
        }

        if (AstroId.equalsIgnoreCase("CUST")){
            vh.mymsg_tv.setText(arrayList.get(i).get("Text"));
            vh.mymsg_tv.setVisibility(View.VISIBLE);
        }
        if (AstroId.equalsIgnoreCase("GUEST")){
            vh.mymsg_tv.setText(arrayList.get(i).get("Text"));
            vh.mymsg_tv.setVisibility(View.VISIBLE);
        }

        if (AstroId.equalsIgnoreCase("DATING")){
            vh.thermsg.setText(arrayList.get(i).get("Text"));
            vh.thermsg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView thermsg, mymsg_tv;
        //        CircleImageView img_icon;
        public VH(@NonNull View itemView) {
            super(itemView);
            thermsg = itemView.findViewById(R.id.thermsg);
            mymsg_tv = itemView.findViewById(R.id.mymsg_tv);
//            txt_rs = itemView.findViewById(R.id.txt_rs);
//            img_icon = itemView.findViewById(R.id.img_icon);
        }
    }
}
