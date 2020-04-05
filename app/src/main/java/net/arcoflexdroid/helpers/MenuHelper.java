package net.arcoflexdroid.helpers;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.Emulator;
import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.R;
import net.arcoflexdroid.input.InputHandler;

public class MenuHelper {

    protected ArcoFlexDroid mm = null;

    public MenuHelper(ArcoFlexDroid value){
        mm = value;
    }

    public boolean createOptionsMenu(Menu menu) {

        MenuInflater inflater = mm.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public boolean prepareOptionsMenu(Menu menu) {

        return true;
    }

    public boolean optionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case (R.id.menu_quit_option):
                mm.showDialog(DialogHelper.DIALOG_EXIT);
                return true;
            case (R.id.menu_quit_game_option):
                if(Emulator.isInMAME())
                {
                    if(!Emulator.isInMenu())
                        mm.showDialog(DialogHelper.DIALOG_EXIT_GAME);
                    else
                    {
                        Emulator.setValue(Emulator.EXIT_GAME_KEY, 1);
                        try {Thread.sleep(100);} catch (InterruptedException e) {}
                        Emulator.setValue(Emulator.EXIT_GAME_KEY, 0);
                    }
                }
                return true;
            case R.id.menu_options_option:
                mm.showDialog(DialogHelper.DIALOG_OPTIONS);
                return true;
            case R.id.vkey_A:
                mm.getInputHandler().handleVirtualKey(InputHandler.C_VALUE);
                return true;
            case R.id.vkey_B:
                mm.getInputHandler().handleVirtualKey(InputHandler.A_VALUE);
                return true;
            case R.id.vkey_X:
                mm.getInputHandler().handleVirtualKey(InputHandler.B_VALUE);
                return true;
            case R.id.vkey_Y:
                mm.getInputHandler().handleVirtualKey(InputHandler.D_VALUE);
                return true;
            case R.id.vkey_MENU:
                mm.getInputHandler().handleVirtualKey(InputHandler.START_VALUE);
                return true;
            case R.id.vkey_SELECT:
                mm.getInputHandler().handleVirtualKey(InputHandler.COIN_VALUE);
                System.out.println("Pulso COIN (IMPLEMENTAR!!!!)");
                // JEmu2
                //ArcoFlexDroid.mm.emulator.keyPress(25);
                // end JEmu2
                return true;
        }

        // JEmu2
        //ArcoFlexDroid.mm.emulator.keyRelease(25);
        // end JEmu2

        return false;

    }

}
