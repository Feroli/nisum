package helper;

import com.evaluacion.nisum.model.Phone;
import com.evaluacion.nisum.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ModelGenerator {

    public static Phone generatePhone(int index) {
        return Phone.builder()
                .number("123456" + index)
                .cityCode(String.valueOf(index))
                .countryCode("57")
                .build();
    }

    public static List<Phone> generatePhones(int count) {
        return IntStream.range(0, count)
                .mapToObj(ModelGenerator::generatePhone)
                .toList();
    }

    public static User generateUser(int index) {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Test User " + index)
                .email("testuser" + index + "@example.com")
                .password("Password123")
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("some-jwt-token-" + index)
                .isActive(true)
                .phones(generatePhones(2))
                .build();
    }

    public static List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(ModelGenerator::generateUser)
                .toList();
    }
}
