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

import edu.rupp.firstite.databinding.FragmentCartsBinding;

public class CartFragment extends Fragment {
    String accessToken;

    private FragmentCartsBinding binding;

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

        }


        return binding.getRoot();
    }

}
