package com.example.warresourcesapi.repository;

import com.example.warresourcesapi.model.War;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarRepository extends JpaRepository<War, Long> {
}
