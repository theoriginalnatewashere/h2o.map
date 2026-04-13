package com.bluewater.cleanwatermap;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

public class TDSMeasurement implements Parcelable {
    String id;
    @SerializedName("body")
    LocalDateTime date;
    String deviceName;
    int tdsValue;

    static private String defaultDeviceName = "TDS-3";
    static private String deviceNameForUntestedWater = "Untested";
    static public int UNTESTED_WATER_VALUE = 0;
    static public int SAFE_TDS_VALUE_LIMIT = 30;

    public TDSMeasurement(int tdsValue) {
        this.tdsValue = tdsValue;
        this.date = LocalDateTime.now();
        if (tdsValue == UNTESTED_WATER_VALUE) {
            this.deviceName = deviceNameForUntestedWater;
        }
        else {
            this.deviceName = defaultDeviceName;
        }
    }

    private TDSMeasurement(Parcel in) {
        id = in.readString();
        date = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());
        deviceName = in.readString();
        tdsValue = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeValue(date);
        dest.writeString(deviceName);
        dest.writeInt(tdsValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TDSMeasurement> CREATOR = new Parcelable.Creator<TDSMeasurement>() {
        @Override
        public TDSMeasurement createFromParcel(Parcel in) {
            return new TDSMeasurement(in);
        }

        @Override
        public TDSMeasurement[] newArray(int size) {
            return new TDSMeasurement[size];
        }
    };
}