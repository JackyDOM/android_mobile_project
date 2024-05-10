package edu.rupp.firstite.Home_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.R;

public class BookDetailActivity extends AppCompatActivity {
    private static final int MAX_LINES_COLLAPED = 5;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String bookImageUrl = getIntent().getStringExtra("book_image_url");
        String bookTitle = getIntent().getStringExtra("book_title");
        String bookPrice = getIntent().getStringExtra("book_price");
        String bookPublisher = getIntent().getStringExtra("book_publisher");
        String bookCategoryName = getIntent().getStringExtra("book_Category_Name");
        String authorName = getIntent().getStringExtra("author_name");
        String authorDecs = getIntent().getStringExtra("author_Decs");
        int bookId = getIntent().getIntExtra("book_id",0); // Retrieve book_id

        // Log the book_id
        Log.d("SignIn", "Book ID: " + bookId);

        // Check if the price equals "free"
        if ("free".equalsIgnoreCase(bookPrice)) {
            // Show "Add to Favorites" button
            findViewById(R.id.btnAddToFavorites).setVisibility(View.VISIBLE);
        } else {
            // Show "Add to Cart" button
            findViewById(R.id.btnAddToCart).setVisibility(View.VISIBLE);
        }

        ImageView imageView = findViewById(R.id.BookImageView);
        TextView textView = findViewById(R.id.txtTitleDetail);
        TextView textViewPrice = findViewById(R.id.txtPriceCategoryGeneral);
        TextView textViewPublisher = findViewById(R.id.txtpublisherCategoryGeneral);
        //TextView textViewBookCategoryName = findViewById(R.id.BookCategoryName);
        Picasso.get().load(bookImageUrl).into(imageView);

        TextView showMoreTextView = findViewById(R.id.showMoreTextView);

        // Create a SpannableString for "Name: " with a different color
        SpannableString spannableTitleLabel = new SpannableString("Title: ");
        spannableTitleLabel.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableTitleLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableTitleLabel);
        textView.append(bookTitle);

        // Create a SpannableString for "Name: " with a different color
        SpannableString spannablePriceLabel = new SpannableString("Price: ");
        spannablePriceLabel.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannablePriceLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewPrice.setText(spannablePriceLabel);
        textViewPrice.append(bookPrice);

        // Create a SpannableString for "Name: " with a different color
        SpannableString spannablePublisherLabel = new SpannableString("Publisher: ");
        spannablePublisherLabel.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannablePublisherLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewPublisher.setText(spannablePublisherLabel);
        textViewPublisher.append(bookPublisher);

        TextView textViewBookCategoryName = findViewById(R.id.BookCategoryName);
        textViewBookCategoryName.setText("Category Name: " + bookCategoryName);

        TextView textViewAuthorName = findViewById(R.id.BookAuthorName);
        textViewAuthorName.setText("Author Name: " + authorName);

        TextView textViewAuthorDecs = findViewById(R.id.BookAuthorDecs);
        textViewAuthorDecs.setText("Description: " + authorDecs);
        textViewAuthorDecs.setMaxLines(MAX_LINES_COLLAPED);

        // Set click listener for "Show more" button
        showMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Collapse the description
                    textViewAuthorDecs.setMaxLines(MAX_LINES_COLLAPED);
                    showMoreTextView.setText("Show more");
                } else {
                    // Expand the description
                    textViewAuthorDecs.setMaxLines(Integer.MAX_VALUE); // Show all lines
                    showMoreTextView.setText("Show less");
                }
                isExpanded = !isExpanded;
            }
        });

    }
}