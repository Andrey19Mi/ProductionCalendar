package org.example.domain.holiday;

import org.example.util.HolidayJsonParse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductionCalendarTest {

   static ProductionCalendar calendar;

    @ParameterizedTest
    @MethodSource("provideLocalDate")
    void testCheckWeekend(LocalDate date, boolean expected) {
        boolean actual = calendar.checkWeekend(date);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideOffsetDateTime")
    void testCheckWorkingDay(OffsetDateTime dateTime, boolean expected) {
        boolean actual = calendar.checkWorkingDay(dateTime);
        assertEquals(expected, actual);
    }

    @BeforeAll
    static void setUp(){
        List<Holiday> holidays = HolidayJsonParse.parseHoliday(
                "https://www.xmlcalendar.ru/data/ru/2024/calendar.json");
        Holiday[] holidaysArr = holidays.toArray(new Holiday[0]);
        OffsetTime startWorkingTime = OffsetTime.of(LocalTime.of(9, 0),
                ZoneOffset.ofHoursMinutes(3, 0));
        OffsetTime endWorkingTime = OffsetTime.of(LocalTime.of(18, 0),
                ZoneOffset.ofHoursMinutes(3, 0));

        calendar = new ProductionCalendar(holidaysArr, startWorkingTime, endWorkingTime);
    }

    static Stream<Arguments> provideOffsetDateTime(){
        return Stream.of(
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 1, 1, 11, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), true),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 1, 1, 8, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), true),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 1, 11, 1, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), true),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 1, 11, 19, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), true),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 3, 7, 17, 30),
                        ZoneOffset.ofHoursMinutes(3, 0)), true),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 1, 11, 11, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), false),
                Arguments.of(OffsetDateTime.of(LocalDateTime.of(2024, 3, 7, 11, 0),
                        ZoneOffset.ofHoursMinutes(3, 0)), false)
        );
    }

    static Stream<Arguments> provideLocalDate(){
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 5, 9), true),
                Arguments.of(LocalDate.of(2024, 5, 14), false),
                Arguments.of(LocalDate.of(2024, 5, 18), true)
        );
    }
}