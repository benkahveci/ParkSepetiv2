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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.parking.parksepeti.Controller.Adapters.ParkYeriSaatleriAdapter;
import com.service.parking.parksepeti.Controller.Adapters.ParkYerlerimAdapter;
import com.service.parking.parksepeti.Model.LocationPin;
import com.service.parking.parksepeti.Model.UserProfile;
import com.service.parking.parksepeti.Utils.LocationConstants;
import com.service.parking.parksepeti.View.SnackbarWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NetworkServices {
    static private DatabaseReference REF = FirebaseDatabase.getInstance().getReference();

    public static UserProfile userProfile;

    public static class ProfileData {

        static DatabaseReference mProfileReference = REF.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");

        public static void updateData(String Name, String email, Context con) {

            final Map<String,Object> UserdataMap = new HashMap<>();
            UserdataMap.put("Name",Name);
            UserdataMap.put("Email",email.trim());

            mProfileReference.updateChildren(UserdataMap).addOnCompleteListener(v -> {

                if (v.isSuccessful()) {
                    SnackbarWrapper.make(con, "Sucessfully Updated Profile");
                }
                else {
                    SnackbarWrapper.make(con, "Please try again!");
                }
            });

        }

        public static void setData(Object name_et, Object email_et, Object mobile_et) {

            mProfileReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    NetworkServices.userProfile = userProfile;

                    if(name_et != null) {
                        if(name_et instanceof EditText) {
                            EditText name = (EditText) name_et;
                            name.setText(userProfile.Name);
                        }
                        if(name_et instanceof TextView) {
                            TextView name = (TextView) name_et;
                            name.setText(userProfile.Name);
                        }
                    }


                    if(email_et != null) {
                        if(email_et instanceof EditText) {
                            EditText email = (EditText) email_et;
                            email.setText(userProfile.Email);
                        }
                        if(email_et instanceof TextView) {
                            TextView email = (TextView) email_et;
                            email.setText(userProfile.Email);
                        }
                    }

                    if(mobile_et != null) {
                        if(mobile_et instanceof EditText) {
                            EditText Mobile_no = (EditText) mobile_et;
                            Mobile_no.setText(userProfile.Mobile_no);
                        }
                        if(mobile_et instanceof TextView) {
                            TextView Mobile_no = (TextView) mobile_et;
                            Mobile_no.setText(userProfile.Mobile_no);
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

                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    NetworkServices.userProfile = userProfile;

                    Log.d("XYZ ABC",NetworkServices.userProfile.Email + " " + NetworkServices.userProfile.Mobile_no + " " + NetworkServices.userProfile.Name);
                    Log.d("XYZ ABC",NetworkServices.userProfile.Total_spots + " " + NetworkServices.userProfile.Balance + " " + NetworkServices.userProfile.Earnings);
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

                    UserProfile user = dataSnapshot.getValue(UserProfile.class);
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
        public static Map<String,LocationPin> globalPins = new HashMap<>();

        public static void setLocationPin(LocationPin locationPin) {

            String area = locationPin.getArea();

            String by = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String,Object> locationpin = new HashMap<>();
            locationpin.put(LocationConstants.by,by);
            locationpin.put(LocationConstants.price,locationPin.getPrice());
            locationpin.put(LocationConstants.visibility,locationPin.getVisibility());
            locationpin.put(LocationConstants.mobile,locationPin.getMobile());
            locationpin.put(LocationConstants.pinloc,locationPin.getPinloc());
            locationpin.put(LocationConstants.address,locationPin.getAddress());
            locationpin.put(LocationConstants.features,locationPin.getFeatures());
            locationpin.put(LocationConstants.type,locationPin.getType());
            locationpin.put(LocationConstants.numberofspot,locationPin.getNumberofspot());
            locationpin.put(LocationConstants.description,locationPin.getDescription());
            locationpin.put(LocationConstants.area,area);

            String pinkey = mGlobalLocationPinRef.push().getKey();

            mGlobalLocationPinRef.child(area).child(pinkey).setValue(locationpin, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    mUserLocationPinRef.child(pinkey).setValue(locationpin);

                    int Spots_used = Integer.parseInt(userProfile.Spots_used) + Integer.parseInt(locationPin.getNumberofspot());
                    Map<String,Object> spotsUpdate = new HashMap<>();
                    spotsUpdate.put("Spots_used",""+Spots_used);
                    ProfileData.mProfileReference.updateChildren(spotsUpdate);

                }
            });
        }

        public static void getMyLocationPins(List<LocationPin> locationPinList, ParkYerlerimAdapter mySpotsAdapter) {
            mUserLocationPinRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    LocationPin pin = dataSnapshot.getValue(LocationPin.class);

                    String pinkey = dataSnapshot.getKey();
                    pin.setPinkey(pinkey);
                    locationPinList.add(pin);
                    mySpotsAdapter.notifyDataSetChanged();
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
                        Log.d("RANDOM AREA :",parkingAreas.toString());
                        getParkingsOfArea(dataSnapshot.getKey(),googleMap,false);
                    } else if (!parkingAreas.contains(dataSnapshot.getKey())) {
                        parkingAreas.add(dataSnapshot.getKey());
                        getParkingsOfArea(dataSnapshot.getKey(),googleMap,false);
                        Log.d("RANDOM AREA :",parkingAreas.toString());
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

                    LocationPin pin = dataSnapshot.getValue(LocationPin.class);

                    String pinkey = dataSnapshot.getKey();
                    pin.setPinkey(pinkey);
                    LatLng latLng = new LatLng(pin.getPinloc().get("lat"), pin.getPinloc().get("long"));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("₹"+pin.getPrice()+"/4 Hour")).setTag(pin);

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


    public static class Booking {
        static DatabaseReference mGlobalLocationPinRef = REF.child("GlobalPins");
        static DatabaseReference mGlobalBookings = REF.child("GlobalBookings");
        static DatabaseReference mUserParkingBookingsRef = REF.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("MyParkingBookings");

        public static void getSlotData(LocationPin locationPin, int year, int monthOfYear, int dayOfMonth, List<Map<String,Object>> slotsDataList, ParkYeriSaatleriAdapter slotsAdapter) {
            mGlobalLocationPinRef.child(locationPin.getArea()).child(locationPin.getPinkey()).child("booking").child(year + "").child("" + (monthOfYear)).child(dayOfMonth + "")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                //Adapter code for booking slots display
                                slotsAdapter.notifyDataSetChanged();

                                for(int i=1;i<=6;i++) {
                                    String slotname = "slot"+i;
                                    Map<String,Object> slot = (Map<String,Object>)dataSnapshot.child(slotname).getValue();
                                    slotsDataList.add(slot);
                                    slotsAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Booking.setDefaultValues(locationPin, year, monthOfYear, dayOfMonth);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private static void setDefaultValues(LocationPin locationPin, int year, int monthOfYear, int dayOfMonth) {
            int time=0;

            Map<String,Object> mains=new HashMap<>();

            for(int i=1;i<=6;i++)
            {
                String st=time+" to "+(time+4);

                time=time+4;
                Map<String,String> s=new HashMap<>();
                s.put("capacity",locationPin.getNumberofspot());
                s.put("booked","0");
                s.put("slot",st);
                mains.put("slot"+i,s);
            }
            mGlobalLocationPinRef.child(locationPin.getArea()).child(locationPin.getPinkey()).child("booking").child(year + "").child("" + (monthOfYear)).child(dayOfMonth + "").setValue(mains).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }

    }

}
