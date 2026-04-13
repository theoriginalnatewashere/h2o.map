package com.bluewater.cleanwatermap

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_adding_water_refill_station.*
import retrofit2.Response
import timber.log.Timber
import java.io.ByteArrayOutputStream


class AddingWaterRefillStationActivity : AppCompatActivity() {

    companion object {
        const val NEW_PHOTO_KEY_INTENT_DATA_KEY = "data"
    }

    private lateinit  var mWaterRefillWaterPhoto : ImageView
    private lateinit var mTDSTextEdit : EditText
    private lateinit var mAddButton : Button

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_water_refill_station)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        performLateInitView()
        updatePhotoFromIntent()
        this.removeHeaderBar()
    }

    private fun performLateInitView() {
        mWaterRefillWaterPhoto = findViewById(R.id.refillStationPhoto)
        mTDSTextEdit = findViewById(R.id.tdsValue)
        mAddButton = findViewById(R.id.addButton)
    }

    private fun updatePhotoFromIntent() {
        mWaterRefillWaterPhoto.setImageBitmap(retrievePhotoData())
    }

    private fun retrievePhotoData() : Bitmap? {
        val aParcelable : Parcelable? = intent.getParcelableExtra(NEW_PHOTO_KEY_INTENT_DATA_KEY)
        if (aParcelable != null) {
            return aParcelable as Bitmap
        }
        else
        {
            Timber.e("Could not find the photo from the intent")
        }
        return null
    }

    private fun getByteArrayFromBitmap(aBitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
//        aBitmap.recycle()
        return byteArray
    }

    private fun convertByteArrayToBase64String(aByteArray : ByteArray): String {
        return Base64.encodeToString(aByteArray, Base64.DEFAULT)
    }

    private fun createNewWaterLocationToAPIAndFinishActivityOnResponse(aWaterProvider: WaterProvider, afterResponseOrFailure : (() -> Unit)?) {
        CleanWaterMapServerAPISingleton.API().createWaterProvider(aWaterProvider).enqueue {
            onResponse = {response : Response<WaterProvider>? ->
                if(response != null && response.isSuccessful) {
                    val waterProviderFromServer : WaterProvider? = response.body()
                    if (waterProviderFromServer != null) {
                        val resultIntent = Intent()
                        resultIntent.putExtra(MapsActivity.ADDED_WATER_PROVIDER_KEY_INTENT_DATA_KEY, waterProviderFromServer)
                        setResult(Activity.RESULT_OK, resultIntent)
                    }
                    finish()
                }
                if (afterResponseOrFailure != null) afterResponseOrFailure()
            }
            onFailure = {
                Toasty.warning(baseContext, "Unable to connect to server").show()
                if (afterResponseOrFailure != null) afterResponseOrFailure()
            }
        }
    }

    private fun getTDSValueString() : String {
        return mTDSTextEdit.text.toString()
    }

    private fun hasUserSpecifiedA0TDSValue() : Boolean {
        val stringTextEdit : String =  this.getTDSValueString()

        return stringTextEdit != "" && stringTextEdit.toInt() == 0
    }

    private fun modifyTDSValueForSpecialCases(aWaterProvider: WaterProvider) {
        val tdsValueString = this.getTDSValueString()
        val number : Int = when (tdsValueString) {
            "" -> TDSMeasurement.UNTESTED_WATER_VALUE
            "0" -> 1
            else -> {
                tdsValueString.toInt()
            }
        }
        aWaterProvider.setLastTDSMeasurementValue(number)
    }

    private fun showConfirmationDialog(yesClicked : () -> Unit, noClicked : () -> Unit) {
        val confirmationDialogTitle= getString(R.string.TDS_Value)
        val messageDialog = getString(R.string.Are_you_SURE_the_TDS_Meter_touched_the_water)
        val yesString = getString(R.string.yes)
        val noString = getString(R.string.no)

        AlertDialog.Builder(this)
            .setTitle(confirmationDialogTitle)
            .setMessage(messageDialog)
            .setPositiveButton(R.string.yes) { _, _ -> yesClicked() }
            .setNegativeButton(R.string.no) { _, _ -> noClicked() }
            .show()
    }

    private fun getTDSValueFromTDSTextEdit() : Int {
        val theStringValue = mTDSTextEdit.text.toString()
        var returnValue = TDSMeasurement.UNTESTED_WATER_VALUE
        if (theStringValue != "") {
            returnValue = theStringValue.toInt()
        }
        return returnValue
    }

    private fun setBackgroundButtonColorToBlue() {
        val color = ContextCompat.getColor(applicationContext, R.color.blueMap)
        this.addButton.setBackgroundColor(color)
    }

    private fun setBackgroundButtonColorToGrey() {
        val color = ContextCompat.getColor(applicationContext, R.color.grey)
        this.addButton.setBackgroundColor(color)
    }

    private fun disableAddButton() {
        this.setBackgroundButtonColorToGrey()
        this.mAddButton.isEnabled = false
        this.mAddButton.text = getString(R.string.sending_with_3_dots)
    }

    private fun enableAddButton() {
        this.setBackgroundButtonColorToBlue()
        this.mAddButton.isEnabled = true
        this.mAddButton.text = getString(R.string.add)
    }

    fun addButtonPressed(@Suppress("UNUSED_PARAMETER") view: View) {
        this.disableAddButton()
        val photoData = retrievePhotoData()
        if (photoData == null) {
            Timber.e("photoData is null in addButtonPressed method")
        }
        val photoByteArray : ByteArray  = this.getByteArrayFromBitmap(photoData as Bitmap)
        val base64PhotoData = this.convertByteArrayToBase64String(photoByteArray)
        val tdsValue : Int = this.getTDSValueFromTDSTextEdit()
        val aTDSMeasurement = TDSMeasurement(tdsValue)
        mFusedLocationClient.lastLocation
            .addOnSuccessListener {location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latitude : Double = location.latitude
                    val longitude : Double = location.longitude
                    val aWaterProvider = WaterProvider(aTDSMeasurement, WaterProviderLocation(latitude, longitude), base64PhotoData)
                    var confirmationDialogHasShownAndUserClickedNo = false
                    if (this.hasUserSpecifiedA0TDSValue()) {
                        this.showConfirmationDialog(yesClicked = {

                        }, noClicked =  {
                            confirmationDialogHasShownAndUserClickedNo = true
                        })
                    }
                    if (!confirmationDialogHasShownAndUserClickedNo) {
                        this.modifyTDSValueForSpecialCases(aWaterProvider)
                        this.createNewWaterLocationToAPIAndFinishActivityOnResponse(aWaterProvider, afterResponseOrFailure =  {
                            enableAddButton()
                        })
                    }
                }
                else {
                    Timber.e("location is null on 'addButtonPressed")
                }
            }
    }
}
