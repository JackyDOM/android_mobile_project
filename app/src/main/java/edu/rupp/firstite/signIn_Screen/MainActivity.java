package edu.rupp.firstite.signIn_Screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.rupp.firstite.Home_screen.HomeFragment;
import edu.rupp.firstite.buttomNavigationBar.MainActivityHomeScreen;
import edu.rupp.firstite.signUp_Screen.MainActivitySignUp;
import edu.rupp.firstite.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button buttonSignIn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        buttonSignIn = findViewById(R.id.btnSignIn);
        button = findViewById(R.id.btnSignUp);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySignUp();
            }
        });
    }

    public void authenticateUser() {
        EditText edtUsername = findViewById(R.id.editTextEmail);
        EditText edtPassword = findViewById(R.id.editTextPassword);

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            // Show a toast message indicating that both fields are required
            Toast.makeText(MainActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("SignIn", "Username: " + username + ", Password: " + password);

        AuthRequest request = new AuthRequest(username, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // Base URL without the endpoint
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<AuthResponse> call = apiService.login(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    String accessToken = authResponse.getAccessToken();
                    Toast.makeText(MainActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                    Log.d("SignIn", "Access Token: " + accessToken);

                    // Store the access token in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", accessToken);
                    editor.apply();

                    // Retrieve the access token from SharedPreferences
                    String storedToken = sharedPreferences.getString("access_token", null);
                    Log.d("SignIn", "Stored Access Token: " + storedToken);

                    // Example usage: If you want to navigate to HomeFragment after successful sign-in
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragmentHome, new HomeFragment())
//                            .commit();
                    Intent intent_success = new Intent(MainActivity.this, MainActivityHomeScreen.class);
                    intent_success.putExtra("username", username); // Pass the username to MainActivityHomeScreen
                    startActivity(intent_success);

//                    HomeFragment homeFragment = new HomeFragment();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, homeFragment)
//                            .commit();

                } else {
                    Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                    Log.e("SignIn", "Failed to sign in: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                Log.e("SignIn", "Failed to sign in", t);
            }
        });
    }

    public void openActivitySignUp() {
        Intent intent = new Intent(this, MainActivitySignUp.class);
        startActivity(intent);
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public AuthRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class AuthResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }

    public interface ApiService {
        @POST("authorization/login") // Endpoint only
        Call<AuthResponse> login(@Body AuthRequest request);
    }
}
