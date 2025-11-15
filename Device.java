public abstract class Device {
    protected boolean on = false;

    public boolean isOn() {
        return on;
    }

    public synchronized void toggle() {
        on = !on;
    }

}
