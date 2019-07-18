package com.github.barriosnahuel.vossosunboton;

import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.nshmura.strictmodenotifier.StrictModeNotifier;
import com.squareup.leakcanary.LeakCanary;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
public class DebugApplication extends MainApplication {

    @DebugLog
    @Override
    public void onCreate() {
        Timber.plant(new Timber.DebugTree());

        super.onCreate();

        Timber.d("Creating DEBUG application...");

        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
        setupStrictModeNotifier();
    }

    /**
     * More info about the notifier app at <a href="https://github.com/nshmura/strictmode-notifier">github.com/nshmura/strictmode-notifier</a>.
     */
    protected void setupStrictModeNotifier() {
        StrictModeNotifier.install(this);

        new Handler().post(this::setupStrictMode);
    }

    /**
     * More info about the Android Strict Mode at <a href="https://developer.android.com/reference/android/os/StrictMode.html">their
     * documentation</a>.
     *
     * Package-protected because method is used from an inner/anonymous class.
     */
    package

    void setupStrictMode() {
        StrictMode.setThreadPolicy(getStrictModeThreadPolicy().build());

        StrictMode.setVmPolicy(getStrictModeVmPolicy().build());
    }

    /**
     * @return <code>null</code> if you want to disable strict mode threading policy
     */
    @NonNull
    private StrictMode.ThreadPolicy.Builder getStrictModeThreadPolicy() {
        final StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectNetwork()
            .permitDiskReads()
            .penaltyLog(); // Required for StrictModeNotifier!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            threadPolicyBuilder.detectResourceMismatches();
        }

        return threadPolicyBuilder;
    }

    /**
     * Create VmPolicy Builder basing on Android Version because StrictMode does not check properly that GB remove
     * activity before report an InstanceCountViolation when API version is 21 or older. To avoid this error, we don't
     * use {@link StrictMode.VmPolicy.Builder#detectActivityLeaks()} (inside {@link
     * StrictMode.VmPolicy.Builder#detectAll()}) and call all other methods where applicable. <p> Note that {@link
     * StrictMode.VmPolicy.Builder#penaltyLog()} is required for {@link StrictModeNotifier} to work. <p> For more info
     * take a look at <a href="https://github.com/mercadolibre/mobile-android_testing/pull/37#discussion_r72981823">this
     * comment</a>.
     *
     * @return <code>null</code> if you want to disable strict mode VM policy.
     */
    @NonNull
    private StrictMode.VmPolicy.Builder getStrictModeVmPolicy() {
        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.detectAll();
        } else {
            builder.detectLeakedRegistrationObjects();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                builder.detectFileUriExposure();
            }

            builder.detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects();
        }

        // penaltyLog is required for StrictModeNotifier!
        builder.penaltyLog();

        return builder;
    }
}
