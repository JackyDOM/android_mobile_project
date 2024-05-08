package edu.rupp.firstite.Logo_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import edu.rupp.firstite.R;
import edu.rupp.firstite.signIn_Screen.MainActivity;

public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        }, 5000);
    }
}
