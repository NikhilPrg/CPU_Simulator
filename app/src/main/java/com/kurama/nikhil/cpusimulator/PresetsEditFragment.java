package com.kurama.nikhil.cpusimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;


public class PresetsEditFragment extends Fragment {
    public PresetsEditFragment() {
    }
    public static PresetsEditFragment newInstance(String param1, String param2) {
        PresetsEditFragment fragment = new PresetsEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView lw;
    String[] ids;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View fl = inflater.inflate(R.layout.fragment_preset_edit, container, false);
        final ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(getContext());

        ids = dataSupply.getListOfIds();
        Arrays.sort(ids);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), R.layout.list_view_textbar, ids);
        lw = (ListView) fl.findViewById(R.id.edit_list);
        lw.setAdapter(aa);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tw = (String) adapterView.getItemAtPosition(i);
                int id = Integer.parseInt(tw.replaceFirst("P", "0").trim());

                Intent intent = new Intent(getActivity(), EditPreset.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) fl.findViewById(R.id.edit_list_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPreset.class);
                startActivity(intent);
            }
        });
        return fl;
    }

    @Override
    public void onResume() {
        super.onResume();
        ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(getContext());
        ids = dataSupply.getListOfIds();
        Arrays.sort(ids);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), R.layout.list_view_textbar, ids);
        lw.setAdapter(aa);
    }
}
