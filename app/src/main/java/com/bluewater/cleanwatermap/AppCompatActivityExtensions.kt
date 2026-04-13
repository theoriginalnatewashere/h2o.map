package com.bluewater.cleanwatermap

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.removeHeaderBar() {
    supportActionBar?.hide()
}

fun AppCompatActivity.distanceString(distanceInMeter : Int) : String {
    var stringToReturn = ""
    if (distanceInMeter >= 1000) {
        val km : Double = (distanceInMeter.toDouble() / 1000.0)

        val kmString = getString(R.string.km)
        stringToReturn = "%.1f ${kmString}".format(km)  // Within %.1f km
    }
    else {
        val metersString = getString(R.string.meters)
        stringToReturn = "$distanceInMeter $metersString"
    }
    return stringToReturn
}
