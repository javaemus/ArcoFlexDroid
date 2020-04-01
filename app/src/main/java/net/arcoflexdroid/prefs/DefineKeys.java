package net.arcoflexdroid.prefs;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.arcoflexdroid.R;
import net.arcoflexdroid.input.InputHandlerExt;

public class DefineKeys extends ListActivity {

    protected int playerIndex = 0;

    public static final String[] playerLabels = {
            "Player 1",
            "Player 2",
            "Player 3",
            "Player 4",
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        drawListAdapter();
    }

    private void drawListAdapter() {
        final Context context = this;

        ArrayAdapter<String> keyLabelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, DefineKeys.playerLabels) {
            @Override
            public View getView(final int position, final View convertView,
                                final ViewGroup parent) {
                final TextView textView = new TextView(context);
                textView.setTextAppearance(context, R.style.ListText);
                textView.setText(getItem(position));
                return textView;
            }
        };

        setListAdapter(keyLabelsAdapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        playerIndex = position;

        boolean auto_detected = false;
        try{auto_detected = InputHandlerExt.deviceIDs[position]!=-1;}catch(Error e){}

        if(auto_detected)
        {
            AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);

            alertDialog.setTitle("GamePad Autodetect is enabled!");
            alertDialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setMessage("Player "+(position+1)+ " gamepad is autodetected! You need to disable autodetection to change keys for this player.");
            alertDialog.show();
        }
        else
        {
            startActivityForResult(new Intent(this, ListKeys.class).putExtra(
                    "playerIndex", playerIndex), 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        drawListAdapter();
    }
}

