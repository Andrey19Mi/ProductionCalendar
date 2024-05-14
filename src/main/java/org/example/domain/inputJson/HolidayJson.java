package org.example.domain.inputJson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"statistic", "year", "transitions"})
public class HolidayJson {

    private MonthJson[] months;
}
