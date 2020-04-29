package com.example.project.service;


import com.example.project.model.Floorplan;
import com.example.project.repositories.FloorplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FloorplanService {

    @Autowired
    FloorplanRepository floorplanRepository;

    public Iterable<Floorplan> getAllFloorplan() {

        return floorplanRepository.findAll();
    }

    public Floorplan getFloorplanByName(String name) {
        return floorplanRepository.findByName(name);
    }

    public Floorplan save(Floorplan floorplan) {
        return floorplanRepository.save(floorplan);
    }

    public Floorplan saveOrUpdateFloorplan(Floorplan floorplan) throws Exception {
        if (floorplan.getName() != null) {
            Floorplan existingFloorplan = floorplanRepository.findByName(floorplan.getName());
            if (existingFloorplan == null) {
                throw new Exception("Floorplan with name : " + floorplan.getName() + " not found.");
            }
        }
        return floorplanRepository.save(floorplan);
    }

    public void deleteFloorplanByName(String name) {
        Floorplan floorplan = getFloorplanByName(name);
        floorplanRepository.delete(floorplan);
    }
}
