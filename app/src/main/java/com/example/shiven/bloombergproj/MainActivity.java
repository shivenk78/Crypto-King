package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    final static String TAG_LOG = "iamdebugging";
    final static String CRYPTO_KEY = "cryptocurrencykey";
    final static String SAVE_FILE_NAME = "savefilename";

    public static ArrayList<Double> initialInvestment;
    public static ArrayList<Double> quantities;

    ArrayList<URL> urls;
    ArrayList<String> cryptoNames;
    public static ArrayList<Crypto> currency;
    BufferedReader reader;
    public static HomeFrag homeFrag;
    public static LoadingFrag loadingFrag;
    public static ProfileFrag profileFrag;
    public static TradingFrag tradingFrag;

    public static BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urls = new ArrayList<>();
        currency = new ArrayList<>();
        quantities = new ArrayList<>();
        initialInvestment = new ArrayList<>();
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

        homeFrag = new HomeFrag();
        profileFrag = new ProfileFrag();
        loadingFrag = new LoadingFrag();

        navigation = findViewById(R.id.menubar);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);

        loadFragment(loadingFrag);
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
            tradingFrag = new TradingFrag();
            Bundle bundle = new Bundle();
            bundle.putInt(CRYPTO_KEY,0);
                tradingFrag.setArguments(bundle);
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
                fragment = homeFrag;
                break;
            case R.id.navigation_profile:
                fragment = profileFrag;
                break;
            case R.id.navigation_trading:
                fragment = tradingFrag;
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    protected void onStart() {
        try {
            FileInputStream inputStream = openFileInput(SAVE_FILE_NAME);
            ArrayList<Double> tempList = new ArrayList<>();
            String tempString="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String nextLn;
            while( (nextLn=reader.readLine())!=null ){
                //tempList.add(Double.parseDouble(nextLn));
                tempString+=nextLn;
            }
            Log.d(TAG_LOG,tempString.toString());
        }catch (Exception e){
            Log.d(TAG_LOG,"onStart caught "+e);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE_NAME,false));
            for(int i=0; i<initialInvestment.size(); i++){
                writer.write(initialInvestment.get(i).toString());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*FileOutputStream fileOutputStream;
        try{
            fileOutputStream = openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
            for(int i=0; i<initialInvestment.size(); i++){
                fileOutputStream.write(initialInvestment.get(i).byteValue());
            }
            fileOutputStream.close();
            Log.d(TAG_LOG, new File(SAVE_FILE_NAME).getAbsoluteFile().toString());
        }catch(Exception e){
            e.printStackTrace();
        }*/
        super.onStop();
    }
}
