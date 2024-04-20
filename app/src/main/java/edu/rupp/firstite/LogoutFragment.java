package edu.rupp.firstite;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import edu.rupp.firstite.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class LogoutFragment extends Fragment {

    private static final String BASE_URL = "http://10.0.2.2:5000/";

    interface YourApiInterface {
        @POST("authorization/logout")
        Call<JsonObject> logout(@Header("Authorization") String authorization);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        //button signOut
        Button btnLogout = view.findViewById(R.id.btnLogout);
        TextView textViewUsername = view.findViewById(R.id.textViewUsername);
        String username = getActivity().getIntent().getStringExtra("username");

        if (username != null) {
            textViewUsername.setText("Welcome, " + username);
        } else {
            textViewUsername.setText("Welcome");
        }

        // set click listener for the logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call method to handle logout
                logout();
            }
        });

        return view;
    }

    // Method to handle logout
    private void logout() {
        // Retrieve access_token from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accessToken = prefs.getString("access_token", "");

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Initialize API interface
        YourApiInterface api = retrofit.create(YourApiInterface.class);

        // Call the logout endpoint
        Call<JsonObject> call = api.logout("Bearer " + accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Handle successful logout response
                    // Navigate to MainActivity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                    // Toast success
                    Toast.makeText(getContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error
                    Toast.makeText(getContext(), "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure
                Toast.makeText(getContext(), "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
