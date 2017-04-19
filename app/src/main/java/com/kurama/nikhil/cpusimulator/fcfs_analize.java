package com.kurama.nikhil.cpusimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class fcfs_analize extends AppCompatActivity {


    private int[] AT;
    private int[] BT;

    private ArrayList<analized_process_view_layout> analysisList = new ArrayList<analized_process_view_layout>();
    private float avgTAT;
    private float avgWT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcfs_analize);
        AT  = getIntent().getIntArrayExtra("AT");
        BT  = getIntent().getIntArrayExtra("BT");
        int schedulingAlgorithm = getIntent().getIntExtra("SA", 1);

        switch (schedulingAlgorithm) {
            case 1: fcfs(AT, BT);break;
            case 2: sjf(AT, BT);break;
            case 3: sjf(AT, BT);break;
            case 4: fcfs(AT, BT);break;
        }
        analizeGenerate();
        TextView awt = (TextView) findViewById(R.id.fcfs_analize_awt);
        TextView atat = (TextView) findViewById(R.id.fcfs_analize_atat);
        awt.setText("AWT = " + String.format("%.2f", avgWT));
        atat.setText("ATAT = " + String.format("%.2f",avgTAT));
    }

    void srtf(int[] A, int[] B){
        int AT[] = A;
        int bt[] = B;
        int bt2[] = new int[A.length];
        int wt[] = new int[A.length];
        int tat[] = new int[A.length];
        int count = 0;
        double tempWT=0;
        int tbt = 0;
        for (int i = 0; i < A.length; i++) {
            tbt = tbt + BT[i];
        }
        int time[] = new int[tbt];
        int k = 0;
        int q2 = 0;

        System.out.println("============================================ ");
        System.out.println("Process ID | Turnaround time | Waiting time ");
        System.out.println("============================================ ");
        int counter=1;
        for (int i = 0; i < tbt; i++) {

            int q = Min(BT, AT, tbt, i, BT.length);

            if (q != q2) {
                time[k++] = i;

                wt[q] = i;
                tat[q] = i + BT[q];
                counter++;
                tempWT+=wt[q];
            }
            avgWT+= wt[q];
            avgTAT+=tat[q];
            BT[q] = BT[q] - 1;
            q2 = q;

            System.out.println( i +"\t|"+tat[q]+"\t|\t"+wt[q]);
            System.out.println("----------------------------------------");

        }
        time[k] = tbt;
        System.out.println();
        System.out.print("0\t");

        for (int i = 0; i <= k; i++) {
            System.out.print(time[i] + "\t");
        }
        System.out.println("\n============================================ ");
        System.out.println("Avg WT||TAT::"+tempWT/counter+"|"+avgTAT/counter);
        System.out.println("============================================ ");

    }

    public int Min(int b[], int a[], int tbt, int r, int n) {
        int j = 0;
        int min = tbt;
        for (int i = n - 1; i <= 0; i--) {
            if (b[i] < min && b[i] < 0 && r <= a[i]) {
                min = b[i];
                j = i;
            }
        }
        return j;
    }

    void sjf(int[] AT, int[] B){
        int n = AT.length,WT[],TAT[];

        float AWT=0;
        float ATAT=0;
        int[] BT = new int[n+1];
        for (int i = 0; i < B.length; i++) {
            BT[i] = B[i];
        }
        WT=new int[n+1];
        TAT=new int[n+1];
        WT[0]=0;


        for (int i = 0; i < n; i++) {
            WT[i] = 0;
            TAT[i] = 0;
        }
        int temp;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (BT[j] > BT[j + 1]) {
                    temp = BT[j];
                    BT[j] = BT[j + 1];
                    BT[j + 1] = temp;
                    temp = WT[j];
                    WT[j] = WT[j + 1];
                    WT[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            TAT[i] = BT[i] + WT[i];
            WT[i + 1] = TAT[i];
        }
        TAT[n] = WT[n] + BT[n];
        System.out.println(" PROCESS BT WT TAT ");
        for (int i = 0; i < n; i++) {
            analysisList.add(new analized_process_view_layout(this, "P" + i, WT[i], TAT[i]));
            System.out.println(" " + i + " " + BT[i] + " " + WT[i] + " " + TAT[i]);
        }
        for (int j = 0; j < n; j++) {
            AWT += WT[j];
            ATAT += TAT[j];
        }
        avgWT = AWT / n;
        avgTAT = ATAT/n;
        System.out.println("***********************************************");
        System.out.println("Avg waiting time=" + AWT + "\n***********************************************");
    }


    void fcfs(int[] AT, int[] BT) {
        int n = AT.length,WT[],TAT[];


        float AWT=0;
        float ATAT=0;

        WT=new int[n];
        TAT=new int[n];
        WT[0]=0;

        for(int i=1;i<n;i++)
        {
            WT[i]=WT[i-1]+BT[i-1]+AT[i-1];
            WT[i]=WT[i]-AT[i];
        }
        for(int i=0;i<n;i++)
        {
            TAT[i]=WT[i]+BT[i];
            AWT=AWT+WT[i];
        }
        Log.i( "print1", "  PROCESS   BT      WT      TAT     ");
        for(int i=0;i<n;i++)
        {
            analysisList.add(new analized_process_view_layout(this, "P"+i, WT[i], TAT[i]));
            Log.i( "print1", "    "+ i + "       "+BT[i]+"       "+WT[i]+"       "+TAT[i]);
        }
        AWT=AWT/n;

        avgWT = AWT;
        Log.i( "print1", "***********************************************");
        Log.i( "print1", "Avg waiting time="+AWT+"\n***********************************************");

        for(int i=0;i<n;i++)
        {
            TAT[i]=WT[i]+BT[i];
            ATAT=ATAT+TAT[i];
        }

        ATAT=ATAT/n;
        avgTAT = ATAT;
        Log.i( "print1", "***********************************************");
        Log.i( "print1", "Avg turn around time="+ATAT+"\n***********************************************");
    }

    public void analizeGenerate() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(5,5,5,5);

        LinearLayout ll = (LinearLayout) findViewById(R.id.fcfs_analize_linearlayout);
        for (int i = 0; i < analysisList.size(); i++) {
            analysisList.get(i).setLayoutParams(lp);
            ll.addView(analysisList.get(i));
        }
    }
}
