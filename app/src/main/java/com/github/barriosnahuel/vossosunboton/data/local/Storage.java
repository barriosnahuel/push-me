package com.github.barriosnahuel.vossosunboton.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Storage {

    private static final String INTERNAL_STORAGE = "my-prefs";

    public String get(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public Set<String> getAll(Context context, String key) {
        return getSharedPreferences(context).getStringSet(key, new HashSet<String>());
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(INTERNAL_STORAGE, Context.MODE_PRIVATE);
    }

    public void save(Context context, String key, Set<String> strings) {
        getSharedPreferences(context).edit().putStringSet(key, strings).commit();
    }

    public void save(Context context, String key, String strings) {
        getSharedPreferences(context).edit().putString(key, strings).commit();
    }
}
