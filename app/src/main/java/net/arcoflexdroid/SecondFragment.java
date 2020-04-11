package net.arcoflexdroid;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.screen;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

                System.out.println("Start...");
                screen.readkey = KeyEvent.KEYCODE_NUMPAD_1;
                screen.key[screen.readkey] = true;
                screen.readkey = KeyEvent.KEYCODE_1;
                screen.key[screen.readkey] = true;
                osd_refresh();

                screen.readkey = KeyEvent.KEYCODE_NUMPAD_1;
                screen.key[screen.readkey] = false;
                screen.readkey = KeyEvent.KEYCODE_1;
                screen.key[screen.readkey] = false;
                osd_refresh();

            }
        });
    }
}
