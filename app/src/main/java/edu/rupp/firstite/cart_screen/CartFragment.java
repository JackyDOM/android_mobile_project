package edu.rupp.firstite.cart_screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.BannerAdaper;
import edu.rupp.firstite.adapter.BookCartsAdapter;
import edu.rupp.firstite.databinding.FragmentCartsBinding;
import edu.rupp.firstite.databinding.FragmentHomeBinding;
import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.modals.CartBookModal;
import edu.rupp.firstite.service.ApiServiceCartBook;
import edu.rupp.firstite.service.ApiServiceGetCartBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment {
    String accessToken;

    private FragmentCartsBinding binding;
    private BookCartsAdapter bookCartsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartsBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
            GetBookCart();
        }

        // Initialize BannerAdapter and set it to RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleView.setLayoutManager(linearLayoutManager);

        bookCartsAdapter = new BookCartsAdapter();
        binding.recycleView.setAdapter(bookCartsAdapter);

        return binding.getRoot();
    }

    private void GetBookCart(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServiceGetCartBook apiServiceGetCartBook = httpClient.create(ApiServiceGetCartBook.class);

        Call<List<CartBookModal>> task = apiServiceGetCartBook.GetBookCart("Bearer " + accessToken);
        task.enqueue(new Callback<List<CartBookModal>>() {
            @Override
            public void onResponse(Call<List<CartBookModal>> call, Response<List<CartBookModal>> response) {
                if(response.isSuccessful()){
                    // Update adapter data with response body
                    bookCartsAdapter.submitList(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed reload banner", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartBookModal>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
