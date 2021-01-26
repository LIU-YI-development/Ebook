package com.alim.ebook.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Activities.BookDetailActivity;
import com.alim.ebook.Activities.BookViewer;
import com.alim.ebook.Models.FavBook;
import com.alim.ebook.Models.FavBooks;
import com.alim.ebook.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.MyBooksViewHolder> {
    private static final String TAG = "MyBooksAdapter";
    static int ReqCode = 1;
    private final Context mContext;
    private final ArrayList<FavBook> list;
    FavBook favBook;
    private String telLink;
    private String tel;

    public MyBooksAdapter(Context mContext, ArrayList<FavBook> mList) {
        this.mContext = mContext;
        this.list = mList;
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @NonNull
    @Override
    public MyBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.books_row_item, parent, false);
        return new MyBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBooksViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        FavBook book = list.get(position);
        Glide.with(mContext).load(book.getBookThumbnail())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.imageView.setImageResource(R.drawable.ic_photo);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void check() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ReqCode);
            } else {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ReqCode);
            }
        } else {
            openBook();
        }
    }

    private void openBook() {
        try {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(folder, "ebook/" + favBook.getBookTitle() + ".pdf");
            if (myFile.exists() && myFile.length() > 10) {
                Intent intent = new Intent(mContext, BookViewer.class);
                intent.putExtra("name", favBook.getBookTitle());
                Log.d(TAG, "onPostExecute: " + favBook.getBookTitle());
                mContext.startActivity(intent);

            } else if (!myFile.exists() && isNetworkConnected(mContext)) {
                downloadFile(favBook.getBookDownloadLink(), favBook.getBookTitle());
            } else if (myFile.length() <= 10 && isNetworkConnected(mContext)) {
                myFile.delete();
                downloadFile(favBook.getBookDownloadLink(), favBook.getBookTitle());
            } else {
                Toast.makeText(mContext, "Internet Connection is required.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String url, String name) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        request.setDescription("Downloading...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/ebook/" + name + ".pdf");
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        Log.d(TAG, "downloadFile: request" + request);

        if (manager != null) {
            manager.enqueue(request);
            Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "downloadFile: started" + manager);

        }

    }

    private void openDetail(String id, Dialog dialog) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra("bookId", id);
        mContext.startActivity(intent);
        dialog.dismiss();

    }

    class MyBooksViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView options;
        ProgressBar progressBar;

        Dialog dialog;
        TextView txtAddComment, txtShare, txtDelete, txtAboutBook;

        MyBooksViewHolder(@NonNull final View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.m_progress);
            imageView = itemView.findViewById(R.id.mBookThumbnail);
            options = itemView.findViewById(R.id.imgBtn_option);
            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.option_row);
            dialog.setCanceledOnTouchOutside(true);

            txtAddComment = dialog.findViewById(R.id.add_comment);
            txtShare = dialog.findViewById(R.id.txt_share);
            txtDelete = dialog.findViewById(R.id.delete_file);
            txtAboutBook = dialog.findViewById(R.id.book);
            imageView.setOnClickListener(view -> {
                try {
                    initSeekBar();
                    favBook = list.get(getAdapterPosition());
                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File myFile = new File(folder, "ebook/" + favBook.getBookTitle() + ".pdf");
                    if (Build.VERSION.SDK_INT >= 22) {
                        check();
                    } else {
                        if (myFile.exists() && myFile.length() > 10) {
                            Intent intent = new Intent(mContext, BookViewer.class);
                            intent.putExtra("name", favBook.getBookTitle());
                            mContext.startActivity(intent);
                        } else if (!myFile.exists() && isNetworkConnected(mContext)) {
                            downloadFile(favBook.getBookDownloadLink(), favBook.getBookTitle());
                        } else if (myFile.length() <= 10 && isNetworkConnected(mContext)) {
                            myFile.delete();
                            downloadFile(favBook.getBookDownloadLink(), favBook.getBookTitle());
                        } else {
                            Toast.makeText(mContext, "Internet Connection Required.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            options.setOnClickListener(view -> dialog.show());
            txtDelete.setOnClickListener(view -> {
                try {
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    String user = mAuth.getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference ref = db.collection("favBooks");
                    FavBook myBook = list.get(getAdapterPosition());
                    for (int i = 0; i < list.size(); i++) {
                        FavBook myfav = list.get(i);
                        if (myfav.getBookId().equals(myBook.getBookId())) {
                            list.remove(i);
                            break;
                        }
                    }
                    FavBooks favourites = new FavBooks();
                    favourites.setBooks(list);
                    ref.document(user).set(favourites);
                    dialog.dismiss();
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            txtAddComment.setOnClickListener(view -> {
                openDetail(list.get(getAdapterPosition()).getBookId(), dialog);
            });

            txtAboutBook.setOnClickListener(view -> {
                openDetail(list.get(getAdapterPosition()).getBookId(), dialog);
            });
            txtShare.setOnClickListener(view -> {
                Toast.makeText(mContext, "Share Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        }

        private void initSeekBar() {
            ProgressDialog pd = new ProgressDialog(mContext);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMessage("Please wait...");
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setCanceledOnTouchOutside(false);

        }
    }

}