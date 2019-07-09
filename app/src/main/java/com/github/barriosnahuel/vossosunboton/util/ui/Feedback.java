package com.github.barriosnahuel.vossosunboton.util.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public final class Feedback {

    private Feedback() {
        // Do nothing.
    }

    public static void send(@NonNull final Context context, @StringRes final int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }
}
