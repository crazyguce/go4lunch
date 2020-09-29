package com.bailleul.tanguy.go4lunch.controller;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bailleul.tanguy.go4lunch.R;
import com.bailleul.tanguy.go4lunch.authentification.BaseActivity;
import com.bailleul.tanguy.go4lunch.controller.lunchfragment.ListRestoFragment;
import com.bailleul.tanguy.go4lunch.controller.lunchfragment.ListWorkmatesFragment;
import com.bailleul.tanguy.go4lunch.controller.lunchfragment.MapFragment;


public class LunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        getSupportFragmentManager().beginTransaction().add(R.id.AL_container, new MapFragment()).commit();

    }
}