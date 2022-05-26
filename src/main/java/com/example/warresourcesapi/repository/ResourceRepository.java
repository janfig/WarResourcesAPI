package com.example.warresourcesapi.repository;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource getByName(String name);

    //    @Query("select new com.demo.entities.ProductJoin(p.id, p.name, p.price, p.category.id, p.category.name) from Product p, Category c where p.category = c")
//    Resource getResourcesByPricesBetween(Long id, LocalDate startDate, LocalDate endDate);
    @Query("SELECT " +
            "new com.example.warresourcesapi.model.Price(p.id, p.price, p.date) " +
            "FROM " +
            "Resource r " +
            "inner join r.prices p " +
            "where r.id= ?1 and (p.date between ?2 and ?3)"
    )
    Set<Price> getResourceByIdAndPricesBetween(Long id, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Resource r WHERE r.id between ?1 and ?2")
    List<Resource> getResourcesByIdBetween(Long id1, Long id2);

}