package com.abms.af.projeversion02;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdfViewer extends AppCompatActivity {

    WebView pdfview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Bundle bundle =getIntent().getExtras();
        String url =bundle.getString("url");
        Toast.makeText(getApplicationContext(),"url: "+url,Toast.LENGTH_LONG).show();

        pdfview = findViewById(R.id.pdfviewer);

        pdfview.getSettings().setJavaScriptEnabled(true);
        pdfview.getSettings().setBuiltInZoomControls(true);
        pdfview.getSettings().setDisplayZoomControls(false);
        pdfview.setWebViewClient(new WebViewClient());
        pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);

    }
}
