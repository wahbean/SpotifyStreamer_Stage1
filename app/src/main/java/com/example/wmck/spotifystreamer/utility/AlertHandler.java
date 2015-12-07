package com.example.wmck.spotifystreamer.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wmck.spotifystreamer.R;

/**
 * Created by wmck on 07/07/15.
 *
 * Helper class used to handle alert messages to user.
 *
 */
public class AlertHandler {

    public static void showAlertDialog(String title, String message, Context context)
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = dialogBuilder.create();


        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleView.setPadding(0, 0, 0, 5);

        TextView messageView = new TextView(context);
        messageView.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);
        messageView.setText(message);

        Button dialogButton = new Button(context);
        dialogButton.setText("Ok");
        // need to find a way of placing button in the centre of dialog, for part 2
        dialogButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setView(messageView);
        dialogBuilder.setNeutralButton(context.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }


}
