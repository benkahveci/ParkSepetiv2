package com.service.parking.parksepeti.Controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;
import butterknife.BindView;
import butterknife.ButterKnife;

public class KullanıcıProfiliActivity extends Activity {

    @BindView(R.id.myProfile_name_et)
    EditText mProfileName;

    @BindView(R.id.myProfile_phone_et)
    EditText mProfileMobileNo;

    @BindView(R.id.myProfile_edit_btn)
    ImageButton mProfileEditbtn;

    @BindView(R.id.myProfile_email_et)
    EditText mProfileEmail;

    @BindView(R.id.myProfile_back_btn)
    ImageButton mProfileBackbtn;
    @BindView(R.id.myProfile_save_btn)
    Button myProfileSaveBtn;

    private Boolean isEditEnable = true;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    Boolean fromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_profili);
        ButterKnife.bind(this);


        NetworkServices.ProfileData.setData(mProfileName, mProfileEmail, mProfileMobileNo);
        NetworkServices.ProfileData.getProfileData();

        mProfileEditbtn.setOnClickListener(v -> {

            UI_Update();

        });

        fromLogin = getIntent().getBooleanExtra("from", false);

        if (fromLogin) {
            mProfileBackbtn.setVisibility(View.INVISIBLE);
        } else {
            mProfileBackbtn.setVisibility(View.VISIBLE);
        }

        mProfileBackbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        myProfileSaveBtn.setOnClickListener(v -> {
            isEditEnable = false;
            UI_Update();
        });

    }

    void UI_Update() {

        if (isEditEnable) {

            isEditEnable = false;
            mProfileEmail.setEnabled(true);
            mProfileEmail.setFocusable(true);
            mProfileName.setEnabled(true);
            mProfileName.setFocusable(true);

            mProfileEditbtn.setBackgroundResource(R.drawable.icon_save);


        } else {

            isEditEnable = true;
            mProfileEmail.setEnabled(false);
            mProfileEmail.setFocusable(false);
            mProfileName.setEnabled(false);
            mProfileName.setFocusable(false);

            NetworkServices.ProfileData.updateData(mProfileName.getText().toString(), mProfileEmail.getText().toString(), this);

            mProfileEditbtn.setBackgroundResource(R.drawable.icon_edit);

            if (fromLogin) {
                startActivity(new Intent(this, BirincilActivity.class));
                finish();
            }

        }

    }
}