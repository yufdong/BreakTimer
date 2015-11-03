package com.yufandong.breaktimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by YuFan on 10/4/15.
 */
public class AlarmStateManager {

    public enum State {
        Setup,
        WorkTimer,
        WorkAlarm,
        BreakTimer,
        BreakAlarm
    }

    private State currentState;
    private static AlarmStateManager instance;

    public static AlarmStateManager getAlarmStateManagerInstance() {
        if(instance == null)
            instance = new AlarmStateManager();

        return instance;
    }

    private AlarmStateManager() {
        currentState = State.Setup;
    }

    public void transitToNextState(Activity currentActivity) {
        currentState = getNextState();
        startCurrentStateActivity(currentActivity);
    }

    public void returnToSetup(Activity currentActivity) {
        currentState = State.Setup;
        startCurrentStateActivity(currentActivity);
    }

    public State getCurrentState() {
        return currentState;
    }

    private State getNextState() {
        switch(currentState) {
            case Setup:
            case BreakAlarm:
                if(getWorkTime() <= 0)
                    return State.BreakTimer;
                return State.WorkTimer;
            case WorkTimer:
                return State.WorkAlarm;
            case WorkAlarm:
                if(getBreakTime() <= 0)
                    return State.WorkTimer;
                return State.BreakTimer;
            case BreakTimer:
                return State.BreakAlarm;
            default:
                return null;
        }
    }

    private void startCurrentStateActivity(Activity currentActivity) {
        Intent intent = null;
        Bundle bundle = new Bundle();

        switch(currentState) {
            case Setup:
                intent = new Intent(currentActivity, TimerSetupActivity.class);
                break;
            case WorkTimer:
                intent = new Intent(currentActivity, TimerActivity.class);
                bundle.putLong("total time", getWorkTime());
                break;
            case WorkAlarm:
                intent = new Intent(currentActivity, AlarmActivity.class);
                bundle.putString("alarm message", currentActivity.getResources().getString(R.string.work_to_break_alarm_text));
                break;
            case BreakTimer:
                intent = new Intent(currentActivity, TimerActivity.class);
                bundle.putLong("total time", getBreakTime());
                break;
            case BreakAlarm:
                intent = new Intent(currentActivity, AlarmActivity.class);
                bundle.putString("alarm message", currentActivity.getResources().getString(R.string.break_to_work_alarm_text));
                break;
        }

        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }


    private long getWorkTime() {
        int hour = TimerSetupActivity.workHour;
        int min = TimerSetupActivity.workMin;
        return (hour * 3600 + min * 60) * 1000;
    }

    private long getBreakTime() {
        int hour = TimerSetupActivity.breakHour;
        int min = TimerSetupActivity.breakMin;
        return (hour * 3600 + min * 60) * 1000;
    }
}
