package com.github.barriosnahuel.vossosunboton.feature.addbutton;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback;
import com.github.barriosnahuel.vossosunboton.commons.file.FileUtils;
import com.github.barriosnahuel.vossosunboton.feature.base.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.feature.base.DeepLinks;
import com.github.barriosnahuel.vossosunboton.feature.base.PermissionsRequest;
import com.github.barriosnahuel.vossosunboton.model.Sound;
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class AddButtonActivity extends AbstractActivity {

    /**
     * file:///storage/emulated/0/WhatsApp/Media/WhatsApp%20Voice%20Notes/201615/PTT-20160407-WA0079.opus
     */
    private Uri uri;

    private EditText name;

    private SoundDao soundsDao;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_addbutton_activity_add_button);

        getSupportActionBar().setTitle(R.string.feature_addbutton_activity_title);

        uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) {
            Feedback.send(this, R.string.feature_addbutton_missing_parameter_error);
        }

        name = (EditText) findViewById(R.id.addbutton_name);
        if (name == null) {
            Feedback.send(this, R.string.feature_base_general_error_contact_support);
        } else {
            name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                    final boolean consumedHere;

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        saveButton(null);
                        consumedHere = true;
                    } else {
                        consumedHere = false;
                    }

                    return consumedHere;
                }
            });
        }

        soundsDao = new SoundDao();
    }

    @Override
    public void onRequestPermissionsResult(
        final int requestCode
        , @NonNull final String[] permissions
        , @NonNull final int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionsRequest.SAVE_NEW_AUDIO_FILE) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                saveNewButton();
            } else {
                showPermissionExplanation();
            }
        }
    }

    /**
     * Check new button required data and saves the new sound for this user.
     *
     * @param view The caller view.
     */
    public void saveButton(final View view) {
        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.feature_addbutton_name_is_required_error));
        } else {
            checkRequiredPermissions();
        }
    }

    private void checkRequiredPermissions() {
        if (PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            saveNewButton();
        } else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                showPermissionExplanation();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                requestStoragePermission();
            }
        }
    }

    private void showPermissionExplanation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.feature_addbutton_permission_required)
                .setMessage(R.string.feature_addbutton_permission_required_error)
                .setPositiveButton(R.string.feature_addbutton_permission_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    requestStoragePermission();
                }
            })
                .setNegativeButton(R.string.feature_addbutton_permission_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    Feedback.send(AddButtonActivity.this, R.string.feature_addbutton_youre_an_idiot_error);
                }
            }).show();
    }

    /**
     * Package-protected because method is used from an inner/anonymous class.
     */
    /* default */ void requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this
            , new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }
            , PermissionsRequest.SAVE_NEW_AUDIO_FILE);
    }

    /**
     * TODO: Stop ignoring this! Currently ignored because I'm upgrading dependencies version.
     */
    @SuppressWarnings("PMD.AvoidFileStream")
    private void saveNewButton() {
        final String targetPath =
            getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "Button-" + System.currentTimeMillis() + ".mp3";

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(targetPath);
        } catch (final FileNotFoundException e) {
            Timber.e("Can't create new button's path");
            fileOutputStream = null;
        }

        int feedbackMessage = R.string.feature_base_general_error_contact_support;
        if (fileOutputStream != null) {
            try {
                // TODO: 11/12/16 Run it on another thread
                final InputStream inputStream = getContentResolver().openInputStream(Uri.parse(uri.toString()));
                if (inputStream == null) {
                    Timber.e("Input stream obtained from the specified content URI is null: %s", uri.toString());
                } else {
                    FileUtils.copy(inputStream, fileOutputStream);
                    soundsDao.save(this, new Sound(name.getText().toString(), targetPath));
                    feedbackMessage = R.string.feature_addbutton_feedback_saved_ok;
                }
            } catch (final IOException e) {
                Timber.e("Can't copy original audio");
            }
        }

        Feedback.send(this, feedbackMessage);

        final Intent intent = DeepLinks.HOME.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public String toString() {
        return "AddButtonActivity{"
                + "uri=" + uri
                + ", name=" + name
                + '}';
    }
}
