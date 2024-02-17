package com.akp.vtalkanoop.Firebase;

import java.util.HashMap;

public class Constanta {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String KEY_Caller_ID = "call_id";
    public static final String KEY_Reciever_ID = "rec_id";
    public static final String KEY_Rate= "rate_id";


    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    /*public static final String API_KEY_SERVER = "AAAAgqFhoHg:APA91bF7Pat_anFVviDxXDycue8FjHN0o4PIdfk0HsXxlJKbBiIpwtykpzpZ3JoNa4X2rAg3M1dlpRHs02zMs0dLGyUr956IXOAdqmnseoVYNC6tsHNKBy-BN_iv0iDBRUYhKyyo9zbV";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constanta.REMOTE_MSG_AUTHORIZATION,
                "key="+ API_KEY_SERVER
        );
        headers.put(Constanta.REMOTE_MSG_CONTENT_TYPE, "application/json");

        return headers;
    }*/
    public static final String API_KEY_SERVER = "AAAAQfpO_bs:APA91bEeobGpDM4oIQ2xGvnH8u9xevVXEftWedKm7UJfrZD6pmFjcEJwvb3pslDbnSUSLHkO4Iv3qPiRD5ZzxNWiT2e35ipFHjK2jXA-f4lDOjjI16OLrVkKSOEUGU7K7Mx7QLoajDdx";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constanta.REMOTE_MSG_AUTHORIZATION,
                "key="+ API_KEY_SERVER
        );
        headers.put(Constanta.REMOTE_MSG_CONTENT_TYPE, "application/json");

        return headers;
    }
}
