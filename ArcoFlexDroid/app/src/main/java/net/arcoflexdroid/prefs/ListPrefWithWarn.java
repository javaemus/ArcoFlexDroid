package net.arcoflexdroid.prefs;

import net.arcoflexdroid.Emulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class ListPrefWithWarn extends ListPreference {

    private Context context;

    public ListPrefWithWarn(Context context) {
        super(context);
        this.context = context;
    }

    public ListPrefWithWarn(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if (!positiveResult) {
            super.onDialogClosed(false);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure? (app restart needed)")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ListPrefWithWarn.super.onDialogClosed(true);
                                Emulator.setNeedRestart(true);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListPrefWithWarn.super.onDialogClosed(false);
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

}