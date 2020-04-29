package com.example.project.repositories;

import com.example.project.model.Floorplan;
import com.example.project.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorplanRepository extends CrudRepository<Floorplan, String> {
    @Override
    Iterable<Floorplan> findAll();
    Floorplan findByName(String name);
}
