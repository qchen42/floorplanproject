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

    public static String uploadDirectory = System.getProperty("user.dir");

    @RequestMapping("/api/floorplan")
    public String uploadPicture(Model model) {
        return "uploadview";
    }

    @RequestMapping("/upload")
    public String upload(Model model, @RequestParam("files") MultipartFile file,
                         @RequestParam("projectname") String projectname) throws IOException {
        File uploadFile = floorplanService.toFile(file);
        String fileName = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
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

        floorplan.setOrigin(uploadFile);

        // Resize original File to Thumb and Large.
        File thumb = floorplanService.resize(
                uploadFile, fileName + "Thumb.png", 100, 100);
        File large = floorplanService.resize(
                uploadFile, fileName + "Large.png", 2000, 2000);
        floorplan.setThumb(thumb);
        floorplan.setLarge(large);
        floorplanService.save(floorplan);

        // Update project, add Floorplan to Project
        project.getFloorplanList().add(floorplan);
        projectService.save(project);

        // Save original File to AWS s3
        s3Service.uploadToS3(uploadFile, fileName);
        s3Service.uploadToS3(thumb, fileName + "Thumb");
        s3Service.uploadToS3(large, fileName + "Large");
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
