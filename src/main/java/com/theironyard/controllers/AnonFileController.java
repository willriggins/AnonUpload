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
import javax.servlet.http.HttpSession;
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

        boolean perm;
        String comments = request.getParameter("comments");
        String permanence = request.getParameter("permanence");
        String password = request.getParameter("password");
//        Iterable<AnonFile> selection;

        if (permanence == null) {
            perm = false;
        }
        else {
            perm = true;
        }

        if (files.count() == FILE_LIMIT) {
            Iterable<AnonFile> permFiles = files.findByPermFalseOrderByIdAsc();
            if (permFiles.iterator().hasNext()) {
                AnonFile af = permFiles.iterator().next();
                files.delete(af.getId());
                File fileName = new File("public/files/" + af.getRealFilename());
                fileName.delete();

            }


//            selection = files.findByOrderByIdAsc();
//            int id = selection.iterator().next().getId();
//            files.delete(id);
//            System.out.println("hit file limit");
        }

        File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(uploadedFile);
        fos.write(file.getBytes());

        if (request.getParameter("password") == null) {
            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), comments, perm);
            files.save(anonFile);
        }
        else {
            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), comments, perm, password);
            files.save(anonFile);
        }

        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
   public String delete(int id, String password) throws Exception {
        AnonFile f = files.findOne(id);

        // got help with this part from what we went over tuesday morning in class
        // TODO: Use password hashing after tuesday night's assignment

        if (f.getPerm()) {
            throw new Exception("File is permanent and cannot be deleted");
        }

        if (f.getPassword() != null && !password.equals(f.getPassword())) {
            throw new Exception("Wrong password");
        }
        else {
            files.delete(id);
            File fileName = new File("public/files/" + f.getRealFilename());
            fileName.delete();
            return "redirect:/";
        }
    }
}
