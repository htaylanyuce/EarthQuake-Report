package com.rapuncat.earthquakereport;


public class EarthQuake {

    private double mMagnitude;
    private double mLatitude;
    private double mLongitude;



    private String mLocation;

    private long mTimeInMilliseconds;

    private String mUrl;


    public EarthQuake(double magnitude, String location, long timeInMilliseconds, String url,double latitude, double longitude) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    public double getMagnitude() {
        return mMagnitude;
    }


    public String getLocation() {
        return mLocation;
    }


    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }


    public String getUrl() {
        return mUrl;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
}