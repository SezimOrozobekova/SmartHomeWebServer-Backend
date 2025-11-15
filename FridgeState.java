public class FridgeState extends Device {

    private int temperature;

    public FridgeState() {
        this.temperature = FridgeConfig.DEFAULT_TEMP;
    }

    public int getTemperature() {
        return temperature;
    }

    public void increaseTemperature() {
        if (temperature < FridgeConfig.MAX_TEMP) {
            temperature++;
        }
    }

    public void decreaseTemperature() {
        if (temperature > FridgeConfig.MIN_TEMP) {
            temperature--;
        }
    }

    public void toggle() {
        on = !on;
    }
}
