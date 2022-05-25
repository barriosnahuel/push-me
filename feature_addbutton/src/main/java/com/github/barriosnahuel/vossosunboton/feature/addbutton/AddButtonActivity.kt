package com.github.barriosnahuel.vossosunboton.feature.addbutton

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback
import com.github.barriosnahuel.vossosunboton.feature.base.AbstractActivity
import com.github.barriosnahuel.vossosunboton.feature.base.NavigationSections
import com.github.barriosnahuel.vossosunboton.feature.base.PermissionsRequest
import kotlinx.android.synthetic.main.feature_addbutton_activity_add_button.feature_addbutton_saveButton
import kotlinx.android.synthetic.main.feature_addbutton_activity_add_button.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
class AddButtonActivity : AbstractActivity() {

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.feature_addbutton_activity_add_button)
        bindToolbar()

        /**
         * file:///storage/emulated/0/WhatsApp/Media/WhatsApp%20Voice%20Notes/201615/PTT-20160407-WA0079.opus
         */
        uri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        if (uri == null) {
            Feedback.send(this, R.string.feature_addbutton_missing_parameter_error)
            finish()
        }

        name.setOnEditorActionListener { _, actionId, _ ->
            val consumedHere = if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveButton(this@AddButtonActivity)
                true
            } else {
                false
            }

            consumedHere
        }

        feature_addbutton_saveButton.setOnClickListener { saveButton(this) }
    }

    override fun bindToolbar() {
        super.bindToolbar()
        supportActionBar?.setTitle(R.string.feature_addbutton_activity_title)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionsRequest.SAVE_NEW_AUDIO_FILE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveNewButton(this)
            } else {
                showPermissionExplanation(this)
            }
        }
    }

    private fun saveButton(context: Context) {
        if (name.text.isEmpty()) {
            name.error = getString(R.string.feature_addbutton_name_is_required_error)
        } else {
            checkRequiredPermissions(context)
        }
    }

    private fun checkRequiredPermissions(context: Context) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveNewButton(context)
        } else {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@AddButtonActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionExplanation(context)
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                requestStoragePermission()
            }
        }
    }

    private fun saveNewButton(context: Context) {
        val deferredFeedbackMessage = AddButtonFeature.instance.saveNewButtonAsync(context, name.text.toString(), uri!!.toString())

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                Feedback.send(context, deferredFeedbackMessage.await())
            }
        }

        val intent = NavigationSections.HOME.getIntent(context)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun showPermissionExplanation(context: Context) {
        AlertDialog.Builder(context)
                .setTitle(R.string.feature_addbutton_permission_required)
                .setMessage(R.string.feature_addbutton_permission_required_error)
                .setPositiveButton(R.string.feature_addbutton_permission_positive) { _, _ -> requestStoragePermission() }
                .setNegativeButton(R.string.feature_addbutton_permission_negative) { _, _ ->
                    Feedback.send(context, R.string.feature_addbutton_youre_an_idiot_error)
                }.show()
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PermissionsRequest.SAVE_NEW_AUDIO_FILE)
    }
}
