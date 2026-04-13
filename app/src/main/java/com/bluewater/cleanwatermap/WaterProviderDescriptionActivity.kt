package com.bluewater.cleanwatermap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.justinnguyenme.base64image.Base64Image
import kotlinx.android.synthetic.main.activity_water_provider_description.*
import timber.log.Timber


class WaterProviderDescriptionActivity : AppCompatActivity() {

    companion object {
        const val WATER_PROVIDER_INTENT_DATA_KEY : String = "waterProviderData"
    }

    private lateinit var mWaterProviderWaterPhoto : ImageView
    private var mWaterProvider : WaterProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_provider_description)
        this.performLateInitView()
        this.updateWaterProviderPhotoFromIntent()
        this.removeHeaderBar()
        this.updateTDSTextView()
        this.setWaterDropImageFromTdsValue()
        this.setPurityTextViewFromTdsValue()
    }

    private fun setWaterDropImageFromTdsValue() {
        // Update image based on number

        val waterProvider : WaterProvider? = mWaterProvider
        if (waterProvider != null) {
            val theTDSInfo = TDSInfo(waterProvider.lastTDSMeasurementValue())
            val id =  theTDSInfo.tdsInfoCategory.waterDropImageID
            waterDropImage.setBackgroundResource(id)
        }
    }

    private fun setPurityTextViewFromTdsValue() {
        val waterProvider : WaterProvider? = mWaterProvider
        if (waterProvider != null) {
            val theTDSInfo = TDSInfo(waterProvider.lastTDSMeasurementValue())
            val textColor = theTDSInfo.tdsInfoCategory.textColor
            val purity: Spannable = SpannableString(theTDSInfo.tdsInfoCategory.purityString)
            purity.setSpan(
                ForegroundColorSpan(textColor),
                0,
                purity.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            purityTextView.text = purity
        }
    }

    private fun updateTDSTextView() {
        val waterProvider : WaterProvider? = mWaterProvider
        if (waterProvider != null) {
            val theTDSInfo = TDSInfo(waterProvider.lastTDSMeasurementValue())
            val tdsValueStringString = theTDSInfo.tdsInfoCategory.tdsValueString

            val wordTwo: Spannable = SpannableString(tdsValueStringString)
            wordTwo.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.blueMap)),
                0,
                wordTwo.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tdsTextView.text = wordTwo
        }
        else {
            Timber.e("waterProvider is null in updateTDSTextView method")
        }
    }

    private fun performLateInitView() {
        mWaterProviderWaterPhoto = findViewById(R.id.waterProviderImage)
    }

    private fun setImageViewFromWaterProvider(aWaterProvider: WaterProvider) {
        Base64Image.instance.decode(aWaterProvider.photoData) { bitmap ->
            bitmap?.let { theDecodedBitmap ->
                mWaterProviderWaterPhoto.setImageBitmap(theDecodedBitmap)
            }
        }
    }

    private fun updateWaterProviderPhotoFromIntent() {
        val waterProviderData : WaterProvider? = this.retrieveWaterProviderData()
        if (waterProviderData != null) {
            this.setImageViewFromWaterProvider(waterProviderData)
        }
    }

    private fun retrieveWaterProviderData() : WaterProvider? {
        if (mWaterProvider != null) {
            return mWaterProvider
        }
        val extras = intent.extras
        if (extras != null) {
            mWaterProvider = extras.getParcelable(WATER_PROVIDER_INTENT_DATA_KEY)
            return mWaterProvider
        }
        return null
    }

    fun shareButtonPressed(view: View) {
        mWaterProvider?.id?.let { waterProviderID : String ->

            val rootWebsite = getString(R.string.websiteLink)
            val website  = rootWebsite + "wp/" + waterProviderID
            val sentence = getString(R.string.Refill_water_here_on_H2O_Map_App)
            val textToShare = "$sentence ${website}"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "H2O Map")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "Share"
                )
            )
        }
    }

    fun directionButtonPressed(view: View) {
        val waterProvider = mWaterProvider
        if (waterProvider != null) {
            val gmmIntentUri: Uri = Uri.parse(waterProvider.googleNavigationString())
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        }
    }
}