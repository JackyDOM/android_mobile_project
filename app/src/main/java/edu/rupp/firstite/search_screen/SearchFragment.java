package edu.rupp.firstite.search_screen;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.SearchingAdapter;
import edu.rupp.firstite.databinding.FragmentSearchBinding;
import edu.rupp.firstite.modals.Searching;
import edu.rupp.firstite.service.ApiServiceSearching;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.appcompat.widget.SearchView; // Ensure this is the correct import

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchingAdapter searchingAdapter;
    private ApiServiceSearching apiService;
    private String accessToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

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

        setupRecyclerView();
        setupSearchView();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        // Create an empty list initially
        List<Searching> searchResults = new ArrayList<>();
        searchingAdapter = new SearchingAdapter(searchResults, getContext()); // Pass the list and context
        binding.recycleViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewSearch.setAdapter(searchingAdapter);
    }


    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchBook(query);
                } else {
                    Toast.makeText(getContext(), "Please enter a title to search", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // Clear the search results when the query is empty
                    searchingAdapter.updateData(new ArrayList<>());
                }
                return false;
            }
        });
    }

    private void searchBook(String title) {
        if (accessToken != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiServiceSearching.class);
            apiService.loadSearchingImage("Bearer " + accessToken, title).enqueue(new Callback<List<Searching>>() {
                @Override
                public void onResponse(Call<List<Searching>> call, Response<List<Searching>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        searchingAdapter.updateData(response.body());
                    } else {
                        Toast.makeText(getContext(), "Title not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Searching>> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Access token not available or empty", Toast.LENGTH_SHORT).show();
        }
    }
}
