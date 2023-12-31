package com.service.parking.parksepeti.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.service.parking.parksepeti.Controller.Activity.KullanıcıKaydet.TelefonDogrulaActivity;
import com.service.parking.parksepeti.Controller.Fragment.GoogleKayıtlıYerlerFragment;
import com.service.parking.parksepeti.Controller.Fragment.ParkYeriKaydetFragment;
import com.service.parking.parksepeti.R;
import com.service.parking.parksepeti.Services.NetworkServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BirincilActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavView_Bar)
    BottomNavigationViewEx navigation;

    TextView mEmail;
    TextView mName;
    private String fragmentName;
    CircleImageView mProfileImageView;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.custom_bar_image)
    CircleImageView mProfileView;

    @BindView(R.id.fragment_name)
    TextView mFragmentName;

    int position = 0;

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationDrawerItemSelectedListener
            = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.profile_activity:
                return kullanıcıProfiliActivity();

            case R.id.logout_btn:
                return logout();

            default:
                return false;
        }
    };

    private boolean kullanıcıProfiliActivity() {
        Intent toProfile = new Intent(this, KullanıcıProfiliActivity.class);
        startActivity(toProfile,null);
        return false;
    }

    private boolean logout() {

        FirebaseAuth.getInstance().signOut();
        gotToLogin();
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.places_fragment:
                        fragmentName = "Müsait Park Yerleri";
                        fragment = new GoogleKayıtlıYerlerFragment();
                        position = 0;
                        break;


                    case R.id.offer_place_fragment:
                        fragmentName = "Park Yeri Buldum";
                        fragment = new ParkYeriKaydetFragment();
                        position = 1;
                        break;
                }
                return loadFragment(fragment,position);
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birincil);
        ButterKnife.bind(this);

        View headerView = navigationView.getHeaderView(0);

        mEmail = headerView.findViewById(R.id.email_view);
        mName = headerView.findViewById(R.id.name_view);
        mProfileImageView = headerView.findViewById(R.id.profileimageView);

        mProfileImageView.setOnClickListener(v -> kullanıcıProfiliActivity());

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            NetworkServices.ProfileData.setData(mName,mEmail,null);
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(true);
        navigation.setTextVisibility(true);

        int position = getIntent().getIntExtra("position",0);

        navigation.setCurrentItem(position);

        navigationView.setNavigationItemSelectedListener(mOnNavigationDrawerItemSelectedListener);

        mProfileView.setOnClickListener(v1 -> drawerLayout.openDrawer(GravityCompat.START));

    }

    public boolean loadFragment(Fragment fragment,int position) {
        if (fragment != null) {
            mFragmentName.setText(fragmentName);
            navigation.enableShiftingMode(position,true);
            navigation.setItemBackgroundResource(R.color.colorPrimaryDark);
            navigation.setItemBackground(position,R.color.colorPrimary);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
           gotToLogin();
        }
    }

    void gotToLogin() {
        startActivity(new Intent(this, TelefonDogrulaActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
