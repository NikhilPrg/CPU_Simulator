package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nikhil on 19/04/17.
 */
public class ProcessAdapter extends ArrayAdapter<Process> {
    public ProcessAdapter(Context context, int textViewResourceId ,ArrayList<Process> processes) {
        super(context, textViewResourceId,processes);
    }

    private static class ViewHolder {
        private EditText nameTv;
        private EditText bTimeTv;
        private EditText aTimeTv;
    }

    ViewHolder viewHolder;

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        Process process = getItem(position);

        if(contentView == null) {
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.edit_process_item, parent , false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv =  (EditText) contentView.findViewById(R.id.edit_process_item_name);
            viewHolder.bTimeTv = (EditText) contentView.findViewById(R.id.edit_process_item_bTime);
            viewHolder.aTimeTv = (EditText) contentView.findViewById(R.id.edit_process_item_aTime);

            contentView.setTag(viewHolder);
        }else  {
            viewHolder = (ViewHolder) contentView.getTag();
        }

       if(process != null) {
           viewHolder.nameTv.setText(process.getProcessName());
           viewHolder.bTimeTv.setText(Integer.toString(process.getBurstTime()));
           viewHolder.aTimeTv.setText(Integer.toString(process.getArrivalTime()));
       }


        return contentView;
    }
}
