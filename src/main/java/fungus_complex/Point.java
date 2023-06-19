package fungus_complex;

import fungus_complex.fungi.AbstractFungus;

import java.util.*;
import java.util.stream.Stream;

import static fungus_complex.Direction.*;


public class Point {

	public final static Random RNG = new Random();
	public static int foodPacketSize = 6;
	public static double continuousFoodProbability = 0.25;
	public static double seasonalFoodProbability = 0.95;
	public static int maximumDormantAge = 12;
	public static State[] states = State.values();
	public State curState = State.EMPTY;
	public State nextState = State.EMPTY;

	private final Map<Direction, ArrayList<Point>> neighbours = new HashMap<>(Map.of(
			NORTH, new ArrayList<>(),
			EAST, new ArrayList<>(),
			SOUTH, new ArrayList<>(),
			WEST, new ArrayList<>()
	));

	private final Map<Food, Integer> foodAmounts = new HashMap<>(Map.of(Food.ALPHA, 0, Food.BETA, 0));
	private final Set<AbstractFungus> presentFungi = new HashSet<>();
	private final Set<ExploratoryMycelium> presentExploratoryMycelia = new HashSet<>();
	private AbstractFungus activeFungus;

	
	public void clear() {
		foodAmounts.put(Food.ALPHA, 0);
		foodAmounts.put(Food.BETA, 0);
		presentFungi.clear();
		presentExploratoryMycelia.clear();
		activeFungus = null;
	}

	/**
	 * First stage of an iteration. Generates a packet of food in a point for each food type. Replenishes food only if
	 * the stockpile of given type is empty.
	 *
	 * @param isSeasonal should seasonal generation occur
	 */
	public void supplyFood(boolean isSeasonal) {
		for (Map.Entry<Food, Integer> entry : foodAmounts.entrySet()) {
			if (entry.getValue() > 0) continue;
			if (RNG.nextFloat() < continuousFoodProbability) foodAmounts.put(entry.getKey(), foodPacketSize);
			if (entry.getValue() > 0 || !isSeasonal) continue;
			if (RNG.nextFloat() < seasonalFoodProbability) foodAmounts.put(entry.getKey(), foodPacketSize);
		}
	}

	/**
	 * Second stage of an iteration. For each dormant fungus in the cell checks if accepted food type is present
	 * in the cell. If so, calls a method for comparing the competitor with potential current active fungus.
	 * Additionally, for B and b species checks if food Alpha is present in the cell. If it is, then they can't become
	 * active.
	 */
	public void resolveCompetition() {
		for (AbstractFungus fungus : presentFungi) {
			if (!fungus.isDormant) continue;
			if (fungus.getAcceptedFood() == Food.BETA) {
				if (foodAmounts.get(Food.ALPHA) > 0) continue;
				if (foodAmounts.get(Food.BETA) == 0) continue;
				checkForDominance(fungus);

			} else if (fungus.getAcceptedFood() == Food.ALPHA) {
				if (foodAmounts.get(Food.ALPHA) == 0) continue;
				checkForDominance(fungus);
			}
		}
	}

	/**
	 * If the cell doesn't have an active fungus, the challenger becomes the active fungus. If there is one checks which
	 * one is dominant. If the challenger is dominant, it becomes the active fungus and the previously active fungus
	 * becomes dormant.
	 *
	 * @param fungus challenger for being active in this cell
	 */
	private void checkForDominance(AbstractFungus fungus) {
		if (activeFungus == null) {
			fungus.isDormant = false;
			fungus.zeroDormantAge();
			activeFungus = fungus;
		} else if (!activeFungus.isDominant() && fungus.isDominant()) {
			activeFungus.isDormant = true;
			fungus.isDormant = false;
			fungus.zeroDormantAge();
			activeFungus = fungus;
		}
	}

	/**
	 * Third stage of an iteration. For each exploratory mycelium in this cell with active parent expands it.
	 */
	//here was an issue with changing the presentExploratoryMycelia while iterating over it
	public void expandMycelia() {
		ExploratoryMycelium myceliumToRemove = null;
		for (ExploratoryMycelium mycelium : presentExploratoryMycelia) {
			if (mycelium.getParentFungus().isDormant) continue;
			myceliumToRemove = expandMycelium(mycelium);
		}
		if(myceliumToRemove != null)
			presentExploratoryMycelia.remove(myceliumToRemove);
	}

