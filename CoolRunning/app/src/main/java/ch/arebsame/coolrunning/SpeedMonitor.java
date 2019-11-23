package ch.arebsame.coolrunning;

public class SpeedMonitor
{
    private float toleranceTreashold = 1;

    float error;
    private RunningError runningError = RunningError.correct;

    private long totalComparisons = 0;
    private long badComparisons = 0;


    public SpeedMonitor()
    {
        error = 0.0f;
    }

    public void compareSpeed(float currentSpeed, float targetSpeed)
    {
        this.totalComparisons++;
        error = currentSpeed - targetSpeed;
        if (error <= -toleranceTreashold) {
            this.runningError = RunningError.tooSlow;
            this.badComparisons++;
        }
        else if (error >= toleranceTreashold) {
            this.runningError = RunningError.tooFast;
            this.badComparisons++;
        }
        else {
            this.runningError = RunningError.correct;
        }
    }

    public float getError()
    {
        return error;
    }

    public RunningError getRunningError() {
        return this.runningError;
    }

    public float getCurrentScore() {
        if (totalComparisons > 0) {
            return 100* (totalComparisons - badComparisons) / totalComparisons;
        }

        return 0;
    }
}
