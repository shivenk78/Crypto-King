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
import java.io.FileReader;
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

    public static Double initialInvestment;
    public static ArrayList<Double> quantities;

    ArrayList<URL> urls;
    ArrayList<String> cryptoNames;
    public static ArrayList<Crypto> currency;
    BufferedReader reader;
    public static File saveFile;
    public static HomeFrag homeFrag;
    public static LoadingFrag loadingFrag;
    public static TradingFrag tradingFrag;


    public static BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveFile = new File(getFilesDir(),"savefile.txt");

        urls = new ArrayList<>();
        currency = new ArrayList<>();
        quantities = new ArrayList<>();
        initialInvestment = 0.0;
        for(int i=0; i<10; i++) {
            quantities.add(0.0);
        }
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
                    currency.add(new Crypto(cryptoNames.get(i),new JSONObject(initialJson)));
                    Log.d(TAG_LOG,"CRYPTO "+i+" "+currency.get(i).getName());
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

    public static void resetProfile(){
        initialInvestment = 0.0;
        for(int i=0; i<10; i++) {
            quantities.set(i,0.0);
        }
    }

    public static double getCurrentValue(){
        Log.d(TAG_LOG,quantities.toString());
        double temp = 0.0;
        for(int i=0; i<currency.size(); i++){
            temp+=currency.get(i).getPrice()*quantities.get(i);
        }
        return temp;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = homeFrag;
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFrag();
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
            FileReader fileReader = new FileReader(saveFile);
            BufferedReader startReader = new BufferedReader(fileReader);

            String nextLn;
            initialInvestment=Double.parseDouble(startReader.readLine());
            int i=0;
            while( (nextLn=startReader.readLine())!=null ){
                quantities.set(i,Double.parseDouble(nextLn));
                i++;
            }
            startReader.close();

            Log.d(TAG_LOG,quantities.toString());
        }catch (Exception e){
            Log.d(TAG_LOG,"onStart caught "+e);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG_LOG,"File Path: "+saveFile.getPath());
        Log.d(TAG_LOG,saveFile.isFile()+"");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile,false));
            writer.write(initialInvestment.toString());
            writer.newLine();
            for (int i = 0; i < quantities.size(); i++) {
                writer.write(quantities.get(i).toString());
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
