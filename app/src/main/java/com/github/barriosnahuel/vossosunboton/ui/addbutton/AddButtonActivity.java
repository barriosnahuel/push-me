package com.github.barriosnahuel.vossosunboton.ui.addbutton;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.data.manager.SoundDao;
import com.github.barriosnahuel.vossosunboton.data.model.Sound;
import com.github.barriosnahuel.vossosunboton.ui.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.ui.DeepLinks;
import com.github.barriosnahuel.vossosunboton.ui.PermissionsRequest;
import com.github.barriosnahuel.vossosunboton.util.file.FileUtils;
import com.github.barriosnahuel.vossosunboton.util.ui.Feedback;
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
        setContentView(R.layout.activity_add_button);

        getSupportActionBar().setTitle(R.string.addbutton_activity_title);

        uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) {
            Feedback.send(this, R.string.addbutton_missing_parameter_error);
        }

        name = (EditText) findViewById(R.id.addbutton_name);
        if (name == null) {
            Feedback.send(this, R.string.general_error_contact_support);
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

    public void saveButton(final View view) {
        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.addbutton_name_is_required_error));
        } else {
            checkRequiredPermissions();
        }
    }

    private void checkRequiredPermissions() {
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
            .setTitle(R.string.permission_required)
            .setMessage(R.string.addbutton_permission_required_error)
            .setPositiveButton(R.string.addbutton_permission_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    requestStoragePermission();
                }
            })
            .setNegativeButton(R.string.addbutton_permission_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    Feedback.send(AddButtonActivity.this, R.string.youre_an_idiot_error);
                }
            }).show();
    }

    /**
     * Package-protected because method is used from an inner/anonymous class.
     */
    /* default */  void requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this
            , new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }
            , PermissionsRequest.SAVE_NEW_AUDIO_FILE);
    }

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

        int feedbackMessage = R.string.general_error_contact_support;
        if (fileOutputStream != null) {
            try {
                // TODO: 11/12/16 Run it on another thread
                final InputStream inputStream = getContentResolver().openInputStream(Uri.parse(uri.toString()));
                if (inputStream == null) {
                    Timber.e("Input stream obtained from the specified content URI is null: %s", uri.toString());
                } else {
                    FileUtils.copy(inputStream, fileOutputStream);
                    soundsDao.save(this, new Sound(name.getText().toString(), targetPath));
                    feedbackMessage = R.string.addbutton_feedback_saved_ok;
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
        return "AddButtonActivity{" +
            "uri=" + uri +
            ", name=" + name +
            '}';
    }
}
