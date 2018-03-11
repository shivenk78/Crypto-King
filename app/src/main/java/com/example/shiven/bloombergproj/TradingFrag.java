package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
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
import java.util.Date;
import java.util.List;

import static com.example.shiven.bloombergproj.MainActivity.CRYPTO_KEY;
import static com.example.shiven.bloombergproj.MainActivity.TAG_LOG;
import static com.example.shiven.bloombergproj.MainActivity.currency;
import static com.example.shiven.bloombergproj.MainActivity.initialInvestment;
import static com.example.shiven.bloombergproj.MainActivity.profileFrag;
import static com.example.shiven.bloombergproj.MainActivity.quantities;

/**
 * Created by Shiven on 2/26/2018.
 */

public class TradingFrag extends Fragment{

    public Crypto crypto = currency.get(0);
    Button add;
    EditText editText;
    GraphView graphView;
    String graphMode;
    ListView tradingListView;
    RadioGroup radioGroup;
    ArrayList<Long> times = new ArrayList<>();
    ArrayList<Double> prices = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_trading, null);

        Bundle bundle = getArguments();
        crypto = currency.get(bundle.getInt(CRYPTO_KEY));
        Log.d(TAG_LOG,"Bundle Received containing: "+crypto.getName());
        editText = (EditText)view.findViewById(R.id.id_addAmount);
        add = (Button)view.findViewById(R.id.id_add);
        tradingListView = (ListView)view.findViewById(R.id.trading_listView);
        radioGroup = (RadioGroup)view.findViewById(R.id.trading_radioGroup);
        graphView = view.findViewById(R.id.id_graphView);
            graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graphView.getGridLabelRenderer().setVerticalAxisTitle("Price (USD)");
            graphView.getGridLabelRenderer().setHorizontalAxisTitle("Date/Time");

        graphMode="1day";
        ((RadioButton)(radioGroup.getChildAt(0))).setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG_LOG,"RadioGroup changed to button "+i);
                switch (i){
                    case R.id.radio_1: graphMode="1day"; break;
                    case R.id.radio_7: graphMode="7day"; break;
                    case R.id.radio_30: graphMode="30day"; break;
                    case R.id.radio_365: graphMode="365day"; break;
                    default: graphMode="1day"; break;
                }
                graph();
            }
        });
        setCrypto();
        graph();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantities.set(crypto.getIndex(),Double.parseDouble(editText.getText().toString())+quantities.get(crypto.getIndex()));
                Toast.makeText(getActivity(), "Added to Portfolio", Toast.LENGTH_SHORT).show();
                initialInvestment+=quantities.get(crypto.getIndex())*crypto.getPrice();
                profileFrag.refresh();
                editText.setText("");
            }
        });

        return view;
    }

    public void setCrypto(){
        ArrayList<Crypto> cryptoList = new ArrayList<>();
        cryptoList.add(crypto);
        CryptoAdapter adapter = new CryptoAdapter(getActivity(), R.layout.crypto_layout, cryptoList);
        tradingListView.setAdapter(adapter);
    }

    public void graph(){
        GraphThread graphThread = new GraphThread();
        graphThread.execute();
    }

    public class GraphThread extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Log.d(TAG_LOG,"Async Graph STARTING using Currency: "+crypto.getName()+" with mode "+graphMode.toUpperCase());
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
                times = new ArrayList<>();
                prices = new ArrayList<>();
                for(int i=0; i<priceArray.length(); i++){
                    times.add( Long.parseLong(priceArray.getJSONArray(i).getString(0)));
                    prices.add( Double.parseDouble(priceArray.getJSONArray(i).getString(1)));
                }
                Log.d(TAG_LOG,"Array Length: "+times.size());
            }catch(Exception e){
                Log.d(TAG_LOG,"Graph AsyncThread caught "+e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            DataPoint[] dataPoints = new DataPoint[times.size()];
            for(int i=0; i<times.size(); i++){
                dataPoints[i] = new DataPoint(times.get(i),prices.get(i));
            }
            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
            graphView.removeAllSeries();
            graphView.addSeries(lineGraphSeries);
            graphView.getViewport().setScalable(true);
            graphView.getViewport().setScrollable(true);
            graphView.getViewport().setScalableY(true);
            graphView.getViewport().setScrollableY(true);
        }
    }


}
