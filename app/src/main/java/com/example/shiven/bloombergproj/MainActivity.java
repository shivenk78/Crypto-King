package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    final static String TAG_LOG = "iamdebugging";

    ArrayList<URL> urls;
    ArrayList<String> cryptoNames;
    public static ArrayList<Crypto> currency;
    BufferedReader reader;

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urls = new ArrayList<>();
        currency = new ArrayList<>();
        cryptoNames = new ArrayList<>();
            cryptoNames.add("BTC"); //bitcoin
            cryptoNames.add("ETH"); //ethereum
            cryptoNames.add("XRP"); //ripple
            cryptoNames.add("BCH"); //bitcoin cash
            cryptoNames.add("LTC"); //litecoin
            cryptoNames.add("NEO"); //neo
            cryptoNames.add("ADA"); //cardano
            cryptoNames.add("XLM"); //stellar
            cryptoNames.add("EOS"); //eos
            cryptoNames.add("IOT"); //iota

        navigation = findViewById(R.id.menubar);
        navigation.setOnNavigationItemSelectedListener(this);

        AsyncThread firstThread = new AsyncThread();
        firstThread.execute();
    }

    public class AsyncThread extends AsyncTask{
        @Override
        protected ArrayList<Crypto> doInBackground(Object[] objects) {
            try{
                for(int i=0; i<cryptoNames.size(); i++) {
                    URL website = new URL("http://coincap.io/page/" + cryptoNames.get(i));
                    URLConnection connection = website.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String nextLn;
                    String initialJson="";
                    while( (nextLn = reader.readLine()) != null){
                        initialJson+=nextLn;
                    }
                    Log.d(TAG_LOG,"CRYPTO "+i+"\t"+initialJson);
                    currency.add(new Crypto(cryptoNames.get(i),new JSONObject(initialJson)));
                }
            }catch(Exception e){
                Log.d(TAG_LOG,"First Async TC caught: "+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            loadFragment(new HomeFrag());
            navigation.setSelectedItemId(R.id.navigation_home);
        }
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
