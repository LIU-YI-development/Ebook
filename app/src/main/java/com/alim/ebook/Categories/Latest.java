package com.alim.ebook.Categories;

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

public class Latest extends AppCompatActivity {

    private static final String TAG = "Latest";
    ArrayList<Book> booksList;
    BookAdapter adapter;
    RecyclerView recyclerView;
    RelativeLayout noNetworkLayout;
    Button btnRefresh, btnOpenMyBooks;
    ProgressBar catProgress;
    RelativeLayout nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        TextView booksTitle = findViewById(R.id.books_title);
        booksTitle.setText(R.string.latest);

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
        FirebaseFirestore.getInstance()
                .collection("booksList")
                .limit(12)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        Log.d(TAG, "onEvent: "+error);
                    } else {
                        try{
                            booksList.clear();
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
                        }catch(Exception error1) {
                            error1.printStackTrace();
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
