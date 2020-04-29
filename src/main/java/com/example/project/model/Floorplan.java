package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity(name = "Floorplan")
@Table(name = "floorplan")
public class Floorplan implements Serializable {
    @Id
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_name")
    private Project project;
    private File origin;
    private File thumb;
    private File large;

    public void setOrigin(File origin) {
        this.origin = origin;
    }

    public File getOrigin() {
        return origin;
    }

    public File getThumb() {
        return thumb;
    }

    public void setThumb(File thumb) {
        this.thumb = thumb;
    }

    public File getLarge() {
        return large;
    }

    public void setLarge(File large) {
        this.large = large;
    }

    public Floorplan(String name) {
        this.name = name;
    }

    public Floorplan() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
