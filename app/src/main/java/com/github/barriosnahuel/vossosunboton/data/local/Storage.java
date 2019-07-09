package com.github.barriosnahuel.vossosunboton.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Storage {

    private static final String INTERNAL_STORAGE = "my-prefs";

    public String get(@NonNull final Context context, final String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public Set<String> getAll(@NonNull final Context context, final String key) {
        return getSharedPreferences(context).getStringSet(key, new HashSet<>());
    }

    private SharedPreferences getSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(INTERNAL_STORAGE, Context.MODE_PRIVATE);
    }

    public void save(@NonNull final Context context, final String key, final Set<String> strings) {
        getSharedPreferences(context).edit().putStringSet(key, strings).apply();
    }

    public void save(@NonNull final Context context, final String key, final String strings) {
        getSharedPreferences(context).edit().putString(key, strings).apply();
    }
}
