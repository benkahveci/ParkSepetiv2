package com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button mLogin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ParkSepeti.animate(this);

        mLogin_btn.setOnClickListener(v -> {

            Intent toMobileVerifyActivity = new Intent(LoginActivity.this, MobileVerifyActivity.class);
            startActivity(toMobileVerifyActivity,null);
        });
    }
}
