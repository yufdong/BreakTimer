package com.yufandong.breaktimer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


public class AlarmActivity extends Activity {

    private final long FLICKER_DELAY = 1000;
    private AnimationTimer animateTimer;
    private ViewGroup background;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_to_break);

        Bundle b = getIntent().getExtras();

        background = (ViewGroup) findViewById(R.id.work_to_break_background);
        message = (TextView) findViewById(R.id.work_to_break_message);
        message.setText(b.getString("alarm message"));

        animateTimer = new AnimationTimer(FLICKER_DELAY);

        animateTimer.setRunnable(
                new AnimationTimer.AnimationRunnable() {
                    @Override
                    public void run() {
                        super.run();
                        flickerBackground();
                    }
                }
        );
        animateTimer.startTimer();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        flickerBackground(); // Called once to set the initial color
    }

    public void stopAlarm(View view) {
        AlarmStateManager.getAlarmStateManagerInstance().transitToNextState(this);
    }

    private void flickerBackground() {
        int firstColor = Color.BLACK;
        int secondColor = Color.WHITE;
        int firstInvert = 0xFFFFFFFF - firstColor + 0xFF000000;
        int secondInvert = 0xFFFFFFFF - secondColor + 0xFF000000;

        if(animateTimer.getFrameNum() % 2 == 0) {
            background.setBackgroundColor(firstColor);
            message.setTextColor(firstInvert);
        }
        else {
            background.setBackgroundColor(secondColor);
            message.setTextColor(secondInvert);
        }

    }
}
