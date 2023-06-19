package fungus_complex;

import fungus_complex.fungi.AbstractFungus;

public class ExploratoryMycelium {
    private final AbstractFungus parentFungus;
    private final int speed;

    public Direction previousDirection;
    public Point currentPoint;

    public ExploratoryMycelium(AbstractFungus parentFungus, Point currentPoint) {
        this.parentFungus = parentFungus;
        this.currentPoint = currentPoint;
        currentPoint.addExploratoryMyceliumAtNextIteration(this);
//        System.out.println("created new mycelium at " + currentPoint);
        speed = parentFungus.getSpeed();
    }

    public void die() {
        if (!currentPoint.removeExploratoryMycelium(this)) System.out.println("uh oh");
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
