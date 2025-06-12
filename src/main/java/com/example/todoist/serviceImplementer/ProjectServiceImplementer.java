package com.example.todoist.serviceImplementer;

import com.example.todoist.model.Project;
import com.example.todoist.model.Section;
import com.example.todoist.model.Task;
import com.example.todoist.repository.ProjectRepository;
import com.example.todoist.repository.SectionRepository;

import com.example.todoist.repository.TaskRepository;
import com.example.todoist.requestBean.DueRequest;
import com.example.todoist.responseBean.*;
import com.example.todoist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImplementer implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskDAO;

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public List<ProjectResponse> getAllProjects() {
        List<Project> projectList = projectRepository.findAll();
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        for (Project projectListIterator : projectList) {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setId(projectListIterator.getId());
            projectResponse.setName(projectListIterator.getName());
            projectResponse.setComment_count(projectListIterator.getCommentCount());
            projectResponse.setOrder(projectListIterator.getProjectOrder());
            projectResponseList.add(projectResponse);
        }
        return projectResponseList;
    }

    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public ProjectSectionTaskResponse findProjectById(Integer id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            ProjectSectionTaskResponse projectSectionTaskResponse = new ProjectSectionTaskResponse();
            projectSectionTaskResponse.setId(project.getId());
            projectSectionTaskResponse.setName(project.getName());
            projectSectionTaskResponse.setComment_count(project.getCommentCount());
            projectSectionTaskResponse.setOrder(project.getProjectOrder());
            List<Section> sectionList = sectionRepository.getSectionByProjectId(id);
            List<SectionTaskResponse> sectionTaskResponseList = new ArrayList<>();
            for (Section section : sectionList) {
                List<Task> taskList = taskDAO.getTaskBySectionIdAndProjectId(section.getId(), id);
                List<CreateTaskResponse> createTaskRequestList = new ArrayList<>();
                for (Task saveTask : taskList) {
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

                SectionTaskResponse sectionTaskResponse = SectionTaskResponse.builder()
                        .id(section.getId())
                        .project_id(section.getProjectId())
                        .order(section.getSectionOrder())
                        .name(section.getName())
                        .task(createTaskRequestList)
                        .build();

                sectionTaskResponseList.add(sectionTaskResponse);
            }

            List<Task> taskList = taskDAO.getTaskBySectionIdAndProjectId(0, id);
            List<CreateTaskResponse> createTaskRequestList = new ArrayList<>();
            for (Task saveTask : taskList) {

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
            projectSectionTaskResponse.setSection(sectionTaskResponseList);
            projectSectionTaskResponse.setTask(createTaskRequestList);
            return projectSectionTaskResponse;
        } else {
            return new ProjectSectionTaskResponse();
        }
    }

    @Override
    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getOneProjectById(Integer id) {
        return projectRepository.getOne(id);
    }
}