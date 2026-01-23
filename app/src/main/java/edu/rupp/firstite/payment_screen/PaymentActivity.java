package edu.rupp.firstite.payment_screen;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.rupp.firstite.R;
import edu.rupp.firstite.utils.ToastUtil;
import edu.rupp.firstite.modals.PaymentCart;
import edu.rupp.firstite.service.ApiServicePayment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {

    private ApiServicePayment apiServicePayment;
    private String accessToken;

    private ArrayList<Integer> bookIds;
    private ArrayList<Double> prices;
    private int userId;
    private int currentBookIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Retrofit with base URL and Gson converter factory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://elibra.skinme.store/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Initialize ApiServicePayment with Retrofit instance
        apiServicePayment = retrofit.create(ApiServicePayment.class);

        // Fetch access token and user_id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", "");

        // Retrieve user_id from intent, fallback to SharedPreferences if not available
        userId = getIntent().getIntExtra("user_id", 0);
        if (userId == 0) {
            userId = sharedPreferences.getInt("user_id", 0);
        }

        // Retrieve book_ids and prices from intent
        bookIds = getIntent().getIntegerArrayListExtra("book_ids");
        prices = (ArrayList<Double>) getIntent().getSerializableExtra("prices");

        // Log userId and book details to Logcat
        Log.d("PaymentActivity", "User ID: " + userId);
        if (bookIds != null && prices != null) {
            Log.d("PaymentActivity", "Number of books: " + bookIds.size());
            for (int i = 0; i < bookIds.size(); i++) {
                Log.d("PaymentActivity", "Book ID: " + bookIds.get(i) + ", Price: " + prices.get(i));
            }
        } else {
            ToastUtil.showCustomToast(this, "No books to pay for", false);
            finish();
            return;
        }

        // Find views
        EditText cardNumber = findViewById(R.id.CardNumber);
        EditText cardHolderName = findViewById(R.id.CardHolderNumber);
        EditText expirationDate = findViewById(R.id.ExpirationCard);
        EditText cvv = findViewById(R.id.Cvv);
        Button btnDone = findViewById(R.id.btnDone);
        ImageView btnCancel = findViewById(R.id.btnCancel);

        // Set input type to number and add InputFilter to restrict input to numbers
        // only
        cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        cardHolderName.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvv.setInputType(InputType.TYPE_CLASS_NUMBER);
        expirationDate.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Add input filters to restrict input to numbers only and show a toast if input
        // is invalid
        InputFilter numberFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        // Toast.makeText(PaymentActivity.this, "Fields must be numbers only",
                        // Toast.LENGTH_SHORT).show();
                        ToastUtil.showCustomToast(PaymentActivity.this, "Fields must be numbers only", false);
                        return "";
                    }
                }
                return null;
            }
        };

        cardNumber.setFilters(new InputFilter[] { numberFilter });
        cardHolderName.setFilters(new InputFilter[] { numberFilter });
        cvv.setFilters(new InputFilter[] { numberFilter });
        expirationDate.setFilters(new InputFilter[] { numberFilter });

        // Set click listener for Done button
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardNumberText = cardNumber.getText().toString().trim();
                String cardHolderNameText = cardHolderName.getText().toString().trim();
                String expirationDateText = expirationDate.getText().toString().trim();
                String cvvText = cvv.getText().toString().trim();

                // Validate input
                if (cardNumberText.isEmpty() || cardHolderNameText.isEmpty() || expirationDateText.isEmpty()
                        || cvvText.isEmpty()) {
                    ToastUtil.showCustomToast(PaymentActivity.this, "Fields must not be empty", false);
                } else if (userId == 0) {
                    ToastUtil.showCustomToast(PaymentActivity.this, "User ID not available", false);
                } else if (bookIds == null || bookIds.isEmpty()) {
                    ToastUtil.showCustomToast(PaymentActivity.this, "No books to pay for", false);
                } else {
                    // Reset current book index and start processing payments
                    currentBookIndex = 0;
                    processNextPayment(cardNumberText, cardHolderNameText, expirationDateText, cvvText);
                }
            }
        });

        // Set click listener for Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Method to process payments for all books sequentially
    private void processNextPayment(String cardNumberText, String cardHolderNameText, String expirationDateText,
            String cvvText) {
        if (currentBookIndex >= bookIds.size()) {
            // All payments processed successfully
            ToastUtil.showCustomToast(PaymentActivity.this, "All payments successful", true);
            finish();
            return;
        }

        int bookId = bookIds.get(currentBookIndex);
        double price = prices.get(currentBookIndex);

        // Create PaymentCart object for current book
        PaymentCart paymentCart = new PaymentCart(userId, bookId, cardNumberText, cardHolderNameText,
                expirationDateText, cvvText, price);

        // Make payment for current book
        makePayment(paymentCart, cardNumberText, cardHolderNameText, expirationDateText, cvvText);
    }

    // Method to make payment for a single book
    private void makePayment(PaymentCart paymentCart, String cardNumberText, String cardHolderNameText,
            String expirationDateText, String cvvText) {
        // Create authorization header
        String authorizationHeader = "Bearer " + accessToken;

        // Call the API using Retrofit
        Call<Void> call = apiServicePayment.makePayment(authorizationHeader, paymentCart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("PaymentActivity", "Payment successful for book ID: " + paymentCart.getBookId());
                    // Move to next book
                    currentBookIndex++;
                    // Process next payment
                    processNextPayment(cardNumberText, cardHolderNameText, expirationDateText, cvvText);
                } else {
                    // Handle failure
                    Log.e("PaymentActivity", "Payment failed for book ID: " + paymentCart.getBookId() + ", Response: "
                            + response.message());
                    ToastUtil.showCustomToast(PaymentActivity.this,
                            "Payment failed for book ID: " + paymentCart.getBookId(), false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log the error
                Log.e("PaymentActivity",
                        "Error making payment for book ID: " + paymentCart.getBookId() + ", Error: " + t.getMessage());
                ToastUtil.showCustomToast(PaymentActivity.this, "Payment failed: " + t.getMessage(), false);
            }
        });
    }
}