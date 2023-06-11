package fungus_simple;

import java.util.ArrayList;
import java.util.Random;


public class Point {

//	public final static Double[] growthProbabilities = new Double[] {0.0, 0.125, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
//	public final static Double[] growthProbabilities = new Double[] {0.0, 0.125, 0.25, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	public final static Double[] growthProbabilities = new Double[] {0.0, 0.125, 0.25, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0};
	public final static State[] states = new State[] {State.EMPTY, State.OCCUPIED};
	public final static Random RNG = new Random();

	private final ArrayList<Point> neighbours = new ArrayList<>();
	private State state = State.EMPTY;
	private State nextState = State.EMPTY;
	
	public void clear() {
		state = State.EMPTY;
		nextState = State.EMPTY;
	}

	public void scheduleNextState() {
		int occupiedNeighbours = (int) neighbours.stream().filter(n -> n.getState() == State.OCCUPIED).count();
		if (RNG.nextDouble() < growthProbabilities[occupiedNeighbours]) nextState = State.OCCUPIED;
	}

	public void progressState() {
		state = nextState;
	}

	public void addNeighbor(Point nei) {
		neighbours.add(nei);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}
}