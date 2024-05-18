package edu.rupp.firstite.author_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.BookAuthorAdapter;
import edu.rupp.firstite.adapter.CategoryAdapter;
import edu.rupp.firstite.modals.AuthorBookDisplay;
import edu.rupp.firstite.modals.CategoryBanner1;
import edu.rupp.firstite.service.ApiServiceAuthorBook;
import edu.rupp.firstite.service.ApiServiceCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthorInformationActivity extends AppCompatActivity {
    private static final int MAX_LINES_COLLAPSED = 2;
    private boolean isExpanded = false;
    private BookAuthorAdapter bookAuthorAdapter;
    private RecyclerView recyclerView;
    private String accessToken;
    private String authorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_information);

        // Fetch access token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);

        if (accessToken != null && !accessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + accessToken);
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show();
        }

        // Initialize the adapter with ListAdapter
        bookAuthorAdapter = new BookAuthorAdapter();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.AuthorBookDisplay);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(bookAuthorAdapter);

        // If access token is available, load banner images
        if (accessToken != null) {
            loadCategoryImage();
        }

        // Find the ImageView by id
        ImageView backToAuthorScreen = findViewById(R.id.BackToAuthorScreen);
        backToAuthorScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the HomeFragment
                navigateToAuthorFragment();
            }
        });

        // Get intent extras
        String authorImageUrl = getIntent().getStringExtra("author_image_url");
        String authorgender = getIntent().getStringExtra("author_gender");
        authorName = getIntent().getStringExtra("author_name");
        String authorDecs = getIntent().getStringExtra("author_decs");

        // Set author image and details
        ImageView imageView = findViewById(R.id.authorImageView);
        Picasso.get().load(authorImageUrl).into(imageView);

        TextView textViewGender = findViewById(R.id.gender);
        TextView textViewName = findViewById(R.id.name);
        TextView textViewDecs = findViewById(R.id.idAuthor);
        TextView showMoreTextView = findViewById(R.id.showMoreTextView);

        // Create and set styled text for author details
        setStyledText(textViewName, "Name: ", authorName);
        setStyledText(textViewGender, "Gender: ", authorgender);
        setStyledText(textViewDecs, "Description: ", authorDecs);
        textViewDecs.setMaxLines(MAX_LINES_COLLAPSED);

        // Set click listener for "Show more" button
        showMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Collapse the description
                    textViewDecs.setMaxLines(MAX_LINES_COLLAPSED);
                    showMoreTextView.setText("Show more");
                } else {
                    // Expand the description
                    textViewDecs.setMaxLines(Integer.MAX_VALUE); // Show all lines
                    showMoreTextView.setText("Show less");
                }
                isExpanded = !isExpanded;
            }
        });
    }

    // Helper method to set styled text
    private void setStyledText(TextView textView, String label, String value) {
        SpannableString spannableLabel = new SpannableString(label);
        spannableLabel.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableLabel);
        textView.append(value);
    }

    // Method to navigate to the HomeFragment
    private void navigateToAuthorFragment() {
        // Create a new instance of the AuthorFragment
        AuthorFragment authorFragment = new AuthorFragment();

        // Replace the current fragment with the AuthorFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, authorFragment)
                .addToBackStack(null)
                .commit();

        finish();
    }

    private void loadCategoryImage() {
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceAuthorBook apiServiceCategory = httpClient.create(ApiServiceAuthorBook.class);
        Call<List<AuthorBookDisplay>> task = apiServiceCategory.FilterAuthorBook("Bearer " + accessToken);
        task.enqueue(new Callback<List<AuthorBookDisplay>>() {
            @Override
            public void onResponse(Call<List<AuthorBookDisplay>> call, Response<List<AuthorBookDisplay>> response) {
                if (response.isSuccessful()) {
                    List<AuthorBookDisplay> authorBookDisplayList = response.body();

                    // Log the response for debugging
                    Log.d("AuthorBooksResponse", "Books: " + authorBookDisplayList);

                    // Initialize the adapter if it's null
                    if (bookAuthorAdapter == null) {
                        bookAuthorAdapter = new BookAuthorAdapter();
                        recyclerView.setAdapter(bookAuthorAdapter);
                    }

                    // Filter books based on the dynamic author's name
                    List<AuthorBookDisplay> filteredList = new ArrayList<>();
                    for (AuthorBookDisplay book : authorBookDisplayList) {
                        if (book.getAuthor().getAuthor_name().trim().equalsIgnoreCase(authorName.trim())) {
                            filteredList.add(book);
                        }
                    }

                    // Log the filtered list for debugging
                    Log.d("FilteredBooks", "Filtered Books for " + authorName + ": " + filteredList);

                    // Update adapter data with the filtered list of books
                    bookAuthorAdapter.submitList(filteredList);
                } else {
                    Log.d("Fail Author", "Failed to reload banner");
                }
            }

            @Override
            public void onFailure(Call<List<AuthorBookDisplay>> call, Throwable t) {
                Log.d("Fail Author", "Failed to reload banner", t);
            }
        });
    }

}
