package com.theironyard.services;

import com.theironyard.entities.AnonFile;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by will on 6/27/16.
 */
public interface AnonFileRepository extends CrudRepository<AnonFile, Integer> {
    public Iterable<AnonFile> findByOrderByIdAsc();
//    public Iterable<AnonFile> findByPerm(Boolean perm);


}
