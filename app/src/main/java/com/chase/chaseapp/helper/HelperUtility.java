package com.chase.chaseapp.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class HelperUtility {

    private Context context;

    public HelperUtility(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder buildDialog(String title, String message) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

    public void showToast(String message) {
        Toast.makeText(context, message,
                Toast.LENGTH_LONG).show();
    }

    public int indexOf(String element, String[] items) {
        int i = 0;
        for(String item : items) {
            if(item.equals(element))
                return i;

            i++;
        }

        return -1;
    }

}
