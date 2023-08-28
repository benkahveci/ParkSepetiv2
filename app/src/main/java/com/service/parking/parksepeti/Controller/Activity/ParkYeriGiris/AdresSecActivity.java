package com.service.parking.parksepeti.Controller.Activity.ParkYeriGiris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.Controller.Activity.BirincilActivity;
import com.service.parking.parksepeti.Model.KonumPini;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;
import com.service.parking.parksepeti.Utils.KonumConstant;

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
    TextView mActionAdı;

    @BindView(R.id.back_btn)
    CircleImageView mGeriBtn;

    @BindView(R.id.next_btn)
    CircleButton mIleriBtn;

    @BindView(R.id.area_edit_text)
    ExtendedEditText mBolge;

    @BindView(R.id.address_edit_text)
    ExtendedEditText mAdres;

    @BindView(R.id.mobileno_edit_text)
    ExtendedEditText mCepNo;

    @BindView(R.id.pincode_edit_text)
    ExtendedEditText mPinKodu;

    private String area;
    private String address;
    private String pincode;

    //String parkingType = "carpool";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adres_sec);
        ButterKnife.bind(this);

        area = getIntent().getStringExtra(KonumConstant.area);
        address = getIntent().getStringExtra(KonumConstant.address);
        pincode = getIntent().getStringExtra(KonumConstant.pincode);

        mBolge.setText(area);
        mAdres.setText(address);
        mPinKodu.setText(pincode);

        mActionAdı.setText("Adres Seçimi");

        mGeriBtn.setOnClickListener(v -> onBackPressed());

        mIleriBtn.setOnClickListener(v -> {
            String mobileno = mCepNo.getEditableText().toString();
            area = mBolge.getEditableText().toString();
            address = mAdres.getEditableText().toString();
            pincode = mPinKodu.getEditableText().toString();

            if (mobileno.isEmpty() || area.isEmpty() || address.isEmpty() || pincode.isEmpty()) {
                Toasty.error(this,"Bilgileri Düzgün Girelim").show();
            } else {
                KonumPini konumPini = ParkSepeti.currentLocationpin;

                konumPini.setAddress(address + " " + pincode);
                konumPini.setArea(area);
                konumPini.setMobile(mobileno);


                NetworkServices.ParkingPin.setLocationPin(konumPini);
                int position = 1;
                Intent toMain = new Intent(this, BirincilActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                toMain.putExtra("position",position);
                startActivity(toMain,null);
                mIleriBtn.setEnabled(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
