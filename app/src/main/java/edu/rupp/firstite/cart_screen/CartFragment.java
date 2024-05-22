package edu.rupp.firstite.cart_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.adapter.CartAdapter;
import edu.rupp.firstite.databinding.FragmentCartsBinding;
import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.modals.Book;
import edu.rupp.firstite.payment_screen.PaymentActivity;
import edu.rupp.firstite.service.ApiServiceGetCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment {
    String accessToken;

    private FragmentCartsBinding binding;
    private CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCartsBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("access_token", null);

        if(retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + retrievedAccessToken);
        } else {
            // Handle scenario where access token is not available or empty
            showCustomToast("Access token not available or empty", false);
        }

        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken;

        // If access token is available, load banner images
        if (accessToken != null) {
            loadCartImage();
        }

        LinearLayoutManager cartLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycleViewCart.setLayoutManager(cartLayoutManager);
        cartAdapter = new CartAdapter(getContext());
        binding.recycleViewCart.setAdapter(cartAdapter);

        // Add click listener for Pay Now button
        binding.btnPaynow.setOnClickListener(v -> {
            if (cartAdapter.getCurrentList().isEmpty()) {
                Toast.makeText(getContext(), "You need to buy first", Toast.LENGTH_SHORT).show();
            } else {
                navigateToPaymentScreen();
            }
        });


        return binding.getRoot();
    }

    private void loadCartImage(){
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServiceGetCart apiServiceGetCart = httpClient.create(ApiServiceGetCart.class);
        Call<List<Book>> task = apiServiceGetCart.loadCartImage("Bearer " + accessToken);

        task.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    if (books != null) {
                        cartAdapter.submitList(books);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                    Log.e("CartFragment", "Failed to load cart items: " + response.message());
                    showCustomToast("Failed to load cart items", false);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment", "Failed to load cart items", t);
                showCustomToast("Failed to load cart items", false);
            }
        });
    }

    private void navigateToPaymentScreen() {
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        startActivity(intent);
    }

    public void showCustomToast(String message, boolean isSuccess) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(isSuccess ? R.layout.toast_success : R.layout.toast_failure,
                requireActivity().findViewById(isSuccess ? R.id.text : R.id.text));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
