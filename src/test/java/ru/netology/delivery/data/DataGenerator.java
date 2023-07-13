package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int addDays) {
        val date = LocalDate.now().plusDays(addDays);
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        final List<String> cities = Arrays.asList("Санкт-Петербург", "Москва", "Казань", "Омск", "Новосибирск", "Липецк", "Тамбов");
        Random random = new Random();
        int randomIndex = random.nextInt(cities.size());
        return cities.get(randomIndex);

    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String lastName = faker.name().lastName();
        String firstName = faker.name().firstName();
        return lastName + " " + firstName;
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber().replaceAll("[^0-9]+", "").replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "$1 $2 $3 $4 $5");
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            val city = generateCity();
            val name = generateName(locale);
            val phone = generatePhone(locale);
            return new UserInfo(city, name, phone);
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
