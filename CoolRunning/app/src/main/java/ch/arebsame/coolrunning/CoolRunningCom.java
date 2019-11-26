package ch.arebsame.coolrunning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * shared data across the application
 * to communicate
 */
public class CoolRunningCom {
    private static float speed;
    private static float averageSpeed;
    private static final int speedDelayLineLength = 30;
    private static int speedDelayLineIndex = 0;
    private static float speedDelayLine[] = new float[speedDelayLineLength];
    private static float targetSpeed;
    private static RunningMode mode;
    private static State state;
    private static RunningError runningError;
    private static float score;
    private static Instant startTime;
    private static Duration runningTime;
    final static DateFormat timeFormat = new SimpleDateFormat( "h:mm:ss") ;


    public static RunningError getRunningError() {
        return runningError;
    }

    public synchronized static void setRunningError(RunningError runningError) {
        CoolRunningCom.runningError = runningError;
    }

    public static float getAverageSpeed() {
        return averageSpeed;
    }

    public synchronized static float getSpeed() {
        return speed;
    }

    public synchronized static void setSpeed(float speed) {
        CoolRunningCom.speed = speed;
        speedDelayLine[speedDelayLineIndex] = speed;
        speedDelayLineIndex++;
        if (speedDelayLineIndex >= speedDelayLineLength) {
            speedDelayLineIndex = 0;
        }
        float sum = 0;
        for (float s : speedDelayLine) {
            sum += s;
        }
        averageSpeed = sum / speedDelayLineLength;
    }

    public synchronized static float getTargetSpeed() {
        return targetSpeed;
    }

    public synchronized static void setTargetSpeed(float targetSpeed) {
        CoolRunningCom.targetSpeed = targetSpeed;
    }

    public synchronized static RunningMode getMode() {
        return mode;
    }

    public synchronized static void setMode(RunningMode mode) {
        CoolRunningCom.mode = mode;
    }

    public synchronized static State getState() {
        return state;
    }

    public synchronized static void setState(State state) {
        CoolRunningCom.state = state;
    }

    public synchronized static float getScore() {
        return score;
    }

    public synchronized static void setScore(float score) {
        if (score < 0) {
            CoolRunningCom.score = 0;
        }
        else if (score > 100) {
            CoolRunningCom.score = 100;
        }
        else {
            CoolRunningCom.score = score;
        }
    }

    public synchronized static void setStartTimeNow() {
        CoolRunningCom.startTime = Instant.now();
    }

    public synchronized static Duration updateRunningTime() {
        CoolRunningCom.runningTime = Duration.between(CoolRunningCom.startTime, Instant.now());
        return CoolRunningCom.runningTime;
    }

    public static Duration getRunningTime() {
        return CoolRunningCom.runningTime;
    }

    public static long getRunningTimeMillis() {
        return CoolRunningCom.runningTime.toMillis();
    }

    public static String getRunningTimeFormated() {
        long currentMillis = CoolRunningCom.getRunningTimeMillis();
        Date d = new Date(currentMillis);
        String timeString = timeFormat.format(d);
        return timeString;
    }
}
