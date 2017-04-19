package com.kurama.nikhil.cpusimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        process_view_layout completed = (process_view_layout) findViewById(R.id.about_completed);
        process_view_layout running = (process_view_layout) findViewById(R.id.about_running);
        process_view_layout waiting = (process_view_layout) findViewById(R.id.about_waiting);

//        completed.setProcessState(2);
//        completed.setBackground();
//        running.setProcessState(1);
//        running.setBackground();
//        waiting.setProcessState(3);
//        running.setBackground();

        completed.setBackgroundResource(R.drawable.completed);
        running.setBackgroundResource(R.drawable.running);
        waiting.setBackgroundResource(R.drawable.waiting);


        final TextView credits = (TextView) findViewById(R.id.about_credits);
        credits.setLongClickable(true);
        credits.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                credits.setText("Dev ONLY by Nikhil :P");
                return false;
            }
        });
    }
}
