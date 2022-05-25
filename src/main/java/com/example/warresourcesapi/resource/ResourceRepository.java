package com.example.warresourcesapi.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource getByName(String name);

    ////    @Query("select new com.demo.entities.ProductJoin(p.id, p.name, p.price, p.category.id, p.category.name) from Product p, Category c where p.category = c")
//    Resource getResourcesByPricesBetween(Long id, LocalDate startDate, LocalDate endDate);
    @Query("SELECT " +
            "new com.example.warresourcesapi.resource.Resource(r.id, r.name, p) " +
            "FROM " +
            "Resource r " +
            "inner join r.prices p " +
            "where r.id= ?1 and (p.date between ?2 and ?3)"
    )
    Resource getResourceByIdAndPricesBetween(Long id, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Resource r WHERE r.id between ?1 and ?2")
    List<Resource> getResourcesByIdBetween(Long id1, Long id2);


}
