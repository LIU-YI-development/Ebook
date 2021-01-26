package com.alim.ebook;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alim.ebook.Fragments.CategoryFragment;
import com.alim.ebook.Fragments.HomeFragment;
import com.alim.ebook.Fragments.MyBooksFragment;
import com.alim.ebook.Fragments.SearchFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "MainActivity";
    public static BottomNavigationView bottomNavigation;

    private final FragmentManager manager = getSupportFragmentManager();
    Toast backToast;
    long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("ebook", "ebook", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general");
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> Log.d(TAG, "Token: "+s));
        if (savedInstanceState == null) {
            loadFragment();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.home: {
                Fragment c = manager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                Fragment s = manager.findFragmentByTag(SearchFragment.class.getSimpleName());
                Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
                Fragment h = manager.findFragmentByTag(HomeFragment.class.getSimpleName());
                if (c != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                if (s != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(SearchFragment.class.getSimpleName())).commit();
                if (m != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();
                if (h != null)
                    manager.beginTransaction().show(manager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                else
                    manager.beginTransaction().add(R.id.frame_container, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();

            }
            return true;

            case R.id.category: {

                Fragment h = manager.findFragmentByTag(HomeFragment.class.getSimpleName());
                Fragment s = manager.findFragmentByTag(SearchFragment.class.getSimpleName());
                Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
                Fragment c = manager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                if (h != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                if (s != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(SearchFragment.class.getSimpleName())).commit();
                if (m != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();

                if (c != null)
                    manager.beginTransaction().show(manager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                else {
                    manager.beginTransaction().add(R.id.frame_container, new CategoryFragment(), CategoryFragment.class.getSimpleName()).commit();

                }

            }
            return true;
            case R.id.search: {
                Fragment c = manager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                Fragment h = manager.findFragmentByTag(HomeFragment.class.getSimpleName());
                Fragment s = manager.findFragmentByTag(SearchFragment.class.getSimpleName());
                Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
                if (h != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                if (c != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                if (m != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();
                if (s != null)
                    manager.beginTransaction().show(manager.findFragmentByTag(SearchFragment.class.getSimpleName())).commit();
                else {
                    manager.beginTransaction().add(R.id.frame_container, new SearchFragment(), SearchFragment.class.getSimpleName()).commit();
                }

            }
            return true;
            case R.id.my_books: {
                Fragment c = manager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                Fragment h = manager.findFragmentByTag(HomeFragment.class.getSimpleName());
                Fragment s = manager.findFragmentByTag(SearchFragment.class.getSimpleName());
                Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
                if (h != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                if (c != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                if (s != null)
                    manager.beginTransaction().hide(manager.findFragmentByTag(SearchFragment.class.getSimpleName())).commit();
                if (m != null)
                    manager.beginTransaction().show(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();
                else {
                    manager.beginTransaction().add(R.id.frame_container, new MyBooksFragment(), MyBooksFragment.class.getSimpleName()).commit();
                }
            }
            return true;
        }
        return false;
    };
    private void loadFragment() {
        manager.beginTransaction().replace(R.id.frame_container, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Double click to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, CredentialActivity.class));
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(getTokenResult -> {
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
