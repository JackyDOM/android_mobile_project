package edu.rupp.firstite.Home_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.utils.ToastUtil;
import edu.rupp.firstite.adapter.BookGridAdapter;
import edu.rupp.firstite.modals.BookGridDisplay;
import edu.rupp.firstite.service.ApiServiceBookGrid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookGridActivity extends AppCompatActivity {
    private String accessToken;
    private RecyclerView recyclerView;

    private ImageView iconBackHomePage;

    private String categoryName;

    private BookGridAdapter bookGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_grid);

        iconBackHomePage = findViewById(R.id.iconBackHomePage);

        iconBackHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHomePage();
            }
        });

        // Fetch access token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);


        // Fetch category name from the intent
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        if (accessToken != null && !accessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + accessToken);
        } else {
            // Handle scenario where access token is not available or empty
//            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show();
            ToastUtil.showCustomToast(this, "Access token not available or empty", false);
        }

        // Initialize the adapter with ListAdapter
        bookGridAdapter = new BookGridAdapter();

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycleViewBookGrid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);  // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(bookGridAdapter);

        // If access token is available, load banner images
        if (accessToken != null) {
            FilterCategoryBook();
        }

    }

    private void FilterCategoryBook(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceBookGrid apiServiceBookGrid = httpClient.create(ApiServiceBookGrid.class);
        Call<List<BookGridDisplay>> task = apiServiceBookGrid.FilterByCategory("Bearer " + accessToken, categoryName);
        task.enqueue(new Callback<List<BookGridDisplay>>() {
            @Override
            public void onResponse(Call<List<BookGridDisplay>> call, Response<List<BookGridDisplay>> response) {
                if (response.isSuccessful()) {
                    List<BookGridDisplay> bookGridDisplayList = response.body();

                    // Filter the list based on category name
                    List<BookGridDisplay> filteredList = new ArrayList<>();
                    for (BookGridDisplay book : bookGridDisplayList) {
                        if (book.getCategory() != null && book.getCategory().getName().equals(categoryName)) {
                            filteredList.add(book);
                        }
                    }

                    // Update adapter data with the filtered list of books
                    bookGridAdapter.submitList(filteredList);
                } else {
                    Log.d("Fail Author", "Failed to reload banner");
                }
            }

            @Override
            public void onFailure(Call<List<BookGridDisplay>> call, Throwable t) {
                Log.d("Fail Author", "Failed to reload banner", t);
            }
        });
    }

    private void BackToHomePage(){
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
