package com.example.project.service;

import com.example.project.model.Project;
import com.example.project.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Project project) throws Exception {
        if (project.getName() != null) {
            Project existingProject = projectRepository.findByName(project.getName());
            if (existingProject == null) {
                throw new Exception("Project with name : " + project.getName() + " not found.");
            }
        }
        return projectRepository.save(project);
    }

    public void deleteProjectByName(String name) {
        Project project = getProjectByName(name);
        projectRepository.delete(project);
    }

}
