package com.bluewater.cleanwatermap

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_duplicate_water_provider_viewer.*
import timber.log.Timber


class DuplicateWaterProviderActivity : AppCompatActivity() {

    companion object {
        const val NEW_PHOTO_KEY_INTENT_DATA_KEY = "newPhoto"
        const val DB_PHOTO_KEY_INTENT_DATA_KEY = "dataBasePhoto"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duplicate_water_provider_viewer)
        loadBothPhotosFromIntent()
        this.removeHeaderBar()
    }

    fun noButtonPressed(view: View) {
        switchToAddingWaterRefillStationActivity()
    }

    fun yesButtonPressed(view: View) {
        returnToPreviousActivity()
    }

    private fun returnToPreviousActivity() {
        finish()
    }

    private fun switchToAddingWaterRefillStationActivity() {
        val intent = Intent(this, AddingWaterRefillStationActivity::class.java)
        intent.putExtra(AddingWaterRefillStationActivity.NEW_PHOTO_KEY_INTENT_DATA_KEY, newPhotoBitmapFromIntent())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(intent)
        this.finish() // For replacing the activity
    }

    private fun bitMapFromIntent(intentDataKey : String) : Bitmap {
        val aParcelable : Parcelable? = intent.getParcelableExtra(
            intentDataKey
        )
        if (aParcelable != null) {
            return aParcelable as Bitmap
        }
        Timber.e("Could not find the photo from the intent")
        throw IntentDataKeyIntentNotFoundException(
            intentDataKey
        )
    }

    private fun newPhotoBitmapFromIntent() : Bitmap {
        return bitMapFromIntent(NEW_PHOTO_KEY_INTENT_DATA_KEY)
    }

    private fun dbPhotoBitmapFromIntent() : Bitmap {
        return bitMapFromIntent(DB_PHOTO_KEY_INTENT_DATA_KEY)
    }

    private fun loadBothPhotosFromIntent() {
        val newPhotoBitmap = newPhotoBitmapFromIntent()
        val dbPhotoBitmap  = dbPhotoBitmapFromIntent()
        newWaterProviderImage.setImageBitmap(newPhotoBitmap)
        registeredWaterProviderImage.setImageBitmap(dbPhotoBitmap)
    }
}
