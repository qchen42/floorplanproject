package com.example.project.controller;

import com.example.project.model.Project;
import com.example.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @GetMapping("/all")
    public Iterable<Project>  getAllProject() {
        return projectService.getAllProjects();

    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        Project project = projectService.getProjectByName(name);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project) throws Exception {
        project.setFloorplanList(new ArrayList<>());
        Project project1 = projectService.save(project);
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<?> updateProject(@Valid @RequestBody Project project) throws Exception {
        Project project1 = projectService.updateProject(project);
        return new ResponseEntity<Project>(project1, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteProject(@PathVariable String name) {
        projectService.deleteProjectByName(name);
        return new ResponseEntity<String>("Project with name: " + name + " was deleted.", HttpStatus.OK);
    }
}
