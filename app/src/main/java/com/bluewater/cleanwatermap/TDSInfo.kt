package com.bluewater.cleanwatermap

import com.google.android.gms.maps.model.BitmapDescriptorFactory

class TDSInfo constructor(aTdsValue: Int) {

    val tdsValue: Int = aTdsValue
    val tdsInfoCategory : TDSInfoCategory
        get() = getTDSInfoCategory()

    private fun getTDSInfoCategory(): TDSInfoCategory {
        // code debt orange color
        val orangeColor :Int = 0xFFFFA500.toInt()
        val greenColor :Int = 0xFF00C400.toInt()
        val yellowColor :Int = 0xFFD8D800.toInt()
        val redColor :Int = 0xFFDD0000.toInt()
        val highPurityString = Strings.get(R.string.high_purity)
        val averagePurityString = Strings.get(R.string.average_purity)
        val lowPurityString = Strings.get(R.string.low_purity)
        val poorPurityString = Strings.get(R.string.poor_purity)
        return when (tdsValue) {
            0 -> UntestedTDSInfoCategory()
            in 1..TDSMeasurement.SAFE_TDS_VALUE_LIMIT -> TDSInfoCategory(
                tdsValue,
                BitmapDescriptorFactory.HUE_GREEN,
                greenColor,
                highPurityString,
                aWaterDropImageID = R.drawable.green_drop
            )
            in 31..50 -> TDSInfoCategory(
                aTdsValue = tdsValue,
                aGoogleMapsMarkerColor = BitmapDescriptorFactory.HUE_YELLOW,
                aTextColor = yellowColor,
                aPurityString = averagePurityString,
                aWaterDropImageID = R.drawable.yellow_drop
            )
            in 51..75 -> TDSInfoCategory(
                aTdsValue = tdsValue,
                aGoogleMapsMarkerColor = BitmapDescriptorFactory.HUE_ORANGE,
                aTextColor = orangeColor,
                aPurityString = lowPurityString,
                aWaterDropImageID = R.drawable.orange_drop
            )
            else -> TDSInfoCategory(
                aTdsValue = tdsValue,
                aGoogleMapsMarkerColor = BitmapDescriptorFactory.HUE_RED,
                aTextColor = redColor,
                aPurityString = poorPurityString,
                aWaterDropImageID = R.drawable.red_drop
            )
        }
    }
}