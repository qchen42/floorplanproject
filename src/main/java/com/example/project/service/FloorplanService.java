package com.example.project.service;


import com.example.project.model.Floorplan;
import com.example.project.repositories.FloorplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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
    public File toFile(MultipartFile file) {
        File toFile = new File("");
        if (file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;

            try {
                ins = file.getInputStream();
                toFile = new File(file.getOriginalFilename());
                inputStreamToFile(ins, toFile);
                ins.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return toFile;
    }
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File resize(File input, String outputFileName, int scaledWidth, int scaledHeight) throws IOException {
        BufferedImage inputImage = ImageIO.read(input);
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        File outputFile = new File(outputFileName);
        ImageIO.write(outputImage, "png", outputFile);
        return outputFile;
    }
}
