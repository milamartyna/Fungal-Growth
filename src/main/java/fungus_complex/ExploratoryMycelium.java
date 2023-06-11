package fungus_complex;

import fungus_complex.fungi.AbstractFungus;

public class ExploratoryMycelium {
    private final AbstractFungus parentFungus;
    private final int speed;

    public Direction previousDirection;
    private Point currentPoint;

    public ExploratoryMycelium(AbstractFungus parentFungus, Point currentPoint) {
        this.parentFungus = parentFungus;
        this.currentPoint = currentPoint;
        speed = parentFungus.getSpeed();
    }

    public int getSpeed() {
        return speed;
    }

    public AbstractFungus getParentFungus() {
        return parentFungus;
    }

    public AbstractFungus createNewFungus(Point point) {
        return parentFungus.createNewFungus(point);
    }

}
