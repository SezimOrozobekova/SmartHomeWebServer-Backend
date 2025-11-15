public class SmartHomeState {

    public LightState light;
    public KettleState kettle;
    public FridgeState fridge;

    public SmartHomeState(){
       light = new LightState();
       kettle = new KettleState();
       fridge = new FridgeState();
    }
}
