package com.bluewater.cleanwatermap

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptorFactory

open class TDSInfoCategory

constructor(aTdsValue : Int,
            aGoogleMapsMarkerColor : Float,
            @ColorInt aTextColor : Int,
            aPurityString : String,
            aWaterDropImageID : Int
) {

    val tdsValue = aTdsValue
    val googleMapsMarkerColor = aGoogleMapsMarkerColor
    val textColor = aTextColor
    val purityString = aPurityString
    val waterDropImageID : Int = aWaterDropImageID
    open val tdsValueString : String
        get() {
            return "${Strings.get(R.string.TDS_Value)} $tdsValue"
        }
}

class UntestedTDSInfoCategory : TDSInfoCategory(
    TDSMeasurement.UNTESTED_WATER_VALUE,
    BitmapDescriptorFactory.HUE_MAGENTA,
    Color.GRAY,
    "", // no purity string
    R.drawable.gray_drop

) {

    override  val tdsValueString : String
        get() {
            return "${Strings.get(R.string.Untested_Water)}"
        }
}