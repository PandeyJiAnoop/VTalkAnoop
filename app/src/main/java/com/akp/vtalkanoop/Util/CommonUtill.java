package com.akp.vtalkanoop.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;




public class CommonUtill {

    public static String getDeviceId(Context pContext) {
        String identifier = Settings.Secure.getString(pContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public static void openBrowser(Activity activity, String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadApp(Activity act, String pkg) {
        try {
            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
        } catch (android.content.ActivityNotFoundException anfe) {
            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
        }
    }

    public static void shareApp(Activity act, String msg, String app) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                msg + ": " + "https://play.google.com/store/apps/details?id=" + app);
        sendIntent.setType("text/plain");
        act.startActivity(sendIntent);
    }

}
