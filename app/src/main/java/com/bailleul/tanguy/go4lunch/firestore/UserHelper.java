package com.bailleul.tanguy.go4lunch.firestore;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

/**
 * Methods related to Use Collection in Firebase
 */
public class UserHelper {
    private static final String COLLECTION_NAME = "users";
    private static final String TAG = "USERHELPER";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createUser(String uid, String username, String userEmail,String urlPicture) {
      com.bailleul.tanguy.go4lunch.firestore.User userToCreate = new User(uid, username, userEmail, urlPicture);
        Log.d(TAG, "createUser: ");
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- GET CURRENT USER ID ---
    public static String getCurrentUserId() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    // --- GET CURRENT USER NAME ---
    public static String getCurrentUserName() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }

    // --- GET CURRENT USER URL PICTURE ---
    public static String getCurrentUserUrlPicture() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
    }

    // --- UPDATE NAME---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }


    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    // -- GET ALL USERS --
    public static Query getAllUsers(){
        return UserHelper.getUsersCollection().orderBy("restoDate", Query.Direction.DESCENDING);
    }
}
