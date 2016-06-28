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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    int FILE_LIMIT = 5;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();

    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file, HttpServletRequest request) throws IOException {
        File dir = new File("public/files");
        dir.mkdirs();

        String comments = request.getParameter("comments");

        Iterable<AnonFile> selection;

        if (files.count() == FILE_LIMIT) {
            selection = files.findByOrderByIdAsc();
            int id = selection.iterator().next().getId();
            files.delete(id);
            System.out.println("hit file limit");
        }

        File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(uploadedFile);
        fos.write(file.getBytes());



        AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), comments);
        files.save(anonFile);

        /**
         * put this into its own method and reuse it for file deletion:
         * AnonFile fileinDB = files.findOne(least);
         * File fileOnDisk = newFile("public/files/" + fileinDB.getRealFilename());
         * fileOnDisk.delete();
         * files.delete(lowest id file);
         *
         *
         */


        return "redirect:/";
    }
}
