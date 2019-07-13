package com.github.barriosnahuel.vossosunboton.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.util.intent.SafeIntent;

/**
 * Created by Nahuel Barrios on 11/12/16.
 */
public enum DeepLinks {

    HOME(R.string.deeplink_home_host, R.string.deeplink_home_path);

    @StringRes
    private final int host;

    @StringRes
    private final int path;

    DeepLinks(@StringRes final int host, @StringRes final int path) {
        this.host = host;
        this.path = path;
    }

    /**
     * @param context The execution context.
     * @return The deep-link that must be used to open a specific screen.
     */
    @NonNull
    public String get(final Context context) {
        return String.format(
                "%s://%s%s"
                , context.getString(R.string.deeplink_app_scheme)
                , context.getString(host)
                , context.getString(path)
        );
    }

    /**
     * @param context The execution context.
     * @return A package-filtered intent to open this screen.
     */
    @NonNull
    public Intent getIntent(final Context context) {
        return new SafeIntent(context, Uri.parse(get(context)));
    }
}
