package com.alim.ebook.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.Models.Report;
import com.alim.ebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class ReportProblemActivity extends AppCompatActivity {

    Uri imageUri;
    EditText editTextTitle, editTextBody;
    ProgressDialog progressDialog;
    ImageView reportThumbnail;
    Dialog dialogReportDone;
    Button btnClose;
    TextView successTitle, successMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        initViews();
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editText_problem_title);
        editTextBody = findViewById(R.id.editText_problem_description);
        reportThumbnail = findViewById(R.id.report_thumbnail);
        dialogReportDone = new Dialog(this);
        dialogReportDone.setContentView(R.layout.row_success_dialog);
        dialogReportDone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogReportDone.getWindow();
        window.setGravity(Gravity.CENTER);
        dialogReportDone.setCanceledOnTouchOutside(true);
        btnClose = dialogReportDone.findViewById(R.id.btn_close);
        successTitle = dialogReportDone.findViewById(R.id.success_title);
        successMessage = dialogReportDone.findViewById(R.id.success_msg);
        btnClose.setOnClickListener(view -> {
            dialogReportDone.dismiss();
            onBackPressed();
        });
    }

    public void reportProblem(View view) {
        String title, description;
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        progressDialog.setMessage("Submitting...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        title = editTextTitle.getText().toString();
        description = editTextBody.getText().toString();

        if (title.isEmpty() | title.trim().equals("")) {
            editTextTitle.setError("*Title is required.");
            editTextTitle.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (description.isEmpty() | description.trim().equals("")) {
            editTextBody.setError("*Description is required.");
            editTextBody.requestFocus();
            progressDialog.dismiss();
        } else {
            editTextTitle.setError(null);
            editTextBody.setError(null);
        }
        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection("reports");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("reports-image");

        if (imageUri != null) {
            try {
                final StorageReference imagePath = storageReference.child(imageUri.getLastPathSegment());
                imagePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imagePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageDownloadLink = uri.toString();
                    Report report = new Report();

                    report.setUserName(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
                    report.setReportText(editTextBody.getText().toString());
                    report.setReportTitle(editTextTitle.getText().toString());
                    report.setUserEmail(mAuth.getCurrentUser().getEmail());
                    report.setReportImage(imageDownloadLink);
                    report.setUid(mAuth.getCurrentUser().getUid());

                    ref.document().set(report).addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        successTitle.setText(R.string.report_submit);
                        successMessage.setText(R.string.thank_feedback);
                        dialogReportDone.show();

                    });
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Report report = new Report();
                report.setUserName(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
                report.setUserEmail(mAuth.getCurrentUser().getEmail());
                report.setReportText(editTextBody.getText().toString());
                report.setReportTitle(editTextTitle.getText().toString());
                report.setUid(mAuth.getCurrentUser().getUid());
                ref.document().set(report).addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    successTitle.setText(R.string.report_submit);
                    successMessage.setText(R.string.thank_feedback);
                    dialogReportDone.show();

                }).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void OpenGallery(View view) {
        checkAndroidVersion();
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    private void cropRequest(Uri imgUri) {
        CropImage.activity(imgUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setOutputCompressQuality(60)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 999 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkAndroidVersion();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imgUri = CropImage.getPickImageResultUri(this, data);
            cropRequest(imgUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            if (resultCode == RESULT_OK) {
                reportThumbnail.setImageURI(result.getUri());
            }
        }
    }

    public void Back(View view) {
        onBackPressed();
    }


}
