package edu.rupp.firstite.Home_screen;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.BannerAdaper;
import edu.rupp.firstite.adapter.Category2Adapter;
import edu.rupp.firstite.adapter.CategoryAdapter;
import edu.rupp.firstite.databinding.FragmentHomeBinding;
import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.modals.CategoryBanner1;
import edu.rupp.firstite.modals.CategoryBanner2;
import edu.rupp.firstite.service.ApiServiceBanner;
import edu.rupp.firstite.service.ApiServiceCategory;
import edu.rupp.firstite.service.ApiServiceCategory2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    String accessToken;
    private FragmentHomeBinding binding;
    private BannerAdaper bannerAdaper;
    private CategoryAdapter categoryAdapter;

    private Category2Adapter category2Adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View dotIndicator = binding.dotLayout;

        ImageView generalBookIcon = binding.getRoot().findViewById(R.id.GeneralBookIcon);
        ImageView comicBookIcon = binding.getRoot().findViewById(R.id.ComicBookIcon);

        generalBookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BookGrid activity and pass accessToken as extra data
                Intent intent = new Intent(getContext(), BookGridActivity.class);
                intent.putExtra("accessToken", accessToken);
                startActivity(intent);
            }
        });

        comicBookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BookGrid activity and pass accessToken as extra data
                Intent intent1 = new Intent(getContext(), BookGridActivity.class);
                intent1.putExtra("accessToken", accessToken);
                startActivity(intent1);
            }
        });

        binding.recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                updateDotIndicator(recyclerView);
            }
        });

        // Fetch access token from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("access_token", null);

        if (retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + retrievedAccessToken);
            // Store the retrieved access token for later use
            accessToken = retrievedAccessToken;
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(getContext(), "Access token not available or empty", Toast.LENGTH_LONG).show();
        }


        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken;

        // If access token is available, load banner images
        if (accessToken != null) {
            loadBannerImage();
            loadCategoryImage();
            loadCategory2Image();
        }

        // Initialize BannerAdapter and set it to RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleView.setLayoutManager(linearLayoutManager);

        bannerAdaper = new BannerAdaper();
        binding.recycleView.setAdapter(bannerAdaper);

        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleViewCategory.setLayoutManager(categoryLayoutManager);
        categoryAdapter = new CategoryAdapter();
        binding.recycleViewCategory.setAdapter(categoryAdapter);

        LinearLayoutManager category2LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleViewCategory2.setLayoutManager(category2LayoutManager); // Change this line
        category2Adapter = new Category2Adapter(); // Change this line
        binding.recycleViewCategory2.setAdapter(category2Adapter); // Change this line

        return binding.getRoot();
    }

    private void updateDotIndicator(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        LinearLayout dotLayout = binding.dotLayout;
        dotLayout.removeAllViews(); // Clear existing dots

        for (int i = 0; i < totalItemCount; i++) {
            View dot = new View(getContext());
            int dotSize = getResources().getDimensionPixelSize(R.dimen._5dp);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
            params.setMargins(4, 0, 4, 0); // Adjust margin as needed

            // Extend the width of the dot only for the currently visible item
            if (i == firstVisibleItemPosition) {
                int extraWidth;
                if (firstVisibleItemPosition == lastVisibleItemPosition) {
                    // If there's only one visible item, set extra width to 0
                    extraWidth = 0;
                } else {
                    // Calculate extra width based on the number of visible items
                    extraWidth = dotSize * (lastVisibleItemPosition - firstVisibleItemPosition);
                }
                params.width = dotSize + extraWidth;
            }

            dot.setLayoutParams(params);
            dot.setBackgroundResource(i == firstVisibleItemPosition ? R.drawable.inactive_dot_background : R.drawable.dot_background);
            dotLayout.addView(dot);
        }
    }

    private void loadBannerImage(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceBanner apiServiceBanner = httpClient.create(ApiServiceBanner.class);

        Call<List<Banner>> task = apiServiceBanner.loadBannerImage("Bearer " + accessToken);
        task.enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                if(response.isSuccessful()){
                    // Update adapter data with response body
                    bannerAdaper.submitList(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed reload banner", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCategoryImage(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceCategory apiServiceCategory = httpClient.create(ApiServiceCategory.class);
        Call<List<CategoryBanner1>> task = apiServiceCategory.loadCategoryImage("Bearer " + accessToken);
        task.enqueue(new Callback<List<CategoryBanner1>>() {

            @Override
            public void onResponse(Call<List<CategoryBanner1>> call, Response<List<CategoryBanner1>> response) {
                if(response.isSuccessful()){
                    // Filter the response based on Category ID
                    List<CategoryBanner1> categoryBanner1List = response.body();
                    List<CategoryBanner1> filteredList = new ArrayList<>();
                    // Iterate through the category list
                    for(CategoryBanner1 category : categoryBanner1List){
                        // Check if category ID equals 1
                        if(category.getCategory().getId() == 1){
                            // Add to filtered list if category ID is 1
                            filteredList.add(category);
                        }
                    }

                    // Update adapter data with filtered list
                    categoryAdapter.submitList(filteredList);
                } else {
                    Toast.makeText(getContext(), "Failed reload banner", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryBanner1>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadCategory2Image(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceCategory2 apiServiceCategory2 = httpClient.create(ApiServiceCategory2.class);
        Call<List<CategoryBanner2>> task = apiServiceCategory2.loadCategory2Image("Bearer " + accessToken);
        task.enqueue(new Callback<List<CategoryBanner2>>() {

            @Override
            public void onResponse(Call<List<CategoryBanner2>> call, Response<List<CategoryBanner2>> response) {
                if(response.isSuccessful()){
                    // Filter the response based on Category ID
                    List<CategoryBanner2> categoryBanner2List = response.body();
                    List<CategoryBanner2> filteredList = new ArrayList<>();
                    // Iterate through the category list
                    for(CategoryBanner2 category : categoryBanner2List){
                        // Check if category ID equals 1
                        if(category.getCategory().getId() == 2){
                            // Add to filtered list if category ID is 1
                            filteredList.add(category);
                        }
                    }

                    // Update adapter data with filtered list
                    category2Adapter.submitList(filteredList);
                } else {
                    Toast.makeText(getContext(), "Failed reload banner", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryBanner2>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
