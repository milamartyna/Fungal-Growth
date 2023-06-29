package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public class SlowBFungus extends Fungus {
    public SlowBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 2;
        isDominant = true;
        acceptedFood = Food.BETA;
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
