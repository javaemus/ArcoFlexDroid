package net.arcoflexdroid.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

import net.arcoflexdroid.Emulator;
import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.input.ControlCustomizer;

public class DialogHelper {

    public static int savedDialog = DialogHelper.DIALOG_NONE;

    public final static int DIALOG_NONE = -1;
    public final static int DIALOG_EXIT = 1;
    public final static int DIALOG_ERROR_WRITING = 2;
    public final static int DIALOG_INFO = 3;
    public final static int DIALOG_EXIT_GAME = 4;
    public final static int DIALOG_OPTIONS = 5;
    public final static int DIALOG_THANKS = 6;
    public final static int DIALOG_FULLSCREEN = 7;
    public final static int DIALOG_LOAD_FILE_EXPLORER = 8;
    public final static int DIALOG_ROMs_DIR = 9;
    public final static int DIALOG_FINISH_CUSTOM_LAYOUT = 10;
    public final static int DIALOG_EMU_RESTART = 11;

    protected ArcoFlexDroid mm = null;

    static protected String errorMsg;
    static protected String infoMsg;

    public void setErrorMsg(String errorMsg) {
        DialogHelper.errorMsg = errorMsg;
    }

    public void setInfoMsg(String infoMsg) {
        DialogHelper.infoMsg = infoMsg;
    }

    public DialogHelper(ArcoFlexDroid value){
        mm = value;
    }

