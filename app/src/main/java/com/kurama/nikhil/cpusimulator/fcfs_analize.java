package com.kurama.nikhil.cpusimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        analize(AT, BT);
        analizeGenerate();
        TextView awt = (TextView) findViewById(R.id.fcfs_analize_awt);
        TextView atat = (TextView) findViewById(R.id.fcfs_analize_atat);
        awt.setText("AWT = " + String.format("%.2f", avgWT));
        atat.setText("ATAT = " + String.format("%.2f",avgTAT));
    }



    void analize(int[] AT, int[] BT) {
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
