package com.example.tfg_nd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class manejadorPreferencias {

    SharedPreferences prefs;
    String name;
    Activity activity;

    public manejadorPreferencias(String name, Activity activity) {
        this.name = name;
        this.activity = activity;
    }

    public manejadorPreferencias() {
    }


    public void put(String key, String data) {
        prefs = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        //Te da una moneda cada vez que inicias en la aplicación
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String get(String key, String def) {
        prefs = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String dato = prefs.getString(key, def);
        return dato;
    }
}
