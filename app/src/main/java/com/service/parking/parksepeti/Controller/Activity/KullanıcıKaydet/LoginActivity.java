package com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.ParkSepeti;

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

//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        ParkSepeti.animate(this);

        mLogin_btn.setOnClickListener(v -> {

            Intent toMobileVerifyActivity = new Intent(LoginActivity.this, MobileVerifyActivity.class);
            Pair pair = new Pair<View, String>(mLogin_btn,"loginTransition");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pair);
            startActivity(toMobileVerifyActivity,options.toBundle());
        });
    }
}
