package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.shiven.bloombergproj.MainActivity.currency;

/**
 * Created by Shiven on 2/26/2018.
 */

public class HomeFrag extends Fragment{

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, null);
        listView = (ListView) view.findViewById(R.id.id_listView);

        CryptoAdapter adapter = new CryptoAdapter(getActivity(), R.layout.crypto_layout, currency);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }
}
