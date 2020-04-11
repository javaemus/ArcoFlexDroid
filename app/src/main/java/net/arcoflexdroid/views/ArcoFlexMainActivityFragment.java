package net.arcoflexdroid.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import net.arcoflexdroid.R;

public class ArcoFlexMainActivityFragment extends Fragment {

    private ArcoFlexEmulatorView view;
    //private jMESYSControl control;

    public View getEmulatorView() {
        return view;
    }

    /*public jMESYSControl getEmulatorControl() {
        return control;
    }*/

    public ArcoFlexMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // initialize the display
        //this.frame = new C64View(this);
        //setContentView(this.frame);

        //return inflater.inflate(R.layout.fragment_main, container, false);

        //return mainView;
        Context c = getContext();
        view = new ArcoFlexEmulatorView(getContext());

        view.setId(R.id.ScreenEmulator);

        /*control = new jMESYSControl(c, view);
        control.setId(R.id.InputPanelEmulator);*/

        RelativeLayout layout = new RelativeLayout(c);
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //p1.addRule(RelativeLayout.ABOVE, control.getId());
        p1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addView(view, p1);
        //layout.addView(control, p2);
        //setContentView(layout);




        return layout;
    }
}

