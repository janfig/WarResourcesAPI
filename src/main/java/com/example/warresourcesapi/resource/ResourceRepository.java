package com.example.warresourcesapi.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    public List<Resource> findByDate(LocalDate date);

}
