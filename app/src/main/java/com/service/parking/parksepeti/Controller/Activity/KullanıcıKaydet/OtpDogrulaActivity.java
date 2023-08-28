package com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.service.parking.parksepeti.Controller.Activity.BirincilActivity;
import com.service.parking.parksepeti.Controller.Activity.KullanıcıProfiliActivity;
import com.service.parking.parksepeti.ParkSepeti;
import com.service.parking.parksepeti.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpDogrulaActivity extends Activity {

    @BindView(R.id.et_otp_num1)
    EditText et_no1;

    @BindView(R.id.et_otp_num2)
    EditText et_no2;

    @BindView(R.id.et_otp_num3)
    EditText et_no3;

    @BindView(R.id.et_otp_num4)
    EditText et_no4;

    @BindView(R.id.et_otp_num5)
    EditText et_no5;

    @BindView(R.id.et_otp_num6)
    EditText et_no6;

    @BindView(R.id.btn_otp_dogrula_ileri)
    CircleButton mOtp_dogrula_btn;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mTekrarCagir;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mUserDatabase;

    private String OTP = null;
    private String mDogrulamaKodu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_dogrula);
        ButterKnife.bind(this);


        firebaseAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        final String Telefon_no = ParkSepeti.Telefon_no;

        mTekrarCagir = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mDogrulamaKodu = s;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };

        startPhoneNumberVerification(Telefon_no);

        mOtp_dogrula_btn.setOnClickListener(v -> {

            OTP = et_no1.getText().toString() + et_no2.getText().toString() + et_no3.getText().toString()
                    + et_no4.getText().toString() + et_no5.getText().toString() + et_no6.getText().toString();

            verifyPhoneNumberWithCode(mDogrulamaKodu, OTP);

        });

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+90" + phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mTekrarCagir);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        final Map<String, String> UserdataMap = new HashMap<>();
                        UserdataMap.put("Name", ParkSepeti.Person_name);
                        UserdataMap.put("Telefon_no", ParkSepeti.Telefon_no);

                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {

                            String Token = instanceIdResult.getToken().trim();
                            UserdataMap.put("device_token", Token);


                        });

                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            mUserDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile").setValue(UserdataMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Intent profileIntent = new Intent(OtpDogrulaActivity.this, KullanıcıProfiliActivity.class);
                                    profileIntent.putExtra("from", true);

                                    startActivity(profileIntent, null);
                                    finish();
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), Objects.requireNonNull(task1.getException()).getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Intent mainIntent = new Intent(OtpDogrulaActivity.this, BirincilActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent, null);
                            finish();
                        }

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

}
