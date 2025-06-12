package com.example.todoist.requestBean;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DueRequest {
    private int id;
    private String string;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date datetime;

    private String timezone;

    private boolean recurring;

    private int recurrenceIntervalDays;

    private boolean reminderEnabled;

    private int reminderMinutesBefore;
}
