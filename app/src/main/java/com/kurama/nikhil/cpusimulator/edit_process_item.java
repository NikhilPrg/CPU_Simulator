package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Nikhil on 19/04/17.
 */
public class edit_process_item extends RelativeLayout {

    String process_name = "P1";
    int burstTime = 0;
    int arrivalTime = 0;

    public edit_process_item(Context context, Process p) {
        super(context);
        process_name = p.getProcessName();
        burstTime = p.getBurstTime();
        arrivalTime = p.getArrivalTime();
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edit_process_item, this);
        setProcessNameAndTime();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
    public void setProcessNameAndTime() {
        TextView name = (TextView) findViewById(R.id.edit_process_item_name);
        TextView bt = (TextView) findViewById(R.id.edit_process_item_bTime);
        TextView at = (TextView) findViewById(R.id.edit_process_item_aTime);

        name.setText(process_name);
        bt.setText(Integer.toString(burstTime));
        at.setText(Integer.toString(arrivalTime));
        setBackgroundResource(R.drawable.square);
    }

    public Process genProcess() {
     return new Process(process_name, burstTime, arrivalTime);
    }

}
