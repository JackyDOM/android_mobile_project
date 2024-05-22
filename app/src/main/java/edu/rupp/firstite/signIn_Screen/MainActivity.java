package edu.rupp.firstite.signIn_Screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    public void showCustomToast(String message, boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(isSuccess ? R.layout.toast_success : R.layout.toast_failure,
                findViewById(isSuccess ? R.id.text : R.id.text));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
                    int userId = authResponse.getUserId();
                    Log.d("SignIn", "Access Token: " + accessToken);
                    Log.d("SignIn", "User ID: " + userId);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", accessToken);
                    editor.apply();

                    String storedToken = sharedPreferences.getString("access_token", null);
                    Log.d("SignIn", "Stored Access Token: " + storedToken);

                    showCustomToast("Sign-in successful", true);

                    Intent intent_success = new Intent(MainActivity.this, MainActivityHomeScreen.class);
                    intent_success.putExtra("username", username);
                    startActivity(intent_success);

                } else {
                    showCustomToast("Failed to sign in", false);
                    Log.e("SignIn", "Failed to sign in: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showCustomToast("Failed to sign in", false);
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
        private int user_id; // Add user_id field


        public String getAccessToken() {
            return access_token;
        }

        public int getUserId() {
            return user_id;
        }
    }

    public interface ApiService {
        @POST("authorization/login") // Endpoint only
        Call<AuthResponse> login(@Body AuthRequest request);
    }
}
