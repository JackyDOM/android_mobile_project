package edu.rupp.firstite.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.utils.ToastUtil;
import edu.rupp.firstite.databinding.ViewHolderCartBinding;
import edu.rupp.firstite.modals.Book;
import edu.rupp.firstite.service.ApiServiceDeleteCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends ListAdapter<Book, CartAdapter.CartViewHolder> {
    private String accessToken;
    private TextView emptyCartTextView;
    private Context context;

    private ProgressDialog progressDialog;
    private boolean isDeleting = false; // Flag to track if deletion is in progress


    public CartAdapter(Context context){
        super(new DiffUtil.ItemCallback<Book>() {
            @Override
            public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });

        this.context = context;
    }

    public CartAdapter(Context context, AsyncDifferConfig<Book> config){
        super(config);
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderCartBinding binding = ViewHolderCartBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Book book = getItem(position);
        holder.bingCart(book);
        updateTotalPrice(); // Update total price on each bind
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCartBinding binding;

        public CartViewHolder(ViewHolderCartBinding binding){
            super(binding.getRoot());
            this.binding = binding;

            // Attach click listeners to buttons
            binding.btnIncrease.setOnClickListener(v -> increaseQuantity());
            binding.btnDecrease.setOnClickListener(v -> decreaseQuantity());
            // Set onClickListener for delete ImageView
            binding.delete.setOnClickListener(v -> deleteItem(getAdapterPosition()));
        }

        public void bingCart(Book book){
            Picasso.get().load(book.getBook().getBook_image()).into(binding.CartBookImage);
            binding.txtQuantity.setText(String.valueOf(book.getQuantity()));
            binding.txtTitleCart.setText(book.getBook().getTitle());
            String priceLabelText = "Price: " + book.getBook().getPrice() + " $";
            binding.txtPriceCart.setText(priceLabelText);
            updateTotalPrice();
        }

        private void increaseQuantity() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = getItem(position);
                book.setQuantity(book.getQuantity() + 1);
                notifyItemChanged(position);
                updateTotalPrice();
            }
        }

        private void decreaseQuantity() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = getItem(position);
                if (book.getQuantity() > 1) {
                    book.setQuantity(book.getQuantity() - 1);
                    notifyItemChanged(position);
                    updateTotalPrice();
                }
            }
        }
    }

    private void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Book book = getItem(position);
            isDeleting = true; // Set deletion flag to true
            int bookId = book.getId(); // Assuming you have a method to get the book ID

            // Show loading indicator
            showProgressDialog();

            // Delete the item after a delay of 2 seconds
            deleteItemWithDelay(bookId);
        }
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        List<Book> cartItems = getCurrentList();
        for (Book book : cartItems) {
            double itemPrice = book.getQuantity() * Double.parseDouble(book.getBook().getPrice());
            Log.d("CartAdapter", "Item price: " + itemPrice);
            totalPrice += itemPrice;
        }
        return totalPrice;
    }




    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        TextView totalTextView = ((Activity) context).findViewById(R.id.txtTotal);
        if (totalTextView != null) {
            totalTextView.setText(String.format("Total = $%.2f", totalPrice));
        }
    }



    private void deleteItemWithDelay(int bookId) {
        // Simulate a delay of 2 seconds before making the deletion request
        new android.os.Handler().postDelayed(
                () -> {
                    // Call the method to delete the item from the server
                    deleteCartItemFromServer(bookId);
                }, 2000); // 2000 milliseconds (2 seconds) delay
    }

    // Method to show loading indicator
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting item...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void deleteCartItemFromServer(int itemId) {
        // Retrieve access token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        // Check if access token is available
        if (accessToken != null && !accessToken.isEmpty()) {
            // Create Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/") // Base URL of your server
                    .addConverterFactory(GsonConverterFactory.create()) // Gson converter factory for JSON parsing
                    .build();

            // Create ApiService interface
            ApiServiceDeleteCart apiService = retrofit.create(ApiServiceDeleteCart.class);

            // Create a Call object for the DELETE request
            Call<Void> call = apiService.deleteCartItem("Bearer " + accessToken, itemId);

            // Enqueue the DELETE request asynchronously
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // Dismiss loading indicator
                    dismissProgressDialog();

                    if (response.isSuccessful()) {
                        // Item successfully deleted from the server
//                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        ToastUtil.showCustomToast(context, "Item deleted successfully", true);

                        // Remove the item from the RecyclerView
                        removeItemAndUpdateTotal(itemId);
                    } else {
                        // Error handling for unsuccessful response
                        Toast.makeText(context, "Failed to delete item from server", Toast.LENGTH_SHORT).show();
                        Log.e("CartAdapter", "Failed to delete item from server: " + response.message());

                        // Reset deletion flag
                        isDeleting = false;
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Dismiss loading indicator
                    dismissProgressDialog();

                    // Error handling for network failure
                    Toast.makeText(context, "Network error. Failed to delete item from server", Toast.LENGTH_SHORT).show();
                    Log.e("CartAdapter", "Failed to delete item from server", t);

                    // Reset deletion flag
                    isDeleting = false;
                }
            });
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(context, "Access token not available or empty", Toast.LENGTH_LONG).show();
        }
    }

    private void removeItemAndUpdateTotal(int itemId) {
        List<Book> currentList = new ArrayList<>(getCurrentList());
        int positionToRemove = -1;

        // Find the position of the item to remove
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getId() == itemId) {
                positionToRemove = i;
                break;
            }
        }

        // Remove the item if found
        if (positionToRemove != -1) {
            currentList.remove(positionToRemove);
            Log.d("CartAdapter", "Item removed. New list size: " + currentList.size());
            submitList(new ArrayList<>(currentList)); // Submit the updated list to the adapter
            notifyItemRemoved(positionToRemove); // Notify the adapter about the removed item

            // Update the total price after modifying the list
            updateTotalPrice(); // Update total price here
        } else {
            Log.e("CartAdapter", "Item not found for removal");
        }
    }




    // Method to dismiss loading indicator
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
