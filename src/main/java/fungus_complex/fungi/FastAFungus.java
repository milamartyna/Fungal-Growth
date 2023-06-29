package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public class FastAFungus extends Fungus {
    public FastAFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 4;
        isDominant = false;
        acceptedFood = Food.ALPHA;
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
