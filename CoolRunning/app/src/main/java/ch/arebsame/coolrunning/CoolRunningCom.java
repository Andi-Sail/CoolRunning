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

    public synchronized static float getSpeed() {
        return speed;
    }

    public synchronized static void setSpeed(float speed) {
        CoolRunningCom.speed = speed;
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
