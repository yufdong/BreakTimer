package com.yufandong.breaktimer;
import android.os.Handler;

public class CountDownTimer {

    private final int DELAY = 100;

    private long totalTime, systemStartTime, timeRemaining; // in milliseconds
    private String timeText;
    private Handler handler;
    private Runnable runnable;

    /**
     * Constructs a new timer that counts down
     */
    public CountDownTimer() {
        this(0);
    }

    /**
     * Constructs a new timer that counts down from a specified time to zero
     */
    public CountDownTimer(long time) {
        handler = new Handler();
        totalTime = time;
    }

    /**
     * Start or resume the count down timer
     */
    public void startTimer() {
        systemStartTime = System.currentTimeMillis();
        handler.postDelayed(runnable, DELAY);
    }

    /**
     * Pause the count down timer
     */
    public void pauseTimer() {
        handler.removeCallbacks(runnable);
        totalTime = timeRemaining;
    }

    /**
     * Stop the timer and reset the count down to a specified time
     * @param time The new time remaining
     */
    public void stopTimer(long time) {
        handler.removeCallbacks(runnable);
        totalTime = time;
    }

    /**
     * Get the time remaining in text format
     * @return The time in the format HH:MM:SS
     */
    public String getTimeText() {
        return timeText;
    }

    /**
     * Get the time remaining in integer
     * @return The time remaining
     */
    public long getTimeRemaining() {
        return timeRemaining;
    }

    public static String formatTimeToString(int time) {
        int seconds = time / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void setRunnable(Runnable r) {
        runnable = r;
    }

    /**
     * The runnable class to contains the method that executes at every interval. Overwrite the
     * run() method to customize what is executed at every interval. When an object of
     * CountDownRunnable is instantiated, it will automatically link itself with the  CountDownTimer
     * that is passed in.
     */
    static class CountDownRunnable implements Runnable {

        CountDownTimer timer;

        /**
         * Construct a runnable to be used by CountDownTimer
         * @param t A count down timer to be linked with this runnable.
         */
        public CountDownRunnable(CountDownTimer t) {
            timer = t;
            timer.setRunnable(this);
        }

        /**
         * Specifies what gets executed at every interval. The base method updates time remaining
         * in both integer and String format.
         */
        @Override
        public void run() {
            timer.timeRemaining = timer.systemStartTime + timer.totalTime - System.currentTimeMillis();
            timer.timeText = CountDownTimer.formatTimeToString((int) timer.timeRemaining);
            timer.handler.postDelayed(this, timer.DELAY);
        }
    }

}
