package ch.arebsame.coolrunning;

/**
 * this class handles the comparisons of the current speed and the target speed
 * It keeps count of how often the user was not correct and calculates a score at the end
 */
public class SpeedMonitor
{
    // currents speed needs to be in targetSpeed +- toleranceTreashold
    private float toleranceTreashold = 1;

    float error;
    private RunningError runningError = RunningError.correct;

    private long totalComparisons = 0;
    private long badComparisons = 0;


    public SpeedMonitor()
    {
        error = 0.0f;
    }

    /**
     * Compares the currentSpeed and target Speed
     * @param currentSpeed the current GPS speed
     * @param targetSpeed the current target speed
     */
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

    /**
     * @return how much the user is off of the target speed
     */
    public float getError()
    {
        return error;
    }

    /**
     * @return the error if too fast, too slow or correct
     */
    public RunningError getRunningError() {
        return this.runningError;
    }

    /**
     * @return the current score | 0 = all bad, 100 = perfect run
     */
    public float getCurrentScore() {
        if (totalComparisons > 0) {
            return 100* (totalComparisons - badComparisons) / totalComparisons;
        }

        return 0;
    }
}
