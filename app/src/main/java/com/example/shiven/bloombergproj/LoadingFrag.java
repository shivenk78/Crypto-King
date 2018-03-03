package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import static android.graphics.Color.rgb;

/**
 * Created by Shiven on 2/26/2018.
 */

public class LoadingFrag extends Fragment{

    public static ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_loading, null);
        progressBar = (ProgressBar) view.findViewById(R.id.load_progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(rgb(80,216,120), android.graphics.PorterDuff.Mode.MULTIPLY);//50c878

        return view;
    }
}