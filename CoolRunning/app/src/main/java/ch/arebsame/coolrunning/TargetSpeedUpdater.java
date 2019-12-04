package ch.arebsame.coolrunning;

/**
 * This class updates the target speed periodically according to the running Mode
 */
public class TargetSpeedUpdater {

    private boolean isRunning = false;
    private float startSpeed;
    private RunningMode mode;

    private Boolean intervalIsFast = true;

    private Thread updaterThread;

    public TargetSpeedUpdater()  {

        this.updaterThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    float nextTargetSpeed = calcNextTargetSpeed();
                    long nextInterval = calcNextInterval();
                    CoolRunningCom.setTargetSpeed(nextTargetSpeed);
                    // pass some time
                    try {
                        Thread.sleep(nextInterval*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * calculates the next target speed according to the running mode
     * @return the next target speed
     */
    private float calcNextTargetSpeed() {

        float nextSpeed = 0; // next Target speed in m/s

        switch (CoolRunningCom.getMode()) {
            case Interval:
                float intervalFast = 6;
                float intervalSlow = 3;
                if (intervalIsFast) {
                    nextSpeed = intervalSlow;
                    intervalIsFast = false;
                }
                else {
                    nextSpeed = intervalFast;
                    intervalIsFast = true;
                }
                break;
            case Random_Speed:
                nextSpeed = (float) (Math.random() * 5) + 3;
                break;
            case Constant_Speed:
                nextSpeed = CoolRunningCom.getTargetSpeed();
                break;
            case Increasing_Speed:
                nextSpeed = CoolRunningCom.getTargetSpeed() + (float) 0.1;
                break;
        }

        return nextSpeed;
    }

    /**
     * Calculates the time until the next update of the target speed
     * @return a time in seconds to wait until the next update
     */
    private long calcNextInterval() {
        long nextInterval = 1; // time to next target speed change in sec

        switch (CoolRunningCom.getMode()) {
            case Interval:
                if (intervalIsFast) nextInterval = 45;
                else nextInterval = 15;
                break;
            case Random_Speed:
                nextInterval = Math.round((Math.random() * 10) + 7);
                break;
            case Constant_Speed:
                nextInterval = 10;
                break;
            case Increasing_Speed:
                nextInterval = 10;
                break;
        }

        return nextInterval;
    }

    /**
     * starts a thread to update the target speed
     */
    public void Start() {
        this.isRunning = true;
        this.updaterThread.start();
    }

    /**
     * stops updating the target speed
     */
    public void Stop() {
        this.isRunning = false;
    }
}
