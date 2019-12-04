package ch.arebsame.coolrunning;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

/**
 * This service uses the Location API to get the speed from GPS
 * This is run as a foreground service to ensure location updates are also provided if the app is
 * in background
 */
public class SpeedService extends Service {

    float speed = 0;
    protected LocationManager locationManager;
    protected LocationListener listener;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 100;

    private GPXGenerator gpxGenerator;

    public SpeedService() {
        if (CoolRunningCom.getSaveRun()) {
            this.gpxGenerator = new GPXGenerator(CoolRunningCom.getRunName());
        }
    }

    @Override
    public void onCreate() {

        /*
        Set this service as a foreground service and show a notification
        References on foreground service:
        https://androidwave.com/foreground-service-android-example
        https://developer.android.com/about/versions/oreo/background-location-limits
         */

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "Speed Service")
                .setContentTitle("Speed Service")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // init location manager for GPS
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        // Retrieve a list of location providers that have fine accuracy, no monetary cost, etc
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(false);

        String providerName = locationManager.getBestProvider(criteria, true);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsEnabled && providerName != null) {
            CoolRunningCom.initPosList();

            //create location listener and request location updates
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null && location.hasSpeed()) {
                        speed = location.getSpeed();
                        CoolRunningCom.setSpeed(speed);
                        CoolRunningCom.addPosition(location.getLatitude(), location.getLongitude());
                        if (CoolRunningCom.getSaveRun() && gpxGenerator != null) {
                            gpxGenerator.addPoint(location);
                        }
                    }
                    else {
                        Log.w("location" ,"no speed in location");
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
        }
        else {
            Toast.makeText(getBaseContext() ,"GPS is not enabled, please enable and try again", Toast.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "Speed Service",
                    "Speed Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
        if (CoolRunningCom.getSaveRun() && gpxGenerator != null) {
            gpxGenerator.writeToFile();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
