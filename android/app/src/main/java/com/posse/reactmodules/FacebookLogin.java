package com.posse.reactmodules;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.posse.models.User;

public class FacebookLogin extends ReactContextBaseJavaModule implements LifecycleEventListener, ActivityEventListener {
    private static final String FACEBOOK_LOGIN = "FacebookLogin";
    final ReactApplicationContext reactContext;
    private Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("Facebook login", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d("Facebook login", "onAuthStateChanged:signed_out");
            }
        }
    };
    private String fbid;
    private String firstName;
    private String lastName;
    private String gender;

    public FacebookLogin(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
        this.reactContext.addLifecycleEventListener(this);
        mAuth.addAuthStateListener(mAuthListener);
        this.activity = activity;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public String getName() {
        return FACEBOOK_LOGIN;
    }

    @ReactMethod
    public void handleFacebookAccessToken(String token, String fbid, String firstName, String lastName, String gender) {
        this.fbid = fbid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Facebook login", "signinWithCredential:onComplete" + task.isSuccessful());

                if (task.isSuccessful()) {
                  onAuthSuccess(task.getResult().getUser());
                } else {
                    Log.w("Facebook login", "signinWithCredential", task.getException());
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        writeNewUser(user.getUid());
    }

    private void writeNewUser(String uid) {
        User user = new User(fbid, firstName, lastName, gender);
        mDatabase.child("users").child(uid).setValue(user);
    }

    @ReactMethod
    public void handleSignout() {
        mAuth.signOut();
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
