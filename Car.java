public class Car {
    private final String manufacturer;
    private final String model;
    private final int year;
    private final int power;

    public Car(String manufacturer, String model, int year, int power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.power = power;
    }

    public @Override String toString() {
        return String.format("model = %s %s,\nyear = %d,\npower = %d hp", manufacturer, model, year, power);
    }

}
