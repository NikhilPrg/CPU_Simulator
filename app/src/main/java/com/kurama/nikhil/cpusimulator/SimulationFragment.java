package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class SimulationFragment extends Fragment {


    int schedulingAlgorithm = 1;
    SharedPreferences setting;
    /*
     KEY:
        FCFS = 1
        SJF = 2
        SRTF = 3
        RR = 4
        PQ = 5
     */
    int preId ;
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
    int speed = 100;

    LinearLayout runningLayout;

    public static SimulationFragment newInstance(String text) {

        SimulationFragment f = new SimulationFragment();
        return f;
    }

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    private process_view_layout tempView;
    Runnable timerRunnable = new Runnable() {
        int seconds = 0;
        @Override
        public void run() {
            seconds++;
            timerTextView = (TextView) getView().findViewById(R.id.timer_tv);
            timerTextView.setText(String.format("%04d", seconds));
            if(isProcessPending()) {
                //DecideSchedulingAlgo
                switch (schedulingAlgorithm) {
                    case 1: FCFS();break;
                    case 2: SJF();break;
                    case 3: SRTF();break;
                    case 4: roundRobin();break;
                    default: FCFS();break;
                }

                if(runningID != -1) {
                    processList.get(runningID).subBurstTime();
                }
                reduceArrivalTime();
                try {
                    tempView.setProcessNameAndTime(processList.get(runningID).getProcess_name(), processList.get(runningID).getBurstTime());
                }catch (Exception e){
                    Log.e("SetCPU", "Error");
                }
                timerHandler.postDelayed(this, speed);
            }else {
                timerHandler.removeCallbacks(timerRunnable);
                Button b = (Button) getView().findViewById(R.id.button);
                b.setText("start");
                b = (Button) getView().findViewById(R.id.fcfs_btn_analize);
                b.setVisibility(Button.VISIBLE);
                tempView.setProcessNameAndTime("CPU",0);
            }
        }
    };

    private RelativeLayout ll;
    private FragmentActivity fa;
    ProcessRecordDataSupply dataSupply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fa = super.getActivity();
        ll = (RelativeLayout) inflater.inflate(R.layout.fragment_simulation, container, false);

        setting = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        speed = setting.getInt("speed", 100);
        Button startButton = (Button) ll.findViewById(R.id.button);
        runningLayout = (LinearLayout) ll.findViewById(R.id.fcfs_running);
        tempView = new process_view_layout(super.getContext(),"CPU" ,0, 0, 1);
        runningLayout.addView(tempView);
        startButton.setText("start");
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button startButton = (Button) v;
                if(isProcessPending()) {
                    if (startButton.getText().equals("stop")) {
                        timerHandler.removeCallbacks(timerRunnable);
                        startButton.setText("start");
                    } else {
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        startButton.setText("stop");
                    }
                }
            }
        });
        burstTimeEt = (EditText) ll.findViewById(R.id.fcfs_burstTime_textView);
        arrivalTimeEt = (EditText) ll.findViewById(R.id.fcfs_arrivalTime_textView);

        Button addButton = (Button) ll.findViewById(R.id.fcfs_add);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                add(v);
            }
        });

        Button b3 = (Button) ll.findViewById(R.id.fcfs_btn_analize);
        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                analizeCaller(v);
            }
        });

        SharedPreferences settings = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        preId = settings.getInt("selectedPreset", 1);
