package com.service.parking.parksepeti.Controller.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.Model.KonumPini;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class GooglePinParkYeriKiralama extends AppCompatActivity {

    KonumPini selectedPin;
    @BindView(R.id.detail_btn_close)
    ImageView detailCloseBtn;
    @BindView(R.id.details_seg_spot_details)
    SegmentedButton detailsSegSpotDetails;
    @BindView(R.id.detail_person_name)
    TextView detailPersonName;
    @BindView(R.id.detail_person_mobile_no)
    TextView detailPersonMobileNo;
    @BindView(R.id.detail_nextPage)
    CircleButton spotDetail_next_Btn;

    List<Map<String, Object>> slotsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pin_park_yeri);
        ButterKnife.bind(this);
        init();

    }

    void init() {
        selectedPin = ParkSepeti.selectedKonumPini;

        slotsData = new ArrayList<>();

        NetworkServices.ProfileData.getProfileDataById(selectedPin.getBy(), detailPersonName);
        detailPersonMobileNo.setText(selectedPin.getMobile());

        detailCloseBtn.setOnClickListener(v -> finish());

        spotDetail_next_Btn.setOnClickListener(v -> Toasty.success(this,"Reserve Edildi").show());

    }
}
