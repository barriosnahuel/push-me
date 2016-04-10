package com.github.barriosnahuel.vossosunboton.feature.addbutton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.barriosnahuel.vossosunboton.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.Feedback;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.util.file.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);

        uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) {
            Feedback.send(this, R.string.addbutton_missing_parameter_error);
        } else {
            TextView path = (TextView) findViewById(R.id.addbutton_file_path);
            path.setText(uri.toString());
        }

        name = (EditText) findViewById(R.id.addbutton_name);
        if (name == null) {
            Feedback.send(this, R.string.general_error_contact_support);
        } else {
            name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean consumedHere;

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

    public void saveButton(View view) {
        Log.v(TAG, "==> saveButton");

        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.addbutton_name_is_required_error));
        } else {

            File sourceFile = new File(URI.create(uri.toString()));

            FileOutputStream fos = null;
            try {
                fos = openFileOutput(sourceFile.getName(), Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                FileUtils.copy(sourceFile, fos);
                File file = getFileStreamPath(sourceFile.getName());
                file.setReadable(true, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            soundsDao.save(this, new Sound(name.getText().toString(), sourceFile.getName()));

            Feedback.send(this, R.string.addbutton_feedback_saved_ok);
        }
    }
}
