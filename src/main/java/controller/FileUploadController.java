package controller;

import com.example.project.model.Floorplan;
import com.example.project.model.Project;
import com.example.project.service.FloorplanService;
import com.example.project.service.ProjectService;
import com.example.project.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class FileUploadController {

    @Autowired
    ProjectService projectService;
    @Autowired
    FloorplanService floorplanService;
    @Autowired
    S3Service s3Service;

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @RequestMapping("/api/floorplan")
    public String uploadPicture(Model model) {
        return "uploadview";
    }

    @RequestMapping("/upload")
    public String upload(Model model, @RequestParam("files") MultipartFile file,
                         @RequestParam("projectname") String projectname) throws IOException {
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("msg", "Successfully uploaded files "+ fileName +
                ", create Floorplan and add the Floorplan to Project.");
        Floorplan floorplan = new Floorplan();
        if (fileName != null && fileName.contains(".")) {
            int index = fileName.indexOf('.');
            fileName = fileName.substring(0, index);
        }

        floorplan.setName(fileName);
        Project project = projectService.getProjectByName(projectname);
        floorplan.setProject(project);


        File tempFile = new File(uploadDirectory + file.getOriginalFilename());
        file.transferTo(tempFile);
        floorplan.setOrigin(tempFile);
        floorplanService.save(floorplan);

        // Update project, add Floorplan to Project
        project.getFloorplanList().add(floorplan);
        projectService.save(project);

        // Save to AWS s3
        // s3Service.uploadToS3(tempFile, fileName);
        return "uploadstatusview";
    }

    @GetMapping("/api/floorplan/all")
    public Iterable<Floorplan> getAllFloorPlan() {

        return floorplanService.getAllFloorplan();
    }

    @GetMapping("/api/floorplan/{name}")
    public ResponseEntity<?> getFloorplanByName(@PathVariable String name) {
        Floorplan floorplan = floorplanService.getFloorplanByName(name);
        return new ResponseEntity<Floorplan>(floorplan, HttpStatus.OK);
    }

    @DeleteMapping("/api/floorplan/{name}")
    public ResponseEntity<?> deleteFloorplan(@PathVariable String name) {
        floorplanService.deleteFloorplanByName(name);
        return new ResponseEntity<String>("Project with name: " + name + " was deleted.", HttpStatus.OK);
    }
}
