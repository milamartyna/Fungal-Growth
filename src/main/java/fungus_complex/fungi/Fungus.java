package fungus_complex.fungi;

import fungus_complex.*;

public abstract class Fungus {
    protected int speed;
    protected boolean isDominant;
    protected Food acceptedFood;

    protected int preferredTemperature;
    protected int preferredPH;
    protected boolean isTemperatureSensitive;
    protected boolean isPHSensitive;
    private int maximumDormantAge;

    public boolean isDormant = true;
    private int dormantAge = 0;

    protected final Point occupiedPoint;
    protected final ExploratoryMycelium exploratoryMycelium;

    public Fungus(Point occupiedPoint) {
        this.occupiedPoint = occupiedPoint;
        exploratoryMycelium = new ExploratoryMycelium(this, occupiedPoint);
        occupiedPoint.placeFungus(this);
    }

    protected void applyEnvironmentEffects() {
        if (isTemperatureSensitive) {
            int temperaturesDifference = Math.abs(occupiedPoint.getTemperature() - preferredTemperature);
            if (temperaturesDifference == 0) speed++;
            if (temperaturesDifference >= 2) speed--;
        }
        if (isPHSensitive) {
            int pHDifference = Math.abs(occupiedPoint.getPH() - preferredPH);
            int dormantAgeUnit = maximumDormantAge / 3;
            maximumDormantAge += (2 - pHDifference) * dormantAgeUnit;
        }
    }

    public abstract void createNewFungus(Point point);

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

}
