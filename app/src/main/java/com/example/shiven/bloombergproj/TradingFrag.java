package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.example.shiven.bloombergproj.MainActivity.currency;

/**
 * Created by Shiven on 2/26/2018.
 */

public class TradingFrag extends Fragment{

    Crypto crypto;
    GraphView graphView;
    String graphMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_trading, null);
        graphView = view.findViewById(R.id.id_graphView);
        graphMode = "7day";

        return view;
    }

    public void setCrypto(int i){
        crypto = currency.get(i);
    }

    public void graph(){

    }

    public class GraphThread extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }
    }
}
