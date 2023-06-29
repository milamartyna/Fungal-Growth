package fungus_complex;

import fungus_complex.fungi.Fungus;
import fungus_complex.fungi.*;

import java.awt.*;

public enum State {
    EMPTY, FAST_A, FAST_B, SLOW_A, SLOW_B, ALPHA, BETA;

    public void createCorrelatedFungus(Point point){
        switch (this) {
            case FAST_A -> new FastAFungus(point);
            case FAST_B -> new FastBFungus(point);
            case SLOW_A -> new SlowAFungus(point);
            case SLOW_B -> new SlowBFungus(point);
            case EMPTY, ALPHA, BETA -> {}
        }
    }

    public Class<? extends Fungus> getCorrelatedFungusClass() {
        return switch (this){
            case FAST_A -> FastAFungus.class;
            case FAST_B -> FastBFungus.class;
            case SLOW_A -> SlowAFungus.class;
            case SLOW_B -> SlowBFungus.class;
            case EMPTY, ALPHA, BETA -> null;
        };
    }

    public boolean representsAFungus() {
        return switch (this) {
            case FAST_A, FAST_B, SLOW_A, SLOW_B -> true;
            default -> false;
        };
    }

    public Color getColor() {
        return switch (this) {
            case EMPTY -> new Color(220, 220, 220);
            case FAST_A -> new Color(214, 24, 43);
            case FAST_B -> new Color(252, 195, 5);
            case SLOW_A -> new Color(196, 51, 112);
            case SLOW_B -> new Color(102, 147, 12);
            case ALPHA -> new Color(138, 227, 141);
            case BETA -> new Color(27, 80, 143);
        };
    }

}
