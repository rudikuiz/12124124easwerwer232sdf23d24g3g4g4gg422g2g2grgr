package com.piramidsoft.wablastadmin.Utils;

/**
 * Created by Web on 16/04/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.piramidsoft.wablastadmin.log";


    public static final String KEY_PHONE = "phone";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String phone) {

        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }


    public String getPhone() {
        return pref.getString(KEY_PHONE, null);
    }


    public void setPhone(String value) {
        editor.putString(KEY_PHONE, value);
        editor.commit();
    }


}