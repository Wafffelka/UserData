package UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserInputApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите данные: Фамилия, Имя, Отчество, Дата рождения, Номер телефона, Пол. Без запятых. Или 'exit' для выхода:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] data = input.split(" ");

            if (data.length != 6) {
                System.out.println("Ошибка: Введено " + (data.length < 6 ? "меньше" : "больше") + " данных, чем требуется.");
                continue;
            }

            String lastName = data[0];
            String firstName = data[1];
            String middleName = data[2];
            String birthDate = data[3];
            String phoneNumber = data[4];
            String gender = data[5];

            try {
                validateData(lastName, firstName, middleName, birthDate, phoneNumber, gender);
                if (!isDuplicateEntry(lastName, firstName, middleName, birthDate, phoneNumber, gender)) {
                    writeToFile(lastName, firstName, middleName, birthDate, phoneNumber, gender);
                    System.out.println("Данные успешно сохранены.");
                } else {
                    System.out.println("Ошибка: Введенные данные уже существуют в файле.");
                }
            } catch (InvalidDataException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Ошибка при чтении/записи файла:");
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private static void validateData(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, String gender) throws InvalidDataException {
        if (!birthDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new InvalidDataException("Неверный формат даты рождения. Ожидается дд.мм.гггг.");
        }

        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Номер телефона должен быть целым числом.");
        }

        if (!(gender.equals("m") || gender.equals("f"))) {
            throw new InvalidDataException("Пол должен быть указан латиницей 'f' или 'm'.");
        }
    }

    private static boolean isDuplicateEntry(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, String gender) throws IOException {
        String fileName = lastName + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String newEntry = lastName + " " + firstName + " " + middleName + " " + birthDate + " " + phoneNumber + " " + gender;
            while ((line = reader.readLine()) != null) {
                if (line.equals(newEntry)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private static void writeToFile(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, String gender) throws IOException {
        String fileName = lastName + ".txt";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(lastName + " " + firstName + " " + middleName + " " + birthDate + " " + phoneNumber + " " + gender + System.lineSeparator());
        }
    }
}

class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
