package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class FCFS extends AppCompatActivity {

    TextView timerTextView;
    long startTime = 0;
    long millis = -1;
    LinearLayout lastInnerLayout;
    LinearLayout lastOuterLayout;
    int numOfViewsInLastRow, numOfViewInEveryRow;

    ArrayList<process_view_layout> processList = new ArrayList<process_view_layout>();
    ArrayList<process_view_layout> originalProcessList = new ArrayList<process_view_layout>();

    ArrayList<analized_process_view_layout> analysisList = new ArrayList<analized_process_view_layout>();
    float avgWT, avgTAT;
    EditText burstTimeEt, arrivalTimeEt;
    int runningID = -1;

    LinearLayout runningLayout;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    private process_view_layout tempView;
    Runnable timerRunnable = new Runnable() {
        int seconds = 0;
        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
//            int seconds = (int) (millis / 1000);
            seconds++;
            timerTextView = (TextView) findViewById(R.id.timer_tv);
            timerTextView.setText(String.format("%04d", seconds));
            if(runningID != -1) {
                processList.get(runningID).subBurstTime();
            }
            if(isProcessPending()) {
                fcfs();
                reduceArrivalTime();
                try {
                    tempView.setProcessNameAndTime(processList.get(runningID).getProcess_name(), processList.get(runningID).getBurstTime());
                }catch (Exception e){

                }
                timerHandler.postDelayed(this, 1000);
            }else {
                timerHandler.removeCallbacks(timerRunnable);
                Button b = (Button) findViewById(R.id.button);
                b.setText("start");
                b = (Button) findViewById(R.id.fcfs_btn_analize);
                b.setVisibility(Button.VISIBLE);
                tempView.setProcessNameAndTime("CPU",0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcfs);

        Button b = (Button) findViewById(R.id.button);
        runningLayout = (LinearLayout) findViewById(R.id.fcfs_running);
        tempView = new process_view_layout(this,"CPU" ,0, 0, 1);
        runningLayout.addView(tempView);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if(isProcessPending()) {
                    if (b.getText().equals("stop")) {
                        timerHandler.removeCallbacks(timerRunnable);
                        b.setText("start");
                    } else {
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        b.setText("stop");
                    }
                }
            }
        });
        burstTimeEt = (EditText) findViewById(R.id.fcfs_burstTime_textView);
        arrivalTimeEt = (EditText) findViewById(R.id.fcfs_arrivalTime_textView);
        createTempProcesses();
        createProcessViews();
    }

    private void createTempProcesses(){
        processList.add(new process_view_layout(this, "P0", 10 , 4));
        processList.add(new process_view_layout(this, "P1", 5 , 2));
        processList.add(new process_view_layout(this, "P2", 6 , 3));
        processList.add(new process_view_layout(this, "P3", 3 , 2));
        processList.add(new process_view_layout(this, "P4", 7 , 1));
        processList.add(new process_view_layout(this, "P5", 4 , 1));
        processList.add(new process_view_layout(this, "P6", 3 , 4));
        processList.add(new process_view_layout(this, "P7", 5 , 6));
        processList.add(new process_view_layout(this, "P8", 2 , 2));

        originalProcessList.add(new process_view_layout(this, "P0", 10 , 4));
        originalProcessList.add(new process_view_layout(this, "P1", 5 , 2));
        originalProcessList.add(new process_view_layout(this, "P2", 6 , 3));
        originalProcessList.add(new process_view_layout(this, "P3", 3 , 2));
        originalProcessList.add(new process_view_layout(this, "P4", 7 , 1));
        originalProcessList.add(new process_view_layout(this, "P5", 4 , 1));
        originalProcessList.add(new process_view_layout(this, "P6", 3 , 4));
        originalProcessList.add(new process_view_layout(this, "P7", 5 , 6));
        originalProcessList.add(new process_view_layout(this, "P8", 2 , 2));
    }

    public void createProcessViews(){
        //Calculating Number of Rows and Columns to fit in screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        final View contentView = this.findViewById(R.id.contentPanel);
        final int paddingLeftRight = contentView.getPaddingLeft() + contentView.getPaddingRight();

        int processesPerRow = (width - paddingLeftRight)/300;
        int total = processList.size();
        int y = total/processesPerRow;
        int numberOfRows = (y > 0) ? y : 1;
        int processesInLastRow = total%processesPerRow;
        numOfViewInEveryRow = processesPerRow;
        numOfViewsInLastRow = processesInLastRow;

        final LinearLayout outerLayout = (LinearLayout) findViewById(R.id.linerLayout1);
        lastOuterLayout = outerLayout;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
        params.setMargins(5,5,5,5);

        //Deleting previous entries
        if(outerLayout.getChildCount() > 0) {
            outerLayout.removeAllViews();
        }

        int k = 0;
        for(int j = 0; j < numberOfRows; j++) {
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
            for(int i = 0; i < processesPerRow; i++) {
                process_view_layout tempP = processList.get(k);
                k++;
                tempP.setLayoutParams(params);
                innerLayout.addView(tempP);
            }
            outerLayout.addView(innerLayout);
        }

        //Last Row
        lastInnerLayout = (LinearLayout)new LinearLayout(this);
        lastInnerLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < processesInLastRow ; i++) {
            process_view_layout tempP = processList.get(k);
            k++;
            tempP.setLayoutParams(params);
            lastInnerLayout.addView(tempP);
        }

        outerLayout.addView(lastInnerLayout);
    }

    public void add(View view) {
        int burstTime = 0, arrivalTime = 0;

        try {
            burstTime = Integer.parseInt(burstTimeEt.getText().toString());
            arrivalTime = Integer.parseInt(arrivalTimeEt.getText().toString());
        } catch (Exception e ){
            Log.e("Add", "Input parsing error");
        }

        //Hiding keyboard onClick
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //Creating new process_view
        processList.add(new process_view_layout(this, "P" + processList.size(), burstTime, arrivalTime));
        originalProcessList.add(new process_view_layout(this, "P" + processList.size(), burstTime, arrivalTime));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
        params.setMargins(5,5,5,5);
        process_view_layout temp = processList.get(processList.size()-1);
        temp.setLayoutParams(params);

        if(numOfViewInEveryRow > numOfViewsInLastRow) {
            lastInnerLayout.addView(temp);
        }else {
            numOfViewsInLastRow = 0;
            lastInnerLayout = (LinearLayout)new LinearLayout(this);
            lastInnerLayout.setOrientation(LinearLayout.HORIZONTAL);
            lastInnerLayout.addView(temp);
            lastOuterLayout.addView(lastInnerLayout);
        }
        numOfViewsInLastRow++;
        burstTimeEt.setText("");
        arrivalTimeEt.setText("");
    }

    public void reduceArrivalTime() {
        for (int i = 0; i < processList.size(); i++) {
            processList.get(i).subArrivalTime();
        }
    }

    LinkedList<Integer> fcfsQueue = new LinkedList<Integer>();
    public void fcfs() {
        for (int i = 0; i < processList.size(); i++) {
            if(processList.get(i).getArrivalTime() == 0) {
                fcfsQueue.add(i);
            }
        }

        try {
            runningID = fcfsQueue.peek();

            if (processList.get(runningID).getBurstTime() <= 1) {
                fcfsQueue.pop();
            }

        }catch (Exception e){
            Log.v("queueExp", "Empty");
        }
    }

    public boolean isProcessPending() {
        boolean flag = false;
        for (int i = 0; i < processList.size(); i++) {
            if(processList.get(i).getBurstTime() > 1){
                flag = true;
                Log.w("pending", "Yes");
                return flag;
            }
        }
        Log.w("pending", "No");
        processList.get(runningID).subBurstTime();
        return flag;
    }

    void analizeCaller(View view){
        int l =  originalProcessList.size();
        int[] AT = new int[l];
        int[] BT = new int[l];
        for (int i = 0; i < l; i++) {
            AT[i] = originalProcessList.get(i).getArrivalTime();
            BT[i] = originalProcessList.get(i).getBurstTime();
        }

        Intent i = new  Intent(this, fcfs_analize.class);
        i.putExtra("AT", AT);
        i.putExtra("BT", BT);
        startActivity(i);
    }
}
