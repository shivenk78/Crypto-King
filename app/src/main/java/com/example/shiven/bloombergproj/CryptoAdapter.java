package com.example.shiven.bloombergproj;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Shiven on 2/27/2018.
 */

public class CryptoAdapter extends ArrayAdapter {

    Context con;
    List<Crypto> cryptoList;

    public CryptoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects){
        super(context, resource, objects);
        con = context;
        cryptoList = objects;
    }
}
