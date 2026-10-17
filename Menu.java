import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    static List<Car> cars = new ArrayList<>();

    public static void populateCars() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("resources/cars.csv"))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                cars.add(new Car(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printCarList() {
        System.out.println("Car List:");
        for (Car car : cars) {
            System.out.println(car.toString());
            System.out.println("--------------------");
        }
    }

    static void run() {
        printCarList();
    }
}
