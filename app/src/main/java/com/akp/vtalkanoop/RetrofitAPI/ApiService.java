package com.akp.vtalkanoop.RetrofitAPI;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("send")
    Call<String> sendRemoteMessage(
            @HeaderMap HashMap<String, String> headers,
            @Body String remoteBody
    );
    @Headers("Content-Type: application/json")
    @POST("Registration")
    Call<String> getRegister(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("OtpVerification")
    Call<String> getOTP(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Login")
    Call<String> getLogin(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("membership")
    Call<String> getMembership(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Profile")
    Call<String> getdetails(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("ProfileUpdate")
    Call<String> getEditProfile(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Chating")
    Call<String> getChatService(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Setting")
    Call<String> getprofile(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("CustomerChatList")
    Call<String> getCustomerList(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AddWalletAmount")
    Call<String> getaddwalletList(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("WalletHistory")
    Call<String> getWalletHistory(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GoLive")
    Call<String> getGoLive(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("LiveBroadCast")
    Call<String> getLiveBraodcast(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Setting")
    Call<String> getSetting(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("UpdateCall")
    Call<String> getRemovecall(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GetCalingDetails")
    Call<String> getStukcall(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GetLiveStreamingList")
    Call<String> getLiveStream(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GenerateToken")
    Call<String> getToken(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("VideoUpload")
    Call<String> getDatingVerifyVideo(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("WithdrawRequest")
    Call<String> getdebitwalletList(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("CallNotificationapi")
    Call<String> getCalling(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("UserBankDetails")
    Call<String> getAccountdetails(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("CallSummary")
    Call<String> getCallSummery(
            @Body String body);
}


