package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public class FastBFungus extends AbstractFungus {
    public FastBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 4;
        isDominant = false;
        acceptedFood = Food.BETA;
    }

    @Override
    public AbstractFungus createNewFungus(Point point) {
        return new FastBFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.FAST_B;
    }
}
