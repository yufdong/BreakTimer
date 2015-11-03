package com.yufandong.breaktimer;

import android.os.Handler;

/**
 * Created by YuFan on 9/17/15.
 */
public class AnimationTimer {

    private long delay = 17;
    private long frameNum;

    private Handler handler;
    private AnimationRunnable runnable;

    public AnimationTimer() {
        frameNum = 0;
        handler = new Handler();
    }

    public AnimationTimer(long delay) {
        this();
        setDelay(delay);
    }

    public void startTimer() {
        handler.postDelayed(runnable, delay);
    }

    public void pauseTimer() {
        handler.removeCallbacks(runnable);
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
        frameNum = 0;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    public long getFrameNum() {
        return frameNum;
    }

    public void setRunnable(AnimationRunnable r) {
        runnable = r;
        r.setTimer(this);
    }

    static class AnimationRunnable implements Runnable {

        AnimationTimer timer;

        public AnimationRunnable() {
        }

        protected void setTimer(AnimationTimer t) {
            timer = t;
        }

        @Override
        public void run() {
            timer.handler.postDelayed(this, timer.getDelay());
            timer.frameNum++;
        }
    }

}
