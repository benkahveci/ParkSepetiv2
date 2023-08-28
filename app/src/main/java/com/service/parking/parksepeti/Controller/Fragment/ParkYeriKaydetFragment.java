package com.service.parking.parksepeti.Controller.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.service.parking.parksepeti.Controller.Activity.ParkYeriGiris.ParkYeriPinleActivity;
import com.service.parking.parksepeti.R;
import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ParkYeriKaydetFragment extends Fragment {

    @BindView(R.id.add_parking_btn)
    CircleButton mAddParkingBtn;

    public ParkYeriKaydetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harita, container, false);
        ButterKnife.bind(this,view);

        mAddParkingBtn.setOnClickListener(v ->{
            Intent addPin = new Intent(getContext(), ParkYeriPinleActivity.class);
            startActivity(addPin,null);
        });

        return view;
    }
}
