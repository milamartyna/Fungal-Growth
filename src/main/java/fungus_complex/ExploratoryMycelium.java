package fungus_complex;

import fungus_complex.fungi.Fungus;

public class ExploratoryMycelium {
    private final Fungus parentFungus;

    public Direction previousDirection;
    public Point currentPoint;

    public ExploratoryMycelium(Fungus parentFungus, Point currentPoint) {
        this.parentFungus = parentFungus;
        this.currentPoint = currentPoint;
        currentPoint.addExploratoryMyceliumAtNextIteration(this);
    }

    public void die() {
        if (!currentPoint.removeExploratoryMycelium(this)) System.out.println("uh oh");
    }

    public int getSpeed() {
        return parentFungus.getSpeed();
    }

    public Fungus getParentFungus() {
        return parentFungus;
    }

    public void createNewFungus(Point point) {
        parentFungus.createNewFungus(point);
    }

}
