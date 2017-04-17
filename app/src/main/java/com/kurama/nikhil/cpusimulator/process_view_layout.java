package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Nikhil on 16/04/17.
 */
public class process_view_layout extends RelativeLayout{



    String process_name = "P1";
    int burstTime = 0;
    int arrivalTime = 0;
    int processState = 0;



    /*
        processState Values:
            0 : Dint run yet
            1 : Running
            2 : Completed
            3 : Waiting
     */

    public void setProcessState(int processState) {
        if(this.processState != 2) {
            this.processState = processState;
            setBackground();
        }

    }

    public process_view_layout(Context context) {
        super(context);
        initializeViews(context);
    }

    public process_view_layout(Context context, String p, int bt, int at) {
        super(context);
        initializeViews(context);
        process_name = p;
        burstTime = bt;
        arrivalTime = at;
        setProcessNameAndTime();
    }

    public process_view_layout(Context context, String p, int bt, int at, int ps) {
        super(context);
        initializeViews(context);
        process_name = p;
        burstTime = bt;
        arrivalTime = at;
        processState = ps;
        setProcessNameAndTime();
        setBackgroundResource(R.drawable.running);
    }

    public process_view_layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public process_view_layout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.process_view_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setProcessNameAndTime();
        super.setBackgroundResource(R.drawable.square);
    }

    public void setProcessNameAndTime() {
        TextView pro = (TextView) findViewById(R.id.temp_textViewProcess);
        TextView tim = (TextView) findViewById(R.id.temp_textViewTime);
        TextView atim = (TextView) findViewById(R.id.temp_textViewArrivalTime);

        pro.setText(process_name);

        if(arrivalTime > 0) {
            atim.setText(Integer.toString(arrivalTime));
        } else {
            atim.setText("");
            setProcessState(3);
        }

        if(burstTime > 0) {
            tim.setText(Integer.toString(burstTime));
        } else {
            tim.setText("Done");
            setProcessState(2);
        }
        setBackground();
    }

    public void setProcessNameAndTime(String n, int t) {
        process_name = n;
        burstTime = t;
        setProcessNameAndTime();
        setBackgroundResource(R.drawable.running);
    }

    public void subBurstTime() {
        if(burstTime > 1) {
            burstTime--;
            setProcessState(1);
            setBackground();
            TextView tim = (TextView) findViewById(R.id.temp_textViewTime);
            tim.setText(Integer.toString(burstTime));
        } else {
            TextView tim = (TextView) findViewById(R.id.temp_textViewTime);
            setProcessState(2);
            setBackground();
            tim.setText("Done");
        }
    }

    public void subArrivalTime() {
        arrivalTime--;
        if(arrivalTime > 1) {
            TextView tim = (TextView) findViewById(R.id.temp_textViewArrivalTime);
            tim.setText(Integer.toString(arrivalTime));
        } else {
//            arrivalTime = 0;
            TextView tim = (TextView) findViewById(R.id.temp_textViewArrivalTime);
            if(processState != 2 && processState != 1 ) {
                setProcessState(3);
            }
            setBackground();
            tim.setText("");
        }
    }

    public void setBackground() {
        switch (processState) {
            case 0:
                setBackgroundResource(R.drawable.square);
                break;
            case 1:
                setBackgroundResource(R.drawable.running);
                break;
            case 2:
                setBackgroundResource(R.drawable.completed);
                break;
            case 3:
                setBackgroundResource(R.drawable.waiting);
                break;
            default:
                setBackgroundResource(R.drawable.square);
                break;
        }
    }


    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }
}
