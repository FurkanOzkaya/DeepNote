package com.abms.af.projeversion02;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class pdfViewer extends AppCompatActivity {

    WebView pdfview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        //Toast.makeText(getApplicationContext(), "url: " + url, Toast.LENGTH_LONG).show();

        pdfview = findViewById(R.id.pdfviewer);

        pdfview.getSettings().setJavaScriptEnabled(true);
        pdfview.getSettings().setBuiltInZoomControls(true);
        pdfview.getSettings().setDisplayZoomControls(false);
        pdfview.setWebViewClient(new WebViewClient());
        pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
    }
}
