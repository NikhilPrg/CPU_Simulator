package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Nikhil on 17/04/17.
 */
public class analized_process_view_layout extends RelativeLayout {

    String process_name = "P1";
    double waitTime = 0;
    double turnAroundTime = 0;
    
    public analized_process_view_layout(Context context) {
        super(context);
        initializeViews(context);
    }

    public analized_process_view_layout(Context context, String p, int bt, int at) {
        super(context);
        process_name = p;
        waitTime = bt;
        turnAroundTime = at;
        initializeViews(context);
    }

    public analized_process_view_layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public analized_process_view_layout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analized_process_view_layout, this);
        setProcessNameAndTime();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
    public void setProcessNameAndTime() {
        TextView pro = (TextView) findViewById(R.id.analize_view_name);
        TextView wt = (TextView) findViewById(R.id.analize_view_waitTime);
        TextView tat = (TextView) findViewById(R.id.analize_view_turnArroundTime);

        pro.setText(process_name);
        wt.setText(Double.toString(waitTime));
        tat.setText(Double.toString(turnAroundTime));
        setBackgroundResource(R.drawable.square);
    }

}
