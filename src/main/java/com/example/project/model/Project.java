package com.example.project.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Project")
@Table(name = "project")
public class Project implements Serializable {
    @Id
    private String name;
    @OneToMany(
//            mappedBy = "project",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
            cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "project", orphanRemoval = true
    )
    private List<Floorplan> floorplanList;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Floorplan> getFloorplanList() {
        return floorplanList;
    }

    public void setFloorplanList(List<Floorplan> floorplanList) {
        this.floorplanList = floorplanList;
    }
}
