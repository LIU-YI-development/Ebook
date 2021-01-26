package com.alim.ebook.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.CredentialActivity;
import com.alim.ebook.MainActivity;
import com.alim.ebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class LogoActivity extends AppCompatActivity {
    private static final String TAG = "LogoActivity";
    FirebaseUser currentUser;
    Animation fadeIn;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        logo = findViewById(R.id.logo);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.setRepeatCount(1);
        logo.setAnimation(fadeIn);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (currentUser != null) {
                Log.i(TAG, "Already logged in");
                startActivity(LogoActivity.this, MainActivity.class);
                finish();
            } else {
                Log.i(TAG, "Not logged in yet");
                startActivity(LogoActivity.this, CredentialActivity.class);
                finish();
            }

        }, 1000);

    }
    public static void startActivity(Context context, Class newClass) {
        Intent intent = new Intent(context, newClass);
        context.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    }
}