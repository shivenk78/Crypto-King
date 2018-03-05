package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.shiven.bloombergproj.MainActivity.TAG_LOG;
import static com.example.shiven.bloombergproj.MainActivity.currency;

/**
 * Created by Shiven on 2/26/2018.
 */

public class TradingFrag extends Fragment{

    Crypto crypto;
    GraphView graphView;
    String graphMode;
    ListView tradingListView;
    ArrayList<String> prices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_trading, null);

        tradingListView = (ListView)view.findViewById(R.id.trading_listView);
        graphView = view.findViewById(R.id.id_graphView);
        graphMode = "7day";

        return view;
    }

    public void setCrypto(Crypto c){
        if (currency.size() > 0) {
            crypto = c;
            ArrayList<Crypto> cryptoList = new ArrayList<>();
            cryptoList.add(crypto);
            CryptoAdapter adapter = new CryptoAdapter(getActivity(), R.layout.crypto_layout, cryptoList);
            tradingListView.setAdapter(adapter);
        }
    }

    public void graph(){
        new GraphThread().execute();
    }

    public class GraphThread extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL website = new URL("http://coincap.io/history/" + graphMode + "/" + crypto.getSymbol());
                URLConnection connection = website.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String nextLn;
                String initialJson = "";
                while ((nextLn = reader.readLine()) != null) {
                    initialJson += nextLn;
                }
                JSONObject temp1 = new JSONObject(initialJson);
                JSONArray priceArray = temp1.getJSONArray("price");
                for(int i=0; i<priceArray.length(); i++){
                    prices.add(priceArray.getJSONArray(i).getString(1));
                }
                Log.d(TAG_LOG,prices.toString());
            }catch(Exception e){
                Log.d(TAG_LOG,"Graph AsyncThread caught "+e);
            }
            return null;
        }
    }
}
