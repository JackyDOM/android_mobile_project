package edu.rupp.firstite;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private String accessToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve the access token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);

// Log the retrieved access token
        Log.d("AccessToken", "Retrieved Access Token: " + accessToken);

// Store the access token for later use
        storeAccessTokenForLaterUse(accessToken);


        // Find the button in the layout
        Button btnGeneralBook = rootView.findViewById(R.id.btnGenderalBook);
        Button btnComdyBook = rootView.findViewById(R.id.btnComdyBook);
        Button btnComicBook = rootView.findViewById(R.id.btnComicBook);

        // Set OnClickListener to the button Genenral-Book
        btnGeneralBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity
                Intent intent = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener to the button Comdy-Book
        btnComdyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComdyBook = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intentComdyBook);
            }
        });

        // Set OnClickListener to the button Comic-Book
        btnComicBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComicBook = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intentComicBook);
            }
        });

        return rootView;
    }

    private void storeAccessTokenForLaterUse(String accessToken) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token_home", accessToken);
        editor.apply();

        // Log the stored access token
        Log.d("AccessToken", "Stored Access Token: " + accessToken);
    }

}
