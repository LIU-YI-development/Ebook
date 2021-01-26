package com.alim.ebook.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.alim.ebook.Helper.CustomConnection;
import com.alim.ebook.Models.BookRequest;
import com.alim.ebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookRequestActivity extends AppCompatActivity {

    EditText bookTitle, bookAuthor, bookTranslator;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Dialog dialog;
    Button btnClose;
    ProgressDialog progressDialog;
    Spinner spinCategory;
    CustomConnection customConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_book_request);
        bookTitle = findViewById(R.id.editText_book_title);
        bookAuthor = findViewById(R.id.editText_book_author);
        bookTranslator = findViewById(R.id.editText_book_translator);
        spinCategory = findViewById(R.id.spinner_category);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.row_success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);
        btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
            onBackPressed();
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.book_lang, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setDropDownVerticalOffset(60);
        spinCategory.setAdapter(adapter);
        spinCategory.getOnItemClickListener();

    }

    public void requestBook(View view) {
        progressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        progressDialog.setMessage("Sending request..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String userEmail, userName;

        String title, author, translator, category;
        title = bookTitle.getText().toString();
        author = bookAuthor.getText().toString();
        translator = bookTranslator.getText().toString();
        userEmail = user.getEmail();
        userName = user.getDisplayName();
        category = spinCategory.getSelectedItem().toString();

        if (title.isEmpty() | title.trim().equals("")){
            bookTitle.setError("*Book Title is required");
            bookTitle.requestFocus();
            progressDialog.dismiss();
            return;
        } if (author.isEmpty() | author.trim().equals("")){
            bookAuthor.setError("*Book Author is required");
            bookAuthor.requestFocus();
            progressDialog.dismiss();
        } else {
            bookTitle.setError(null);
            bookAuthor.setError(null);
        }
        BookRequest bookRequest = new BookRequest()
                .setUid(mAuth.getCurrentUser().getUid())
                .setUserName(userName)
                .setUserEmail(userEmail)
                .setBookTitle(title)
                .setBookAuthor(author)
                .setBookTranslator(translator)
                .setBookLanguage(category);

        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection("book_request");
        if (isNetworkConnected(BookRequestActivity.this)) {

            ref.add(bookRequest).addOnSuccessListener(aVoid -> {
                progressDialog.dismiss();
                dialog.show();
            });
        } else {
            checkInternetConnection();
        }
    }
    private void checkInternetConnection() {
        if (!isNetworkConnected(BookRequestActivity.this)) {
            customConnection.setConnection(CustomConnection.CONNECTION_STATUS.NO_INTERNET_CONNECTION);
            final Handler handler = new Handler();
            handler.postDelayed(() -> customConnection.hideConnectionBar(), 3000);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void Back(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    }

