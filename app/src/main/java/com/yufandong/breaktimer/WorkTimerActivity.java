package com.yufandong.breaktimer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WorkTimerActivity extends ActionBarActivity {

    private long totalWorkTime; // in milliseconds
    private TextView timeTextView;
    private Bundle savedBundle;

    Runnable runnable;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_timer);
        savedBundle = getIntent().getExtras();

        int hour = savedBundle.getInt("Work Hour");
        int min = savedBundle.getInt("Work Minute");
        totalWorkTime = (hour * 3600 + min * 60) * 1000;

        timeTextView = (TextView) findViewById(R.id.timeRemaining);
        // Setting initial value fixes a bug where there is a delay before the text view appears
        timeTextView.setText(CountDownTimer.formatTimeToString((int) totalWorkTime));

        timer = new CountDownTimer(totalWorkTime);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startAlarm() {
        Intent intent = new Intent(this, WorkToBreakActivity.class);
        startActivity(intent);
    }
}
