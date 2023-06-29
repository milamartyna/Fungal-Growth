package fungus_complex;

import fungus_complex.fungi.Fungus;
import fungus_complex.fungi.*;

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
}
