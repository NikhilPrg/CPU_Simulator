package com.kurama.nikhil.cpusimulator;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedFragment extends Fragment {


    public SpeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fl = inflater.inflate(R.layout.fragment_speed, container, false);
        NumberPicker numberPicker =(NumberPicker) fl.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        String[] displayValues = new String[20];
        for (int i = 0; i < 20; i++) {
            displayValues[i] = Integer.toString(i*100) + " ms";
        }
        SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        numberPicker.setDisplayedValues(displayValues);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                editor.putInt("speed", numberPicker.getValue()*100);
                editor.commit();
                Log.v("Seedpicked", Integer.toString(numberPicker.getValue()*100));
            }
        });
        return fl;
    }

}
