package legitish.utils;

public class CooldownUtils {
    private long start;
    private long duration;
    private boolean checkedFinish;

    public CooldownUtils(long duration) {
        this.duration = duration;
    }

    public void start() {
        this.start = System.currentTimeMillis();
        checkedFinish = false;
    }

    public boolean hasFinished() {
        return System.currentTimeMillis() >= (start + duration);
    }

    public boolean firstFinish() {
        if ((System.currentTimeMillis() >= (start + duration)) && !checkedFinish) {
            checkedFinish = true;
            return true;
        }
        return false;
    }

    public void setCooldown(long time) {
        this.duration = time;
    }

    public long getCooldownTime() {
        return duration;
    }

    public long getElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - this.start;
        return Math.min(elapsedTime, duration);
    }

    public long getTimeLeft() {
        long timeLeft = duration - (System.currentTimeMillis() - start);
        return timeLeft < 0 ? 0 : timeLeft;
    }
}