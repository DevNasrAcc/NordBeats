package com.adeel.nordbeats.music;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

public class GpsTrackerService extends Service implements LocationListener {
    private Context context;
    Location location;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 20000;

    protected LocationManager locationManager;

    /**
     * @param context Context
     */
    public GpsTrackerService(Context context) {
        this.context = context;
        getLocation();
    }

    /**
     * Get the location on the basis of latitude and longitude
     * after checking the permissions of location/gps
     *
     * @return this
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return location;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled)
                {
                    if (location==null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location!=null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS()
    {
        if (locationManager!=null)
        {
            locationManager.removeUpdates(GpsTrackerService.this);
        }
    }

    /**
     * @return double
     */
    public double getLatitude()
    {
        if (location!=null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * @return double
     */
    public double getLongitude()
    {
        if (location!=null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * @return boolean
     */
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    public void showSettingsAlert()
    {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
        alertdialog.setTitle("GPS settings");
        alertdialog.setMessage("GPS is not enabled, do you want to enable it");
        alertdialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertdialog.show();
    }

    /**
     * @return boolean
     */
    public boolean isGPSEnabled()
    {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isGPSEnabled;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
