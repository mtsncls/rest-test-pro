package com.quality.api.utils;

import com.github.javafaker.Faker;
import com.quality.api.models.User;
import com.quality.api.models.Pet;
import java.util.Collections;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    private TestDataGenerator() {
    }

    public static User generateRandomUser() {
        return User.builder()
                .id(faker.number().randomNumber())
                .username(faker.name().username())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phone(faker.phoneNumber().phoneNumber())
                .userStatus(1)
                .build();
    }

    public static Pet generateRandomPet() {
        return Pet.builder()
                .id(faker.number().randomNumber(10, true))
                .name(faker.dog().name())
                .status("available")
                .category(Pet.Category.builder()
                        .id(faker.number().numberBetween(1, 10))
                        .name(faker.dog().breed())
                        .build())
                .photoUrls(Collections.singletonList(faker.internet().url()))
                .tags(Collections.singletonList(Pet.Tag.builder()
                        .id(faker.number().numberBetween(1, 10))
                        .name("dog")
                        .build()))
                .build();
    }
}
