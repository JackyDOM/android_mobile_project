package edu.rupp.firstite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BookGridActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_grid);
    }

    // Method to handle click event on the ImageView
    public void onImageClick(View view){
        // Start the new activity using an Intent
        Intent intent = new Intent(this, BookDetailActivity.class);
        startActivity(intent);
    }
}
