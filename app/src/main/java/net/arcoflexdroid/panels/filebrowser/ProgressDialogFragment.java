package net.arcoflexdroid.panels.filebrowser;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ProgressDialogFragment  extends DialogFragment {

    private Context mContext;
    private String mProgressText;
    private ProgressDialogFragment.ProgressDialogCancellationSignal mProgressDialogCancelHandler;

    public ProgressDialogFragment(Context context, String progressText, ProgressDialogFragment.ProgressDialogCancellationSignal handler)
    {
        mContext = context;
        mProgressText = progressText;
        mProgressDialogCancelHandler = handler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(mProgressDialogCancelHandler == null ? false : false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        if (mProgressText != null)
        {
            progressDialog.setMessage(mProgressText);
        }
        return progressDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        if (mProgressDialogCancelHandler != null)
        {
            mProgressDialogCancelHandler.onCancel();
        }
        super.onCancel(dialog);
    }

    /**
     * Used to set progress in progress dialog at runtime
     * @param totalProgress : total progress done till now on the given task
     * @param totalValue    : total value of the given task
     * @param units         : measurement unit of progress of given task
     */
    public void setProgress(String totalProgress, String totalValue, String units)
    {
        Dialog dialog = getDialog();
        if (dialog instanceof ProgressDialog && mProgressText != null)
        {
            ((ProgressDialog) dialog).setMessage(mProgressText + " " + totalProgress + units + " / " + totalValue + units);
        }
    }

    public interface ProgressDialogCancellationSignal
    {
        void onCancel();
    }
}


