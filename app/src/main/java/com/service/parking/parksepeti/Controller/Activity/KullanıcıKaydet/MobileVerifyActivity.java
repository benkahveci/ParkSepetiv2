package com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MobileVerifyActivity extends Activity {

    @BindView(R.id.btn_phone_verify_next)
    CircleButton mPhoneVerify_btn;

    @BindView(R.id.et_phone_number)
    EditText mPhoneNumber;

    @BindView(R.id.mobile_verify_back_btn)
    ImageView mMobile_verify_back_btn;

    @BindView(R.id.et_name)
    EditText mPersonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);
        ButterKnife.bind(this);

        ParkSepeti.animate(this);


        mMobile_verify_back_btn.setOnClickListener(v -> onBackPressed());

        mPhoneVerify_btn.setOnClickListener(v -> {
            String Mobile_no= mPhoneNumber.getText().toString().trim();
            String PersonName = mPersonName.getText().toString().trim();

            if(!TextUtils.isEmpty(Mobile_no)) {

                if(Mobile_no.length()==10 || !PersonName.isEmpty())
                {

                    ParkSepeti.Mobile_no = Mobile_no;
                    ParkSepeti.Person_name = PersonName;

                    Intent otpActivity = new Intent(MobileVerifyActivity.this,OtpVerifyActivity.class);

                    startActivity(otpActivity,null);
                    finish();
                    ParkSepeti.animate(this);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParkSepeti.animate(this);
    }
}
