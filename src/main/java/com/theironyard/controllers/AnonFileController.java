package com.theironyard.controllers;

import com.theironyard.entities.AnonFile;
import com.theironyard.services.AnonFileRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by will on 6/27/16.
 */
@Controller
public class AnonFileController {
    @Autowired
    AnonFileRepository files;

    int FILE_LIMIT = 10;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();

    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file) throws IOException {
        File dir = new File("public/files");
        dir.mkdirs();

        Iterable<AnonFile> selection;

        if (files.count() == FILE_LIMIT) {
            selection = files.findByOrderByIdAsc();
            int id = selection.iterator().next().getId();
            files.delete(id);
            System.out.println("success");
        }
        File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(uploadedFile);
        fos.write(file.getBytes());

        AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName());
        files.save(anonFile);


        return "redirect:/";
    }
}
