package edu.rupp.firstite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.rupp.firstite.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivitySignUp extends AppCompatActivity {
    private Button button;
    private Button buttonBackSignIn;
    private SharedPreferences sharedPreferences; // Declare sharedPreferences variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up2);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Initialize sharedPreferences

        button = findViewById(R.id.SignUpButton123);
        buttonBackSignIn = findViewById(R.id.btnSignInBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUserSignUp();
            }
        });

        buttonBackSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backSignInActivity();
            }
        });
    }

    public void authenticateUserSignUp() {
        EditText edtTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        EditText edtTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        EditText edtConfirmPasswordSignUp = findViewById(R.id.editTextConfirmPassword);

        String username = edtTextEmailSignUp.getText().toString().trim();
        String password = edtTextPasswordSignUp.getText().toString().trim();
        String confirmPassword = edtConfirmPasswordSignUp.getText().toString().trim();


        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(MainActivitySignUp.this, "Username or password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("SignUp", "Username: " + username + ", Password: " + password + ", Confirm Password: " + confirmPassword);

        AuthRequest request = new AuthRequest(username, password, confirmPassword); // Remove MainActivitySignUp from AuthRequest

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // Base URL without the endpoint
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class); // Remove MainActivitySignUp from ApiService

        Call<AuthResponse> call = apiService.register(request); // Remove MainActivitySignUp from AuthResponse
        call.enqueue(new Callback<AuthResponse>() { // Remove MainActivitySignUp from AuthResponse
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    String accessToken = authResponse.getAccessToken();
                    Toast.makeText(MainActivitySignUp.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    Log.d("SignUp", "Access Token: " + accessToken);

                    // Store the access token in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", accessToken);
                    editor.apply();

                    // Retrieve the access token from SharedPreferences
                    String storedToken = sharedPreferences.getString("access_token", null);
                    Log.d("SignUp", "Stored Access Token: " + storedToken);

                    Intent intent_success = new Intent(MainActivitySignUp.this, MainActivityHomeScreen.class);
                    intent_success.putExtra("username", username); // Pass the username to MainActivityHomeScreen
                    startActivity(intent_success);

                } else {
                    Toast.makeText(MainActivitySignUp.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                    Log.e("SignUp", "Failed to sign up: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivitySignUp.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                Log.e("SignUp", "Failed to sign up", t);
            }
        });

    }

    public void backSignInActivity() {
        Intent intent = new Intent(this, MainActivitySignUp.class);
        startActivity(intent);
    }

    public static class AuthRequest {
        private String username;
        private String password;
        private String confirmPassword;

        public AuthRequest(String username, String password, String confirmPassword) {
            this.username = username;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }
    }

    public static class AuthResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }

    public interface ApiService {
        @POST("auth/register") // Endpoint only
        Call<AuthResponse> register(@Body AuthRequest request); // Remove MainActivitySignUp from AuthRequest
    }

}
