package com.example.shiven.bloombergproj;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shiven on 2/26/2018.
 */

public class ProfileFrag extends Fragment{

    Button reset, confirm;
    TextView initialVal, currentVal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, null);

        reset = (Button)view.findViewById(R.id.id_reset);
        confirm  = (Button)view.findViewById(R.id.id_confirm);
            confirm.setVisibility(View.INVISIBLE);
        initialVal = (TextView)view.findViewById(R.id.id_initialValue);
        currentVal = (TextView)view.findViewById(R.id.id_currentValue);
        refresh();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirm.getVisibility()==View.VISIBLE){
                    confirm.setVisibility(View.INVISIBLE);
                }else{
                    confirm.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Click CONFIRM to Reset Portfolio", Toast.LENGTH_SHORT).show();
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.resetProfile();
                refresh();
                confirm.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    public void refresh(){

        String initialTemp = MainActivity.initialInvestment.toString();
        String currentTemp = MainActivity.getCurrentValue()+"";

        initialVal.setText("$"+initialTemp);
        currentVal.setText("$"+currentTemp);
        if( Double.parseDouble(currentTemp) > Double.parseDouble(initialTemp)){
            currentVal.setTextColor(Color.GREEN);
        }else if( Double.parseDouble(currentTemp) == Double.parseDouble(initialTemp)){
            currentVal.setTextColor(Color.BLUE);
        }else{
            currentVal.setTextColor(Color.RED);
        }
    }
}
