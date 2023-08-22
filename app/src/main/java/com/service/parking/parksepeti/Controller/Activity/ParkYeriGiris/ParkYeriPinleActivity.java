package com.service.parking.parksepeti.Controller.Activity.ParkYeriGiris;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Utils.LocationConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ParkYeriPinleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 12;

    @BindView(R.id.action_bar_name)
    TextView mActionBarName;

    Boolean mLocationPermissionGranted = false;
    FusedLocationProviderClient mFusedLocationProviderClient;

    @BindView(R.id.back_btn)
    CircleImageView mBackBtn;

    @BindView(R.id.next_btn)
    CircleButton mNextBtn;

    SupportMapFragment supportMapFragment;

    GoogleMap map;
    private Location mLastKnownLocation;

    private int count = 0;
    private String area;
    private String address;
    private String pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_pin);
        ButterKnife.bind(this);


        mActionBarName.setText("Park yerini işaretle");

        mBackBtn.setOnClickListener(v -> onBackPressed());

        mNextBtn.setOnClickListener(v -> {

            if (count > 0) {
                Intent areaAddress = new Intent(ParkYeriPinleActivity.this, AdresSecActivity.class);
                areaAddress.putExtra(LocationConstants.address,address);
                areaAddress.putExtra(LocationConstants.area,area);
                areaAddress.putExtra(LocationConstants.pincode,pincode);

                startActivity(areaAddress,null);
            } else {
                Toasty.error(this, "Önce park yerini seçin").show();
            }
        });

        supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();


        map.setOnMapLongClickListener(v -> {

            count += 1;

            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(v);

            markerOptions.title(v.latitude + " : " + v.longitude);

            map.clear();

            map.animateCamera(CameraUpdateFactory.newLatLng(v));

            map.addMarker(markerOptions);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(v.latitude, v.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0).getAddressLine(0);
            pincode = addresses.get(0).getPostalCode();

            area = addresses.get(0).getSubLocality();
            if (area == null) {
                area = addresses.get(0).getLocality();
            }

            Map<String,Double> pinloc = new HashMap<>();
            pinloc.put("lat",v.latitude);
            pinloc.put("long",v.longitude);

            ParkSepeti.currentLocationpin.setPinloc(pinloc);

            Toasty.success(this, "Konum belirlendi.").show();

        });

        map.setOnMyLocationButtonClickListener(() -> {
            getDeviceLocation();
            return false;
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
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
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                    } else {
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch(SecurityException ignored)  {
        }
    }

}
