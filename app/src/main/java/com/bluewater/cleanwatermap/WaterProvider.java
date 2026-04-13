package com.bluewater.cleanwatermap;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class WaterProvider implements Parcelable {
    private String id;
    @SerializedName("body")
    private LocalDateTime creationDate;
    private TDSMeasurement[] tdsMeasurements;
    private WaterProviderLocation location;
    private String photoData; // base 64

    public WaterProvider(TDSMeasurement tdsMeasurements, WaterProviderLocation waterProviderLocation, String photoData) {
        this.tdsMeasurements = new TDSMeasurement[1];
        this.tdsMeasurements[0] = tdsMeasurements;
        this.location = waterProviderLocation;
        this.photoData = photoData;
    }

    protected WaterProvider(Parcel in) {
        id = in.readString();
        creationDate = (LocalDateTime)in.readValue(LocalDateTime.class.getClassLoader());
        tdsMeasurements = in.createTypedArray(TDSMeasurement.CREATOR);
        location = in.readParcelable(WaterProviderLocation.class.getClassLoader());
        photoData = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeValue(creationDate);
        dest.writeTypedArray(tdsMeasurements, flags);
        dest.writeParcelable(location, flags);
        dest.writeString(photoData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WaterProvider> CREATOR = new Creator<WaterProvider>() {
        @Override
        public WaterProvider createFromParcel(Parcel in) {
            return new WaterProvider(in);
        }

        @Override
        public WaterProvider[] newArray(int size) {
            return new WaterProvider[size];
        }
    };

    public int lastTDSMeasurementValue() {
        if (BuildConfig.DEBUG) {
            assert (tdsMeasurements.length > 0);
        }
        return tdsMeasurements[tdsMeasurements.length -1].tdsValue;
    }

    public void setLastTDSMeasurementValue(int value) {
        tdsMeasurements[tdsMeasurements.length - 1].tdsValue = value;
    }

    public String googleNavigationString() {
        return String.format(Locale.getDefault(), "google.navigation:q=%.7f,%.7f&mode=l", this.location.getLatitude(), this.location.getLongitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterProvider that = (WaterProvider) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creationDate, that.creationDate) &&
                Arrays.equals(tdsMeasurements, that.tdsMeasurements) &&
                Objects.equals(location, that.location) &&
                Objects.equals(photoData, that.photoData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, creationDate, location, photoData);
        result = 31 * result + Arrays.hashCode(tdsMeasurements);
        return result;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public TDSMeasurement[] getTdsMeasurements() {
        return tdsMeasurements;
    }

    public void setTdsMeasurements(TDSMeasurement[] tdsMeasurements) {
        this.tdsMeasurements = tdsMeasurements;
    }

    public WaterProviderLocation getWaterProviderLocation() {
        return location;
    }

    public void setWaterProviderLocation(WaterProviderLocation aLocation) {
        this.location = aLocation;
    }

    public String getId() {
        return id;
    }

    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }

    public float distanceTo(WaterProvider waterProvider) {
        return location.distanceTo(waterProvider.location);
    }

    public float distanceTo(Location aLocation) {
        return location.distanceTo(aLocation);
    }

    public boolean isCloseTo(WaterProvider aWaterProvider, float thresholdDistance) {
        return this.distanceTo(aWaterProvider) < thresholdDistance;
    }

}
