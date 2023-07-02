package fungus_complex;

public class Config {
    public static class Simulation {
        public static int threadsNumber = 8;

        public static double initialFoodProbability = 0.1;
        public static double initialFungusProbability = 0.01;

        public static int foodPacketSize = 6;
        public static double continuousFoodProbability = 0.02;
        public static double seasonalFoodProbability = 0;
        public static int maximumDormantAge = 9;
        public static int seasonLength = 30;
    }

    public static class FastA {
        public static int speed = 4;
        public static boolean isDominant = false;
        public static Food acceptedFood = Food.ALPHA;

        public static int preferredTemperature = 3;
        public static int preferredPH = 4;
        public static boolean isTemperatureSensitive = false;
        public static boolean isPHSensitive = false;
    }

    public static class FastB {
        public static int speed = 4;
        public static boolean isDominant = false;
        public static Food acceptedFood = Food.BETA;

        public static int preferredTemperature = 3;
        public static int preferredPH = 4;
        public static boolean isTemperatureSensitive = false;
        public static boolean isPHSensitive = false;
    }

    public static class SlowA {
        public static int speed = 2;
        public static boolean isDominant = true;
        public static Food acceptedFood = Food.ALPHA;

        public static int preferredTemperature = 3;
        public static int preferredPH = 4;
        public static boolean isTemperatureSensitive = false;
        public static boolean isPHSensitive = false;
    }

    public static class SlowB {
        public static int speed = 2;
        public static boolean isDominant = true;
        public static Food acceptedFood = Food.BETA;

        public static int preferredTemperature = 3;
        public static int preferredPH = 4;
        public static boolean isTemperatureSensitive = false;
        public static boolean isPHSensitive = false;
    }
}
