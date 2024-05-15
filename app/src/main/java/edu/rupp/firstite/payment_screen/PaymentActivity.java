package edu.rupp.firstite.payment_screen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.rupp.firstite.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Retrofit with base URL and Gson converter factory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Initialize ApiServicePayment with Retrofit instance
        apiServicePayment = retrofit.create(ApiServicePayment.class);

        // Fetch access token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", "");

        // Find views
        EditText cardNumber = findViewById(R.id.CardNumber);
        EditText cardHolderName = findViewById(R.id.CardHolderNumber);
        EditText expirationDate = findViewById(R.id.ExpirationCard);
        EditText cvv = findViewById(R.id.Cvv);
        Button btnDone = findViewById(R.id.btnDone);
        ImageView btnCancel = findViewById(R.id.btnCancel);

        // Set click listener for Done button
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String cardNumberText = cardNumber.getText().toString().trim();
                String cardHolderNameText = cardHolderName.getText().toString().trim();
                String expirationDateText = expirationDate.getText().toString().trim();
                String cvvText = cvv.getText().toString().trim();

                // Validate input
                if (cardNumberText.isEmpty() || cardHolderNameText.isEmpty() || expirationDateText.isEmpty() || cvvText.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Fields must not be empty", Toast.LENGTH_LONG).show();
                } else {
                    // Create PaymentCart object
                    PaymentCart paymentCart = new PaymentCart(0, 0, cardNumberText, cardHolderNameText, expirationDateText, cvvText, 0);

                    // Call makePayment method
                    makePayment(paymentCart);
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

    // Method to make payment
    private void makePayment(PaymentCart paymentCart) {
        // Create authorization header
        String authorizationHeader = "Bearer " + accessToken;

        // Call the API using Retrofit
        Call<Void> call = apiServicePayment.makePayment(authorizationHeader, paymentCart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_LONG).show();
                    finish(); // Finish the activity after successful payment
                } else {
                    // Handle failure
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log the error
                Log.e("PaymentActivity", "Error making payment: " + t.getMessage());

                // Handle failure
                Toast.makeText(PaymentActivity.this, "Payment failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
