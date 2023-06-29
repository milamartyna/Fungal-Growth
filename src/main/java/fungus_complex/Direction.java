package fungus_complex;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public int getX() {
        return switch (this) {
            case NORTH, SOUTH -> 0;
            case EAST -> 1;
            case WEST -> -1;
        };
    }

    public int getY() {
        return switch (this) {
            case EAST, WEST -> 0;
            case NORTH -> 1;
            case SOUTH -> -1;
        };
    }

}
