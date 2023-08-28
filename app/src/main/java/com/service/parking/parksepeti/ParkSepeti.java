package com.service.parking.parksepeti;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet.TelefonDogrulaActivity;
import com.service.parking.parksepeti.Model.KonumPini;
import com.service.parking.parksepeti.Services.NetworkServices;

public class ParkSepeti extends Application {

    public static String Telefon_no;
    public static String Person_name;
    public static KonumPini currentLocationpin = new KonumPini();
    public static KonumPini selectedKonumPini = new KonumPini();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            Intent LoginIntent = new Intent(this, TelefonDogrulaActivity.class);
            LoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(LoginIntent);
        }
        else  {
            String uid = firebaseAuth.getCurrentUser().getUid();
            currentLocationpin.setBy(uid);
            NetworkServices.ProfileData.getProfileData();
        }
    }
}
