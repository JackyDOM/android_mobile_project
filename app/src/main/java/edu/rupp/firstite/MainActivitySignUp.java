package edu.rupp.firstite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivitySignUp extends AppCompatActivity {
    private Button button;
    private Button buttonBackSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up2);

        button = (Button) findViewById(R.id.SignUpButton123);
        buttonBackSignIn = (Button) findViewById(R.id.btnSignInBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHomePage();
            }
        });

        buttonBackSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { backSignInActivity();}
        });
    }

    public void openActivityHomePage(){
        Intent intent = new Intent(this, MainActivityHomeScreen.class);
        startActivity(intent);
    }
    public void backSignInActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}