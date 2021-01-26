package com.alim.ebook.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.CommentAdapter;
import com.alim.ebook.Adapters.RelatedContentAdapter;
import com.alim.ebook.Models.Book;
import com.alim.ebook.Models.Comment;
import com.alim.ebook.Models.FavBook;
import com.alim.ebook.Models.FavBooks;
import com.alim.ebook.Models.Group;
import com.alim.ebook.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BookDetailActivity extends AppCompatActivity {

    boolean flag;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    static int ReqCode = 1;
    FirebaseAuth mAuth;
    ImageView thumbnail;
    TextView bookTitle, bookAuthor, bookDescription;
    Button btnDownload, addToFav;
    String bookId;
    RatingBar ratingBar, userRating;
    Book book;
    long rateVal;
    float total = 0;
    long numberOfRates;
    LinearLayout linearLayout;
    // ----- Comments Views start ----
    CommentAdapter adapter;
    ArrayList<Comment> commentsList;
    RecyclerView commentRv;
    EditText editTextComment;
    ProgressDialog progressDialog;
    //    rating
    LinearLayout layout;
    Button btnMore;
    LinearLayout iUnderstand;
    Button btnUnderstand, btnInsta, btnFb, btnTelegram;
    LinearLayout progressLayout;
    TextView ratingText, numOfVotes;

    //---- related content
    private RelatedContentAdapter relatedContentAdapter;
    private ArrayList<Book> relatedBooks;
    RecyclerView rvRelatedContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        double x = mDisplay.getWidth();
        double y = mDisplay.getHeight();
        if (x < 768 && y < 1366) {
            setContentView(R.layout.activity_book_detail);
        } else {
            setContentView(R.layout.activity_book_detail);
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(this);
        progressLayout = findViewById(R.id.pg_layout);
        initViews();
        isSaved(bookId, addToFav);
    }

    private void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            bookId = intent.getExtras().getString("bookId");
        }
        flag = false;
        bookDescription = findViewById(R.id.tv_description);
        thumbnail = findViewById(R.id.report_thumbnail);
        bookTitle = findViewById(R.id.txt_book_title);
        bookAuthor = findViewById(R.id.txt_book_author);
        btnDownload = findViewById(R.id.btn_download_book);
        addToFav = findViewById(R.id.add_to_shelf);
        ratingBar = findViewById(R.id.ratingBar);
        btnMore = findViewById(R.id.btnMore);
        userRating = findViewById(R.id.user_rating);
        linearLayout = findViewById(R.id.detail_activity_parent);
        ratingText = findViewById(R.id.rating_text);
        layout = findViewById(R.id.rate_me);
        btnFb = layout.findViewById(R.id.facebook_page);
        btnInsta = layout.findViewById(R.id.instagram_page);
        btnTelegram = layout.findViewById(R.id.telegram_channel);
        iUnderstand = findViewById(R.id.layout_i_understand);
        btnUnderstand = iUnderstand.findViewById(R.id.btn_understand);
        bookDescription.setMaxLines(10);
        btnMore.setText("See more");

        numOfVotes = findViewById(R.id.num_of_votes);
        setAverageRating();
        initCommentViews();
        rvRelatedContent = findViewById(R.id.recycler_related_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvRelatedContent.setLayoutManager(layoutManager);
        //set adapter
        ArrayList<Group> title = new ArrayList<>();
        relatedBooks = new ArrayList<>();
        title.add(new Group()
                .setGroupTitle("Related Books")
                .setGroupButtonTitle("View All »")
        );
        relatedContentAdapter = new RelatedContentAdapter(this, title, relatedBooks);
        rvRelatedContent.setAdapter(relatedContentAdapter);
        setAverageRating();
        initCommentViews();


        btnUnderstand.setOnClickListener(view -> iUnderstand.setVisibility(View.GONE));
        userRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            if (ratingBar.getRating() == 1) {
                ratingText.setText("Very Weak");
                ratingText.setVisibility(View.VISIBLE);
            }
            if (v == 2) {
                ratingText.setText("I don't like it");
                ratingText.setVisibility(View.VISIBLE);
            }
            if (v == 3) {
                ratingText.setText("Not bad");
                ratingText.setVisibility(View.VISIBLE);
            }
            if (v == 4) {
                ratingText.setText("Good");
                ratingText.setVisibility(View.VISIBLE);
            }
            if (v == 5) {
                ratingText.setText("Very Good");
                ratingText.setVisibility(View.VISIBLE);
            }
        });

        loadBooks();

    }

    private void loadRelatedBook(String cat) {
        FirebaseFirestore.getInstance()
                .collection("booksList")
                .orderBy("categoryChild")
                .startAt(cat)
                .endAt(cat + "\uf0ff")

                .limitToLast(7)
                .addSnapshotListener((value, error) -> {
                    relatedBooks.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        Book book = snapshot.toObject(Book.class);
                        if (!book.getKey().equals(bookId)) relatedBooks.add(book);
                    }
                    relatedContentAdapter.notifyDataSetChanged();
                    if (relatedBooks.size() == 0) {
                        rvRelatedContent.setVisibility(View.GONE);
                    }
                });

    }

    private void loadBooks() {
        FirebaseFirestore.getInstance()
                .collection("booksList")
                .document(bookId)
                .addSnapshotListener((value, error) -> {
                    try {
                        if (value.exists()) {
                            book = value.toObject(Book.class);
                            bookTitle.setText(book.getBookTitle());
                            if (book.getBookDescription().trim().length() > 0) {
                                bookDescription.setText(book.getBookDescription());
                            } else {
                                RelativeLayout layout = findViewById(R.id.parent_description);
                                layout.setVisibility(View.GONE);
                            }
                            bookAuthor.setText(book.getBookAuthor());
                            Glide.with(getApplicationContext()).load(book.getBookThumbnail())
                                    .into(thumbnail);
                            downloadBook(book.getBookDownloadLink(), book.getBookTitle());
                            setUserRating();
                            progressLayout.setVisibility(View.GONE);
                            loadRelatedBook(book.getCategoryChild());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    //    Add book to bookshelf
    public void addBookToMyShelf(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("favBooks");

        if (addToFav.getText().equals("Add to library")) {
            try {
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                ref.document(uid).get().addOnSuccessListener(documentSnapshot -> {
                    FavBooks books = documentSnapshot.toObject(FavBooks.class);
                    FavBook newBook = new FavBook();
                    newBook.setBookId(bookId);
                    newBook.setBookThumbnail(book.getBookThumbnail());
                    newBook.setBookTitle(book.getBookTitle());
                    newBook.setBookAuthor(book.getBookAuthor());
                    newBook.setBookDownloadLink(book.getBookDownloadLink());

                    if (books == null) {
                        books = new FavBooks();
                        books.setBooks(new ArrayList<>());
                    }
                    books.getBooks().add(newBook);
                    ref.document(uid).set(books).addOnSuccessListener(aVoid -> showSnack("Added to library."));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //   OnDownload Click
    private void downloadBook(final String url, final String title) {
        btnDownload.setOnClickListener(view -> {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(folder, "/ebook/" + title + ".pdf");
            try {
                if (Build.VERSION.SDK_INT >= 22) {
                    check(url, title);
                } else {
                    if (myFile.exists() && myFile.length() > 10) {
                        Toast.makeText(BookDetailActivity.this, "Already exists in" + " Download/ebook ", Toast.LENGTH_LONG).show();
                    } else if (myFile.exists() && myFile.length() <= 10) {
                        myFile.delete();
                        downloadFile(url, title);
                    } else {
                        downloadFile(url, title);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void isSaved(final String savedBookId, final Button btn) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("favBooks");

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            ref.document(uid).addSnapshotListener((documentSnapshot, e) -> {
                FavBooks books = documentSnapshot.toObject(FavBooks.class);
                if (books != null) {
                    for (FavBook b : books.getBooks()) {
                        if (b.getBookId().equals(savedBookId)) {
                            btn.setText("Av. in library");
                            btn.setEnabled(false);
                            btn.setClickable(false);
                            @SuppressLint("UseCompatLoadingForDrawables") Drawable bgColor = getResources().getDrawable(R.drawable.bg_disabled);
                            btn.setBackground(bgColor);
                            btn.setTextColor(getResources().getColor(R.color.black));
                            Drawable icBookmark = getApplicationContext().getResources().getDrawable(R.drawable.ic_bookmark_black);
                            btn.setCompoundDrawablesWithIntrinsicBounds(icBookmark, null, null, null);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAverageRating() {

        FirebaseFirestore.getInstance()
                .collection("comments")
                .document(bookId)
                .collection("comments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    numberOfRates = queryDocumentSnapshots.size();
                    float sumOfAll = 0;
                    try {
                        numOfVotes.setText(((numberOfRates)) + " vote");
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            rateVal = (long) snapshot.getDouble("rating").floatValue();
                            Log.d("TAG", "val: "+rateVal);
                            total += rateVal;
                            Log.d("TAG", "total: "+total);
                        }
                        sumOfAll = (total / numberOfRates);
                        Log.d("TAG", "all: "+sumOfAll);
                        ratingBar.setRating(sumOfAll);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void setUserRating() {

        FirebaseFirestore.getInstance()
                .collection("comments")
                .document(bookId)
                .collection("comments")
                .document(mAuth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            double rate = (double) documentSnapshot.get("rating");
                            userRating.setRating((long) rate);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //    Check for permissions
    private void check(String url, String title) {
        if (ContextCompat.checkSelfPermission(BookDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BookDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(BookDetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ReqCode);
            } else {
                ActivityCompat.requestPermissions(BookDetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ReqCode);
            }
        } else {
            checkFile(url, title);
        }
    }

    //    check if file exists offline
    private void checkFile(String url, String title) {
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(folder, "ebook/" + title + ".pdf");
        if (myFile.exists() && myFile.length() > 10) {
            Toast.makeText(BookDetailActivity.this, "Already exists in" + " Download/ebook ", Toast.LENGTH_LONG).show();
        } else if (myFile.exists() && myFile.length() <= 10) {
            myFile.delete();
            downloadFile(url, title);
        } else {
            downloadFile(url, title);
        }
    }

    //    Download file
    public void downloadFile(String url, String name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        request.setDescription("Downloading...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/ebook/" + name + ".pdf");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        if (manager != null) {
            manager.enqueue(request);
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        }
    }

    //    sharing section
    public void allApps(View view) {
        String bookTitle = "\uD83D\uDCD7 Book " + book.getBookTitle();
        String author;
        if (book.getBookAuthor() != null) {
            author = "✍️ By " + book.getBookAuthor();
        } else {
            author = "✍️ By {Author} ";
        }
        String ebook = "Download from EbookApp";
        String nextLine = "\n";
//        ---------------------------------------
        String intentOne = bookTitle + nextLine + author + " " + ebook + "\n\n";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, intentOne );
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "EbookApp");
        startActivity(Intent.createChooser(sharingIntent, "Share"));

    }

    //    Comments Section
    private void initCommentViews() {
        commentRv = findViewById(R.id.recyclerView_comments);
        editTextComment = findViewById(R.id.editText_comment);

        loadComments();
        initCommentsAdapter();
    }

    public void saveComment(View view) {
        progressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        progressDialog.setMessage("Adding comment...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        editTextComment = findViewById(R.id.editText_comment);
        final String stringComment = editTextComment.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userName = user.getDisplayName();
        final String uid = user.getUid();

        if (stringComment.equals("") && userRating.getRating() == 0.0) {
            editTextComment.setError("Please rate!");
            editTextComment.requestFocus();
            progressDialog.dismiss();
        } else if (!stringComment.equals("") | userRating.getRating() != 0.0) {
            editTextComment.setError(null);
            editTextComment.clearComposingText();
            float value = userRating.getRating();

            if (value < 1) {
                value = 5;
                Object timeStamp = System.currentTimeMillis();
                Map<String, Object> comment = new HashMap<>();
                comment.put("bookId", bookId);
                comment.put("uid", uid);
                comment.put("userName", userName);
                comment.put("rating", value);
                comment.put("comment", stringComment);
                comment.put("timeStamp", timeStamp);

                FirebaseFirestore.getInstance()
                        .collection("comments")
                        .document(bookId)
                        .collection("comments")
                        .document(mAuth.getUid())
                        .set(comment).addOnSuccessListener(aVoid -> {
                    editTextComment.setText("");
                    userRating.setRating(0.0f);
                    ratingText.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    showSnack("Comment posted!");
                });
            } else {
//                Comment comment = new Comment(bookId, uid, userName, value, stringComment);
                Object timestamp = System.currentTimeMillis();
                Map<String, Object> comment = new HashMap<>();
                comment.put("bookId", bookId);
                comment.put("uid", uid);
                comment.put("userName", userName);
                comment.put("rating", value);
                comment.put("comment", stringComment);
                comment.put("timeStamp", timestamp);
                FirebaseFirestore.getInstance()
                        .collection("comments")
                        .document(bookId)
                        .collection("comments")
                        .document(mAuth.getUid())
                        .set(comment).addOnSuccessListener(aVoid -> {
                    editTextComment.setText("");
                    ratingBar.setRating(0.0f);
                    ratingText.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    showSnack("Comment posted!");
                });

            }
        }

    }

    private void loadComments() {
        commentsList = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("comments")
                .document(bookId)
                .collection("comments")
                .addSnapshotListener((value, error) -> {
                    commentsList.clear();
                    try {
                        if (!value.isEmpty()) {
                            commentRv.setVisibility(View.VISIBLE);
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Comment comment = snapshot.toObject(Comment.class);
                                commentsList.add(comment);
                            }
                            adapter.notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    private void initCommentsAdapter() {
        adapter = new CommentAdapter(this, commentsList);
        commentRv.setLayoutManager(new LinearLayoutManager(this));
        commentRv.setAdapter(adapter);
        commentRv.setNestedScrollingEnabled(true);

    }

    //    Profiles
    public void openFacebookPage(View view) {
        makeToast("Facebook URL");
    }

    public void openTelegramChannel(View view) {
        makeToast("Telegram Channel Link");
    }

    public void openInstagramPage(View view) {
        makeToast("Instagram Page Link");
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    expand/collapse description
    public void description(View view) {
        if (flag) {
            bookDescription.setMaxLines(10);
            btnMore.setText("See more");
            flag = false;

        } else {
            bookDescription.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            bookDescription.setMaxLines(100);
            btnMore.setText("Close");
            flag = true;
        }
    }


    private void showSnack(String text) {
        Snackbar snackbar = Snackbar.make(linearLayout, text, Snackbar.LENGTH_SHORT);
        ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_LTR);
        snackbar.show();
    }

    public void studyGuide(View view) {
        startActivity(new Intent(this, AppGuideActivity.class));
    }

    public void Back(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
