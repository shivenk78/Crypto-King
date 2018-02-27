package com.example.shiven.bloombergproj;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Shiven on 2/27/2018.
 */

public class Crypto {

    int rank;
    double price;
    String symbol,name;
    JSONObject page;

    public Crypto(String sym, JSONObject page){
        symbol = sym;
        this.page = page;
        try {
            price = page.getDouble("price");
            rank = page.getInt("rank");
            name = page.getString("display_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getPrice(){
        return price;
    }
    public int getRank(){
        return rank;
    }
    public String getSymbol(){
        return symbol;
    }
}
