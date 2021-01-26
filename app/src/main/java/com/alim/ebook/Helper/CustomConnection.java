package com.alim.ebook.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alim.ebook.R;


public class CustomConnection {
    private RelativeLayout connection_parent;
    private TextView connection_text;
    private Context context;

    public CustomConnection(View view, Context context) {
        this.context = context;
        setType(view);
    }

    public enum CONNECTION_STATUS {
        NO_INTERNET_CONNECTION,
        CONNECTION_FAILED,
        CONNECTION_SUCCESS,
        CONNECTION_WAIT,
        SERVER_CONNECTION_FAILED,
        INTERNET_CONNECTION_OK
    }

    private void setType(View view){
        connection_parent = view.findViewById(R.id.connection_parent);
        connection_text = view.findViewById(R.id.connection_text);
    }

    @SuppressLint("ResourceType")
    public void setConnection(CONNECTION_STATUS connection) {
        connection_parent.setVisibility(View.VISIBLE);
        if (connection == CONNECTION_STATUS.NO_INTERNET_CONNECTION) {
            connection_text.setText(context.getString(R.string.no_internet_connection));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionFailed)));
        } else if (connection == CONNECTION_STATUS.CONNECTION_FAILED) {
            connection_text.setText(context.getString(R.string.connection_failed));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionFailed)));
        } else if (connection == CONNECTION_STATUS.CONNECTION_SUCCESS) {
            connection_text.setText(context.getString(R.string.connection_success));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionOk)));
        } else if (connection == CONNECTION_STATUS.SERVER_CONNECTION_FAILED) {
            connection_text.setText(context.getString(R.string.server_connection_failed));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionFailed)));
        }else if(connection == CONNECTION_STATUS.INTERNET_CONNECTION_OK){
            connection_text.setText(context.getString(R.string.ok_internet_connection));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionOk)));
        } else {
            connection_text.setText(context.getString(R.string.connection_wait));
            connection_parent.setBackgroundColor(Color.parseColor(context.getString(R.color.colorConnectionWait)));
        }
    }

    public void hideConnectionBar(){
        connection_parent.setVisibility(View.GONE);
    }
}
