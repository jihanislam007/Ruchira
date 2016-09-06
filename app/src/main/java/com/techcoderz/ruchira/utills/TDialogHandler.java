package com.techcoderz.ruchira.utills;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Shahriar Workspace on 9/6/2016.
 */
public class TDialogHandler {

    /*@param title*/

    public static void showDialog(Context context, @Nullable String title, @Nullable String message, @Nullable String positiveButtonText,
                                  @Nullable DialogButtonListener positiveButtonListener, @Nullable String negativeButtonText, @Nullable DialogButtonListener negativeButtonListener){
        Activity activityContext = (Activity) context;
        showDialog(activityContext, title, message, positiveButtonText, positiveButtonListener, negativeButtonText, negativeButtonListener,false);

    }

    public static void showDialog(Activity activity, @Nullable String title, @Nullable String message, @Nullable String positiveButtonText,
                                  @Nullable DialogButtonListener positiveButtonListener, @Nullable String negativeButtonText, @Nullable DialogButtonListener negativeButtonListener, @Nullable Boolean canceledOnTouchOutside) {
        if (activity == null) {
            throw new IllegalArgumentException();
        }

        TAlertDialog fcsAlertDialog = new TAlertDialog(activity,canceledOnTouchOutside == null ? true : canceledOnTouchOutside);

        if (TaskUtils.isNotEmpty(title)) {
            fcsAlertDialog.setTitle(title);
        }

        if (TaskUtils.isNotEmpty(message)) {
            fcsAlertDialog.setMessage(message);
        }

        if(TaskUtils.isNotEmpty(positiveButtonText)) {
            fcsAlertDialog.setPositiveButton(positiveButtonText, positiveButtonListener);
        }

        if(TaskUtils.isNotEmpty(negativeButtonText)) {
            fcsAlertDialog.setNegativeButton(negativeButtonText, negativeButtonListener);
        }

        fcsAlertDialog.show();
    }
}
