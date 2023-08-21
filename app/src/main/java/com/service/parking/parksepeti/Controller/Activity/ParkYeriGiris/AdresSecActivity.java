package com.service.parking.parksepeti.Controller.Activity.ParkYeriGiris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.Controller.Activity.BirincilActivity;
import com.service.parking.parksepeti.Model.LocationPin;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;
import com.service.parking.parksepeti.Utils.LocationConstants;

import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class AdresSecActivity extends AppCompatActivity {

    @BindView(R.id.action_bar_name)
    TextView mActionBarName;

    @BindView(R.id.back_btn)
    CircleImageView mBackBtn;

    @BindView(R.id.next_btn)
    CircleButton mNextBtn;

    @BindView(R.id.area_edit_text)
    ExtendedEditText mArea;

    @BindView(R.id.address_edit_text)
    ExtendedEditText mAddress;

    @BindView(R.id.mobileno_edit_text)
    ExtendedEditText mMobileNo;

    @BindView(R.id.pincode_edit_text)
    ExtendedEditText mPinCode;

    private String area;
    private String address;
    private String pincode;

    String parkingType = "carpool";
    String noOFSpots = "25";

    Boolean coveredFeature = false;
    Boolean staffFeature = false;
    Boolean cameraFeature = false;
    Boolean disabledAccessFeature = false;

    String description = "boşş";
    String price = "25";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_and_address);
        ButterKnife.bind(this);
        ParkSepeti.animate(this);

        area = getIntent().getStringExtra(LocationConstants.area);
        address = getIntent().getStringExtra(LocationConstants.address);
        pincode = getIntent().getStringExtra(LocationConstants.pincode);

        mArea.setText(area);
        mAddress.setText(address);
        mPinCode.setText(pincode);

        mActionBarName.setText("Area and Address");

        mBackBtn.setOnClickListener(v -> {
            onBackPressed();
            ParkSepeti.animate(this);
        });

        mNextBtn.setOnClickListener(v -> {
            String mobileno = mMobileNo.getEditableText().toString();
            area = mArea.getEditableText().toString();
            address = mAddress.getEditableText().toString();
            pincode = mPinCode.getEditableText().toString();

            if (mobileno.isEmpty() || area.isEmpty() || address.isEmpty() || pincode.isEmpty()) {
                Toasty.error(this,"Please fill the information correctly").show();
            } else {
                Map<String,Boolean> features = new HashMap<>();
                features.put("Covered",coveredFeature);
                features.put("Security Camera",cameraFeature);
                features.put("Onsite Staff",staffFeature);
                features.put("Disabled Access",disabledAccessFeature);

                LocationPin locationPin = ParkSepeti.currentLocationpin;

                locationPin.setFeatures(features);
                locationPin.setNumberofspot(noOFSpots);
                locationPin.setType(parkingType);

                locationPin.setAddress(address + " " + pincode);
                locationPin.setArea(area);
                locationPin.setMobile(mobileno);
                locationPin.setPrice("25");
                locationPin.setDescription("null");

                NetworkServices.ParkingPin.setLocationPin(locationPin);
                int position = 2;
                Intent toMain = new Intent(this, BirincilActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                toMain.putExtra("position",position);
                startActivity(toMain,null);
                mNextBtn.setEnabled(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParkSepeti.animate(this);
    }
}
