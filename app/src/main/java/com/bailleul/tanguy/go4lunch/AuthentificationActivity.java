package com.bailleul.tanguy.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.firebase.ui.auth.AuthUI;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class AuthentificationActivity extends AppCompatActivity {
    private ConstraintLayout mainActivityLinearLayout;
    private Button facebookBtn;
    private Button googleBtn;
    private static final String TAG = "AUTHENTICATION_ACTIVITY";

    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN_GOOGLE = 123;
    private static final int RC_SIGN_IN_FACEBOOK = 456;

    private static final String USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        layoutLinks();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //links between activity and layout
    private void layoutLinks() {
        mainActivityLinearLayout = findViewById(R.id.mainActivity_constraint_layout);
        facebookBtn = findViewById(R.id.button_login_facebook);
        googleBtn = findViewById(R.id.button_login_google);

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Sign-In Activity when user clicked on Facebook Login Button
                startSignInActivityFacebook();
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Sign-In Activity when user clicked on Google Login Button
                startSignInActivityGoogle();
            }
        });

    }

    private void showSnackBar(LinearLayout linearLayout, String message) {
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //Launch Sign-In Activity with Google
    private void startSignInActivityGoogle() {
        Log.d(TAG, "startSignInActivityGoogle");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN_GOOGLE);
    }

    //Launch Sign-In Activity with Facebook
    private void startSignInActivityFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN_FACEBOOK);
    }

}

