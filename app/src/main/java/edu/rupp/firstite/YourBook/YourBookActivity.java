package edu.rupp.firstite.YourBook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import edu.rupp.firstite.R;
import edu.rupp.firstite.utils.ToastUtil;
import edu.rupp.firstite.adapter.YourBookAdapter;
import edu.rupp.firstite.logOut_screen.LogoutFragment;
import edu.rupp.firstite.modals.YourBookModal;
import edu.rupp.firstite.service.ApiServiceYourBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YourBookActivity extends AppCompatActivity {

    private String accessToken;
    private YourBookAdapter yourBookAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_book);

        ImageView backToLogout = findViewById(R.id.backToLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);

        if (accessToken != null && !accessToken.isEmpty()) {
            Log.d("AccessToken", "Retrieved Access Token: " + accessToken);
        } else {
            ToastUtil.showCustomToast(YourBookActivity.this, "Access token not available or empty", false);
        }

        if (accessToken != null) {
            loadYourBook();
        }

        yourBookAdapter = new YourBookAdapter(accessToken);

        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(YourBookActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(yourBookAdapter);

        backToLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogOutScreen();
            }
        });
    }

    private void backToLogOutScreen() {
        LogoutFragment logoutFragment = new LogoutFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, logoutFragment)
                .addToBackStack(null)
                .commit();
        finish();
    }

    private void loadYourBook() {
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServiceYourBook apiServiceYourBook = httpClient.create(ApiServiceYourBook.class);
        Call<List<YourBookModal>> task = apiServiceYourBook.loadYourBook("Bearer " + accessToken);

        task.enqueue(new Callback<List<YourBookModal>>() {
            @Override
            public void onResponse(Call<List<YourBookModal>> call, Response<List<YourBookModal>> response) {
                if (response.isSuccessful()) {
                    List<YourBookModal> yourBookModals = response.body();
                    if (yourBookModals != null) {
                        yourBookAdapter.submitList(yourBookModals);
                        ToastUtil.showCustomToast(YourBookActivity.this, "Your Book loaded successfully", true);
                    }
                } else {
                    ToastUtil.showCustomToast(YourBookActivity.this, "Failed to load your book",false);
                    Log.e("YourBookActivity", "Failed to load Your Book: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<YourBookModal>> call, Throwable t) {
                ToastUtil.showCustomToast(YourBookActivity.this, "Failed to load your book", false);
                Log.e("YourBookActivity", "Failed to load Your Book", t);
            }
        });
    }
}

