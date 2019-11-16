package ch.arebsame.coolrunning;

/**
 * shared data across the application
 * to communicate
 */
public class CoolRunningCom {
    private static float speed;
    private static float targetSpeed;
    private static long runningTimeMs;
    private static RunningMode mode;
    private static State state;

    public static float getSpeed() {
        return speed;
    }

    public static void setSpeed(float speed) {
        CoolRunningCom.speed = speed;
    }

    public static float getTargetSpeed() {
        return targetSpeed;
    }

    public static void setTargetSpeed(float targetSpeed) {
        CoolRunningCom.targetSpeed = targetSpeed;
    }

    public static long getRunningTimeMs() {
        return runningTimeMs;
    }

    public static void setRunningTimeMs(long runningTimeMs) {
        CoolRunningCom.runningTimeMs = runningTimeMs;
    }

    public static RunningMode getMode() {
        return mode;
    }

    public static void setMode(RunningMode mode) {
        CoolRunningCom.mode = mode;
    }

    public static State getState() {
        return state;
    }

    public static void setState(State state) {
        CoolRunningCom.state = state;
    }
}
