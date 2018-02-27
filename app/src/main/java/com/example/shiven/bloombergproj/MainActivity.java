package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.menubar);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFrag());
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getFragmentManager().beginTransaction().replace(R.id.id_fragment_frame, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFrag();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFrag();
                break;
            case R.id.navigation_trading:
                fragment = new TradingFrag();
                break;
        }

        return loadFragment(fragment);
    }
}
