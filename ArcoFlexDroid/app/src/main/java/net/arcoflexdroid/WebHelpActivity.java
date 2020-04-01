package net.arcoflexdroid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebHelpActivity extends Activity {

    WebView lWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String path = null;//"/storage/self/ROMs/ArcoFlexDroid/";
        setContentView(R.layout.webhelp);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("INSTALLATION_PATH");
        }
        lWebView = (WebView)this.findViewById(R.id.webView);
        WebSettings webSettings = lWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBuiltInZoomControls(true);
        //lWebView.setWebViewClient(new WebViewClient());
        lWebView.setBackgroundColor(Color.DKGRAY);
        if(!path.endsWith("/"))
            path+="/";

        lWebView.loadUrl("file:///" +  path +"help/index.htm");
        lWebView.requestFocus();
    }

    public void onBackPressed() {

        if (this.lWebView.canGoBack())
            this.lWebView.goBack();
        else
            super.onBackPressed();

        lWebView.requestFocus();
    }

}

