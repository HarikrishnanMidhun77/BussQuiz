package com.quizapp.hp.quiz;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by VishnuMidhun on 06-11-2015.
 */
public class PrefManager {
     // Shared Preferences
                SharedPreferences pref;
        // Editor for Shared preferences
                Editor editor;
        // Context
                Context _context;
    // Shared pref mode
        int PRIVATE_MODE = 0;
        // Shared preferences file name
                private static final String PREF_NAME = "AndroidHive";
        // All Shared Preferences Keys
        private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
     //   private static final String KEY_MOBILE_NUMBER = "mobile_number";
        private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
        private static final String KEY_NAME = "name";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_MOBILE = "mobile";
        private static final String KEY_UID = "uid";
        private static final String KEY_QODID = "qodid";
        private static final String KEY_QODSET = "qodset";
        private static final String KEY_NOTIF_ACTIVE = "notif";
        private static final String KEY_QOD_DATE= "qod_date";
        private static final String KEY_NETBOOL= "net_bool";
    private static final String KEY_NETBOOL2= "net_bool2";
    private static final String KEY_NETBOOL3= "net_bool3";
    private static final String KEY_GIFTED= "key_gifted";
        public PrefManager(Context context) {
                this._context = context;
                pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                editor = pref.edit();
            }
        public void setIsWaitingForSms(boolean isWaiting) {
                editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
                editor.commit();
            }
        public boolean isWaitingForSms() {
                return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
            }
        public void setMobileNumber(String mobileNumber) {
                editor.putString(KEY_MOBILE, mobileNumber);
                editor.commit();
            }
    public void setQodId(String qid) {
        editor.putString(KEY_QODID, qid);
        editor.commit();
    }
    public void setQodSet(boolean qidset) {
        editor.putBoolean(KEY_QODSET, qidset);
        editor.commit();
    }
    public void setQodDate(String qid) {
        editor.putString(KEY_QOD_DATE, qid);
        editor.commit();
    }
    public void setNetBool(Boolean netBool) {
        editor.putBoolean(KEY_NETBOOL, netBool);
        editor.commit();
    }
    public void setNetBool2(Boolean netBool) {
        editor.putBoolean(KEY_NETBOOL2, netBool);
        editor.commit();
    }
    public void setNetBool3(Boolean netBool) {
        editor.putBoolean(KEY_NETBOOL3, netBool);
        editor.commit();
    }
    public void setGifted(Boolean gif) {
        editor.putBoolean(KEY_GIFTED, gif);
        editor.commit();
    }
    public String getKeyQodDate() {
        return pref.getString(KEY_QOD_DATE, "2015-08-14");
    }
    public boolean getGifted() {
        return pref.getBoolean(KEY_GIFTED,false);
    }
    public boolean getNetBool() {
        return pref.getBoolean(KEY_NETBOOL, true);
    }
    public boolean getNetBool2() {
        return pref.getBoolean(KEY_NETBOOL2, true);
    }
    public boolean getNetBool3() {
        return pref.getBoolean(KEY_NETBOOL3, true);
    }
    public String getQodid() {
        return pref.getString(KEY_QODID, "8");
    }
    public boolean getQodSet() {
        return pref.getBoolean(KEY_QODSET, false);
    }
    public void notifActivate()
    {
        editor.putBoolean(KEY_NOTIF_ACTIVE, true);
        editor.commit();
    }
    public boolean isNotifActivated() {
        return pref.getBoolean(KEY_NOTIF_ACTIVE, false);
    }
        public String getMobileNumber() {
                return pref.getString(KEY_MOBILE, null);
            }
    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }
    public String getName() {
        return pref.getString(KEY_NAME, null);
    }
    public int getUid() {
        return pref.getInt(KEY_UID, 0);
    }
        public void createLogin(int Uid,String name, String email, String mobile) {
                 editor.putInt(KEY_UID,Uid);
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_MOBILE, mobile);
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.commit();
            }
        public boolean isLoggedIn() {
                return pref.getBoolean(KEY_IS_LOGGED_IN, false);
            }
        public void clearSession() {
                editor.clear();
                editor.commit();
            }
        public HashMap<String, String> getUserDetails() {
                HashMap<String, String> profile = new HashMap<>();
         profile.put("name", pref.getString(KEY_NAME, null));
                profile.put("email", pref.getString(KEY_EMAIL, null));
                profile.put("mobile", pref.getString(KEY_MOBILE, null));
                return profile;
            }
}
