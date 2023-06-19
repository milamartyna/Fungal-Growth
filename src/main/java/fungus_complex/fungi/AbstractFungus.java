package fungus_complex.fungi;

import fungus_complex.ExploratoryMycelium;
import fungus_complex.Food;
import fungus_complex.Point;
import fungus_complex.State;

public abstract class AbstractFungus {
    protected int speed;
    protected boolean isDominant;
    protected Food acceptedFood;

    public boolean isDormant = true;
    private int dormantAge = 0;

    protected final Point occupiedPoint;
    protected final ExploratoryMycelium exploratoryMycelium;

    public AbstractFungus(Point occupiedPoint) {
        this.occupiedPoint = occupiedPoint;
        exploratoryMycelium = new ExploratoryMycelium(this, occupiedPoint);
    }

    public abstract AbstractFungus createNewFungus(Point point);

    public void die() {
        exploratoryMycelium.die();
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isDominant() {
        return isDominant;
    }

    public Food getAcceptedFood() {
        return acceptedFood;
    }

    public void incrementDormantAge() {
        dormantAge++;
    }

    public int getDormantAge() {
        return dormantAge;
    }

    public void zeroDormantAge() {
        dormantAge = 0;
    }

    public abstract State getCorrelatedState();

    public ExploratoryMycelium getExploratoryMycelium(){
        return exploratoryMycelium;
    }
}
