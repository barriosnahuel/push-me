package com.github.barriosnahuel.vossosunboton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Nahuel Barrios on 11/12/16.
 */
public enum DeepLinks {

    HOME(R.string.deeplink_home_host, R.string.deeplink_home_path);

    private final int host;
    private final int path;

    DeepLinks(final int host, final int path) {
        this.host = host;
        this.path = path;
    }

    @NonNull
    public String get(final Context context) {
        return String.format(
            "%s://%s%s"
            , context.getString(R.string.deeplink_app_scheme)
            , context.getString(host)
            , context.getString(path)
        );
    }

    @NonNull
    public Intent getIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setData(Uri.parse(get(context)));
        return intent;
    }
}
