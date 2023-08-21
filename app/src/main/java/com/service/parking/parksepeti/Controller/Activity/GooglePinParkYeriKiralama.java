package com.service.parking.parksepeti.Controller.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.Model.LocationPin;
import com.service.parking.parksepeti.Model.ParkingBooking;
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

    LocationPin selectedPin;
    @BindView(R.id.detail_btn_close)
    ImageView detailCloseBtn;
    @BindView(R.id.details_seg_spot_details)
    SegmentedButton detailsSegSpotDetails;
    @BindView(R.id.segmentedButtonGroup)
    SegmentedButtonGroup segmentedButtonGroup;
    @BindView(R.id.prof_img)
    CircleImageView profImg;
    @BindView(R.id.detail_person_name)
    TextView detailPersonName;
    @BindView(R.id.call_img)
    CircleImageView callBtn;
    @BindView(R.id.detail_person_mobile_no)
    TextView detailPersonMobileNo;
    @BindView(R.id.spot_type)
    TextView spotType;
    @BindView(R.id.spot_price)
    TextView spotPrice;
    @BindView(R.id.spot_description)
    TextView spotDescription;
    @BindView(R.id.detail_nextPage)
    CircleButton spotDetail_next_Btn;
    @BindView(R.id.spot_detail_layout)
    RelativeLayout spotDetailLayout;
    @BindView(R.id.mobileNoLayout)
    RelativeLayout mobileNoLayout;
    @BindView(R.id.layer0)
    RelativeLayout layer0;

    List<Map<String, Object>> slotsData;

    public static TextView AmountToPay;
    public static RelativeLayout AmountToPayLayout;

    public static String noOfSlotsToBeBooked;
    public static ParkingBooking parkingBooking = new ParkingBooking();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_pin_detail);
        ButterKnife.bind(this);


        init();

    }

    void init() {
        selectedPin = ParkSepeti.selectedLocationPin;

        slotsData = new ArrayList<>();

        NetworkServices.ProfileData.getProfileDataById(selectedPin.getBy(), detailPersonName);
        detailPersonMobileNo.setText(selectedPin.getMobile());
        spotDescription.setText(selectedPin.getDescription()); //Bunu da silelim.
        spotPrice.setText("₹" + selectedPin.getPrice() + "/4 Hour");
        spotType.setText(selectedPin.getType()); //Bunu da silelim.

        detailCloseBtn.setOnClickListener(v -> finish());

        //Buraya bastığımızda toasty verecek. Kiralama tamamlandı falan filan.
        spotDetail_next_Btn.setOnClickListener(v -> Toasty.success(this,"Parking Booked Successfully").show());

    }
}
