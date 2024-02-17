package com.akp.vtalkanoop.Home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.vtalkanoop.R;
//import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public WalletAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public WalletAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_wallet, viewGroup, false);
        WalletAdapter.VH viewHolder = new WalletAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull WalletAdapter.VH vh, int i) {
            vh.msg_tv.setText(arrayList.get(i).get("Message"));
            vh.date_tv.setText(arrayList.get(i).get("Entrydate"));

            if (arrayList.get(i).get("DebitAmount").equalsIgnoreCase("null")){
                vh.amount_tv.setVisibility(View.GONE);
            }
            if (arrayList.get(i).get("CreditAmount").equalsIgnoreCase("null")){
            vh.credit_amount_tv.setVisibility(View.GONE); }
            vh.amount_tv.setText("- "+arrayList.get(i).get("DebitAmount"));
            vh.credit_amount_tv.setText("+ "+arrayList.get(i).get("CreditAmount"));







    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView msg_tv,date_tv,amount_tv,credit_amount_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            msg_tv=itemView.findViewById(R.id.msg_tv);
            date_tv=itemView.findViewById(R.id.date_tv);
            amount_tv=itemView.findViewById(R.id.amount_tv);
            credit_amount_tv=itemView.findViewById(R.id.credit_amount_tv);
        }
    }
}

