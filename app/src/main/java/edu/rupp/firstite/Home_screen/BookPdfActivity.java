package edu.rupp.firstite.Home_screen;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import edu.rupp.firstite.R;

public class BookPdfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdf);

        // Get the PDF URL from the intent
        String pdfUrl = getIntent().getStringExtra("book_pdf_url");

        // Log the value of pdfUrl
        Log.d("BookPdfActivity", "PDF URL: " + pdfUrl);

        // Load PDF in WebView
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Log a message when the PDF finishes loading
                Log.d("WebView", "PDF loaded successfully");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // Log any errors that occur during PDF loading
                Log.e("WebView", "Error loading PDF: " + description);
            }
        });

        // Load the PDF URL
        webView.loadUrl(pdfUrl);
    }
}


