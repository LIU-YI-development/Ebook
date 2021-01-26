package com.alim.ebook.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(view -> AboutActivity.this.onBackPressed());
    }

    private void openProfiles(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void devFacebook(View view) {
        openProfiles("https://m.facebook.com/alim.ahmady.50");
    }

    public void devInstagram(View view) {
    }

    public void devTwitter(View view) {
        openProfiles("https://twitter.com/alim_ahmady");
    }

    public void devGithub(View view) {
        openProfiles("https://github.com/alimahmady");
    }
}
