package com.bailleul.tanguy.go4lunch.controller.lunchfragment;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bailleul.tanguy.go4lunch.R;
import com.bailleul.tanguy.go4lunch.util.MyDateFormat;
import com.bailleul.tanguy.go4lunch.controller.DisplayNearbyPlaces;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, DisplayNearbyPlaces {

    private View mView;

    private final static String TAG = "MapFragment";
    private static final float DEFAULT_ZOOM = 16f;

    //widgets
    private ImageView mGps;

    //Vars
    private GoogleMap mMap;
    private double myLatitude;
    private double myLongitude;
    private String today;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mGps = mView.findViewById(R.id.ic_gps);

        MyDateFormat forToday = new MyDateFormat();
        today = forToday.getTodayDate();

        // Default position before geolocalisation
        myLatitude = 48.858511;
        myLongitude = 2.294524;

        return mView;
    }



    public void setUserLocation(LatLng userLatLng){
        // update location with geolocalisation
        myLatitude = userLatLng.latitude;
        myLongitude = userLatLng.longitude;

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), DEFAULT_ZOOM));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMap();
    }

    //-------------------------------------------------------------------------------------------------------------
    // Map
    //-------------------------------------------------------------------------------------------------------------
    //initializing map
    private void initMap() {
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), DEFAULT_ZOOM));

        try {

            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        init();
    }


    private void init() {
        // click on gps
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), DEFAULT_ZOOM));
            }
        });
    }


}