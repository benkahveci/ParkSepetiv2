package com.service.parking.parksepeti;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet.LoginActivity;
import com.service.parking.parksepeti.Model.LocationPin;
import com.service.parking.parksepeti.Services.NetworkServices;
import com.service.parking.parksepeti.View.ActivityAnimator;

public class ParkSepeti extends Application {

    public static String Mobile_no;
    public static String Person_name;
    public static LocationPin currentLocationpin = new LocationPin();
    public static LocationPin selectedLocationPin = new LocationPin();

    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            Intent LoginIntent = new Intent(this,LoginActivity.class);
            LoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(LoginIntent);
        }
        else  {
            String uid = firebaseAuth.getCurrentUser().getUid();
            currentLocationpin.setBy(uid);
            NetworkServices.ProfileData.getProfileData();
        }

    }

    public static void animate(Activity activity) {
        try {
            ActivityAnimator.fadeAnimation(activity);
        } catch (Exception ignore) {}
    }
    public static void animateSlide(Activity activity) {
        try {
            ActivityAnimator.slideInBottomAnimation(activity);
        } catch (Exception ignore) {}
    }
}
