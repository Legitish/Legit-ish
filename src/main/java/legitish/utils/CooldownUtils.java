package legitish.utils;

public class CooldownUtils {
    private long start;
    private long duration;

    public CooldownUtils(long duration) {
        this.duration = duration;
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public boolean hasFinished() {
        return System.currentTimeMillis() >= (start + duration);
    }

    public void setCooldown(long time) {
        this.duration = time;
    }

    public long getTimeLeft() {
        return duration - (System.currentTimeMillis() - start);
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - start;
    }
}