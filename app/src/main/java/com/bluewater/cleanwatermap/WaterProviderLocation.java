package com.bluewater.cleanwatermap;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

class WaterProviderLocation implements Parcelable {

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public WaterProviderLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterProviderLocation that = (WaterProviderLocation) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng convertToLatLng() {
        return new LatLng(latitude, longitude);
    }

    // In meters
    public float distanceTo(Location aLocation) {
        Location thisLocation = new Location("");
        thisLocation.setLatitude(latitude);
        thisLocation.setLongitude(longitude);
        return thisLocation.distanceTo(aLocation);
    }

    public float distanceTo(WaterProviderLocation aWaterProviderLocation) {
        Location thisLocation = new Location("");
        thisLocation.setLatitude(latitude);
        thisLocation.setLongitude(longitude);

        Location distantLocation = new Location("");
        distantLocation.setLatitude(aWaterProviderLocation.latitude);
        distantLocation.setLongitude(aWaterProviderLocation.longitude);

        return thisLocation.distanceTo(distantLocation);
    }

    private WaterProviderLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WaterProviderLocation> CREATOR = new Parcelable.Creator<WaterProviderLocation>() {
        @Override
        public WaterProviderLocation createFromParcel(Parcel in) {
            return new WaterProviderLocation(in);
        }

        @Override
        public WaterProviderLocation[] newArray(int size) {
            return new WaterProviderLocation[size];
        }
    };

}