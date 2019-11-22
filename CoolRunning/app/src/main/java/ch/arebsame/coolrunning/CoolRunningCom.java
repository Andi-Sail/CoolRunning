package ch.arebsame.coolrunning;

/**
 * shared data across the application
 * to communicate
 */
public class CoolRunningCom {
    private static float speed;
    private static float averageSpeed;
    private static final int speedDelayLineLength = 10;
    private static int speedDelayLineIndex = 0;
    private static float speedDelayLine[] = new float[speedDelayLineLength];
    private static float targetSpeed;
    private static long runningTimeMs;
    private static RunningMode mode;
    private static State state;

    public static float getAverageSpeed() {
        return averageSpeed;
    }

    public synchronized static float getSpeed() {
        return speed;
    }

    public synchronized static void setSpeed(float speed) {
        CoolRunningCom.speed = speed;
        speedDelayLine[speedDelayLineIndex] = speed;
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

    public synchronized static long getRunningTimeMs() {
        return runningTimeMs;
    }

    public synchronized static void setRunningTimeMs(long runningTimeMs) {
        CoolRunningCom.runningTimeMs = runningTimeMs;
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
}
