package com.service.parking.parksepeti.Controller.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service.parking.parksepeti.R;

import butterknife.ButterKnife;


public class ReserveEdilenFragment extends Fragment {

    public ReserveEdilenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking, container, false);
        ButterKnife.bind(this,v);

        return v;
    }


}