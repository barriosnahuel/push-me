package com.github.barriosnahuel.vossosunboton.model.data.local;

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

    /**
     * @param context The execution context.
     * @param key     The key to look for.
     * @return Current value or <code>null</code> instead.
     */
    public String get(@NonNull final Context context, final String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    /**
     * @param context The execution context.
     * @param key     The key to look for.
     * @return All values for the given <code>key</code> or an empty set instead.
     */
    public Set<String> getAll(@NonNull final Context context, final String key) {
        return getSharedPreferences(context).getStringSet(key, new HashSet<String>());
    }

    private SharedPreferences getSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(INTERNAL_STORAGE, Context.MODE_PRIVATE);
    }

    /**
     * @param context The execution context.
     * @param key     The key used to save the <code>strings</code>.
     * @param strings values to save.
     */
    public void save(@NonNull final Context context, final String key, final Set<String> strings) {
        getSharedPreferences(context).edit().putStringSet(key, strings).apply();
    }

    /**
     * @param context The execution context.
     * @param key     The key used to save the <code>string</code>.
     * @param string  the value to save.
     */
    public void save(@NonNull final Context context, final String key, final String string) {
        getSharedPreferences(context).edit().putString(key, string).apply();
    }

    /**
     * @param context The execution context.
     * @param key     The key used to remove the <code>string</code>
     */
    public void remove(@NonNull final Context context, final String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }
}
