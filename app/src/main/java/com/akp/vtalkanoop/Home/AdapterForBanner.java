package com.akp.vtalkanoop.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.akp.vtalkanoop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterForBanner extends PagerAdapter {

    private ArrayList<HashMap<String, String>> images;
    private LayoutInflater inflater;
    private Context context;
    List<BannerData> bannerData;



    public AdapterForBanner(FragmentActivity activity, List<BannerData> bannerData) {
        this.context = activity;
        this.bannerData = bannerData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.from(view.getContext()).inflate(R.layout.banner, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.img_banner);

        Glide.with(context)
                .asBitmap()
                .load(bannerData.get(position).getBannerImage())
                .into(new BitmapImageViewTarget(myImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        myImage.setImageBitmap(resource);
                    }
                });
//        Picasso.with(context).load( bannerData.get(position).getBannerImage()).error(R.drawable.girlb).into(myImage);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, Subcategory.class);
//                intent.putExtra("id", bannerData.get(position).getCategoryid());
//                context.startActivity(intent);
            }
        });

        view.addView(myImageLayout);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view.equals(o);
    }


}


