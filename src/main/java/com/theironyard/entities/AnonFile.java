package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by will on 6/27/16.
 */
@Entity
@Table(name = "files")
public class AnonFile {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    String originalFilename;

    @Column(nullable = false)
    String realFilename;

    String comments;

    public AnonFile() {
    }

    public AnonFile(String originalFilename, String realFilename, String comments) {
        this.originalFilename = originalFilename;
        this.realFilename = realFilename;
        this.comments = comments;
    }

    public AnonFile(String originalFilename, String realFilename) {
        this.originalFilename = originalFilename;
        this.realFilename = realFilename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getRealFilename() {
        return realFilename;
    }

    public void setRealFilename(String realFilename) {
        this.realFilename = realFilename;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

