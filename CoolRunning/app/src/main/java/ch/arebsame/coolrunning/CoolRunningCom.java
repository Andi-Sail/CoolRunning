package ch.arebsame.coolrunning;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    final static DateFormat timeFormat = new SimpleDateFormat( "mm:ss") ;
    private static LinkedList<LatLng> positionList;
    private static String runName = "CoolRunningTrack";
    private static Boolean saveRun = false;

    public static Boolean getSaveRun() {
        return saveRun;
    }

    public static void setSaveRun(Boolean saveRun) {
        CoolRunningCom.saveRun = saveRun;
    }

    public static String getRunName() {
        return runName;
    }

    public static void setRunName(String runName) {
        CoolRunningCom.runName = runName;
    }

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
        if (CoolRunningCom.startTime == null) {
            CoolRunningCom.setStartTimeNow();
        }
        CoolRunningCom.runningTime = Duration.between(CoolRunningCom.startTime, Instant.now());
        return CoolRunningCom.runningTime;
    }

    public static Duration getRunningTime() {
        if (CoolRunningCom.runningTime != null) {
            return CoolRunningCom.runningTime;
        }
        return Duration.ZERO;
    }

    public static long getRunningTimeMillis() {
        if (CoolRunningCom.runningTime != null) {
            return CoolRunningCom.runningTime.toMillis();
        }
        return 0;
    }

    public static String getRunningTimeFormated() {
        long currentMillis = CoolRunningCom.getRunningTimeMillis();
        Date d = new Date(currentMillis);
        String timeString = timeFormat.format(d);
        return timeString;
    }

    public synchronized static void initPosList() {
        CoolRunningCom.positionList = new LinkedList<LatLng>();
    }

    public synchronized static void addPosition(LatLng pos) {
        if (CoolRunningCom.positionList != null && pos != null) {
            CoolRunningCom.positionList.add(pos);
        }
    }

    public synchronized static void addPosition(double lat, double lng) {
        CoolRunningCom.addPosition(new LatLng(lat, lng));
    }

    public static LinkedList<LatLng> getPositionList() {
        return CoolRunningCom.positionList;
    }
}
