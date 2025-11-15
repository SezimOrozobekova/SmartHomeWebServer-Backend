public class LightState extends Device {
    private String color;

    public LightState() {
        this.color = Colors.WHITE;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String newColor) {

        String value = newColor.toLowerCase();

        if (!value.equals(Colors.WHITE) &&
            !value.equals(Colors.RED) &&
            !value.equals(Colors.GREEN) &&
            !value.equals(Colors.BLUE)) {

            throw new IllegalArgumentException("Invalid color: " + newColor);
        }

        this.color = value;
    }

}
