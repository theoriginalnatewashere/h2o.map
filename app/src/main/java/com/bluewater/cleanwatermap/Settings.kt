package com.bluewater.cleanwatermap

import android.location.Location

object Settings {

    var filterData : FilterData = FilterData()

    val ignoreFilter : Boolean
        get() = filterData.ignoreFilter

    val userSpecifiedFilter : Boolean
        get() = !this.ignoreFilter

    var filterDistance : Int
        get() = filterData.distance

        set(distance)  {
            assert(distance >= 0)
            filterData.distance = distance
        }

    var onlyTDSTestedWaterMachine : Boolean
        get() = filterData.onlyTDSTestedWaterMachine
        set(aBoolean) {
            filterData.onlyTDSTestedWaterMachine = aBoolean
        }

    var onlySafeWaterMachine : Boolean
        get() = filterData.onlySafeWaterMachine
        set(aBoolean) {
            filterData.onlySafeWaterMachine = aBoolean
        }

    fun matchFilterWithLocationAndWaterProvider(location : Location, aWaterProvider : WaterProvider) : Boolean {
        val tdsValue = aWaterProvider.lastTDSMeasurementValue()
        if (filterData.onlyTDSTestedWaterMachine && tdsValue == TDSMeasurement.UNTESTED_WATER_VALUE) return false
        if (filterData.onlySafeWaterMachine && (tdsValue > TDSMeasurement.SAFE_TDS_VALUE_LIMIT || tdsValue == TDSMeasurement.UNTESTED_WATER_VALUE)) return false
        return if (filterData.ignoreDistance) true else aWaterProvider.distanceTo(location) < filterData.distance
    }

    fun resetDistanceFilter() {
        filterData.ignoreDistance = true
    }

}