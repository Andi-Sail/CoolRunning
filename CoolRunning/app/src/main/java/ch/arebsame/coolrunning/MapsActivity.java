package ch.arebsame.coolrunning;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                // get al logged points
                LinkedList<LatLng> posList = CoolRunningCom.getPositionList();

                // make sure we have at leas 2 points to make a line
                if (posList != null && posList.size() >= 2) {
                    // add line between all points
                    mMap.addPolyline(new PolylineOptions().addAll(posList));

                    // set bounds for map to be zoomed in to relevant region with all points in those bounds
                    LatLngBounds bounds = new LatLngBounds(posList.getFirst(), posList.getFirst());
                    for (LatLng p : posList) {
                        bounds = bounds.including(p);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }
            }
        });
    }

    /**
     * close the map and go back to result
     */
    public void onBackClick(View view)
    {
        finish();
    }
}
