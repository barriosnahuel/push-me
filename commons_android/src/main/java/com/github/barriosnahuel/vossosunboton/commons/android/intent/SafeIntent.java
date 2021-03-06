package com.github.barriosnahuel.vossosunboton.commons.android.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Nahuel Barrios on 11/15/16.
 */
public class SafeIntent extends Intent {

    /**
     * Build an instance of a SafeIntent
     *
     * @param context The execution context. Required to get the application package to set it by calling {@link
     * #setPackage(String)}.
     * @param uri The Uri of the data this intent is now targeting. It is a shortcut to {@link #setData(Uri)}.
     */
    public SafeIntent(@NonNull final Context context, @Nullable final Uri uri) {
        setPackage(context.getPackageName());
        setData(uri);
    }
}
