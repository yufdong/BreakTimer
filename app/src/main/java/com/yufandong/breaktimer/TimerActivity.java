package com.yufandong.breaktimer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends ActionBarActivity {

    private long totalTime; // in milliseconds
    private TextView timeTextView;

    Runnable runnable;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_timer);

        Bundle b = getIntent().getExtras();
        totalTime = b.getLong("total time");

        timeTextView = (TextView) findViewById(R.id.timeRemaining);
        // Setting initial value fixes a bug where there is a delay before the text view appears
        timeTextView.setText(CountDownTimer.formatTimeToString((int) totalTime));

        timer = new CountDownTimer(totalTime);
        runnable = new CountDownTimer.CountDownRunnable(timer) {
            @Override
            public void run() {
                super.run();
                timeTextView.setText(timer.getTimeText());
                if (timer.getTimeRemaining() <= 0) {
                    startAlarm();
                }
            }
        };
        timer.startTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pauseOrStart(View view) {
        Button button = (Button) view;
        String pause = getResources().getString(R.string.pause_timer_button_text);
        String start = getResources().getString(R.string.restart_timer_button_text);
        if(button.getText().equals(pause)){
            button.setText(start);
            timer.pauseTimer();
        } else {
            button.setText(pause);
            timer.startTimer();
        }
    }

    public void stopTimer(View view) {
        timer.pauseTimer();
        AlarmStateManager.getAlarmStateManagerInstance().returnToSetup(this);
    }

    private void startAlarm() {
        timer.pauseTimer();
        AlarmStateManager.getAlarmStateManagerInstance().transitToNextState(this);
    }

    // test method
    public void testStartAlarm(View view) {
        startAlarm();
    }
}
