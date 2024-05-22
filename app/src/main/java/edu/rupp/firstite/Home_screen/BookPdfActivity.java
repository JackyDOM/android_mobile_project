package edu.rupp.firstite.Home_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

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
        showCustomToast("PDF URL: " + pdfUrl, true);

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
                showCustomToast("PDF loaded successfully", true);
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
    public void showCustomToast(String message, boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(isSuccess ? R.layout.toast_success : R.layout.toast_failure,
                findViewById(isSuccess ? R.id.text : R.id.text));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
