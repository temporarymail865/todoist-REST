package com.example.todoist.serviceImplementer;

import com.example.todoist.model.Due;
import com.example.todoist.model.Label;
import com.example.todoist.model.Task;
import com.example.todoist.repository.*;
import com.example.todoist.requestBean.DueRequest;
import com.example.todoist.requestBean.TaskRequest;
import com.example.todoist.responseBean.ActiveTaskResponse;
import com.example.todoist.responseBean.CreateTaskResponse;
import com.example.todoist.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskServiceImplementer implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    DueRepository dueRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public List<CreateTaskResponse> getActiveTasks() {
        List<CreateTaskResponse> createTaskRequestList = new ArrayList<>();
        List<Task> taskList = taskRepository.findAll();
        for (Task saveTask : taskList) {
            if (saveTask.isCompleted() == false) {
                DueRequest dueRequest = new DueRequest();
                if (saveTask.getDue() != null) {
                    dueRequest.setDate(saveTask.getDue().getDate());
                    dueRequest.setDatetime(saveTask.getDue().getDatetime());
                    dueRequest.setString(saveTask.getDue().getString());
                    dueRequest.setTimezone(saveTask.getDue().getTimezone());
                    dueRequest.setRecurring(saveTask.getDue().isRecurring());
                    dueRequest.setRecurrenceIntervalDays(saveTask.getDue().getRecurrenceIntervalDays());
                    dueRequest.setReminderEnabled(saveTask.getDue().isReminderEnabled());
                    dueRequest.setReminderMinutesBefore(saveTask.getDue().getReminderMinutesBefore());
                }
                CreateTaskResponse createTaskResponse = CreateTaskResponse.builder()
                        .comment_count(saveTask.getCommentCount())
                        .completed(saveTask.isCompleted())
                        .content(saveTask.getContent())
                        .due(dueRequest)
                        .id(saveTask.getId())
                        .order(saveTask.getOrders())
                        .priority(saveTask.getPriority())
                        .project_id(saveTask.getProjectId())
                        .section_id(saveTask.getSectionId())
                        .url(saveTask.getUrl())
                        .build();
                createTaskRequestList.add(createTaskResponse);
            }
        }
        return createTaskRequestList;
    }


    @Override
    public CreateTaskResponse createTask(TaskRequest taskRequest) {
        Task task = new Task();

        if (taskRequest.getContent() == null || taskRequest.getContent().trim().length() == 0) {
            return null;
        }
        task.setContent(taskRequest.getContent().trim());

        //Set project Id
        if (projectRepository.existsById(taskRequest.getProject_id()))
            task.setProjectId(taskRequest.getProject_id());
        else
            task.setProjectId(0);
        //Set Section Id
        if (sectionRepository.existsById(taskRequest.getSection_id()))
            task.setSectionId(taskRequest.getSection_id());
        else
            task.setSectionId(0);
        task.setCompleted(taskRequest.isCompleted());
        task.setParent(taskRequest.getParent());
        task.setOrders(taskRequest.getOrder());
        task.setPriority(taskRequest.getPriority());
        task.setUrl("https://todoistrest.herokuapp.com/rest/v1/tasks/" + task.getId());
        task.setCommentCount(taskRequest.getComment_count());
        Due due = null;
        if (taskRequest.getDue() != null && dueRepository.existsByString(taskRequest.getDue().getString())) {
            due = dueRepository.findDueByString(taskRequest.getDue().getString());
        } else if (taskRequest.getDue() != null) {
            due = new Due();
            due.setString(taskRequest.getDue().getString());
            due.setDate(taskRequest.getDue().getDate());
            due.setDatetime(taskRequest.getDue().getDatetime());
            due.setTimezone(taskRequest.getDue().getTimezone());
            due.setRecurring(taskRequest.getDue().isRecurring());
            due.setRecurrenceIntervalDays(taskRequest.getDue().getRecurrenceIntervalDays());
            due.setReminderEnabled(taskRequest.getDue().isReminderEnabled());
            due.setReminderMinutesBefore(taskRequest.getDue().getReminderMinutesBefore());
            dueRepository.save(due);
        }
        task.setDue(due);
        List<Label> labelList = new ArrayList<>();
        if (!(taskRequest.getLabel_ids() == null)) {
            for (Integer id : taskRequest.getLabel_ids()) {
                Label label = new Label();
                if (!labelRepository.existsById(id)) {
                    label.setId(id);
                    labelRepository.save(label);
                } else {
                    label = labelRepository.getOne(id);
                }
                labelList.add(label);
            }
        }
        task.setLabelList(labelList);
        Task saveTask = taskRepository.save(task);

        DueRequest dueRequest = new DueRequest();
        if (saveTask.getDue() != null) {
            dueRequest.setDate(saveTask.getDue().getDate());
            dueRequest.setDatetime(saveTask.getDue().getDatetime());
            dueRequest.setString(saveTask.getDue().getString());
            dueRequest.setTimezone(saveTask.getDue().getTimezone());
            dueRequest.setRecurring(saveTask.getDue().isRecurring());
            dueRequest.setRecurrenceIntervalDays(saveTask.getDue().getRecurrenceIntervalDays());
            dueRequest.setReminderEnabled(saveTask.getDue().isReminderEnabled());
            dueRequest.setReminderMinutesBefore(saveTask.getDue().getReminderMinutesBefore());
        }
        CreateTaskResponse createTaskResponse = CreateTaskResponse.builder()
                .comment_count(saveTask.getCommentCount())
                .completed(saveTask.isCompleted())
                .content(saveTask.getContent())
                .due(dueRequest)
                .id(saveTask.getId())
                .order(saveTask.getOrders())
                .priority(saveTask.getPriority())
                .project_id(saveTask.getProjectId())
                .section_id(saveTask.getSectionId())
                .url(saveTask.getUrl())
                .build();
        return createTaskResponse;
    }


    @Override
    public CreateTaskResponse getActiveTask(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = taskRepository.getOne(id);
            DueRequest dueRequest = new DueRequest();
            if (!(task.getDue() == null)) {
                dueRequest.setDate(task.getDue().getDate());
                dueRequest.setDatetime(task.getDue().getDatetime());
                dueRequest.setString(task.getDue().getString());
                dueRequest.setTimezone(task.getDue().getTimezone());
                dueRequest.setRecurring(task.getDue().isRecurring());
                dueRequest.setRecurrenceIntervalDays(task.getDue().getRecurrenceIntervalDays());
                dueRequest.setReminderEnabled(task.getDue().isReminderEnabled());
                dueRequest.setReminderMinutesBefore(task.getDue().getReminderMinutesBefore());
            }
            CreateTaskResponse createTaskResponse = CreateTaskResponse.builder()
                    .comment_count(task.getCommentCount())
                    .completed(task.isCompleted())
                    .content(task.getContent())
                    .due(dueRequest)
                    .id(task.getId())
                    .order(task.getOrders())
                    .priority(task.getPriority())
                    .project_id(task.getProjectId())
                    .section_id(task.getSectionId())
                    .url(task.getUrl())
                    .build();
            return createTaskResponse;
        }
        return null;
    }


    @Override
    public int updateTask(int id, TaskRequest taskRequest) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = taskRepository.getOne(id);
            if (taskRequest.getContent() == null || taskRequest.getContent().trim().length() == 0) {
                return -1;
            }
            task.setContent(taskRequest.getContent().trim());

            //Set project Id
            if (projectRepository.existsById(taskRequest.getProject_id()))
                task.setProjectId(taskRequest.getProject_id());
            else
                task.setProjectId(0);
            //Set Section Id
            if (sectionRepository.existsById(taskRequest.getSection_id()))
                task.setSectionId(taskRequest.getSection_id());
            else
                task.setSectionId(0);
            task.setCompleted(taskRequest.isCompleted());
            task.setParent(taskRequest.getParent());
            task.setOrders(taskRequest.getOrder());
            task.setPriority(taskRequest.getPriority());
            task.setCommentCount(taskRequest.getComment_count());
            Due due = null;
            if (!(taskRequest.getDue() == null) && dueRepository.existsByString(taskRequest.getDue().getString())) {
                due = dueRepository.findDueByString(taskRequest.getDue().getString());
            } else if (!(taskRequest.getDue() == null)) {
                due = new Due();
                due.setString(taskRequest.getDue().getString());
                due.setDate(taskRequest.getDue().getDate());
                due.setDatetime(taskRequest.getDue().getDatetime());
                due.setTimezone(taskRequest.getDue().getTimezone());
                due.setRecurring(taskRequest.getDue().isRecurring());
                due.setRecurrenceIntervalDays(taskRequest.getDue().getRecurrenceIntervalDays());
                due.setReminderEnabled(taskRequest.getDue().isReminderEnabled());
                due.setReminderMinutesBefore(taskRequest.getDue().getReminderMinutesBefore());
                dueRepository.save(due);
            }
            task.setDue(due);
            List<Label> labelList = new ArrayList<>();
            if (!(taskRequest.getLabel_ids() == null)) {
                for (Integer ids : taskRequest.getLabel_ids()) {
                    Label label = new Label();
                    if (!labelRepository.existsById(ids)) {
                        label.setId(ids);
                        labelRepository.save(label);
                    } else {
                        label = labelRepository.getOne(ids);
                    }
                    labelList.add(label);
                }
            }
            task.setLabelList(labelList);
            Task saveTask = taskRepository.save(task);
            return saveTask.getId();
        }
        return 0;
    }

    @Override
    public int closeTask(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = taskRepository.getOne(id);
            task.setCompleted(true);
            Task saveTask = taskRepository.save(task);
            return saveTask.getId();
        }
        return 0;
    }

    @Override
    public int reopenTask(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = taskRepository.getOne(id);
            task.setCompleted(false);
            Task saveTask = taskRepository.save(task);
            return saveTask.getId();
        }
        return 0;
    }

    @Override
    public int deleteTask(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(id);
            return 1;
        }
        return 0;
    }
}
