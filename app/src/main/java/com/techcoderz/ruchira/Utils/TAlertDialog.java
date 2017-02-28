package com.techcoderz.ruchira.Utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

/**
 * Created by Shahriar Workspace on 9/6/2016.
 */
public class TAlertDialog extends AlertDialog {

    public TAlertDialog(Context context, Boolean canceledOnTouchOutside) {
        super(context);
        this.setCanceledOnTouchOutside(canceledOnTouchOutside);
        this.setCancelable(canceledOnTouchOutside);
    }

    public TAlertDialog(Context context, String message) {
        super(context);
        setMessage(message);
    }

    public void setText(String textMessage) {
        setMessage(textMessage);
    }

    public void setTitle(String title) {
        setTitle(title);
    }

    public void setPositiveButton(String buttonText, @Nullable final DialogButtonListener positiveButtonListener) {
        setButton(BUTTON_POSITIVE, buttonText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (positiveButtonListener == null) {
                    return;
                }
                positiveButtonListener.onDialogButtonClick();
            }
        });
    }

    public void setNegativeButton(String buttonText, @Nullable final DialogButtonListener negativeButtonListener) {
        setButton(BUTTON_NEGATIVE, buttonText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (negativeButtonListener == null) {
                    return;
                }
                negativeButtonListener.onDialogButtonClick();

            }
        });
    }

    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
