package edu.rupp.firstite.buttomNavigationBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.rupp.firstite.author_screen.AuthorFragment;
import edu.rupp.firstite.cart_screen.CartFragment;
import edu.rupp.firstite.Home_screen.HomeFragment;
import edu.rupp.firstite.R;
import edu.rupp.firstite.search_screen.SearchFragment;
import edu.rupp.firstite.logOut_screen.LogoutFragment;

public class MainActivityHomeScreen extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_screen);

        bottomNavigationView = findViewById(R.id.bottomNaviView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                } else if(itemId == R.id.navCart) {
                    loadFragment(new CartFragment(), false);
                }else if (itemId == R.id.navSearch) {
                    loadFragment(new AuthorFragment(), false);
                } else if (itemId == R.id.navNotification) {
                    loadFragment(new SearchFragment(), false);
                }else{
                    loadFragment(new LogoutFragment(), false);
                }

                return true;
            }
        });

        loadFragment(new HomeFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }
}
