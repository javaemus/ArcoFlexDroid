package net.arcoflexdroid.prefs;

import net.arcoflexdroid.Emulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

public class CheckBoxPrefWithWarn extends CheckBoxPreference {

    private Context context;

    public CheckBoxPrefWithWarn(Context context) {
        super(context);
        this.context = context;
    }

    public CheckBoxPrefWithWarn(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onClick() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure? (app restart needed)")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CheckBoxPrefWithWarn.super.onClick();
                                Emulator.setNeedRestart(true);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

}

