package com.alim.ebook.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.io.File;

public class BookViewer extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);
        pdfView = findViewById(R.id.pdfView);
        MobileAds.initialize(this, initializationStatus -> {
        });

        findViewById(R.id.img_back).setOnClickListener(view -> onBackPressed());
        TextView textView = findViewById(R.id.book_title);
        Intent intent = getIntent();
        if (intent != null) {
            String file = intent.getExtras().getString("name");
            openPdf(file);
            textView.setText(file);
        }
    }
    private void openPdf(String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
//                File file = getFileStreamPath(fileName);
                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File myFile = new File(folder, "ebook/" + fileName + ".pdf");
                if (myFile.exists()) {
                    pdfView.fromFile(myFile)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .onError(t -> t.printStackTrace())
                            .enableAntialiasing(true)
                            .spacing(0)
                            .enableAnnotationRendering(false)
                            .scrollHandle(new DefaultScrollHandle(this))
//                    ----------
                            .load();
                }
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
