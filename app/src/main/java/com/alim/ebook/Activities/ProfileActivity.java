package com.alim.ebook.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.CredentialActivity;
import com.alim.ebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class ProfileActivity extends AppCompatActivity {

    LinearLayout layout;
    TextView userName, userEmail, numberOfBooks;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        layout = findViewById(R.id.profile_item);
        userName = layout.findViewById(R.id.user_name);
        userEmail = layout.findViewById(R.id.user_email);
        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        numberOfBooks = layout.findViewById(R.id.tv_number_of_books);
        FirebaseFirestore.getInstance()
                .collection("booksList")
                .get().addOnCompleteListener(task -> {
            try {
                numberOfBooks.setText(("Number of books:  " + task.getResult().getDocuments().size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, CredentialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void askForBook(View view) {
        startActivity(new Intent(this, BookRequestActivity.class));
    }


    public void studyGuide(View view) {
        startActivity(new Intent(this, AppGuideActivity.class));
    }
    public void aboutDev(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }



    public void reportProblemActivity(View view) {
        startActivity(new Intent(this, ReportProblemActivity.class));
    }
    public void Back(View view) {
        onBackPressed();
    }

}
