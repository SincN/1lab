import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Car> cars = new CustomArrayList<>();
    private final List<String> errors = new CustomArrayList<>();

    private void populateCars() {
        cars.clear();
        try (BufferedReader reader = Files.newBufferedReader(Path.of("resources/cars.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(";");
                    cars.add(new Car(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    errors.add("Ошибка при чтении данных автомобиля: Недостаточно данных в строке '" + line + "'");
                    // Добавляем null в список, чтобы сохранить индексы как в файле, даже если произошла ошибка
                    cars.add(null);
                } catch (NumberFormatException e) {
                    errors.add("Ошибка при чтении данных автомобиля: Неверный формат числа в строке '" + line + "'");
                    // Добавляем null в список, чтобы сохранить индексы как в файле, даже если произошла ошибка
                    cars.add(null);
                } catch (IllegalArgumentException e) {
                    errors.add("Ошибка при чтении данных автомобиля: " + e.getMessage() + " в строке '" + line + "'");
                    // Добавляем null в список, чтобы сохранить индексы как в файле, даже если произошла ошибка
                    cars.add(null);
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
            errors.add("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private void removeCarFromFile(int index) {
        try {
            List<String> lines = Files.readAllLines(Path.of("resources/cars.csv"));
            if (index < 0 || index >= lines.size() - 1) {
                errors.add("Ошибка при удалении автомобиля: Индекс " + index + " выше размера списка " + (lines.size() - 1));
                return;
            }
            lines.remove(index + 1);
            Files.write(Path.of("resources/cars.csv"), lines);
        } catch (IOException e) {
            errors.add("Ошибка при удалении автомобиля из файла: " + e.getMessage());
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
        for (Car car : cars.stream().filter(car -> car != null).toList()) {
            System.out.println("Индекс: " + cars.indexOf(car));
            System.out.println(car.toString());
            System.out.println("--------------------");
        }
        backToMenu();
    }

    private void printMenu() {
        if (!errors.isEmpty()) {
            System.out.println("Ошибки:");
            for (String error : errors) {
                System.out.println(" - " + error);
            }
        }
        System.out.println("\n========================================");
        System.out.println("Меню:");
        System.out.println("1. Показать список автомобилей");
        System.out.println("2. Добавить автомобиль");
        System.out.println("3. Обновить список автомобилей (считать из файла)");
        System.out.println("4. Фильтровать автомобили по производителю");
        System.out.println("5. Удалить автомобиль по индексу");
        System.out.println("6. Прочитать автомобиль по индексу");
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
            yearInt = Integer.parseInt(year);
            powerInt = Integer.parseInt(power);
            Car newCar = new Car(manufacturer, model, yearInt, powerInt);
            cars.add(newCar);
            writeCarToFile(newCar);
            System.out.println("Автомобиль успешно добавлен!");
            backToMenu();
        } catch (NumberFormatException e) {
            errors.add("Ошибка при добавлении автомобиля: Год выпуска и мощность должны быть числами");
        } catch (IllegalArgumentException e) {
            errors.add("Ошибка при добавлении автомобиля: " + e.getMessage());
        }
    }

    private void filterManufacturerCars() {
        System.out.print("Введите производителя для фильтрации: ");
        String manufacturer = scanner.nextLine();
        List<String> filterResult = cars.stream()
                .filter(car -> car.getManufacturer().equalsIgnoreCase(manufacturer))
                .sorted((car1, car2) -> Integer.compare(car1.getYear(), car2.getYear()))
                .map(car -> {
                        return car.toString() + "\n--------------------";
                })
                .toList();
        if (filterResult.isEmpty()) {
            System.out.println("Автомобили не найдены.");
        } else {
            System.out.println("Список автомобилей " + manufacturer + " (по году):");
            filterResult.forEach(System.out::println);
        }
        backToMenu();
    }

    private void removeCar() {
        System.out.print("Введите индекс автомобиля для удаления: ");
        String indexStr = scanner.nextLine();
        try {
            int index = Integer.parseInt(indexStr);
            Car removedCar = cars.remove(index);
            removeCarFromFile(index);
            System.out.println("Автомобиль успешно удален: " + removedCar);
        } catch (NumberFormatException e) {
            errors.add("Ошибка при удалении автомобиля: Индекс должен быть числом");
        } catch (IndexOutOfBoundsException e) {
            errors.add("Ошибка при удалении автомобиля: " + e.getMessage());
        }
    }

    private void readCarByIndex() {
        System.out.print("Введите индекс автомобиля для чтения: ");
        String indexStr = scanner.nextLine();
        try {
            int index = Integer.parseInt(indexStr);
            Car car = cars.get(index);
            System.out.println("Автомобиль по индексу " + index + ":");
            System.out.println(car.toString());
            backToMenu();
        } catch (NumberFormatException e) {
            errors.add("Ошибка при чтении автомобиля: Индекс должен быть числом");
        } catch (IndexOutOfBoundsException e) {
            errors.add("Ошибка при чтении автомобиля: " + e.getMessage());
        }
    }

    public void run() {
        populateCars();
        while (true) {
            clearScreen();
            printMenu();
            errors.clear();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> printCarList();
                case "2" -> addCar();
                case "3" -> populateCars();
                case "4" -> filterManufacturerCars();
                case "5" -> removeCar();
                case "6" -> readCarByIndex();
                case "0" -> {
                    return;
                }
                default -> errors.add("Выбрана несуществующая команда. Попробуйте снова.");
            }
        }
    }
}
