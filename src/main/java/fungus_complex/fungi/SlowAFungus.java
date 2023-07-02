package fungus_complex.fungi;

import fungus_complex.Config;
import fungus_complex.Point;
import fungus_complex.State;

public class SlowAFungus extends Fungus {
    public SlowAFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = Config.SlowA.speed;
        isDominant = Config.SlowA.isDominant;
        acceptedFood = Config.SlowA.acceptedFood;

        preferredTemperature = Config.SlowA.preferredTemperature;
        preferredPH = Config.SlowA.preferredPH;
        isTemperatureSensitive = Config.SlowA.isTemperatureSensitive;
        isPHSensitive = Config.SlowA.isPHSensitive;
        applyEnvironmentEffects();
    }

    @Override
    public void createNewFungus(Point point) {
        new SlowAFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.SLOW_A;
    }
}
