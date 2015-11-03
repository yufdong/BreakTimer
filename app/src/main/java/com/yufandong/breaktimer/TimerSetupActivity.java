package com.yufandong.breaktimer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TimerSetupActivity extends ActionBarActivity {

    public static int workHour, workMin, breakHour, breakMin;

    private static TextView workTimeText;
    private static TextView breakTimeText;
    private Bundle dialogBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recover data from re-rendering upon rotated screen
        if(savedInstanceState != null) {
            workHour = savedInstanceState.getInt("Work Hour");
            workMin = savedInstanceState.getInt("Work Minute");
            breakHour = savedInstanceState.getInt("Break Hour");
            breakMin = savedInstanceState.getInt("Break Minute");
        }

        setContentView(R.layout.activity_timer_setup);

        workTimeText = (TextView) findViewById(R.id.work_time_text);
        breakTimeText = (TextView) findViewById(R.id.break_time_text);
        setTimeText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save data to be recovered upon re-rendering
        outState.putInt("Work Hour", workHour);
        outState.putInt("Work Minute", workMin);
        outState.putInt("Break Hour", breakHour);
        outState.putInt("Break Minute", breakMin);
    }

    public void startWorkTimer(View view) {
        AlarmStateManager.getAlarmStateManagerInstance().transitToNextState(this);
    }

    public void openWorkTimePickerDialog(View view) {
        dialogBundle = new Bundle();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(dialogBundle,
                getString(R.string.work_time_picker_dialog_title), workHour, workMin);
        timePickerDialog.setOnDialogDismissListener(new TimePickerDialog.OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                fetchWorkTimeFromDialog();
            }
        });
        timePickerDialog.show(getFragmentManager(), getString(R.string.work_time_picker_dialog_title));
    }

    public void openBreakTimePickerDialog(View view) {
        dialogBundle = new Bundle();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(dialogBundle,
                getString(R.string.break_time_picker_dialog_title), breakHour, breakMin);
        timePickerDialog.setOnDialogDismissListener(new TimePickerDialog.OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                fetchBreakTimeFromDialog();
            }
        });
        timePickerDialog.show(getFragmentManager(), getString(R.string.break_time_picker_dialog_title));
    }


    private void fetchWorkTimeFromDialog() {
        workHour = dialogBundle.getInt("Hour");
        workMin = dialogBundle.getInt("Minute");
        setTimeText();
    }

    private void fetchBreakTimeFromDialog() {
        breakHour = dialogBundle.getInt("Hour");
        breakMin = dialogBundle.getInt("Minute");
        setTimeText();
    }

    private void setTimeText() {
        String workTime = String.format("%dh:%02dm", workHour, workMin);
        workTimeText.setText(workTime);
        String breakTime = String.format("%dh:%02dm", breakHour, breakMin);
        breakTimeText.setText(breakTime);
    }
}
