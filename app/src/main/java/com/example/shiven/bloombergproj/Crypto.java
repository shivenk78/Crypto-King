package com.example.shiven.bloombergproj;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shiven on 2/27/2018.
 */

public class Crypto{

    static int indexCount = 0;
    int rank,picID,index;
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
        switch(sym){
            case "BTC": picID = R.drawable.icon_btc; break;
            case "ETH": picID = R.drawable.icon_eth; break;
            case "BCH": picID = R.drawable.icon_bch; break;
            case "ADA": picID = R.drawable.icon_ada; break;
            case "XLM": picID = R.drawable.icon_xlm; break;
            case "XRP": picID = R.drawable.icon_xrp; break;
            case "IOT": picID = R.drawable.icon_iot; break;
            case "NEO": picID = R.drawable.icon_neo; break;
            case "EOS": picID = R.drawable.icon_eos; break;
            case "LTC": picID = R.drawable.icon_ltc; break;
            default: picID = R.drawable.ic_home_black_24dp; break;
        }
        index=indexCount;
        indexCount++;
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
    public String getName(){
        return name;
    }
    public int getPic(){
        return picID;
    }
    public int getIndex(){return  index;}
}
