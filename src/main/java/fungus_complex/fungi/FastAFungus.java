package fungus_complex.fungi;

import fungus_complex.Config;
import fungus_complex.Point;
import fungus_complex.State;

public class FastAFungus extends Fungus {
    public FastAFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = Config.FastA.speed;
        isDominant = Config.FastA.isDominant;
        acceptedFood = Config.FastA.acceptedFood;

        preferredTemperature = Config.FastA.preferredTemperature;
        preferredPH = Config.FastA.preferredPH;
        isTemperatureSensitive = Config.FastA.isTemperatureSensitive;
        isPHSensitive = Config.FastA.isPHSensitive;
        applyEnvironmentEffects();
    }

    @Override
    public void createNewFungus(Point point) {
        new FastAFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.FAST_A;
    }
}
