package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;

public class SlowBFungus extends AbstractFungus {
    public SlowBFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 1;
        isDominant = true;
        acceptedFood = Food.BETA;
    }

    @Override
    public AbstractFungus createNewFungus(Point point) {
        return new SlowBFungus(point);
    }
}
