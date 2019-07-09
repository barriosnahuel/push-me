package com.github.barriosnahuel.vossosunboton.util.ui;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public final class Feedback {

    private Feedback() {
        // Do nothing.
    }

    /**
     * @param context   The execution context.
     * @param stringRes Message to display to the user.
     */
    public static void send(@NonNull final Context context, @StringRes final int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }
}
