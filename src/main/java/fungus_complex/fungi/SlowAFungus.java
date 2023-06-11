package fungus_complex.fungi;

import fungus_complex.Food;
import fungus_complex.Point;

public class SlowAFungus extends AbstractFungus {
    public SlowAFungus(Point occupiedPoint) {
        super(occupiedPoint);
        speed = 1;
        isDominant = true;
        acceptedFood = Food.ALPHA;
    }

    @Override
    public AbstractFungus createNewFungus(Point point) {
        return new SlowAFungus(point);
    }
}
