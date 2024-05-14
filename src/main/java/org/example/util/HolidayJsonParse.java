package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.holiday.Holiday;
import org.example.domain.inputJson.HolidayJson;
import org.example.domain.inputJson.MonthJson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class HolidayJsonParse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static HolidayJson fetchHoliday(URI uri){
        HolidayJson holiday = null;
        try {
            holiday = objectMapper.readValue(uri.toURL(), HolidayJson.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return holiday;
    }

    public static List<Holiday> parseHoliday(String url){
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e){
            e.getStackTrace();
            return null;
        }
        HolidayJson holidayJson = fetchHoliday(uri);

        List<Holiday> result = new LinkedList<>();
        MonthJson[] monthJsons = holidayJson.getMonths();

        for (MonthJson monthJson : monthJsons) {
            int month = monthJson.getMonth();
            String[] tempArr = monthJson.getDays().split("[,\s\\+]+");
            List<Integer> holidays = new LinkedList<>();
            List<Integer> shortenedDays = new LinkedList<>();
            for (String day : tempArr){
                if ( day.contains("*")){
                    day = day.replace("*", "");
                    shortenedDays.add(Integer.valueOf(day));
                } else {
                    holidays.add(Integer.valueOf(day));
                }
            }
            result.add(new Holiday(
                    monthJson.getMonth(),
                    holidays.stream().mapToInt(Integer::intValue).toArray(),
                    shortenedDays.stream().mapToInt(Integer::intValue).toArray()));
        }
        return result;
    }
}
