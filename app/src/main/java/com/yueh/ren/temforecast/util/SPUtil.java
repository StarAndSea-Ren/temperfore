package com.yueh.ren.temforecast.util;

import android.content.SharedPreferences;

import com.yueh.ren.temforecast.app.TFApplication;

/**
 * Created by chge on 2016/3/30 0030.
 */
public class SPUtil {

    public static void putData(String spName, String dataName, String data) {
        if (spName.isEmpty() || dataName.isEmpty() || data.isEmpty()) {
            return;
        }
        SharedPreferences sp = TFApplication.getInstance().getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    public static String getData(String spName, String dataName){
        SharedPreferences sp = TFApplication.getInstance().getSharedPreferences(spName, 0);
        String data = sp.getString(dataName,"");
        return data;
    }
}
