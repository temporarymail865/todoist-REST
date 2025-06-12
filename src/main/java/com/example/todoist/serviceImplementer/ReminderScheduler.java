package com.example.todoist.serviceImplementer;

import com.example.todoist.model.Due;
import com.example.todoist.model.Task;
import com.example.todoist.repository.DueRepository;
import com.example.todoist.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ReminderScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DueRepository dueRepository;

    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        Date now = new Date();
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            Due due = task.getDue();
            if (due == null) {
                continue;
            }
            Date dueTime = due.getDatetime() != null ? due.getDatetime() : due.getDate();
            if (dueTime == null) {
                continue;
            }
            long diffMinutes = (dueTime.getTime() - now.getTime()) / 60000;
            if (due.isReminderEnabled() && diffMinutes <= due.getReminderMinutesBefore() && diffMinutes >= 0) {
                log.info("Reminder: Task '{}' is due at {}", task.getContent(), dueTime);
            }
            if (due.isRecurring() && dueTime.before(now)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dueTime);
                cal.add(Calendar.DATE, due.getRecurrenceIntervalDays());
                Date newDue = cal.getTime();
                if (due.getDatetime() != null) {
                    due.setDatetime(newDue);
                }
                if (due.getDate() != null) {
                    due.setDate(newDue);
                }
                dueRepository.save(due);
            }
        }
    }
}