//        createTempProcesses();
        dataSupply = new ProcessRecordDataSupply(getContext());
        ArrayList<Process> pAL = dataSupply.getPreset(preId);

        for (int i = 0; i < pAL.size(); i++) {
            processList.add(new process_view_layout(super.getContext(), pAL.get(i)));
            originalProcessList.add(new process_view_layout(super.getContext(), pAL.get(i)));
        }

        createProcessViews();
        return ll;
    }



    private void createTempProcesses() {
        processList.add(new process_view_layout(super.getContext(), "P0", 10 , 4));
        processList.add(new process_view_layout(super.getContext(), "P1", 5 , 2));
        processList.add(new process_view_layout(super.getContext(), "P2", 6 , 3));
        processList.add(new process_view_layout(super.getContext(), "P3", 3 , 2));
        processList.add(new process_view_layout(super.getContext(), "P4", 7 , 1));
        processList.add(new process_view_layout(super.getContext(), "P5", 4 , 1));
        processList.add(new process_view_layout(super.getContext(), "P6", 3 , 4));
        processList.add(new process_view_layout(super.getContext(), "P7", 5 , 6));
        processList.add(new process_view_layout(super.getContext(), "P8", 2 , 2));

        originalProcessList.add(new process_view_layout(super.getContext(), "P0", 10 , 4));
        originalProcessList.add(new process_view_layout(super.getContext(), "P1", 5 , 2));
        originalProcessList.add(new process_view_layout(super.getContext(), "P2", 6 , 3));
        originalProcessList.add(new process_view_layout(super.getContext(), "P3", 3 , 2));
        originalProcessList.add(new process_view_layout(super.getContext(), "P4", 7 , 1));
        originalProcessList.add(new process_view_layout(super.getContext(), "P5", 4 , 1));
        originalProcessList.add(new process_view_layout(super.getContext(), "P6", 3 , 4));
        originalProcessList.add(new process_view_layout(super.getContext(), "P7", 5 , 6));
        originalProcessList.add(new process_view_layout(super.getContext(), "P8", 2 , 2));
    }

    public void createProcessViews(){
        //Calculating Number of Rows and Columns to fit in screen
        Display display = super.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        final View contentView = this.ll.findViewById(R.id.contentPanel);
        final int paddingLeftRight = contentView.getPaddingLeft() + contentView.getPaddingRight();

        int processesPerRow = (width - paddingLeftRight)/300;
        int total = processList.size();
        int y = total/processesPerRow;
        int numberOfRows = (y > 0) ? y : 1;
        int processesInLastRow = total%processesPerRow;
        numOfViewInEveryRow = processesPerRow;
        numOfViewsInLastRow = processesInLastRow;

        final LinearLayout outerLayout = (LinearLayout) ll.findViewById(R.id.linerLayout1);
        lastOuterLayout = outerLayout;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
        params.setMargins(5,5,5,5);

        //Deleting previous entries
        if(outerLayout.getChildCount() > 0) {
            outerLayout.removeAllViews();
        }

        int k = 0;
        for(int j = 0; j < numberOfRows; j++) {
            LinearLayout innerLayout = new LinearLayout(super.getContext());
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
        lastInnerLayout = (LinearLayout)new LinearLayout(super.getContext());
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
            return;
        }

        //Hiding keyboard onClick
        InputMethodManager inputManager = (InputMethodManager)
                super.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(super.getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //Creating new process_view
        processList.add(new process_view_layout(super.getContext(), "P" + processList.size(), burstTime, arrivalTime));
        originalProcessList.add(new process_view_layout(super.getContext(), "P" + processList.size(), burstTime, arrivalTime));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
        params.setMargins(5,5,5,5);
        process_view_layout temp = processList.get(processList.size()-1);
        temp.setLayoutParams(params);

        if(numOfViewInEveryRow > numOfViewsInLastRow) {
            lastInnerLayout.addView(temp);
        }else {
            numOfViewsInLastRow = 0;
            lastInnerLayout = (LinearLayout)new LinearLayout(super.getContext());
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
    public void FCFS() {
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

    void SJF() {
        for (int i = 0; i < processList.size(); i++) {
            if(processList.get(i).getArrivalTime() == 0) {
                fcfsQueue.add(i);
            }
        }
        Collections.sort(fcfsQueue, new Comparator<Integer>(){
            @Override
            public int compare(Integer integer, Integer t1) {
                if(originalProcessList.get(integer).getBurstTime() < originalProcessList.get(t1).getBurstTime()){
                    return -1;
                }
                if(originalProcessList.get(integer).getBurstTime() > originalProcessList.get(t1).getBurstTime()){
                    return 1;
                }
                return 0;
            }
        });
        try {
            runningID = fcfsQueue.peek();

            if (processList.get(runningID).getBurstTime() <= 1) {
                fcfsQueue.pop();
            }

        }catch (Exception e){
            Log.v("queueExp", "Empty");
        }
    }

    void SRTF() {
        for (int i = 0; i < processList.size(); i++) {
            if(processList.get(i).getArrivalTime() == 0) {
                fcfsQueue.add(i);
            }
        }
        Collections.sort(fcfsQueue, new Comparator<Integer>(){
            @Override
            public int compare(Integer integer, Integer t1) {
                if(processList.get(integer).getBurstTime() < processList.get(t1).getBurstTime()){
                    return -1;
                }
                if(processList.get(integer).getBurstTime() > processList.get(t1).getBurstTime()){
                    return 1;
                }
                return 0;
            }
        });
        try {
            runningID = fcfsQueue.peek();

            if (processList.get(runningID).getBurstTime() <= 1) {
                fcfsQueue.pop();
            }

        }catch (Exception e){
            Log.v("queueExp", "Empty");
        }
    }
    int prev = -1;
    LinkedList<Integer> poppedProcessesIDs = new LinkedList<Integer>();
    public void roundRobin() {
        for (int i = 0; i < processList.size(); i++) {
            if(processList.get(i).getArrivalTime() == 0 && processList.get(i).getBurstTime() > 0) {
                fcfsQueue.add(i);
            }
        }

        try {
            printFcfsQueue();
            runningID = fcfsQueue.peek();
            fcfsQueue.pop();
                if(processList.get(runningID).getBurstTime() > 1){
                    fcfsQueue.add(runningID);
                }
            printFcfsQueue();
        }catch (Exception e){
            Log.v("queueExp", "Empty");
        }


    }

    private void printFcfsQueue() {
        String print = "";
        for (int i = 0; i < fcfsQueue.size(); i++) {
            print += fcfsQueue.get(i) + ", ";
        }

        Log.v("Round Robin Queue", print);
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
        try {
            processList.get(runningID).subBurstTime();
        }catch (Exception e){

        }
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
        Intent i = new  Intent(super.getContext(), fcfs_analize.class);
        i.putExtra("AT", AT);
        i.putExtra("BT", BT);
        i.putExtra("SA", schedulingAlgorithm);
        startActivity(i);
    }

    public void setSchedulingAlgorithm(int schedulingAlgorithm) {
        this.schedulingAlgorithm = schedulingAlgorithm;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
