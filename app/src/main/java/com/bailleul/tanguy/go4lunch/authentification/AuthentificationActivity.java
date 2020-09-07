package com.bailleul.tanguy.go4lunch.authentification;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bailleul.tanguy.go4lunch.R;
import com.bailleul.tanguy.go4lunch.firestore.UserHelper;
import com.firebase.ui.auth.AuthUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.Objects;

public class AuthentificationActivity extends BaseActivity {
    private ConstraintLayout mainActivityConstraintLayout;
    private Button facebookBtn;
    private Button googleBtn;
    private static final String TAG = "AUTHENTICATION_ACTIVITY";

    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN_GOOGLE = 123;
    private static final int RC_SIGN_IN_FACEBOOK = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        layoutLinks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_authentification;
    }

    // Retrieves the return of the authentication activity to check if it went well
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    //links between activity and layout
    private void layoutLinks() {
        mainActivityConstraintLayout = findViewById(R.id.mainActivity_constraint_layout);
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

    private void updateUIWhenResuming() {
        if (this.isCurrentUserLogged()) {
            Log.d(TAG, "updateUIWhenResuming: currentUserLogged");
            facebookBtn.setVisibility(View.GONE);
            googleBtn.setVisibility(View.GONE);

        } else {
            Log.d(TAG, "updateUIWhenResuming: currentUser not Logged");
            facebookBtn.setVisibility(View.VISIBLE);
            googleBtn.setVisibility(View.VISIBLE);
        }
    }
    @Nullable
    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null); }


    private void showSnackBar(ConstraintLayout linearLayout, String message) {
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

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        Log.d(TAG, "handleResponseAfterSignIn");
        if (requestCode == RC_SIGN_IN_GOOGLE || requestCode == RC_SIGN_IN_FACEBOOK)
        {
            if (resultCode == RESULT_OK) { // SUCCESS
                Log.d(TAG, "handleResponseAfterSignIn: Success");
                // CREATE USER IN FIRESTORE
                this.createUserInFirestore();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.mainActivityConstraintLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.mainActivityConstraintLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.mainActivityConstraintLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    //---------------------
    // REST REQUEST
    //---------------------
    // Http request that create user in firestore

    private void createUserInFirestore(){
        if (isCurrentUserLogged()) {
            // if (UserHelper.getCurrentUserId() == null) {
            Log.d(TAG, "createUserInFirestore");
            String urlPicture = (Objects.requireNonNull(this.getCurrentUser()).getPhotoUrl() != null) ? Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            String userEmail = this.getCurrentUser().getEmail();
            UserHelper.createUser(uid, username, userEmail, urlPicture).addOnFailureListener(this.onFailureListener());
            //}
        }
    }


}

