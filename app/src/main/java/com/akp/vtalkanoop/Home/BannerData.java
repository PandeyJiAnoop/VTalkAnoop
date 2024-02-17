package com.akp.vtalkanoop.Home;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class BannerData implements Serializable {
    String BannerImage;


    public String getBannerImage() {

        return BannerImage;
    }

    public void setBannerImage(String BannerImage) {
        this.BannerImage = BannerImage;
    }



    public static List<BannerData> createJsonInList(String str){
        Gson gson=new Gson();
        Type type=new TypeToken<List<BannerData>>(){
        }.getType();
        return gson.fromJson(str,type);
    }

}



