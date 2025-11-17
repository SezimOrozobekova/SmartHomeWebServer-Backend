public class LightState extends Device {

    private String color;

    public LightState() {
        this.color = Colors.WHITE;
    }

    public String getColor() {
        return color;
    }

    public synchronized void setColor(String newColor) {

        String value = newColor.toLowerCase();

        if (!value.equals(Colors.WHITE) &&
            !value.equals(Colors.RED) &&
            !value.equals(Colors.GREEN) &&
            !value.equals(Colors.BLUE)) {

            throw new IllegalArgumentException("Invalid color: " + newColor);
        }

        this.color = value;
    }

    public static class Colors {
        public static final String WHITE = "white";
        public static final String RED   = "red";
        public static final String GREEN = "green";
        public static final String BLUE  = "blue";

        private Colors() {}
    }
}
