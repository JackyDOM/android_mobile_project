package edu.rupp.firstite.signUp_Screen;

import androidx.appcompat.app.AppCompatActivity;

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

import edu.rupp.firstite.buttomNavigationBar.MainActivityHomeScreen;
import edu.rupp.firstite.R;
import edu.rupp.firstite.signIn_Screen.MainActivity;
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
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up2);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

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
        EditText editTextUsername = findViewById(R.id.editTextUsernameSignUp);
        EditText editTextEmail = findViewById(R.id.editTextEmailSignUp);
        EditText editTextPassword = findViewById(R.id.editTextPasswordSignUp);
        EditText editTextGender = findViewById(R.id.editTextGender);

        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            showCustomToast("All fields cannot be empty", false);
            return;
        }

        Log.d("SignUp", "Username: " + username + ", email: " + email + ", password: " + password + ", gender: " + gender);

        AuthRequest request = new AuthRequest(username, email, password, gender);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<AuthResponse> call = apiService.register(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    String accessToken = authResponse.getAccessToken();
                    showCustomToast("Sign-up successful", true);
                    Log.d("SignUp", "Access Token: " + accessToken);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", accessToken);
                    editor.apply();

                    String storedToken = sharedPreferences.getString("access_token", null);
                    Log.d("SignUp", "Stored Access Token: " + storedToken);

                    Intent intent_success = new Intent(MainActivitySignUp.this, MainActivityHomeScreen.class);
                    intent_success.putExtra("username", username);
                    startActivity(intent_success);

                } else {
                    showCustomToast("Failed to sign up", false);
                    Log.e("SignUp", "Failed to sign up: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showCustomToast("Failed to sign up", false);
                Log.e("SignUp", "Failed to sign up", t);
            }
        });

    }

    public void backSignInActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    public static class AuthRequest {
        private String username;
        private String email;
        private String password;
        private String gender;

        public AuthRequest(String username, String email, String password, String gender) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.gender = gender;
        }
    }

    public static class AuthResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }

    public interface ApiService {
        @POST("authorization/register")
        Call<AuthResponse> register(@Body AuthRequest request);
    }

}
