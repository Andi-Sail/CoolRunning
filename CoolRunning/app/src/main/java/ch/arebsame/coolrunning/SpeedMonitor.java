package ch.arebsame.coolrunning;

public class SpeedMonitor
{
    float result;

    public SpeedMonitor()
    {
        result = 0.0f;
    }

    public void compareSpeed(float currentSpeed, float targetSpeed)
    {
        result = currentSpeed - targetSpeed;
    }

    public float getResult()
    {
        return result;
    }
}
