package edu.rupp.firstite.cart_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import edu.rupp.firstite.adapter.CartAdapter;
import edu.rupp.firstite.databinding.FragmentCartsBinding;
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
    int userId; // Add this line

    private FragmentCartsBinding binding;
    private CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartsBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String retrievedAccessToken = sharedPreferences.getString("access_token", null);
        userId = sharedPreferences.getInt("user_id", -1);



        if (retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + retrievedAccessToken);

        } else {
            // Handle scenario where access token is not available or empty
//            Toast.makeText(getContext(), "Access token not available or empty", Toast.LENGTH_LONG).show();
            ToastUtil.showCustomToast(requireContext(), "Access token not available or empty", false);
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

        binding.btnPaynow.setOnClickListener(v -> {
            List<Book> currentList = cartAdapter.getCurrentList();

            if (currentList.isEmpty()) {
//                Toast.makeText(getContext(), "You need to buy first", Toast.LENGTH_SHORT).show();
                ToastUtil.showCustomToast(requireContext(), "You need to buy first", false);
            } else {
                ArrayList<Integer> bookIds = new ArrayList<>();
                ArrayList<Double> prices = new ArrayList<>();
                for (Book book : currentList) {
                    Log.d("CartFragment", "Cart Item ID: " + book.getId());
                    Log.d("CartFragment", "Actual Book ID: " + book.getBook().getId());
                    Log.d("CartFragment", "Price: " + book.getBook().getPrice());

                    bookIds.add(book.getBook().getId());
                    prices.add(Double.parseDouble(book.getBook().getPrice()));
                }
                Log.d("CartFragment", "User ID: " + userId);

                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra("user_id", userId);
                intent.putIntegerArrayListExtra("book_ids", bookIds);
                intent.putExtra("prices", prices);
                startActivity(intent);
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
//                    Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                    ToastUtil.showCustomToast(requireContext(), "Failed to load cart items", false);
                    Log.e("CartFragment", "Failed to load cart items: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                ToastUtil.showCustomToast(requireContext(), "Failed to load cart items", false);
                Log.e("CartFragment", "Failed to load cart items", t);            }
        });
    }

    private void navigateToPaymentScreen() {
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        startActivity(intent);
    }
}
