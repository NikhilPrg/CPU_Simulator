package com.kurama.nikhil.cpusimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditPreset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);

        ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(this);
        ArrayList<Process> processes = dataSupply.getPreset(id);

        ArrayAdapter<Process> aa = new ProcessAdapter(this,R.layout.edit_process_item,processes);
        ListView lw = (ListView) findViewById(R.id.preset_edit_list);
        lw.setAdapter(aa);

    }

}
