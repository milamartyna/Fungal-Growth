package fungus_complex;

import fungus_complex.fungi.AbstractFungus;

import java.util.*;
import java.util.stream.Stream;

import static fungus_complex.Direction.*;


public class Point {

	public final static State[] states = new State[] {State.EMPTY, State.OCCUPIED};
	public final static Random RNG = new Random();
	public static int foodPacketSize = 6;
	public static double continuousFoodProbability = 0.25;
	public static double seasonalFoodProbability = 0.95;
	public static int maximumDormantAge = 12;

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

	public void supplyFood(boolean isSeasonal) {
		for (Map.Entry<Food, Integer> entry : foodAmounts.entrySet()) {
			if (entry.getValue() > 0) continue;
			if (RNG.nextFloat() < continuousFoodProbability) foodAmounts.put(entry.getKey(), foodPacketSize);
			if (entry.getValue() > 0 || !isSeasonal) continue;
			if (RNG.nextFloat() < seasonalFoodProbability) foodAmounts.put(entry.getKey(), foodPacketSize);
		}
	}

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

	public void expandMycelia() {
		for (ExploratoryMycelium mycelium : presentExploratoryMycelia) {
			if (mycelium.getParentFungus().isDormant) continue;
			expandMycelium(mycelium);
		}
	}

	private void expandMycelium(ExploratoryMycelium mycelium) {
		List<Direction> availableDirections = Stream
				.of(Direction.values())
				.filter(d -> mycelium.previousDirection != d)
				.toList();

		Direction direction = availableDirections.get(RNG.nextInt(availableDirections.size()));
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
		presentExploratoryMycelia.remove(mycelium);
		if (!arrivedToDestination) finalPoint.presentExploratoryMycelia.add(mycelium);

	}

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
		decrementFoodAmount(activeFungus.getAcceptedFood());
		if (foodAmounts.get(activeFungus.getAcceptedFood()) == 0) activeFungus.isDormant = true;
	}

	private void decrementFoodAmount(Food food) {
		foodAmounts.put(food, foodAmounts.get(food) - 1);
	}

	public void addNeighbor(Direction direction, Point nei) {
		neighbours.get(direction).add(nei);
	}

	public void placeFungus(AbstractFungus fungus) {
		presentFungi.add(fungus);
	}
}