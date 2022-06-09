package com.example.warresourcesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Price implements Comparable<Price> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Getter(AccessLevel.NONE)
    private Resource resource;
//
//    @Column(name="resource_id")
//    private Long resourceId;


    private Double price;
    private LocalDate date;

    public Price(Double price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public Price(Long id, Double price, LocalDate date) {
        this.id = id;
        this.price = price;
        this.date = date;
    }

    @Override
    public int compareTo(Price p) {
        if(this.date.isAfter(p.date))
            return 1;
        return -1;
    }
}
