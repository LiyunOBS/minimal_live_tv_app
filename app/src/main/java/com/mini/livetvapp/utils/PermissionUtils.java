package com.mini.livetvapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/** Util class to handle permissions. */
public class PermissionUtils {
    /** Permission to read the TV listings. */
    public static final String PERMISSION_READ_TV_LISTINGS = "android.permission.READ_TV_LISTINGS";
    private static Boolean sHasAccessAllEpgPermission;
//    private static Boolean sHasAccessWatchedHistoryPermission;
//    private static Boolean sHasModifyParentalControlsPermission;
//    private static Boolean sHasChangeHdmiCecActiveSource;
//    private static Boolean sHasReadContentRatingSystem;

    public static boolean hasAccessAllEpg(Context context) {
        if (sHasAccessAllEpgPermission == null) {
            sHasAccessAllEpgPermission =
                    context.checkSelfPermission(
                            "com.android.providers.tv.permission.ACCESS_ALL_EPG_DATA")
                            == PackageManager.PERMISSION_GRANTED;
        }
        return sHasAccessAllEpgPermission;
    }

    public static boolean hasReadTvListings(Context context) {
        return context.checkSelfPermission(PERMISSION_READ_TV_LISTINGS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasInternet(Context context) {
        return context.checkSelfPermission("android.permission.INTERNET")
                == PackageManager.PERMISSION_GRANTED;
    }
}

