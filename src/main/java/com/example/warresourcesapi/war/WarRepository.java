package com.example.warresourcesapi.war;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarRepository extends JpaRepository<War, Long> {
}
