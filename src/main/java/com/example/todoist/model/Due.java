package com.example.todoist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "due_table")
public class Due implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
