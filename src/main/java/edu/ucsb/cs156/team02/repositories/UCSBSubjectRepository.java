package edu.ucsb.cs156.team02.repositories;

import edu.ucsb.cs156.team02.entities.UCSBSubject;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UCSBSubjectRepository extends CrudRepository<UCSBSubject, Long> {
    Iterable<UCSBSubject> findBySubjectCode(String subject);
}