	/**
	 * Generates a list of available directions for the mycelium to take (it can't go back where it came from) and
	 * selects one of them.
	 * Then iterates over points going in the selected direction and plants dormant fungus of the same class as parent
	 * fungus when there isn't already a fungus of this class. When encountering a cell with non-zero amount of food
	 * accepted by myceliums parent fungus it terminates the iteration and the mycelium dies off.
	 * If such a cell wasn't encountered the mycelium is transferred to last processed point.
	 *
	 * @param mycelium mycelium to expand.
	 */
	private ExploratoryMycelium expandMycelium(ExploratoryMycelium mycelium) {
		List<Direction> availableDirections = Stream
				.of(Direction.values())
				.filter(d -> mycelium.previousDirection != d)
				.toList();

		Direction direction = availableDirections.get(RNG.nextInt(availableDirections.size()));
		mycelium.previousDirection = direction;
		int distance = 0;
		Point finalPoint = this;
		boolean arrivedToDestination = false;
		Class<? extends AbstractFungus> myceliumSpecies = mycelium.getParentFungus().getClass();

		for (Point point : neighbours.get(direction)) {
			finalPoint = point;
			if (point.presentFungi.stream().anyMatch(f -> f.getClass() == myceliumSpecies)) { // species the same as mycelium is already present in this point
				if (point.foodAmounts.get(mycelium.getParentFungus().getAcceptedFood()) > 0) {
					arrivedToDestination = true;
					break;
				}
			}
			AbstractFungus newFungus = mycelium.createNewFungus(point);
			point.placeFungus(newFungus);
			if (point.foodAmounts.get(mycelium.getParentFungus().getAcceptedFood()) > 0) {
				arrivedToDestination = true;
				break;
			}

			distance++;
			if (distance >= mycelium.getSpeed()) break;
		}
		if (!arrivedToDestination) finalPoint.presentExploratoryMycelia.add(mycelium);
		return mycelium;
	}

	/**
	 * Fourth stage of an iteration. For each dormant fungus increase its dormant age and kill it if the maximum dormant
	 * age was reached. Decrement the amount of food accepted by the active fungus and make it dormant on food
	 * depletion.
	 */
	public void grow() {
		for (AbstractFungus fungus : presentFungi) {
			if (fungus.isDormant) {
				fungus.incrementDormantAge();
				if (fungus.getDormantAge() == maximumDormantAge) {
					fungus.die();
					presentFungi.remove(fungus);
				}
 			}
		}
		if (activeFungus == null) return;
		decrementFoodAmount(activeFungus.getAcceptedFood());
		if (foodAmounts.get(activeFungus.getAcceptedFood()) == 0){
			activeFungus.isDormant = true;
			activeFungus = null;
		}
	}

	private void decrementFoodAmount(Food food) {
		foodAmounts.put(food, foodAmounts.get(food) - 1);
	}

	public void addNeighbor(Direction direction, Point nei) {
		neighbours.get(direction).add(nei);
	}

	/**
	 * The user creates fungus and places it in the board, its set as the active fungus
	 */
	public void placeFungus(AbstractFungus fungus) {
		presentFungi.add(fungus);
		activeFungus = fungus;
		presentExploratoryMycelia.add(fungus.getExploratoryMycelium());
	}

	public State getState(){
		return curState;
	}

	public void setState(State editState){
		curState = editState;
	}

	public void setNextState(State editState){
		nextState = editState;
	}

	public void scheduleNextState(){
		if(activeFungus != null) {
			nextState = activeFungus.getCorrelatedState();
		}else if(presentFungi.size() != 0) {
			for (AbstractFungus fungus : presentFungi) {
				nextState = fungus.getCorrelatedState();
			}
		// FIXME if we have the board filled with food, we see only ALPHA
		}else if(foodAmounts.get(Food.ALPHA) > 0 && foodAmounts.get(Food.BETA) <= foodAmounts.get(Food.ALPHA)){
			nextState = State.ALPHA;
		}else if(foodAmounts.get(Food.BETA) > 0 && foodAmounts.get(Food.BETA) > foodAmounts.get(Food.ALPHA)){
			nextState = State.BETA;
		}else{
			nextState =  State.EMPTY;
		}
	}

	public void progressState() {
		curState = nextState;
	}
}