package fungus_complex;

import fungus_complex.fungi.Fungus;

public class ExploratoryMycelium {
    private final Fungus parentFungus;
    private final int speed;

    public Direction previousDirection;
    public Point currentPoint;

    public ExploratoryMycelium(Fungus parentFungus, Point currentPoint) {
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

    public Fungus getParentFungus() {
        return parentFungus;
    }

    public Fungus createNewFungus(Point point) {
        return parentFungus.createNewFungus(point);
    }

}
