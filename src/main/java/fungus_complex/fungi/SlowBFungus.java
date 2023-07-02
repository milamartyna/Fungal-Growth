package fungus_complex.fungi;

import fungus_complex.Config;
import fungus_complex.Point;
import fungus_complex.State;

public class SlowBFungus extends Fungus {
    public SlowBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = Config.SlowB.speed;
        isDominant = Config.SlowB.isDominant;
        acceptedFood = Config.SlowB.acceptedFood;

        preferredTemperature = Config.SlowB.preferredTemperature;
        preferredPH = Config.SlowB.preferredPH;
        isTemperatureSensitive = Config.SlowB.isTemperatureSensitive;
        isPHSensitive = Config.SlowB.isPHSensitive;
        applyEnvironmentEffects();
    }

    @Override
    public void createNewFungus(Point point) {
        new SlowBFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.SLOW_B;
    }
}
