package fungus_complex.fungi;

import fungus_complex.Config;
import fungus_complex.Point;
import fungus_complex.State;

public class FastBFungus extends Fungus {
    public FastBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = Config.FastB.speed;
        isDominant = Config.FastB.isDominant;
        acceptedFood = Config.FastB.acceptedFood;

        preferredTemperature = Config.FastB.preferredTemperature;
        preferredPH = Config.FastB.preferredPH;
        isTemperatureSensitive = Config.FastB.isTemperatureSensitive;
        isPHSensitive = Config.FastB.isPHSensitive;
        applyEnvironmentEffects();
    }

    @Override
    public void createNewFungus(Point point) {
        new FastBFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.FAST_B;
    }
}
