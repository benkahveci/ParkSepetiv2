package com.service.parking.parksepeti.Controller.Activity.ParkYeriGiris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.Controller.Activity.BirincilActivity;
import com.service.parking.parksepeti.Model.LocationPin;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;

import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class FiyatActivity extends AppCompatActivity {

    @BindView(R.id.action_bar_name)
    TextView mActionBarName;

    @BindView(R.id.back_btn)
    CircleImageView mBackBtn;

    @BindView(R.id.finish_btn)
    CircleButton mFinishBtn;

    @BindView(R.id.parking_description)
    EditText mDescription;

    @BindView(R.id.price_edit_text)
    ExtendedEditText mPrice;

    @BindView(R.id.add_imageBtn)
    Button mAddImage;

    String description;
    String price;
    LocationPin locationPin = ParkSepeti.currentLocationpin;

    String parkingType = "carpool";
    String noOFSpots = "25";

    Boolean coveredFeature = false;
    Boolean staffFeature = false;
    Boolean cameraFeature = false;
    Boolean disabledAccessFeature = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_and_additional_details);
        ButterKnife.bind(this);
        ParkSepeti.animate(this);
        mActionBarName.setText("Fiyat ve son detaylar");

        mBackBtn.setOnClickListener(v -> {
            onBackPressed();
            ParkSepeti.animate(this);
        });

        mFinishBtn.setOnClickListener(v -> {
            if (checkData()) {
                mFinishBtn.setEnabled(false);
                NetworkServices.ParkingPin.setLocationPin(locationPin);
                int position = 2;
                Intent toMain = new Intent(this, BirincilActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                toMain.putExtra("position",position);
                startActivity(toMain,null);
                mFinishBtn.setEnabled(true);
            }
        });
    }

    boolean checkData() {
        description = mDescription.getText().toString();
        price = mPrice.getEditableText().toString();
        if(price.isEmpty()) {
            Toasty.error(this,"Please fill the information correctly").show();
            return false;
        } else {
            Map<String,Boolean> features = new HashMap<>();
            features.put("Covered",coveredFeature);
            features.put("Security Camera",cameraFeature);
            features.put("Onsite Staff",staffFeature);
            features.put("Disabled Access",disabledAccessFeature);

            locationPin.setFeatures(features);
            locationPin.setNumberofspot(noOFSpots);
            locationPin.setType(parkingType);



            if (description.isEmpty()) {
                description = "No Description Provided by the Host";
                locationPin.setDescription(description);
                locationPin.setPrice(price);
                return true;
            } else {
                locationPin.setDescription(description);
                locationPin.setPrice(price);
                return true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParkSepeti.animate(this);
    }
}
