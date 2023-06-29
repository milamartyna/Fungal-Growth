package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public class SlowBFungus extends Fungus {
    public SlowBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 1;
        isDominant = true;
        acceptedFood = Food.BETA;
    }

    @Override
    public Fungus createNewFungus(Point point) {
        return new SlowBFungus(point);
    }

    @Override
    public State getCorrelatedState() {
        return State.SLOW_B;
    }
}
