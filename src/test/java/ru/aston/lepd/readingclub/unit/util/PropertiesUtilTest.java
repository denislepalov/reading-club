package ru.aston.lepd.readingclub.unit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.aston.lepd.readingclub.util.PropertiesUtil;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesUtilTest {

    @ParameterizedTest
    @MethodSource("getPropertyArguments")
    void getProperty(String key, String expectedResult) {
        String actualResult = PropertiesUtil.getProperty(key);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getPropertyArguments() {
        return Stream.of(
            Arguments.of("db.url", "jdbc:postgresql://localhost:5432/postgres"),
            Arguments.of("db.username", "postgres"),
            Arguments.of("db.password", "pass")
        );
    }



}