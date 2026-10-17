package src;

public class Car {
    private final String manufacturer;
    private final String model;
    private final int year;
    private final int power;

    public static void validateCarData(String manufacturer, String model, int year, int power) {
        if (manufacturer == null || manufacturer.isEmpty()) {
            throw new IllegalArgumentException("Производитель не может быть пустым");
        }
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Модель не может быть пустой");
        }
        if (year < 1886 || year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Год выпуска должен быть между 1886 и " + java.time.Year.now().getValue());
        }
        if (power <= 0) {
            throw new IllegalArgumentException("Мощность должна быть положительным числом");
        }
    }

    public Car(String manufacturer, String model, int year, int power) {
        validateCarData(manufacturer, model, year, power);
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getPower() {
        return power;
    }

    public @Override String toString() {
        return String.format("Модель = %s %s,\nГод выпуска = %d,\nМощность = %d л.с.", manufacturer, model, year, power);
    }
}
