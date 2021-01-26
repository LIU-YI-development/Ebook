package com.alim.ebook.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.BookAdapter;
import com.alim.ebook.Fragments.CategoryFragment;
import com.alim.ebook.Helper.NetworkConnectivity;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    private static final String TAG = "BooksActivity";
    ArrayList<Book> booksList;
    BookAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar catProgress;
    RelativeLayout nestedScrollView;
    RelativeLayout noNetworkLayout;
    Button btnRefresh, btnOpenMyBooks;
    String title;
    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getExtras().getString("title");
        }
        setContentView(R.layout.activity_category);
        TextView booksTitle = findViewById(R.id.books_title);
        booksTitle.setText(title);
        initViews();
        if (!NetworkConnectivity.isNetworkConnected(this)) {
            catProgress.setVisibility(View.GONE);
            noNetworkLayout.setVisibility(View.VISIBLE);
            return;
        }

        initBooks();
        initAdapter();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.books_rv);
        noNetworkLayout = findViewById(R.id.no_network_layout);
        booksList = new ArrayList<>();
        nestedScrollView = findViewById(R.id.nested_layout);
        catProgress = findViewById(R.id.catProgress);
        catProgress.setVisibility(View.VISIBLE);
        findViewById(R.id.img_back).setOnClickListener(view -> onBackPressed());
        btnRefresh = noNetworkLayout.findViewById(R.id.btn_retry);
        btnOpenMyBooks = noNetworkLayout.findViewById(R.id.btn_go_to_mybooks);

        btnRefresh.setOnClickListener(view -> {
            if (NetworkConnectivity.isNetworkConnected(getApplicationContext())) {
                noNetworkLayout.setVisibility(View.GONE);
                catProgress.setVisibility(View.VISIBLE);
                initBooks();
                initAdapter();
            }
        });
        btnOpenMyBooks.setOnClickListener(view -> {
            CategoryFragment.goToMyBooks = true;
            finish();
        });
    }

    private void initBooks() {
        switch (title) {
            case "Story":
                cat = "story";
                break;
            case "Novel":
                cat = "novel";
                break;
            case "Biography":
                cat = "biography";
                break;
            case "Memoirs":
                cat = "memoir";
                break;

//        Literature Ends
            case "Success":
                cat = "success";
                break;
            case "Family and Relations":
                cat = "family";
                break;
            case "Self-development":
                cat = "selfdev";
                break;

            case "Kids":
                cat = "children";
                break;

            case "Literature & Fiction":
                cat = "literature_fiction";
                break;
            case "Entrepreneurship":
                cat = "english_Entrepreneurship";
                break;
            case "Biographies":
                cat = "english_biography";
                break;
            case "Sci-fi":
                cat = "scifi";
                break;
            case "Business":
                cat = "business";
                break;
            case "Comics":
                cat = "comic";
                break;
            case "Other":
                cat = "english_other";
                break;
            case "Computer and Internet":
                cat = "tech_computer";
                break;
            case "Programming":
                cat = "tech_programming";
                break;
            case "E-commerce":
                cat = "tech_online_business";
                break;
            case "Graphic":
                cat = "tech_graphic";
                break;
            case "Network":
                cat = "tech_network";
                break;

            case "Islam":
                cat = "islam";
                break;
            case "World":
                cat = "world";
                break;
        }

        FirebaseFirestore.getInstance()
                .collection("booksList")
                .whereEqualTo("categoryChild", cat)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    booksList.clear();
                    if (error != null) {
                        Log.d(TAG, "onEvent: " + error);
                    } else {
                        for (QueryDocumentSnapshot snapshot : value) {

                            Book book = snapshot.toObject(Book.class);
                            booksList.add(book);
                        }
                        adapter.notifyDataSetChanged();
                        if (booksList.size() == 0) {
                            catProgress.setVisibility(View.GONE);
                            findViewById(R.id.yet_uploading).setVisibility(View.VISIBLE);
                        } else {
                            nestedScrollView.setVisibility(View.VISIBLE);
                            catProgress.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void initAdapter() {
        adapter = new BookAdapter(this, booksList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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
