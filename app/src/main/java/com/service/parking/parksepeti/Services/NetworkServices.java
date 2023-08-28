package com.service.parking.parksepeti.Services;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.parking.parksepeti.Model.KonumPini;
import com.service.parking.parksepeti.Model.KullanıcıProfili;
import com.service.parking.parksepeti.Utils.KonumConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class NetworkServices {
    static private DatabaseReference REF = FirebaseDatabase.getInstance().getReference();

    public static KullanıcıProfili kullanıcıProfili;

    public static class ProfileData {

        static DatabaseReference mProfileReference = REF.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");

        public static void updateData(String Name, String email, Context con) {

            final Map<String,Object> UserdataMap = new HashMap<>();
            UserdataMap.put("Name",Name);
            UserdataMap.put("Email",email.trim());

            mProfileReference.updateChildren(UserdataMap).addOnCompleteListener(v -> {

                if (v.isSuccessful()) {
                    Toasty.success(con,"Profil Güncellendi").show();

                }
                else {
                    Toasty.success(con,"Tekrar Deneyin").show();
                }
            });

        }

        public static void setData(Object name_et, Object email_et, Object mobile_et) {

            mProfileReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    KullanıcıProfili kullanıcıProfili = dataSnapshot.getValue(KullanıcıProfili.class);
                    NetworkServices.kullanıcıProfili = kullanıcıProfili;

                    if(name_et != null) {
                        if(name_et instanceof EditText) {
                            EditText name = (EditText) name_et;
                            name.setText(kullanıcıProfili.Name);
                        }
                        if(name_et instanceof TextView) {
                            TextView name = (TextView) name_et;
                            name.setText(kullanıcıProfili.Name);
                        }
                    }


                    if(email_et != null) {
                        if(email_et instanceof EditText) {
                            EditText email = (EditText) email_et;
                            email.setText(kullanıcıProfili.Email);
                        }
                        if(email_et instanceof TextView) {
                            TextView email = (TextView) email_et;
                            email.setText(kullanıcıProfili.Email);
                        }
                    }

                    if(mobile_et != null) {
                        if(mobile_et instanceof EditText) {
                            EditText Telefon_no = (EditText) mobile_et;
                            Telefon_no.setText(kullanıcıProfili.Telefon_no);
                        }
                        if(mobile_et instanceof TextView) {
                            TextView Telefon_no = (TextView) mobile_et;
                            Telefon_no.setText(kullanıcıProfili.Telefon_no);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public static void getProfileData() {

            mProfileReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    KullanıcıProfili kullanıcıProfili = dataSnapshot.getValue(KullanıcıProfili.class);
                    NetworkServices.kullanıcıProfili = kullanıcıProfili;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public static void getProfileDataById(String id,TextView nameview) {
            DatabaseReference userRef = REF.child("Users").child(id).child("Profile");

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    KullanıcıProfili user = dataSnapshot.getValue(KullanıcıProfili.class);
                    nameview.setText(user.Name);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public static class ParkingPin {
        static DatabaseReference mGlobalLocationPinRef = REF.child("GlobalPins");
        static DatabaseReference mUserLocationPinRef = REF.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("MyLocationPins");
        public static ArrayList<String> parkingAreas = new ArrayList<>();
        public static Map<String, KonumPini> globalPins = new HashMap<>();

        public static void setLocationPin(KonumPini konumPini) {

            String area = konumPini.getArea();

            String by = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String,Object> locationpin = new HashMap<>();
            locationpin.put(KonumConstant.by,by);

            locationpin.put(KonumConstant.visibility, konumPini.getVisibility());
            locationpin.put(KonumConstant.mobile, konumPini.getMobile());
            locationpin.put(KonumConstant.pinloc, konumPini.getPinloc());
            locationpin.put(KonumConstant.address, konumPini.getAddress());
            locationpin.put(KonumConstant.area,area);

            String pinkey = mGlobalLocationPinRef.push().getKey();

            mGlobalLocationPinRef.child(area).child(pinkey).setValue(locationpin, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    mUserLocationPinRef.child(pinkey).setValue(locationpin);

                }
            });
        }

        public static void getParkingAreas(GoogleMap googleMap) {

            if(!parkingAreas.isEmpty()) {
                for(String area : parkingAreas) {
                    getParkingsOfArea(area,googleMap,false);
                }
            }

            mGlobalLocationPinRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (parkingAreas.isEmpty()) {
                        parkingAreas.add(dataSnapshot.getKey());
                        getParkingsOfArea(dataSnapshot.getKey(),googleMap,false);
                    } else if (!parkingAreas.contains(dataSnapshot.getKey())) {
                        parkingAreas.add(dataSnapshot.getKey());
                        getParkingsOfArea(dataSnapshot.getKey(),googleMap,false);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public static void getParkingsOfArea(String area, GoogleMap googleMap,Boolean fromFragment) {
            if(fromFragment) {
                googleMap.clear();
            }
            if(area == "All") {
                googleMap.clear();
                getParkingAreas(googleMap);
            }
            mGlobalLocationPinRef.child(area).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    KonumPini pin = dataSnapshot.getValue(KonumPini.class);

                    String pinkey = dataSnapshot.getKey();
                    pin.setPinkey(pinkey);
                    LatLng latLng = new LatLng(pin.getPinloc().get("lat"), pin.getPinloc().get("long"));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("")).setTag(pin);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {    }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {    }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {  }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

}
