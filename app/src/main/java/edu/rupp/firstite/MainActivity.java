package edu.rupp.firstite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        AuthRequest request = new AuthRequest(username, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/auth/")
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
                    System.out.println("Access Token: " + accessToken);

                    // Navigate to HomeFragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentHome, new HomeFragment())
                            .commit();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
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
        @POST("login")
        Call<AuthResponse> login(@Body AuthRequest request);
    }
}
