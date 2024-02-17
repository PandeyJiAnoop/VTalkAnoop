package com.akp.vtalkanoop.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */


import org.json.JSONException;
import org.json.JSONObject;

public class GlobalAppApis {
    public String ProfileDetails(String hotelid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", hotelid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String EditProfileDetails(String name,String email,String about,String accountid,String file,String expertise,String perminrate,String videocall,
                                     String voicecall,String address) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Name", name);
            jsonObject1.put("Email", email);
            jsonObject1.put("About", about);
            jsonObject1.put("AccountId", accountid);
            jsonObject1.put("File", file);
            jsonObject1.put("Expertise", expertise);
            jsonObject1.put("ChatRate", perminrate);
            jsonObject1.put("VideoCallRate", videocall);
            jsonObject1.put("VoiceCallRate", voicecall);
            jsonObject1.put("Address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String Register(String name,String email,String mobile,String pass,String usertype,String exp,String language,String file,String intrest,String token,
                           String voice,String video,String chat,String add) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Name", name);
            jsonObject1.put("Email", email);
            jsonObject1.put("Mobile", mobile);
            jsonObject1.put("Password", pass);
            jsonObject1.put("UserType", usertype);
            jsonObject1.put("Experience", exp);
            jsonObject1.put("Language", language);
            jsonObject1.put("File", file);
            jsonObject1.put("Interest", intrest);
            jsonObject1.put("DeviceId", token);
            jsonObject1.put("VoiceCallRate", voice);
            jsonObject1.put("VideoCallRate", video);
            jsonObject1.put("ChatRate", chat);
            jsonObject1.put("Location", add);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Login(String mobile,String deviceid,String token) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MobileNumber", mobile);
            jsonObject1.put("Password", deviceid);
            jsonObject1.put("DeviceId", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String MembershipCard(String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OTPVerify(String action,String mobilemo,String otp,String deviceid,String name,String email) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("MobileNumber", mobilemo);
            jsonObject1.put("Otp", otp);
            jsonObject1.put("DeviceId", deviceid);
            jsonObject1.put("Name", name);
            jsonObject1.put("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String ChatDetails(String custAccountId,String astroAccountId,String rtype,String text,String staus,String time,String action) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustAccountId", custAccountId);
            jsonObject1.put("AstroAccountId", astroAccountId);
            jsonObject1.put("ReceiverType", rtype);
            jsonObject1.put("Text", text);
            jsonObject1.put("Status", staus);
            jsonObject1.put("Time", time);
            jsonObject1.put("Action", action);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ProfileDetials(String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String CustomerList(String accountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", accountid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String WalletHistoryList(String accountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", accountid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String WalletAPI(String accountid,String amount) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", accountid);
            jsonObject1.put("Amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GoLiveAPI(String action,String acountid,String type) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("AccountId", acountid);
            jsonObject1.put("Type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String LiveBraodcast(String acuntid,String entrycode,String type) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", acuntid);
            jsonObject1.put("EntryCode", entrycode);
            jsonObject1.put("UserType", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String SettingApi(String acountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", acountid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String StukCall(String acountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", acountid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String RemoveStukCall(String acountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ID", acountid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }





    public String LiveStreamAPI(String acountid,String type) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", acountid);
            jsonObject1.put("text", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String TokenAPI(String appurl,String appid,String currency,String orderamt,String orderid,String secretkey) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("APIUrl", appurl);
            jsonObject1.put("App_Id", appid);
            jsonObject1.put("Currency", currency);
            jsonObject1.put("OrderAmt", orderamt);
            jsonObject1.put("OrderId", orderid);
            jsonObject1.put("Sacret_key", secretkey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String DatingVerifyAPI(String mobileno,String videourl) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MobileNumber", mobileno);
            jsonObject1.put("File", videourl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String DebitWalletAPI(String accountid,String amount,String ac_name,String acc_no,String ifsc,String branch) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", accountid);
            jsonObject1.put("Amount", amount);

            jsonObject1.put("AccHolderName", ac_name);
            jsonObject1.put("AccNumber", acc_no);
            jsonObject1.put("Ifsc", ifsc);
            jsonObject1.put("BranchName", branch);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String AccountDetailsAPI(String accountid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", accountid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }




    public String CallingApi(String caller_acountid,String reciever_accountid,String status, String calling_type,String calling_time) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CallerAccountId", caller_acountid);
            jsonObject1.put("ReceiverAccountId", reciever_accountid);
            jsonObject1.put("Status", status);
            jsonObject1.put("CallingType", calling_type);
            jsonObject1.put("CallingTime", calling_time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String CallSummery(String id) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("AccountId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
}



