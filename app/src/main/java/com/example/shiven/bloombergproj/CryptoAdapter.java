package com.example.shiven.bloombergproj;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shiven on 2/27/2018.
 */

public class CryptoAdapter extends ArrayAdapter {

    Context con;
    List<Crypto> cryptoList;

    public CryptoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        con = context;
        cryptoList = objects;
    }

    @NonNull
    @Override
    public View getView (final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.crypto_layout,null);
        Crypto currentCrypto = cryptoList.get(position);

        TextView name = (TextView)layoutView.findViewById(R.id.id_name);
        TextView symbol = (TextView)layoutView.findViewById(R.id.id_symbol);
        TextView price = (TextView)layoutView.findViewById(R.id.id_price);
        ImageView icon = (ImageView)layoutView.findViewById(R.id.id_icon);

        name.setText(currentCrypto.getName());
        symbol.setText(currentCrypto.getSymbol());
        price.setText("$"+currentCrypto.getPrice());
        icon.setImageResource(currentCrypto.getPic());

        return layoutView;
    }
}
