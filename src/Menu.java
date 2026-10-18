package src;
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
    static List<String> errors = new ArrayList<>();

    private void populateCars() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("resources/cars.csv"))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(";");
                    cars.add(new Car(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    errors.add("Ошибка при чтении данных автомобиля: Недостаточно данных в строке '" + line + "'");
                } catch (NumberFormatException e) {
                    errors.add("Ошибка при чтении данных автомобиля: Неверный формат числа в строке '" + line + "'");
                } catch (IllegalArgumentException e) {
                    errors.add("Ошибка при чтении данных автомобиля: " + e.getMessage() + " в строке '" + line + "'");
                }
            }
        } catch (IOException e) {
            errors.add("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private void writeCarToFile(Car car) {
        try (var writer = Files.newBufferedWriter(Path.of("resources/cars.csv"), java.nio.file.StandardOpenOption.APPEND)) {
                writer.write(String.format("\n%s;%s;%d;%d", car.getManufacturer(), car.getModel(), car.getYear(), car.getPower()));
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private void clearScreen() {
        System.out.print("\033[3J\033[2J\033[H");
        System.out.flush();
    }

    private void backToMenu() {
        System.out.println("Нажмите Enter, чтобы вернуться в меню...");
        scanner.nextLine();
    }

    private void printCarList() {
        System.out.println("Список автомобилей:");
        for (Car car : cars) {
            System.out.println(car.toString());
            System.out.println("--------------------");
        }
        backToMenu();
    }

    private void printMenu() {
        if (!errors.isEmpty()) {
            System.out.println("Ошибки при выполнении программы, данные могут быть неполными:");
            for (String error : errors) {
                System.out.println(" - " + error);
            }
        }
        System.out.println("\n========================================");
        System.out.println("Меню:");
        System.out.println("1. Показать список автомобилей");
        System.out.println("2. Добавить автомобиль");
        System.out.println("0. Выход");
        System.out.println("========================================\n");
        System.out.print("Выберите команду: ");
    }

    private void addCar() {
        System.out.print("Введите производителя: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Введите модель: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска: ");
        String year = scanner.nextLine();
        System.out.print("Введите мощность (л.с.): ");
        String power = scanner.nextLine();
        int yearInt, powerInt;
        
        try {
        } catch (NumberFormatException e) {
            System.err.println("Год выпуска и мощность должны быть числами. Попробуйте снова.");
            backToMenu();
            return;
        }

        try {
            yearInt = Integer.parseInt(year);
            powerInt = Integer.parseInt(power);
            Car newCar = new Car(manufacturer, model, yearInt, powerInt);
            cars.add(newCar);
            writeCarToFile(newCar);
            System.out.println("Автомобиль успешно добавлен!");
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при добавлении автомобиля: " + e.getMessage());
        }
        backToMenu();
    }

    public void run() {
        populateCars();
        while (true) {
            clearScreen();
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> printCarList();
                case "2" -> addCar();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Выбрана несуществующая команда. Попробуйте снова.");
            }

        }
    }
}
