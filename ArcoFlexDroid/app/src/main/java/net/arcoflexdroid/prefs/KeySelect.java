package net.arcoflexdroid.prefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;


public class KeySelect extends Activity {

    protected int emulatorInputIndex;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        emulatorInputIndex = getIntent().getIntExtra("emulatorInputIndex", 0);
        setTitle("Press button for \""+ListKeys.emulatorInputLabels[emulatorInputIndex]+"\"");


        final Button chancelButton = new androidx.appcompat.widget.AppCompatButton(this) {
            {
                setText("Cancel");
                setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED, new Intent());
                        finish();
                    }
                });
            }
        };

        final Button clearButton = new androidx.appcompat.widget.AppCompatButton(this) {
            {
                setText("Clear");
                setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_OK, new Intent().putExtra("androidKeyCode",  -1));
                        finish();
                    }
                });
            }
        };

        final View primaryView = new View(this) {
            {
                setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
            }
            /*
			@Override
			public boolean onKeyPreIme (int keyCode, KeyEvent event) {

				setResult(RESULT_OK, new Intent().putExtra("androidKeyCode", keyCode));
				finish();
				return true;
			}
			*/
            @Override
            public boolean onKeyDown (int keyCode, KeyEvent event) {

                setResult(RESULT_OK, new Intent().putExtra("androidKeyCode", keyCode));
                finish();
                return true;
            }
        };

        final LinearLayout parentContainer = new LinearLayout(this) {
            {
                setOrientation(LinearLayout.VERTICAL);
                addView(chancelButton);
                addView(clearButton);
                addView(primaryView);
            }
        };

        setContentView(parentContainer, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

    }

}