    public Dialog createDialog(int id) {

        if(id==DialogHelper.DIALOG_LOAD_FILE_EXPLORER)
        {
            return mm.getFileExplore().create();
        }

        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mm);
        switch(id) {
            case DIALOG_FINISH_CUSTOM_LAYOUT:

                builder.setMessage("Do you want to save changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_FINISH_CUSTOM_LAYOUT);
                                ControlCustomizer.setEnabled(false);
                                mm.getInputHandler().getControlCustomizer().saveDefinedControlLayout();
                                mm.getEmuView().setVisibility(View.VISIBLE);
                                mm.getEmuView().requestFocus();
                                Emulator.resume();
                                mm.getInputView().invalidate();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_FINISH_CUSTOM_LAYOUT);
                                ControlCustomizer.setEnabled(false);
                                mm.getInputHandler().getControlCustomizer().discardDefinedControlLayout();
                                mm.getEmuView().setVisibility(View.VISIBLE);
                                mm.getEmuView().requestFocus();
                                Emulator.resume();
                                mm.getInputView().invalidate();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_ROMs_DIR:

                builder.setMessage("Do you want to use default ROMs path? (recomended)")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_ROMs_DIR);
                                if(mm.getMainHelper().ensureInstallationDIR(mm.getMainHelper().getInstallationDIR()))
                                {
                                    mm.getPrefsHelper().setROMsDIR("");
                                    mm.runArcoFlexDroid();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_ROMs_DIR);
                                mm.showDialog(DialogHelper.DIALOG_LOAD_FILE_EXPLORER);
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_EXIT:

                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //System.exit(0);
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Emulator.resume();
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_EXIT);
                                //dialog.cancel();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_ERROR_WRITING:
                builder.setMessage("Error")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //System.exit(0);
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_ERROR_WRITING);
                                mm.getMainHelper().restartApp();
                                //mm.showDialog(DialogHelper.DIALOG_LOAD_FILE_EXPLORER);
                            }
                        });

                dialog = builder.create();
                break;
            case DIALOG_INFO:
                builder.setMessage("Info")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                Emulator.resume();
                                mm.removeDialog(DIALOG_INFO);
                            }
                        });

                dialog = builder.create();
                break;
            case DIALOG_EXIT_GAME:
                builder.setMessage("Are you sure you want to exit game?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                Emulator.resume();
                                Emulator.setValue(Emulator.EXIT_GAME_KEY, 1);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Emulator.setValue(Emulator.EXIT_GAME_KEY, 0);
                                mm.removeDialog(DIALOG_EXIT_GAME);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Emulator.resume();
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.removeDialog(DIALOG_EXIT_GAME);
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_OPTIONS:
            case DIALOG_FULLSCREEN:
                final CharSequence[] items1 = {"Load State", "Save State","Help","Settings", "Netplay" /*"Support",*/};
                final CharSequence[] items2 = {"Help","Settings", "Netplay"/*"Support"*/};
                final CharSequence[] items3 = {"Exit","Load State", "Save State","Help","Settings", "Netplay"/*"Support",*/};
                final CharSequence[] items4 = {"Exit","Help","Settings", "Netplay" /*"Support"*/};

                final int a = id == DIALOG_FULLSCREEN ? 0 : 1;
                final int b = Emulator.isInMAME() ? 0 : 2;

                if(a == 1)
                    builder.setTitle("Choose an option from the menu.");

                builder.setCancelable(true);
                builder.setItems(Emulator.isInMAME() ? (id==DIALOG_OPTIONS?items1:items3) : (id==DIALOG_OPTIONS?items2:items4), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if(item == 0 && a==0)
                        {
                            if(Emulator.isInMenu())
                            {
                                Emulator.setValue(Emulator.EXIT_GAME_KEY, 1);
                                Emulator.resume();
                                try {Thread.sleep(100);} catch (InterruptedException e) {}
                                Emulator.setValue(Emulator.EXIT_GAME_KEY, 0);
                            }
                            else if(!Emulator.isInMAME())
                                mm.showDialog(DialogHelper.DIALOG_EXIT);
                            else
                                mm.showDialog(DialogHelper.DIALOG_EXIT_GAME);
                        } else if (item == 1-a &&  b==0){ Emulator.setValue(Emulator.LOADSTATE, 1);Emulator.resume();
                        } else if (item == 2-a &&  b==0){ Emulator.setValue(Emulator.SAVESTATE, 1);Emulator.resume();
                        } else if (item == 3-a-b){ mm.getMainHelper().showHelp();
                        } else if (item == 4-a-b){ mm.getMainHelper().showSettings();
                        } else if (item == 5-a-b){
                            //mm.showDialog(DialogHelper.DIALOG_THANKS);
                            //mm.getNetPlay().showView();
                            mm.getNetPlay().createDialog();

                        }

                        DialogHelper.savedDialog = DIALOG_NONE;
                        mm.removeDialog(DIALOG_OPTIONS);
                        mm.removeDialog(DIALOG_FULLSCREEN);
                    }
                });
                builder.setOnCancelListener(new  DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        DialogHelper.savedDialog = DIALOG_NONE;
                        Emulator.resume();
                        if(a!=0)
                            mm.removeDialog(DIALOG_OPTIONS);
                        else
                            mm.removeDialog(DIALOG_FULLSCREEN);
                    }
                });
                dialog = builder.create();
                break;
            case DIALOG_THANKS:
                builder.setMessage("I am releasing everything for free, in keeping with the licensing MAME terms, which is free for non-commercial use only. This is strictly something I made because I wanted to play with it and have the skills to make it so. That said, if you are thinking on ways to support my development I suggest you to check my support page of other free works for the community.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DialogHelper.savedDialog = DIALOG_NONE;
                                mm.getMainHelper().showWeb();
                                mm.removeDialog(DIALOG_THANKS);
                            }
                        });

                dialog = builder.create();
                break;
            case DIALOG_EMU_RESTART:
                builder.setTitle("Restart needed!")
                        .setMessage("ArcoFlexDroid needs to restart for the changes to take effect.")
                        .setCancelable(false)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mm.getMainHelper().restartApp();
                            }
                        });
                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
	    /*
	    if(dialog!=null)
	    {
	    	dialog.setCanceledOnTouchOutside(false);
	    }*/
        return dialog;

    }

    public void prepareDialog(int id, Dialog dialog) {

        if(id==DIALOG_ERROR_WRITING)
        {
            ((AlertDialog)dialog).setMessage(errorMsg);
            DialogHelper.savedDialog = DIALOG_ERROR_WRITING;
        }
        else if(id==DIALOG_INFO)
        {
            ((AlertDialog)dialog).setMessage(infoMsg);
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_INFO;
        }
        else if(id==DIALOG_THANKS)
        {
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_THANKS;
        }
        else if(id==DIALOG_EXIT)
        {
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_EXIT;
        }
        else if(id==DIALOG_EXIT_GAME)
        {
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_EXIT_GAME;
        }
        else if(id==DIALOG_OPTIONS)
        {
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_OPTIONS;
        }
        else if(id==DIALOG_FULLSCREEN)
        {
            Emulator.pause();
            DialogHelper.savedDialog = DIALOG_FULLSCREEN;
        }
        else if(id==DIALOG_ROMs_DIR)
        {
            DialogHelper.savedDialog = DIALOG_ROMs_DIR;
        }
        else if(id==DIALOG_LOAD_FILE_EXPLORER)
        {
            DialogHelper.savedDialog = DIALOG_LOAD_FILE_EXPLORER;
        }
        else if(id==DIALOG_FINISH_CUSTOM_LAYOUT)
        {
            DialogHelper.savedDialog = DIALOG_FINISH_CUSTOM_LAYOUT;
        }
        else if(id==DIALOG_EMU_RESTART)
        {
            Emulator.pause();
        }

    }

    public void removeDialogs() {
        if(savedDialog==DIALOG_FINISH_CUSTOM_LAYOUT)
        {
            mm.removeDialog(DIALOG_FINISH_CUSTOM_LAYOUT);
            DialogHelper.savedDialog = DIALOG_NONE;
        }
    }

}

