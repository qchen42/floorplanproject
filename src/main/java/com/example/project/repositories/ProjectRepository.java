package com.example.project.repositories;

import com.example.project.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {

    @Override
    Iterable<Project> findAll();
    Project findByName(String name);

}
