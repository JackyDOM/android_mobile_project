package edu.rupp.firstite.author_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.AuthorAdapter;
import edu.rupp.firstite.databinding.FragmentAuthorBinding;
import edu.rupp.firstite.modals.Author;
import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.service.ApiServiceAuthor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthorFragment extends Fragment {
    String accessToken;
    private FragmentAuthorBinding binding;
    private AuthorAdapter authorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAuthorBinding.inflate(inflater,container,false);

        //Fetch accessToken from sharedPreference
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("access_token", null);

        if (retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token in Author: " + retrievedAccessToken);
            // Store the retrieved access token for later use
            accessToken = retrievedAccessToken;
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(getContext(), "Access token not available or empty", Toast.LENGTH_LONG).show();
        }


        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken;

        if(accessToken != null){
            loadAuthorImage();
        }

        LinearLayoutManager authorLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycleViewAuthor.setLayoutManager(authorLayoutManager);
        authorAdapter = new AuthorAdapter();
        binding.recycleViewAuthor.setAdapter(authorAdapter);

        return binding.getRoot();
    }

    private void loadAuthorImage(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceAuthor apiServiceAuthor = httpClient.create(ApiServiceAuthor.class);

        Call<List<Author>> task = apiServiceAuthor.loadAuthorImage("Bearer " + accessToken);

        task.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if(response.isSuccessful()){
                    // Update adapter data with response body
                    authorAdapter.submitList(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed reload Author", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                // Log the error message
                Log.e("AuthorFragment", "Failed to load authors: " + t.getMessage(), t);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}