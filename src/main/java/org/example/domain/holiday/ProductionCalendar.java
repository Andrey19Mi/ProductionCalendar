package org.example.domain.holiday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionCalendar {
    private Holiday[] holidays;
    private OffsetTime startWorkingDay;
    private OffsetTime endWorkingDay;
    private final int SHORTER_HOURS = 1;

    public boolean checkWeekend(LocalDate date){
        Holiday holidayMonth = holidays[date.getMonthValue()-1];
        int[] weekends = holidayMonth.getWeekend();

        for (int weekend : weekends) {
            if (weekend == date.getDayOfMonth()){
                return true;
            }
        }
        return false;
    }

    private boolean checkShortenedDay(LocalDate date){
        Holiday holidayMonth = holidays[date.getMonthValue()-1];
        int[] shortenedDays = holidayMonth.getShortenedDay();

        for (int shortenedDay : shortenedDays) {
            if (shortenedDay == date.getDayOfMonth()){
                return true;
            }
        }
        return false;
    }

    public boolean checkWorkingDay(OffsetDateTime dateTime){
        boolean isHoliday = checkWeekend(dateTime.toLocalDate());
        if (isHoliday){
            return true;
        } else {
            OffsetTime time = dateTime.toOffsetTime();
            if (time.isBefore(startWorkingDay) || time.isAfter(endWorkingDay)){
                return true;
            } else if (checkShortenedDay(dateTime.toLocalDate()) && (
                    time.isBefore(startWorkingDay) || time.isAfter(endWorkingDay.minusHours(SHORTER_HOURS)))){
                return true;
            } else {
                return false;
            }
        }
    }
}
