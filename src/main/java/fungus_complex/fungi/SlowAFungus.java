package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public class SlowAFungus extends Fungus {
    public SlowAFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 2;
        isDominant = true;
        acceptedFood = Food.ALPHA;

        preferredTemperature = 4;
//        isTemperatureSensitive = true;
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
