package com.github.barriosnahuel.vossosunboton.feature.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.github.barriosnahuel.vossosunboton.commons.android.intent.SafeIntent;

/**
 * Created by Nahuel Barrios on 11/12/16.
 */
public enum DeepLinks {

    HOME("home", "/");

    private final String host;

    private final String path;

    DeepLinks(final String host, final String path) {
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
                , "sosunboton"
                , host
                , path
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
