public class FridgeState extends Device {

    private int temperature;

    public FridgeState() {
        this.temperature = Config.DEFAULT_TEMP;
    }

    public int getTemperature() {
        return temperature;
    }

    public synchronized void increaseTemperature() {
        if (temperature < Config.MAX_TEMP) {
            temperature++;
        }
    }

    public synchronized void decreaseTemperature() {
        if (temperature > Config.MIN_TEMP) {
            temperature--;
        }
    }

    public static class Config {
        public static final int MIN_TEMP = 1;
        public static final int MAX_TEMP = 10;
        public static final int DEFAULT_TEMP = 5;
    }
}
