package com.bluewater.cleanwatermap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_filter.*


class FilterActivity : AppCompatActivity() {

    /*
        UI Names
        applyFiltersButton
        safeWaterMachineSwitch
        onlyTDSTestedWaterMachineSwitch
        distanceSeekBar
        titleTextView
     */

    private var mTmpFilterData = FilterData()

    companion object {
        const val MINIMUM_DISTANCE = 50
        const val MAXIMUM_SEEK_BAR_VALUE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        configureSeekBarChangeListener()
        this.configureApplyFiltersButton()
        this.configureOnlyTDSTestedWaterMachineSwitch()
        this.configureSafeWaterMachineSwitch()
        this.updateUIFromFilter()
        this.removeHeaderBar()
    }

    private fun updateUIFromFilter() {
        if (Settings.userSpecifiedFilter) {
            distanceSeekBar.progress = this.seekBarValueFromFilterDistance(Settings.filterDistance)
            onlyTDSTestedWaterMachineSwitch.isChecked = Settings.onlyTDSTestedWaterMachine
            safeWaterMachineSwitch.isChecked = Settings.onlySafeWaterMachine
            updateTitleFromSeekBarValue(distanceSeekBar.progress)
        }
    }

    private fun configureSafeWaterMachineSwitch() {
        this.safeWaterMachineSwitch.setOnCheckedChangeListener { _, isChecked ->
            mTmpFilterData.onlySafeWaterMachine = isChecked
        }
    }

    private fun configureOnlyTDSTestedWaterMachineSwitch() {
        this.onlyTDSTestedWaterMachineSwitch.setOnCheckedChangeListener { _, isChecked ->
            mTmpFilterData.onlyTDSTestedWaterMachine = isChecked
        }
    }

    private fun configureSeekBarChangeListener() {
        distanceSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int,
                                           fromUser: Boolean) {
                // write custom code for progress is changed
                updateTitleFromSeekBarValue(progress)
                if (progress >= MAXIMUM_SEEK_BAR_VALUE) {
                    mTmpFilterData.ignoreDistance = true
                }
                else {
                    mTmpFilterData.distance = maxOf(progress * 50, MINIMUM_DISTANCE)
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })
    }

    private fun configureApplyFiltersButton() {
        applyFiltersButton.setOnClickListener {
            this.sendFilterDataBackToPreviousActivity()
        }
    }

    private fun seekBarValueFromFilterDistance(filterDistance : Int) : Int {
        assert(filterDistance >= 0)
        return maxOf(filterDistance / 50, MINIMUM_DISTANCE / 50)
    }

    private fun filterDistanceFromSeekBarValue(seekBarValue : Int) : Int {
        return maxOf(seekBarValue * 50, MINIMUM_DISTANCE)
    }

    private fun updateTitleFromSeekBarValue(seekBarValue : Int) {
        if (seekBarValue >= MAXIMUM_SEEK_BAR_VALUE) {
            titleTextView.text = getString(R.string.No_filter_distance)
        }
        else {
            val meters : Int = this.filterDistanceFromSeekBarValue(seekBarValue)
            if (meters >= 1000) {
                val km : Double = (meters.toDouble() / 1000.0)
                titleTextView.text = getString(R.string.Within_X_km_point_1_precision).format(km)  // Within %.1f km
            }
            else {
                val withinXMeterString = getString(R.string.WithinXMeters) // Within X Meters
                titleTextView.text = withinXMeterString.replace("X", meters.toString())
            }

        }
    }

    private fun sendFilterDataBackToPreviousActivity() {
        Settings.filterData = mTmpFilterData
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
}
