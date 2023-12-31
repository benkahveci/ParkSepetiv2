package com.service.parking.parksepeti.Controller.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.service.parking.parksepeti.Controller.Activity.GooglePinParkYeriKiralama;
import com.service.parking.parksepeti.Model.KonumPini;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;
import com.service.parking.parksepeti.ParkSepeti;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleKayıtlıYerlerFragment extends Fragment {

    @BindView(R.id.areaNameField)
    TextView mAreaNameField;

    MapView mMapView;
    private GoogleMap googleMap;

    private Location mLastKnownLocation;

    Boolean mLocationPermissionGranted = false;
    FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_yeri_kaydet, container, false);
        ButterKnife.bind(this,view);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        setupMapView();

        return view;
    }

    void setupMapView() {

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            NetworkServices.ParkingPin.getParkingAreas(googleMap);

            getLocationPermission();
            updateLocationUI();
            getDeviceLocation();


            googleMap.setOnMarkerClickListener(marker -> {

                KonumPini pin = (KonumPini) marker.getTag();
                ParkSepeti.selectedKonumPini = pin;

                startActivity(new Intent(getContext(), GooglePinParkYeriKiralama.class));

                return false;
            });
        });
    }

    private void getLocationPermission() {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        if (checkGPSStatus(getActivity())) {
                            mLastKnownLocation = task.getResult();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {

                        }

                    } else {
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static boolean checkGPSStatus(Context context){
        LocationManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    };

    public GoogleKayıtlıYerlerFragment() {
    }

}
