package edu.rupp.firstite.Home_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.modals.AddCartBook;
import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.service.ApiServiceBanner;
import edu.rupp.firstite.service.ApiServiceCartBook;
import edu.rupp.firstite.signIn_Screen.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookDetailActivity extends AppCompatActivity {
    private static final int MAX_LINES_COLLAPED = 5;
    private boolean isExpanded = false;
    // Add a HashSet to keep track of added bookIds
    private HashSet<Integer> addedBookIds = new HashSet<>();
    private boolean isItemAddedToCart = false; // Initialize flag

    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Find the ImageView by id
        ImageView backHomeImageView = findViewById(R.id.backHomefragment);

        // Set an OnClickListener to the ImageView
        backHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the HomeFragment
                navigateToHomeFragment();
            }
        });

        // Fetch access token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("access_token", null);

        if (retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + retrievedAccessToken);
            // Store the retrieved access token for later use
            accessToken = retrievedAccessToken;
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show();
        }


        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken;

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


        // Find the Add to Button id
        Button addToCartButton = findViewById(R.id.btnAddToCart);

        //set onClickListener for the button addToCart
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCartBook();
            }
        });

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

    private void AddCartBook() {
        // Check if the item is already added to the cart using bookId
        int bookId = getIntent().getIntExtra("book_id", 0);
        if (addedBookIds.contains(bookId)) {
            Toast.makeText(this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
            return; // Exit the method to avoid adding the item again
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Fetch book details from intent
        //int bookId = getIntent().getIntExtra("book_id", 0);
        int userId = getIntent().getIntExtra("user_id",0); // Implement this method to get the user ID
        int quantity = 1; // You can set the quantity as per your requirements

        // Create an instance of AddCartBook and set user_id, book_id, and quantity
        AddCartBook addCartBook = new AddCartBook();
        addCartBook.setUser_id(userId);
        addCartBook.setBook_id(bookId);
        addCartBook.setQuantity(quantity);

        // Create ApiServiceCartBook instance
        ApiServiceCartBook apiServiceCartBook = retrofit.create(ApiServiceCartBook.class);

        // Make the POST request with the access token in the header and AddCartBook instance in the request body
        apiServiceCartBook.AddCartBook("Bearer " + accessToken, addCartBook).enqueue(new Callback<AddCartBook>() {
            @Override
            public void onResponse(Call<AddCartBook> call, Response<AddCartBook> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Toast.makeText(BookDetailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    isItemAddedToCart = true;

                    // Add the bookId to the HashSet
                    addedBookIds.add(bookId);
                } else {
                    // Handle unsuccessful response
                    Log.e("AddToCart", "Failed to add book to cart: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AddCartBook> call, Throwable t) {
                // Handle failure
                Log.e("AddToCart", "Failed to add book to cart", t);
            }
        });
    }

    // Method to navigate to the HomeFragment
    private void navigateToHomeFragment() {
        // Create a new instance of the HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Replace the current fragment with the HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, homeFragment)
                .addToBackStack(null)
                .commit();

        // Finish the BookDetailActivity
        finish();
    }

}