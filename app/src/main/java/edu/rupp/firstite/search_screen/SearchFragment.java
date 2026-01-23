package edu.rupp.firstite.search_screen;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.utils.ToastUtil;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
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
            // Toast.makeText(getContext(), "Access token not available or empty",
            // Toast.LENGTH_LONG).show();
            ToastUtil.showCustomToast(requireContext(), "Access token not available or empty", false);
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
                    // Toast.makeText(getContext(), "Please enter a title to search",
                    // Toast.LENGTH_SHORT).show();
                    ToastUtil.showCustomToast(requireContext(), "Please enter a title to search", false);
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

    private void searchBook(String query) {
        if (accessToken == null) {
            ToastUtil.showCustomToast(requireContext(), "Access token not available or empty", false);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://elibra.skinme.store/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiServiceSearching.class);

        // Check if query is numeric (ID) or text (title)
        if (isNumeric(query)) {
            // Search by ID
            int bookId = Integer.parseInt(query);
            apiService.loadSearchingById("Bearer " + accessToken, bookId).enqueue(new Callback<Searching>() {
                @Override
                public void onResponse(Call<Searching> call, Response<Searching> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Searching> singleResult = new ArrayList<>();
                        singleResult.add(response.body());
                        searchingAdapter.updateData(singleResult);
                    } else {
                        ToastUtil.showCustomToast(requireContext(), "Book ID not found", false);
                    }
                }

                @Override
                public void onFailure(Call<Searching> call, Throwable t) {
                    ToastUtil.showCustomToast(requireContext(), "Failed to fetch data", false);
                    Log.e("SearchFragment", "Error searching by ID: " + t.getMessage());
                }
            });
        } else {
            // Search by title
            apiService.loadSearchingImage("Bearer " + accessToken, query).enqueue(new Callback<List<Searching>>() {
                @Override
                public void onResponse(Call<List<Searching>> call, Response<List<Searching>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        searchingAdapter.updateData(response.body());
                    } else {
                        ToastUtil.showCustomToast(requireContext(), "Title not found", false);
                    }
                }

                @Override
                public void onFailure(Call<List<Searching>> call, Throwable t) {
                    ToastUtil.showCustomToast(requireContext(), "Failed to fetch data", false);
                    Log.e("SearchFragment", "Error searching by title: " + t.getMessage());
                }
            });
        }
    }

    // Helper method to check if string is numeric
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
