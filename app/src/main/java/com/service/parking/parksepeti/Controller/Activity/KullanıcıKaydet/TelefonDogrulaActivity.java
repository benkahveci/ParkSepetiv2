package com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TelefonDogrulaActivity extends Activity {

    @BindView(R.id.btn_telefon_dogrula_sonraki)
    CircleButton mTelefonDogrula_btn;

    @BindView(R.id.et_telefon_no)
    EditText mTelefonNo;

    @BindView(R.id.et_isim)
    EditText mKullanıcıAdı;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefon_dogrula);
        ButterKnife.bind(this);


        mTelefonDogrula_btn.setOnClickListener(v -> {
            String Telefon_no= mTelefonNo.getText().toString().trim();
            String KullanıcıAdı = mKullanıcıAdı.getText().toString().trim();

            if(!TextUtils.isEmpty(Telefon_no)) {

                if(Telefon_no.length()==10 || !KullanıcıAdı.isEmpty())
                {

                    ParkSepeti.Telefon_no = Telefon_no;
                    ParkSepeti.Person_name = KullanıcıAdı;

                    Intent OtpDogrula = new Intent(TelefonDogrulaActivity.this, OtpDogrulaActivity.class);

                    startActivity(OtpDogrula,null);
                    finish();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
