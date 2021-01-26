package com.alim.ebook.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.RelatedBookAdapter;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RelatedBooks extends AppCompatActivity {

    private static final String TAG = "RelatedBooks";
    ArrayList<Book> booksList;
    RelatedBookAdapter adapter;
    RecyclerView recyclerView;
    String bookCategory, bookId;
    ProgressBar catProgress;
    RelativeLayout nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        TextView booksTitle = findViewById(R.id.books_title);
        booksTitle.setText(R.string.related_books);
        initViews();
        initAdapter();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.books_rv);
        booksList = new ArrayList<>();
        findViewById(R.id.img_back).setOnClickListener(view -> onBackPressed());
        nestedScrollView = findViewById(R.id.nested_layout);
        catProgress = findViewById(R.id.catProgress);
        catProgress.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            bookCategory = intent.getExtras().getString("category");
            bookId = intent.getExtras().getString("bookId");
            FirebaseFirestore.getInstance()
                    .collection("booksList")
                    .orderBy("categoryChild")
                    .startAt(bookCategory)
                    .endAt(bookCategory + "\uf0ff")
                    .limitToLast(15)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d(TAG, "onEvent: "+error);
                        } else {
                            booksList.clear();
                            for (QueryDocumentSnapshot snapshot : value) {
                                Book book = snapshot.toObject(Book.class);
                                booksList.add(book);
                            }
                            adapter.notifyDataSetChanged();
                            if (booksList.size() == 0) {
                                catProgress.setVisibility(View.GONE);
                                findViewById(R.id.yet_uploading).setVisibility(View.VISIBLE);
                                return;
                            }
                            nestedScrollView.setVisibility(View.VISIBLE);
                            catProgress.setVisibility(View.GONE);
                        }
                    });

        }
    }


    private void initAdapter() {
        adapter = new RelatedBookAdapter(this, booksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
    }

    public void BackBtn(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
