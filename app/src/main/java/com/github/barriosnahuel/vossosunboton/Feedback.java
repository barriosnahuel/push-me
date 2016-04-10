package com.github.barriosnahuel.vossosunboton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public final class Feedback {

    public static void send(@NonNull Context context, @StringRes int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }
}
